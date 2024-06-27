package com.fourleafclover.tarot.ui.screen.harmony

import com.fourleafclover.tarot.chatViewModel
import com.fourleafclover.tarot.harmonyViewModel
import com.fourleafclover.tarot.loadingViewModel
import io.socket.emitter.Emitter
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.json.JSONObject

/*                             <inviter>                      <invitee>
* connect               RoomCreateLoadingScreen               ShareUtil
* emit - create         RoomCreateLoadingScreen
* on - createComplete   RoomCreateLoadingScreen
* emit - join           RoomInviteLoadingScreen         RoomInviteLoadingScreen
* on - joinComplete     RoomInviteLoadingScreen         RoomInviteLoadingScreen
* on - partnerChecked       RoomChatScreen                   RoomChatScreen
* */

// 방 생성 완료
var onCreateComplete = Emitter.Listener { args ->
    CoroutineScope(Dispatchers.Main).launch {
        harmonyViewModel.roomCode.value = JSONObject(args[0].toString()).getString("roomCode")
    }
}

// 전원 입장 완료
var onJoinComplete = Emitter.Listener { args ->
    CoroutineScope(Dispatchers.Main).launch {
        val response = JSONObject(args[0].toString())
        val partnerNickname = response.getString("partnerNickname")
        harmonyViewModel.setPartnerNickname(partnerNickname)
        loadingViewModel.updateLoadingState(false)
    }
}

// 상대방이 시작하기 누름 or 카드 선택함
var onNext = Emitter.Listener { args ->
    CoroutineScope(Dispatchers.Main).launch {

        if (chatViewModel.chatState.value.scenario != chatViewModel.partnerChatState.value.scenario){
            chatViewModel.removeChatListLastItem()
            chatViewModel.addChatItem(
                Chat(
                    type = ChatType.GuidText,
                    text = "상대방이 답변을 선택했습니다.✨️"
                )
            )
            chatViewModel.addNextScenario()
        }
        
        chatViewModel.updatePartnerScenario()
    }
}

var onResult = Emitter.Listener { args ->
    CoroutineScope(Dispatchers.Main).launch {
        loadingViewModel.updateLoadingState(false)

        // TODO 받은 궁합 결과 저장
    }
}


