package com.fourleafclover.tarot.ui.screen.harmony.viewmodel

import android.util.Log
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.fourleafclover.tarot.data.MatchTarotInputDto
import com.fourleafclover.tarot.data.PickedCardNumberState
import com.fourleafclover.tarot.data.repository.RoomEvent
import com.fourleafclover.tarot.data.repository.TarotRepository
import com.fourleafclover.tarot.ui.screen.fortune.viewModel.PickTarotViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

val scenarioSequence = arrayListOf(
    Scenario.Opening,
    Scenario.FirstCard,
    Scenario.SecondCard,
    Scenario.ThirdCard,
    Scenario.Complete,
    Scenario.End
)

data class Chat(
    val type: ChatType,
    var text: String = "",
    val drawable: Int = -1,
    val code: String = "",
    var isShown: Boolean = false
)

enum class ChatType {
    PartnerChatText,
    PartnerChatImage,
    PartnerChatButton,
    MyChatText,
    MyChatImage,
    GuidText,
    Button,
    PickCard,
}

enum class Scenario { Opening, FirstCard, SecondCard, ThirdCard, Complete, End }
enum class CardDeckStatus { Gathered, Spread }

data class ChatState(
    var nickname: String = "",
    var scenario: Scenario = Scenario.Opening,
    var cardDeckStatus: CardDeckStatus = CardDeckStatus.Gathered,
    var pickedCardNumberState: PickedCardNumberState = PickedCardNumberState()
)

@HiltViewModel
class ChatViewModel @Inject constructor(
    private val repository: TarotRepository
) : ViewModel() {

    // ----- existing chat-display state (unchanged shape) -----

    private var _chatState = MutableStateFlow(ChatState())
    val chatState: StateFlow<ChatState> = _chatState.asStateFlow()

    private var _partnerChatState = MutableStateFlow(ChatState())
    val partnerChatState: StateFlow<ChatState> = _partnerChatState.asStateFlow()

    private var _pickSequence = MutableStateFlow(1)
    val pickSequence: StateFlow<Int> = _pickSequence.asStateFlow()

    private var chatList = mutableStateListOf<Chat>()
    val startButtonVisibility = mutableStateOf(true)

    private lateinit var opening: List<Chat>
    private lateinit var firstCard: List<Chat>
    private lateinit var secondCard: List<Chat>
    private lateinit var thirdCard: List<Chat>
    private lateinit var complete: List<Chat>

    private var _isExiting = mutableStateOf(false)
    val isExiting get() = _isExiting

    // ----- bindings to other VMs (set when chat screen starts) -----

    private var harmonyVm: HarmonyViewModel? = null
    private var pickTarotVm: PickTarotViewModel? = null
    private var resultVm: ResultViewModel? = null
    private var loadingVm: LoadingViewModel? = null
    private var eventCollector: Job? = null

    fun setStartButtonVisible() { startButtonVisibility.value = false }
    fun setStartButtonInvisible() { startButtonVisibility.value = true }

    fun clear() {
        _isExiting.value = false
        _chatState.value = ChatState()
        _partnerChatState.value = ChatState()
        _pickSequence.value = 1
        chatList.clear()
        opening = listOf()
        firstCard = listOf()
        secondCard = listOf()
        thirdCard = listOf()
        complete = listOf()
        eventCollector?.cancel()
        eventCollector = null
        harmonyVm = null
        pickTarotVm = null
        resultVm = null
        loadingVm = null
    }

    fun setExit(isExiting: Boolean) { _isExiting.value = isExiting }

    fun startChat(myNickname: String, partnerNickname: String) {
        _chatState.value = ChatState(nickname = myNickname)
        _partnerChatState.value = ChatState(nickname = partnerNickname)
        chatList = mutableStateListOf()
        initOpening()
    }

    /** Bind sibling VMs and begin listening to room events from the repository. */
    fun bindSession(
        harmony: HarmonyViewModel,
        pickTarot: PickTarotViewModel,
        result: ResultViewModel,
        loading: LoadingViewModel
    ) {
        harmonyVm = harmony
        pickTarotVm = pickTarot
        resultVm = result
        loadingVm = loading

        eventCollector?.cancel()
        eventCollector = viewModelScope.launch {
            repository.observeRoomEvents().collect { event ->
                when (event) {
                    is RoomEvent.PartnerNext -> handlePartnerNext(event.cardNumber)
                    is RoomEvent.PartnerExited -> handlePartnerExited()
                    is RoomEvent.ResultPrepared -> handleResultPrepared(event.tarotId)
                    else -> { /* not relevant in chat screen */ }
                }
            }
        }
    }

    // ----- user-driven actions -----

    /** User clicked "시작하기". */
    fun onUserStart(buttonText: String) {
        val harmony = harmonyVm ?: return
        addChatItem(Chat(type = ChatType.MyChatText, text = buttonText))
        viewModelScope.launch {
            repository.signalStart(harmony.roomId.value, harmony.getUserNickname())
            checkEachOtherScenario()
        }
    }

    /** User finished picking a card in the deck. */
    fun onUserCardSelected(cardNumber: Int, cardImageRes: Int) {
        val harmony = harmonyVm ?: return
        val pickTarot = pickTarotVm ?: return
        val seq = _pickSequence.value
        pickTarot.setPickedCard(seq)
        updatePickedCardNumberState(pickTarot.pickedCardNumberState.value)
        addChatItem(Chat(type = ChatType.MyChatImage, drawable = cardImageRes))

        viewModelScope.launch {
            repository.selectCard(
                harmony.roomId.value,
                harmony.getUserNickname(),
                cardNumber
            )
            updatePickSequence()
            updateCardDeckStatus(CardDeckStatus.Gathered)
            pickTarot.resetNowSelectedCardIdx()
            checkEachOtherScenario()
        }
    }

    // ----- repository event handlers -----

    private fun handlePartnerNext(cardNumber: Int?) {
        updatePartnerScenario()
        val myScenario = _chatState.value.scenario
        val partnerScenario = _partnerChatState.value.scenario
        Log.d("chat-vm", "partnerNext my=$myScenario partner=$partnerScenario card=$cardNumber")

        if (myScenario != partnerScenario) {
            // Partner is ahead of where they were; record their card if provided.
            cardNumber?.let {
                updatePartnerCardNumber(getBeforeScenario(partnerScenario), it)
            }
        } else {
            // We are synced; partner just caught up to me. Show finish text + advance.
            updateGuidText("상대방이 답변을 선택했습니다.✨️")
            cardNumber?.let {
                updatePartnerCardNumber(getBeforeScenario(partnerScenario), it)
            }
            addNextScenario()
        }

        if (checkAllCardsPicked()) {
            requestMatchResult()
        }
    }

    private fun handlePartnerExited() {
        updateCardDeckStatus(CardDeckStatus.Gathered)
        addChatItem(Chat(ChatType.PartnerChatText, "상대방이 초대방에서 나가셔서 궁합 보기가 중단되었어요. 아쉽지만 다음 기회에 다시 봐요😢"))
        addChatItem(Chat(ChatType.GuidText, "궁합 보기가 종료되었습니다⚡"))
        setExit(true)
        harmonyVm?.deleteRoom()
    }

    private fun handleResultPrepared(tarotId: String) {
        val loading = loadingVm ?: return
        val result = resultVm ?: return
        val harmony = harmonyVm ?: return

        if (tarotId.isEmpty()) {
            // Failure path
            return
        }
        viewModelScope.launch {
            repository.getTarotById(tarotId)
                .onSuccess { tarot ->
                    result.setIsMatchResultPrepared(true)
                    result.distinguishCardResult(tarot, harmony.isRoomOwner.value)
                    loading.updateLoadingState(false)
                }
            // Acknowledge receipt back to the room.
            repository.confirmResultReceived(harmony.roomId.value, tarotId)
            repository.disconnect()
        }
    }

    private fun requestMatchResult() {
        val harmony = harmonyVm ?: return
        val pickTarot = pickTarotVm ?: return
        updatePickedCardNumberState(pickTarot.pickedCardNumberState.value)
        viewModelScope.launch {
            repository.requestMatchResult(
                MatchTarotInputDto(
                    firstUser = harmony.getOwnerNickname(),
                    secondUser = harmony.getInviteeNickname(),
                    roomId = harmony.roomId.value,
                    cards = collectAllCards(harmony.isRoomOwner.value)
                )
            )
            // Result delivery happens via RoomEvent.ResultPrepared above.
        }
    }

    private fun collectAllCards(isRoomOwner: Boolean): ArrayList<Int> {
        val mine = _chatState.value.pickedCardNumberState
        val theirs = _partnerChatState.value.pickedCardNumberState
        return if (isRoomOwner) {
            arrayListOf(
                mine.firstCardNumber, mine.secondCardNumber, mine.thirdCardNumber,
                theirs.firstCardNumber, theirs.secondCardNumber, theirs.thirdCardNumber
            )
        } else {
            arrayListOf(
                theirs.firstCardNumber, theirs.secondCardNumber, theirs.thirdCardNumber,
                mine.firstCardNumber, mine.secondCardNumber, mine.thirdCardNumber
            )
        }
    }

    private fun checkAllCardsPicked(): Boolean {
        val mine = _chatState.value.pickedCardNumberState
        val theirs = _partnerChatState.value.pickedCardNumberState
        return mine.firstCardNumber != -1 && mine.secondCardNumber != -1 && mine.thirdCardNumber != -1 &&
            theirs.firstCardNumber != -1 && theirs.secondCardNumber != -1 && theirs.thirdCardNumber != -1
    }

    /** Compares scenarios after a user action and inserts the appropriate guide. */
    private fun checkEachOtherScenario() {
        val mine = _chatState.value
        val theirs = _partnerChatState.value
        if (mine.scenario != theirs.scenario) {
            confirmSelectedCard(mine)
            moveToNextScenario()
        } else {
            confirmSelectedCard(mine)
            updateScenario()
            addChatItem(Chat(ChatType.GuidText, "상대방의 답변을 기다리는 중입니다...✏️"))
        }
    }

    private fun confirmSelectedCard(mine: ChatState) {
        if (mine.scenario == Scenario.Opening) return
        val text = when {
            mine.pickedCardNumberState.thirdCardNumber != -1 ->
                "세번째 카드 선택이 완료되었습니다! ${getAdjectives()} 카드를 뽑으셨네요✨"
            mine.pickedCardNumberState.secondCardNumber != -1 ->
                "두번째 카드 선택이 완료되었습니다! ${getAdjectives()} 카드를 뽑으셨네요✨"
            else ->
                "첫번째 카드 선택이 완료되었습니다! ${getAdjectives()} 카드를 뽑으셨네요✨"
        }
        addChatItem(Chat(ChatType.PartnerChatText, text, code = "secondCard_1"))
    }

    // ----- chat list & scenario plumbing (unchanged) -----

    fun initOpening() {
        opening = listOf(
            Chat(ChatType.PartnerChatText, "${_chatState.value.nickname}님, 그 사람과의 운명이 궁금하시군요!", code = "opening_1"),
            Chat(ChatType.PartnerChatText, "지금부터 타로카드를 통해 서로의 운명 궁합을 봐드릴게요!\n궁합 보실 준비가 되셨다면 [시작하기]를 눌러주세요!", code = "opening_2"),
            Chat(ChatType.Button, "시작하기", code = "opening_3")
        )
        chatList.addAll(opening)
    }

    private fun initFirst() {
        firstCard = listOf(
            Chat(ChatType.PartnerChatText, "두분 모두 궁합 볼 준비가 되셨군요! 이제부터 차례대로 총 세장의 카드를 선택하실 수 있어요.", code = "firstCard_1"),
            Chat(ChatType.PartnerChatText, "서로 선택한 카드를 기반으로, 두분의 궁합 운명을 해석해드릴게요.🔮", code = "firstCard_2"),
            Chat(ChatType.PartnerChatText, "우선 상대방을 떠올리며 첫번째 카드를 골라주세요.", code = "firstCard_3"),
            Chat(ChatType.PickCard, code = "fristCard_4"),
        )
    }

    private fun initSecond() {
        secondCard = listOf(
            Chat(ChatType.PartnerChatText, "${_partnerChatState.value.nickname}님은 이 카드를 선택하셨어요.", code = "secondCard_2"),
            Chat(type = ChatType.PartnerChatImage, drawable = _partnerChatState.value.pickedCardNumberState.firstCardNumber, code = "secondCard_2"),
            Chat(ChatType.PartnerChatText, "이제 두번째 카드를 골라봐요!", code = "secondCard_3"),
            Chat(ChatType.PickCard, code = "secondCard_4"),
        )
    }

    private fun initThird() {
        thirdCard = listOf(
            Chat(ChatType.PartnerChatText, "${_partnerChatState.value.nickname}님은 이 카드를 선택하셨어요.", code = "thirdCard_2"),
            Chat(type = ChatType.PartnerChatImage, drawable = _partnerChatState.value.pickedCardNumberState.secondCardNumber, code = "secondCard_2"),
            Chat(ChatType.PartnerChatText, "이제 세번째 카드를 골라봐요!", code = "thirdCard_3"),
            Chat(ChatType.PickCard, code = "thirdCard_4"),
        )
    }

    fun initComplete() {
        complete = listOf(
            Chat(ChatType.PartnerChatButton, "짝짝짝👏\n모든 카드 선택이 완료되었습니다! ${_chatState.value.nickname}님과 ${_partnerChatState.value.nickname}님의 궁합은...", code = "complete_1")
        )
    }

    fun removeChatListLastItem() = chatList.removeAt(chatList.size - 1)

    fun updateGuidText(text: String) {
        chatList.lastOrNull()?.text = text
    }

    fun getChatListSize(): Int = chatList.size
    fun getChatItem(idx: Int): Chat = chatList[idx]
    fun addChatItem(chatItem: Chat) = chatList.add(chatItem)
    fun updatePickSequence() { _pickSequence.value += 1 }

    fun getNextScenario(now: Scenario) = scenarioSequence[scenarioSequence.indexOf(now) + 1]
    fun getBeforeScenario(now: Scenario) = scenarioSequence[scenarioSequence.indexOf(now) - 1]

    fun updateScenario() { _chatState.value.scenario = getNextScenario(_chatState.value.scenario) }
    fun updatePartnerScenario() { _partnerChatState.value.scenario = getNextScenario(_partnerChatState.value.scenario) }

    fun moveToNextScenario() {
        updateScenario()
        addNextScenario()
    }

    fun addNextScenario() {
        when (_chatState.value.scenario) {
            Scenario.FirstCard -> { initFirst(); chatList.addAll(firstCard) }
            Scenario.SecondCard -> { initSecond(); chatList.addAll(secondCard) }
            Scenario.ThirdCard -> { initThird(); chatList.addAll(thirdCard) }
            Scenario.Complete -> { initComplete(); chatList.addAll(complete) }
            else -> {}
        }
    }

    fun getSec(chat: Chat): Int = when (_chatState.value.scenario) {
        Scenario.Opening -> opening.indexOf(chat)
        Scenario.FirstCard -> firstCard.indexOf(chat)
        Scenario.SecondCard -> secondCard.indexOf(chat)
        Scenario.ThirdCard -> thirdCard.indexOf(chat)
        Scenario.Complete -> complete.indexOf(chat)
        else -> 0
    }

    fun updateCardDeckStatus(newStatus: CardDeckStatus) {
        _chatState.value = ChatState(
            _chatState.value.nickname,
            _chatState.value.scenario,
            newStatus,
            _chatState.value.pickedCardNumberState
        )
    }

    fun updatePickedCardNumberState(state: PickedCardNumberState) {
        _chatState.value.pickedCardNumberState.firstCardNumber = state.firstCardNumber
        _chatState.value.pickedCardNumberState.secondCardNumber = state.secondCardNumber
        _chatState.value.pickedCardNumberState.thirdCardNumber = state.thirdCardNumber
    }

    fun updatePartnerCardNumber(partnerScenario: Scenario, cardNumber: Int) {
        when (partnerScenario) {
            Scenario.FirstCard -> _partnerChatState.value.pickedCardNumberState.firstCardNumber = cardNumber
            Scenario.SecondCard -> _partnerChatState.value.pickedCardNumberState.secondCardNumber = cardNumber
            Scenario.ThirdCard -> _partnerChatState.value.pickedCardNumberState.thirdCardNumber = cardNumber
            else -> {}
        }
    }

    private val adjectives = arrayListOf(
        "의미심장한", "멋진", "신비로운", "비밀스러운", "상징적인",
        "심오한", "미묘한", "몽환적인", "수수께끼같은", "독특한"
    )

    fun getAdjectives(): String = adjectives[(0 until adjectives.size).random()]
}
