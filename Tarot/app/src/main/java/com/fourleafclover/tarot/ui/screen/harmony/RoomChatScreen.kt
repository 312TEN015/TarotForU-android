package com.fourleafclover.tarot.ui.screen.harmony

import android.util.Log
import androidx.collection.mutableIntSetOf
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.core.EaseOutQuart
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.animateScrollBy
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.requiredHeight
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.fourleafclover.tarot.MyApplication
import com.fourleafclover.tarot.R
import com.fourleafclover.tarot.SubjectHarmony
import com.fourleafclover.tarot.ui.component.AppBarCloseOnChatWithDialog
import com.fourleafclover.tarot.ui.component.AppBarCloseOnRoomInviteWithDialog
import com.fourleafclover.tarot.ui.component.ButtonNext
import com.fourleafclover.tarot.ui.component.ButtonText
import com.fourleafclover.tarot.ui.component.getBackgroundModifier
import com.fourleafclover.tarot.ui.navigation.PreventBackPressed
import com.fourleafclover.tarot.ui.navigation.ScreenEnum
import com.fourleafclover.tarot.ui.navigation.navigateInclusive
import com.fourleafclover.tarot.ui.screen.fortune.CardDeck
import com.fourleafclover.tarot.ui.screen.fortune.viewModel.FortuneViewModel
import com.fourleafclover.tarot.ui.screen.fortune.viewModel.PickTarotViewModel
import com.fourleafclover.tarot.ui.screen.harmony.viewmodel.CardDeckStatus
import com.fourleafclover.tarot.ui.screen.harmony.viewmodel.Chat
import com.fourleafclover.tarot.ui.screen.harmony.viewmodel.ChatState
import com.fourleafclover.tarot.ui.screen.harmony.viewmodel.ChatType
import com.fourleafclover.tarot.ui.screen.harmony.viewmodel.ChatViewModel
import com.fourleafclover.tarot.ui.screen.harmony.viewmodel.HarmonyViewModel
import com.fourleafclover.tarot.ui.screen.harmony.viewmodel.LoadingViewModel
import com.fourleafclover.tarot.ui.screen.harmony.viewmodel.ResultViewModel
import com.fourleafclover.tarot.ui.screen.harmony.viewmodel.Scenario
import com.fourleafclover.tarot.ui.screen.main.DialogViewModel
import com.fourleafclover.tarot.ui.theme.TextB03M14
import com.fourleafclover.tarot.ui.theme.TextB04M12
import com.fourleafclover.tarot.ui.theme.backgroundColor_2
import com.fourleafclover.tarot.ui.theme.gray_1
import com.fourleafclover.tarot.ui.theme.gray_5
import com.fourleafclover.tarot.ui.theme.gray_6
import com.fourleafclover.tarot.ui.theme.gray_7
import com.fourleafclover.tarot.ui.theme.gray_8
import com.fourleafclover.tarot.ui.theme.highlightPurple
import com.fourleafclover.tarot.ui.theme.purple50
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

private val toShowProfileList = mutableIntSetOf()

var initialComposition = true

@Composable
@Preview
fun RoomChatScreen(
    navController: NavHostController = rememberNavController(),
    harmonyViewModel: HarmonyViewModel = hiltViewModel(),
    chatViewModel: ChatViewModel = hiltViewModel(),
    fortuneViewModel: FortuneViewModel = hiltViewModel(),
    pickTarotViewModel: PickTarotViewModel = hiltViewModel(),
    resultViewModel: ResultViewModel = hiltViewModel(),
    loadingViewModel: LoadingViewModel = hiltViewModel(),
    dialogViewModel: DialogViewModel = hiltViewModel()
) {

    val chatState = chatViewModel.chatState.collectAsState()
    val partnerChatState = chatViewModel.partnerChatState.collectAsState()

    PreventBackPressed()

    LaunchedEffect(Unit) {
        if (initialComposition) {
            setOnNext(harmonyViewModel, loadingViewModel, chatViewModel, pickTarotViewModel)
            setOnResult(harmonyViewModel, loadingViewModel, resultViewModel)
            setOnExit(chatViewModel, harmonyViewModel)
            initialComposition = false
        }
    }

    Column(modifier = getBackgroundModifier(backgroundColor_2)) {
        AppBarCloseOnChatWithDialog(
            navController = navController,
            pickedTopicTemplate = SubjectHarmony,
            backgroundColor = backgroundColor_2,
            isTitleVisible = false,
            harmonyViewModel = harmonyViewModel,
            dialogViewModel = dialogViewModel
        )

        Box(contentAlignment = Alignment.BottomCenter) {

            val scope = rememberCoroutineScope()
            val scrollState = rememberLazyListState()

            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                state = scrollState
            ) {

                items(chatViewModel.getChatListSize() + 1) {

                    val itemSize = 20.dp    // 대략적인 사이즈
                    val density = LocalDensity.current
                    val itemSizePx = with(density) { itemSize.toPx() }
                    val itemsScrollCount = chatViewModel.getChatListSize()

                    if (it == chatViewModel.getChatListSize()) {
                        Box(modifier = Modifier
                            .requiredHeight(310.dp)
                            .fillMaxWidth())
                    } else {
                        val chatItem by remember { mutableStateOf(chatViewModel.getChatItem(it)) }
                        val sec by remember { mutableStateOf(chatViewModel.getSec(chatItem)) }

                        withChatAnimation(
                            idx = sec,
                            chatItem = chatItem
                        ) {

                            when (chatItem.type) {
                                ChatType.PartnerChatText -> {
                                    if (sec == 0){
                                        toShowProfileList.add(it)
                                    }
                                    PartnerChattingBox(
                                        text = chatItem.text,
                                        idx = it,
                                        chatViewModel = chatViewModel,
                                        loadingViewModel = loadingViewModel,
                                        fortuneViewModel = fortuneViewModel
                                    )
                                }

                                ChatType.PartnerChatButton -> {
                                    if (sec == 0){
                                        toShowProfileList.add(it)
                                    }
                                    PartnerChattingBox(
                                        text = chatItem.text,
                                        idx = it,
                                        buttonText = "궁합결과 보러가기",
                                        navController = navController,
                                        chatViewModel = chatViewModel,
                                        loadingViewModel = loadingViewModel,
                                        fortuneViewModel = fortuneViewModel
                                    )
                                }

                                ChatType.PartnerChatImage -> {
                                    if (sec == 0){
                                        toShowProfileList.add(it)
                                    }
                                    PartnerChattingBox(
                                        text = chatItem.text,
                                        idx = it,
                                        drawable = chatItem.drawable,
                                        chatViewModel = chatViewModel,
                                        loadingViewModel = loadingViewModel,
                                        fortuneViewModel = fortuneViewModel
                                    )
                                }

                                ChatType.Button -> {
                                    if (!chatItem.isShown) {
                                        var buttonVisibility by remember { mutableStateOf(true) }
                                        ButtonSelect(
                                            text = chatItem.text,
                                            onClick = {
                                                buttonVisibility = false

                                                chatViewModel.addChatItem(
                                                    Chat(
                                                        type = ChatType.MyChatText,
                                                        text = chatItem.text
                                                    )
                                                )

                                                emitStart(harmonyViewModel)

                                                checkEachOtherScenario(chatState.value, partnerChatState.value, chatViewModel)
                                            },
                                            buttonVisibility
                                        )
                                    }
                                }

                                ChatType.MyChatText -> {
                                    MyChattingBox(text = chatItem.text)
                                }

                                ChatType.MyChatImage -> {
                                    MyChattingBox(drawable = chatItem.drawable)
                                }

                                ChatType.PickCard -> {

                                    if (chatViewModel.isExiting.value) return@withChatAnimation

                                    if (chatState.value.scenario != Scenario.Complete) {
                                        LaunchedEffect(Unit) {
                                            chatViewModel.updateCardDeckStatus(CardDeckStatus.Spread)
                                        }
                                    }
                                }

                                ChatType.GuidText -> {
                                    GuidBox(text = chatItem.text)
                                }

                                else -> {}
                            }

                        }

                        if (!chatItem.isShown) {
                            scope.launch {
                                scrollState.animateScrollBy(
                                    value = itemSizePx * itemsScrollCount,
                                    animationSpec = tween(
                                        durationMillis = 1500,
                                        easing = EaseOutQuart
                                    )
                                )
                            }

                        }
                    }
                }

            }

            if (chatState.value.cardDeckStatus == CardDeckStatus.Spread)
                withChatAnimation(){
                    ChatCardDeck(harmonyViewModel, chatViewModel, fortuneViewModel, pickTarotViewModel)
                }

            if (chatViewModel.isExiting.value) {
                Box(modifier = Modifier.padding(horizontal = 20.dp)) {
                    ButtonNext(
                        onClick = {
                            dialogViewModel.closeDialog()
                            harmonyViewModel.deleteRoom()
                            MyApplication.closeSocket()
                            navigateInclusive(navController, ScreenEnum.HomeScreen.name)
                            chatViewModel.setExit(false)
                        },
                        enabled = { true },
                        content = { ButtonText(true, "메인으로 이동") }
                    )
                }

            }
        }

    }

}

fun checkEachOtherScenario(chatState: ChatState, partnerChatState: ChatState, chatViewModel: ChatViewModel) {

    Log.d("socket-test", "emit my: " + chatViewModel.chatState.value.scenario.name)
    Log.d("socket-test", "emit partner: " + chatViewModel.partnerChatState.value.scenario.name)

    // 상대방이랑 다름 = 내가 뒤처짐
    if (chatState.scenario != partnerChatState.scenario){
        confirmSelectedCard(chatState, chatViewModel)
        chatViewModel.moveToNextScenario()
    }
    // 상대방이랑 같음 = 내가 먼저함
    else {
        confirmSelectedCard(chatState, chatViewModel)

        chatViewModel.updateScenario()

        chatViewModel.addChatItem(
            Chat(
                type = ChatType.GuidText,
                text = "상대방의 답변을 기다리는 중입니다...✏️"
            )
        )
        // -> onPartnerChecked
    }
}

fun confirmSelectedCard(chatState: ChatState, chatViewModel: ChatViewModel) {

    if (chatState.scenario != Scenario.Opening) {
        if (chatState.pickedCardNumberState.thirdCardNumber != -1){
            chatViewModel.addChatItem(
                Chat(ChatType.PartnerChatText, "세번째 카드 선택이 완료되었습니다! ${chatViewModel.getAdjectives()} 카드를 뽑으셨네요✨", code = "secondCard_1")
            )
        } else if (chatState.pickedCardNumberState.secondCardNumber != -1){
            chatViewModel.addChatItem(
                Chat(ChatType.PartnerChatText, "두번째 카드 선택이 완료되었습니다! ${chatViewModel.getAdjectives()} 카드를 뽑으셨네요✨", code = "secondCard_1")
            )
        } else{
            chatViewModel.addChatItem(
                Chat(ChatType.PartnerChatText, "첫번째 카드 선택이 완료되었습니다! ${chatViewModel.getAdjectives()} 카드를 뽑으셨네요✨", code = "secondCard_1")
            )
        }
    }

}


@Composable
fun ChatCardDeck(
    harmonyViewModel: HarmonyViewModel,
    chatViewModel: ChatViewModel,
    fortuneViewModel: FortuneViewModel,
    pickTarotViewModel: PickTarotViewModel
) {
    val localContext = LocalContext.current
    val pickSequence = chatViewModel.pickSequence.collectAsState()

    // 카드덱
    Column(
        modifier = Modifier.padding(top = 80.dp, bottom = 32.dp),
        verticalArrangement = Arrangement.Bottom,
    ) {

        CardDeck(pickTarotViewModel)

        ButtonSelect(
            text = "선택완료",
            onClick = {
                pickTarotViewModel.setPickedCard(pickSequence.value)
                chatViewModel.updatePickedCardNumberState(pickTarotViewModel.pickedCardNumberState.value)
                chatViewModel.addChatItem(
                    Chat(
                        type = ChatType.MyChatImage,
                        drawable = fortuneViewModel.getCardImageId(localContext, pickTarotViewModel.getCardNumber(pickSequence.value).toString())
                    )
                )

                emitCardSelect(harmonyViewModel, pickTarotViewModel, pickSequence.value)

                chatViewModel.updatePickSequence()
                chatViewModel.updateCardDeckStatus(CardDeckStatus.Gathered)

                pickTarotViewModel.resetNowSelectedCardIdx()
                checkEachOtherScenario(chatViewModel.chatState.value, chatViewModel.partnerChatState.value, chatViewModel)
            },
            isEnable = pickTarotViewModel.isCompleteButtonEnabled()
        )

    }
}

@OptIn(ExperimentalAnimationApi::class)
@Composable
fun withChatAnimation(
    idx: Int = 0,
    chatItem: Chat,
    content: @Composable () -> Unit = {}) {
    var visible by remember { mutableStateOf(false) }

    if (!visible) {
        LaunchedEffect(Unit) {
            delay(((idx + 1) * 1200).toLong())
            visible = true
        }
    }

    AnimatedVisibility(
        visible = if (chatItem.isShown) true else visible,
        modifier = Modifier,
        enter = slideInVertically(
            animationSpec = tween(durationMillis = 1500, easing = EaseOutQuart),
            initialOffsetY = { it * 2 }
        ) + fadeIn(animationSpec = tween(durationMillis = 1900)),
        exit = fadeOut(animationSpec = tween(durationMillis = 500))
    ) {
        content()
        if (this.transition.currentState == this.transition.targetState){
            chatItem.isShown = true
        }
    }
}

@Composable
fun withChatAnimation(
    idx: Int = 0,
    content: @Composable () -> Unit = {}) {
    var visible by remember { mutableStateOf(false) }

    if (!visible) {
        LaunchedEffect(Unit) {
            delay(((idx) * 1200).toLong())
            visible = true
        }
    }

    AnimatedVisibility(
        visible = visible,
        modifier = Modifier,
        enter = slideInVertically(
            animationSpec = tween(durationMillis = 1500, easing = EaseOutQuart),
            initialOffsetY = { it * 2 }
        ) + fadeIn(animationSpec = tween(durationMillis = 1900)),
        exit = fadeOut(animationSpec = tween(durationMillis = 500))
    ) {
        content()
    }
}

@Composable
fun PartnerChattingBox(
    text: String = "",
    idx: Int = 0,
    buttonText: String = "",
    navController: NavHostController = rememberNavController(),
    drawable: Int = -1,
    chatViewModel: ChatViewModel,
    loadingViewModel: LoadingViewModel,
    fortuneViewModel: FortuneViewModel
) {

    Row(
        modifier = Modifier
            .padding(start = 20.dp, end = 48.dp)
            .fillMaxWidth(),
        horizontalArrangement = Arrangement.Start
    ) {
        Image(
            modifier = Modifier
                .size(40.dp)
                .padding(end = 14.dp)
                .alpha(if (idx in toShowProfileList) 1f else 0f),
            painter = painterResource(id = R.drawable.default_profile),
            contentDescription = null
        )

        Row(
            modifier = Modifier
                .padding(top = 16.dp)
        ) {
            Image(
                painter = painterResource(id = R.drawable.chat_tail_left_gray),
                contentDescription = null
            )

            Column(
                Modifier
                    .background(
                        color = gray_7,
                        shape = RoundedCornerShape(0.dp, 10.dp, 10.dp, 10.dp)
                    )
                    .padding(8.dp)

            ) {

                if (drawable != -1) {
                    Image(
                        modifier = Modifier.width(60.dp),
                        painter = painterResource(id = fortuneViewModel.getCardImageId(LocalContext.current, drawable.toString())),
                        contentDescription = null
                    )
                } else {
                    TextB03M14(
                        text = text,
                        color = gray_1
                    )
                }

                if (buttonText.isNotEmpty()) {
                    ButtonSelectInChat(
                        text = buttonText,
                        onClick = {
                            chatViewModel.moveToNextScenario()

                            loadingViewModel.startLoading(
                                navController,
                                ScreenEnum.LoadingScreen,
                                ScreenEnum.RoomResultScreen
                            )

                        }
                    )
                }
            }
        }

    }


}

@Composable
@Preview
fun MyChattingBox(text: String = "", drawable: Int = 0) {

    Row(
        modifier = Modifier
            .padding(start = 48.dp, end = 20.dp)
            .fillMaxWidth(),
        horizontalArrangement = Arrangement.End
    ) {
        Row(
            modifier = Modifier
                .padding(top = 16.dp)
        ) {

            Box(
                Modifier
                    .background(
                        color = purple50,
                        shape = RoundedCornerShape(10.dp, 0.dp, 10.dp, 10.dp)
                    )
                    .padding(8.dp)

            ) {

                if (drawable != 0) {
                    Image(
                        modifier = Modifier.width(60.dp),
                        painter = painterResource(id = drawable),
                        contentDescription = null
                    )
                } else {
                    TextB03M14(
                        text = text,
                        color = gray_8
                    )
                }


            }

            Image(
                painter = painterResource(id = R.drawable.chat_tail_right_purple),
                contentDescription = null
            )
        }

    }
}

@Preview
@Composable
fun GuidBox(text: String = "상대방의 답변을 기다리는 중입니다...✏️") {

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 32.dp, bottom = 40.dp), contentAlignment = Alignment.Center
    ) {
        TextB04M12(
            modifier = Modifier
                .background(color = gray_8, shape = RoundedCornerShape(6.dp))
                .padding(vertical = 8.dp, horizontal = 16.dp),
            text = text,
            color = gray_5,
            textAlign = TextAlign.Center
        )

    }

}


@Composable
fun ButtonSelect(
    text: String = "시작하기",
    onClick: () -> Unit,
    isVisible: Boolean = true,
    isEnable: Boolean = true
) {

    if (isVisible) {
        Box(
            modifier = Modifier
                .padding(top = 32.dp)
                .fillMaxWidth(),
            contentAlignment = Alignment.Center
        ) {
            Box(
                modifier = Modifier
                    .background(
                        shape = RoundedCornerShape(10.dp),
                        color = if (isEnable) highlightPurple else gray_6
                    )
                    .clickable(enabled = isEnable) {
                        onClick()
                    }
                    .padding(horizontal = 50.dp)
                    .wrapContentHeight(),
                contentAlignment = Alignment.Center
            ) {
                ButtonText(isEnabled = isEnable, text = text, paddingVertical = 9.dp)
            }

        }
    }

}

@Preview
@Composable
fun ButtonSelectInChat(
    text: String = "궁합 결과 보러가기",
    onClick: () -> Unit = {},
){
    Box(
        modifier = Modifier
            .padding(top = 16.dp)
            .fillMaxWidth()
            .background(
                shape = RoundedCornerShape(10.dp),
                color = highlightPurple
            )
            .clickable {
                onClick()
            }
            .wrapContentHeight(),
        contentAlignment = Alignment.Center
    ) {
        ButtonText(isEnabled = true, text = text, paddingVertical = 9.dp)
    }
}
