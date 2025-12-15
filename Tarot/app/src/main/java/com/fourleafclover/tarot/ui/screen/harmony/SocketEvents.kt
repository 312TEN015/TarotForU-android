package com.fourleafclover.tarot.ui.screen.harmony

import android.util.Log
import androidx.navigation.NavHostController
import com.fourleafclover.tarot.MyApplication
import com.fourleafclover.tarot.ui.navigation.ScreenEnum
import com.fourleafclover.tarot.ui.navigation.navigateInclusive
import com.fourleafclover.tarot.ui.screen.fortune.viewModel.PickTarotViewModel
import com.fourleafclover.tarot.ui.screen.harmony.viewmodel.CardDeckStatus
import com.fourleafclover.tarot.ui.screen.harmony.viewmodel.Chat
import com.fourleafclover.tarot.ui.screen.harmony.viewmodel.ChatType
import com.fourleafclover.tarot.ui.screen.harmony.viewmodel.ChatViewModel
import com.fourleafclover.tarot.ui.screen.harmony.viewmodel.HarmonyViewModel
import com.fourleafclover.tarot.ui.screen.harmony.viewmodel.LoadingViewModel
import com.fourleafclover.tarot.ui.screen.harmony.viewmodel.ResultViewModel
import com.fourleafclover.tarot.ui.screen.main.DialogViewModel
import com.fourleafclover.tarot.utils.getCertainTarotDetail
import com.fourleafclover.tarot.utils.getMatchResult
import io.socket.emitter.Emitter
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.json.JSONArray
import org.json.JSONObject
import java.util.Arrays

/*                             <inviter>                      <invitee>
* connect               RoomCreateLoadingScreen               ShareUtil
* emit - create         RoomCreateLoadingScreen
* on - createComplete   RoomCreateLoadingScreen
* emit - join           RoomInviteLoadingScreen         RoomInviteLoadingScreen
* on - joinComplete     RoomInviteLoadingScreen         RoomInviteLoadingScreen
* on - partnerChecked       RoomChatScreen                   RoomChatScreen
* */


fun setOnCreateComplete(harmonyViewModel: HarmonyViewModel, loadingViewModel: LoadingViewModel) {

    // 방 생성 완료
    val onCreateComplete = Emitter.Listener { args ->
        CoroutineScope(Dispatchers.Main).launch {
            Log.d("socket-test", "createComplete " + args[0].toString())

            val roomId = JSONObject(args[0].toString()).getString("roomId")
            harmonyViewModel.createNewRoom(roomId)

            loadingViewModel.updateLoadingState(false)
        }
    }

    MyApplication.socket.on("createComplete", onCreateComplete)
}

fun emitCreate() {
    CoroutineScope(Dispatchers.IO).launch {
        try {
            MyApplication.socket.emit("create")
            withContext(Dispatchers.Main) {
                Log.d("socket-test", "emit create success")
            }
        } catch (e: Exception) {
            withContext(Dispatchers.Main) {
                Log.d("socket-test", "emit create fail")
            }
        }
    }
}

fun setOnJoin(harmonyViewModel: HarmonyViewModel, loadingViewModel: LoadingViewModel, chatViewModel: ChatViewModel) {

    // 전원 입장 완료
    val onJoinComplete = Emitter.Listener { args ->
        CoroutineScope(Dispatchers.Main).launch {
            Log.d("socket-test", "joinComplete " + args[0].toString())

            val response = JSONObject(args[0].toString())
            val nicknameArray: JSONArray = response.getJSONArray("nickname")
            val nicknames: List<String> = mutableListOf<String>().apply {
                for (i in 0 until nicknameArray.length()) {
                    add(nicknameArray.getString(i))
                }
            }

            val partnerNickname =
                nicknames.find { it != harmonyViewModel.getUserNickname() } ?: " "
            harmonyViewModel.setPartnerNickname(partnerNickname)
            chatViewModel.startChat(harmonyViewModel.getUserNickname(), partnerNickname)
            loadingViewModel.updateLoadingState(false)
        }
    }

    MyApplication.socket.on("joinComplete", onJoinComplete)
}


fun emitJoin(harmonyViewModel: HarmonyViewModel) {

    CoroutineScope(Dispatchers.IO).launch {
        try {
            val jsonObject = JSONObject()
            jsonObject.put("nickname", harmonyViewModel.getUserNickname())
            jsonObject.put("roomId", harmonyViewModel.roomId.value)
            MyApplication.socket.emit("join", jsonObject)

            withContext(Dispatchers.Main) {
                Log.d("socket-test", "emit join success")
            }
        } catch (e: Exception) {
            withContext(Dispatchers.Main) {
                Log.d("socket-test", "emit join fail")
            }
        }
    }
}

fun emitStart(harmonyViewModel: HarmonyViewModel) {

    CoroutineScope(Dispatchers.IO).launch {
        try {
            val jsonObject = JSONObject()
            jsonObject.put("nickname", harmonyViewModel.getUserNickname())
            jsonObject.put("roomId", harmonyViewModel.roomId.value)
            MyApplication.socket.emit("start", jsonObject)

            withContext(Dispatchers.Main) {
                Log.d("socket-test", "roomId - ${harmonyViewModel.roomId.value}")
                Log.d("socket-test", "emit start")
            }
        } catch (e: Exception) {
            withContext(Dispatchers.Main) {
                Log.d("socket-test", "emit start fail")
            }
        }
    }
}

fun emitCardSelect(harmonyViewModel: HarmonyViewModel, pickTarotViewModel: PickTarotViewModel, pickSequence: Int) {

    CoroutineScope(Dispatchers.IO).launch {
        try {
            val jsonObject = JSONObject()
            jsonObject.put("nickname", harmonyViewModel.getUserNickname())
            jsonObject.put("roomId", harmonyViewModel.roomId.value)
            jsonObject.put("cardNum", pickTarotViewModel.getCardNumber(pickSequence))
            MyApplication.socket.emit("cardSelect", jsonObject)

            withContext(Dispatchers.Main) {
                Log.d("socket-test", "emit cardSelect success")
            }
        } catch (e: Exception) {
            withContext(Dispatchers.Main) {
                Log.d("socket-test", "emit cardSelect fail")
            }
        }
    }
}


fun setOnNext(
    harmonyViewModel: HarmonyViewModel,
    loadingViewModel: LoadingViewModel,
    chatViewModel: ChatViewModel,
    pickTarotViewModel: PickTarotViewModel
) {

    fun checkIsAllCardPicked() : Boolean {
        return (pickTarotViewModel.pickedCardNumberState.value.firstCardNumber != -1
                && pickTarotViewModel.pickedCardNumberState.value.secondCardNumber != -1
                && pickTarotViewModel.pickedCardNumberState.value.thirdCardNumber != -1
                && chatViewModel.partnerChatState.value.pickedCardNumberState.firstCardNumber != -1
                && chatViewModel.partnerChatState.value.pickedCardNumberState.firstCardNumber != -1
                && chatViewModel.partnerChatState.value.pickedCardNumberState.firstCardNumber != -1
                )
    }

    // 상대방이 시작하기 누름 or 카드 선택함
    val onNext = Emitter.Listener { args ->
        // 상대방 업데이트 한 후에
        chatViewModel.updatePartnerScenario()

        val myScenario = chatViewModel.chatState.value.scenario
        val partnerScenario = chatViewModel.partnerChatState.value.scenario
        Log.d("socket-test", "onNext my: " + myScenario.name)
        Log.d("socket-test", "onNext partner: " + partnerScenario.name)
        Log.d("socket-test", "onNext args: ${Arrays.toString(args)}")

        // 업데이트 한게 나랑 다름 = 내가 뒤처짐
        if (myScenario != partnerScenario){
            if (args.isNotEmpty()){
                val partnerCardNum = JSONObject(args[0].toString()).getString("cardNum")
                chatViewModel.updatePartnerCardNumber(chatViewModel.getBeforeScenario(partnerScenario), partnerCardNum.toInt())
                Log.d("socket-test", "onNext partner card - $partnerCardNum")
            }else{
                Log.d("socket-test", "onNext - partner started")
            }
        }
        // 업데이트 한게 나랑 같음 = 내가 먼저함
        else {
            chatViewModel.updateGuidText("상대방이 답변을 선택했습니다.✨️")
            if (args.isNotEmpty()){
                val partnerCardNum = JSONObject(args[0].toString()).getString("cardNum")
                chatViewModel.updatePartnerCardNumber(chatViewModel.getBeforeScenario(partnerScenario), partnerCardNum.toInt())
                Log.d("socket-test", "onNext partner card - $partnerCardNum")
            }else{
                Log.d("socket-test", "onNext - I started")
            }
            chatViewModel.addNextScenario()
        }

        Log.d("socket-test", "onNext my cards : "
                + chatViewModel.chatState.value.pickedCardNumberState.firstCardNumber + ", "
                + chatViewModel.chatState.value.pickedCardNumberState.secondCardNumber + ", "
                + chatViewModel.chatState.value.pickedCardNumberState.thirdCardNumber)
        Log.d("socket-test", "onNext partner cards : "
                + chatViewModel.partnerChatState.value.pickedCardNumberState.firstCardNumber + ", "
                + chatViewModel.partnerChatState.value.pickedCardNumberState.secondCardNumber + ", "
                + chatViewModel.partnerChatState.value.pickedCardNumberState.thirdCardNumber)

        // 둘다 완료된 경우
        if (checkIsAllCardPicked()) {
            Log.d("socket-test", "onNext result request send")
            chatViewModel.updatePickedCardNumberState(pickTarotViewModel.pickedCardNumberState.value)
            getMatchResult(harmonyViewModel, loadingViewModel, chatViewModel)
        }
    }


    MyApplication.socket.on("next", onNext)

    pickTarotViewModel.initCardDeck()
}

fun setOnResult(
    harmonyViewModel: HarmonyViewModel,
    loadingViewModel: LoadingViewModel,
    resultViewModel: ResultViewModel) {

    // 응답 생성 완료
    val onResult = Emitter.Listener { args ->
        Log.d("socket-test", "resultPrepared " + args[0].toString())

        if (args.isNotEmpty()){
            val tarotId = JSONObject(args[0].toString()).getString("tarotId")

            // 궁합 결과 가져오기 실패한 경우
            if (tarotId.isNullOrEmpty()) {
                CoroutineScope(Dispatchers.Main).launch {
                    loadingViewModel.changeDestination(ScreenEnum.HomeScreen)
                    loadingViewModel.updateLoadingState(false)
                    MyApplication.toastUtil.makeShortToast("네트워크 상태를 확인 후 다시 시도해 주세요.")
                }
            }
            // 성공한 경우
            else{
                getCertainTarotDetail(
                    tarotId,
                    loadingViewModel,
                    onResponse = {
                        resultViewModel.setIsMatchResultPrepared(true)
                        resultViewModel.distinguishCardResult(it, harmonyViewModel.isRoomOwner.value)
                        loadingViewModel.updateLoadingState(false)
                    }
                )
            }

            MyApplication.closeSocket()

        }
    }

    MyApplication.socket.on("resultPrepared", onResult)

}

fun emitResultPrepared(harmonyViewModel: HarmonyViewModel, tarotId: String = "") {
    CoroutineScope(Dispatchers.IO).launch {
        try {
            val jsonObject = JSONObject()
            jsonObject.put("roomId", harmonyViewModel.roomId.value)
            jsonObject.put("tarotId", tarotId)
            MyApplication.socket.emit("resultReceived", jsonObject)

            withContext(Dispatchers.Main) {
                Log.d("socket-test", "emit resultReceived success")
            }
        } catch (e: Exception) {
            // 오류 처리
            withContext(Dispatchers.Main) {
                Log.d("socket-test", "emit resultReceived failed")
                Log.e("socket-test", "exception: ${e.message}")
                harmonyViewModel.deleteRoom()
                MyApplication.closeSocket()
            }
        }
    }


}

fun setOnExit(chatViewModel: ChatViewModel, harmonyViewModel: HarmonyViewModel) {
    val onExit = Emitter.Listener { args ->
        Log.d("socket-test", "onExit")

        chatViewModel.updateCardDeckStatus(CardDeckStatus.Gathered)

        chatViewModel.addChatItem(Chat(ChatType.PartnerChatText, "상대방이 초대방에서 나가셔서 궁합 보기가 중단되었어요. 아쉽지만 다음 기회에 다시 봐요\uD83D\uDE22"))
        chatViewModel.addChatItem(Chat(ChatType.GuidText, "궁합 보기가 종료되었습니다⚡"))

        chatViewModel.setExit(true)

        MyApplication.closeSocket()
        harmonyViewModel.deleteRoom()
    }

    MyApplication.socket.on("exit", onExit)
}


fun emitExit(harmonyViewModel: HarmonyViewModel) {
    CoroutineScope(Dispatchers.IO).launch {
        try {
            val jsonObject = JSONObject()
            jsonObject.put("roomId", harmonyViewModel.roomId.value)
            Log.d("socket-test", "exit roomId - ${harmonyViewModel.roomId.value}")
            MyApplication.socket.emit("exit", jsonObject)

            withContext(Dispatchers.Main) {
                Log.d("socket-test", "emit exit success")
            }
        } catch (e: Exception) {

            withContext(Dispatchers.Main) {
                Log.d("socket-test", "emit exit failed")
                Log.e("socket-test", "exception: ${e.message}")
                harmonyViewModel.deleteRoom()
                MyApplication.closeSocket()
            }
        }
    }
}


