package com.fourleafclover.tarot.ui.screen.loading

import android.util.Log
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.fourleafclover.tarot.MyApplication
import com.fourleafclover.tarot.SubjectHarmony
import com.fourleafclover.tarot.harmonyShareViewModel
import com.fourleafclover.tarot.loadingViewModel
import com.fourleafclover.tarot.ui.component.AppBarCloseChatWithDialog
import com.fourleafclover.tarot.ui.component.AppBarCloseWithDialog
import com.fourleafclover.tarot.ui.component.LoadingCircle
import com.fourleafclover.tarot.ui.component.ShareLinkOrCopy
import com.fourleafclover.tarot.ui.component.getBackgroundModifier
import com.fourleafclover.tarot.ui.navigation.OpenDialogOnBackPressed
import com.fourleafclover.tarot.ui.navigation.PreventBackPressed
import com.fourleafclover.tarot.ui.screen.harmony.onJoinComplete
import com.fourleafclover.tarot.ui.theme.TextB03M14
import com.fourleafclover.tarot.ui.theme.backgroundColor_2
import com.fourleafclover.tarot.ui.theme.gray_5
import org.json.JSONObject

// 추후 로딩 화면 컴포넌트화 하기
@Composable
@Preview
fun RoomInviteLoadingScreen(navController: NavHostController = rememberNavController()) {

    if (!loadingViewModel.isLoading.value) {
        loadingViewModel.endLoading(navController)
    }

    PreventBackPressed()

    LaunchedEffect(Unit){
        val jsonObject = JSONObject()
        jsonObject.put("nickname", harmonyShareViewModel.getUserNickname())
        jsonObject.put("roomId", harmonyShareViewModel.roomId.value)
        MyApplication.socket.on("joinComplete", onJoinComplete)
        MyApplication.socket.emit("join", jsonObject)
        Log.d("socket-test", "set onJoinComplete")
        Log.d("socket-test", "emit join")


        /* 테스트 코드 */
//        Handler(Looper.getMainLooper())
//            .postDelayed({
//                onJoinComplete()
//            }, 4000)
    }
    

    Column(modifier = getBackgroundModifier(backgroundColor_2)) {
        AppBarCloseChatWithDialog(
            navController = navController,
            pickedTopicTemplate = SubjectHarmony,
            backgroundColor = backgroundColor_2,
            isTitleVisible = false
        )

        Column(
            modifier = getBackgroundModifier(backgroundColor_2)
                .padding(horizontal = 20.dp)
                .padding(bottom = 24.dp),
            horizontalAlignment = Alignment.CenterHorizontally,

            ) {


            LoadingCircle(
                modifier = Modifier.weight(1f),
                "상대방을 기다리는 중입니다...",
                "1시간 안에 모두 입장하지 않으면 초대방이 사라져요!"
            )

            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {

                TextB03M14(
                    text = "상대방이 계속 들어오지 않는다면?\n한번 더 초대 링크를 공유해보세요!",
                    color = gray_5,
                    textAlign = TextAlign.Center
                )

                ShareLinkOrCopy()

            }


        }
    }

}