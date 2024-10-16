package com.fourleafclover.tarot.ui.screen.loading

import android.util.Log
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.fourleafclover.tarot.MyApplication
import com.fourleafclover.tarot.harmonyShareViewModel
import com.fourleafclover.tarot.loadingViewModel
import com.fourleafclover.tarot.ui.component.LoadingCircle
import com.fourleafclover.tarot.ui.component.getBackgroundModifier
import com.fourleafclover.tarot.ui.navigation.PreventBackPressed
import com.fourleafclover.tarot.ui.screen.harmony.onCreateComplete
import com.fourleafclover.tarot.ui.theme.gray_9

// 추후 로딩 화면 컴포넌트화 하기
@Composable
@Preview
fun RoomCreateLoadingScreen(navController: NavHostController = rememberNavController()) {

    if (!loadingViewModel.isLoading.value) {
        loadingViewModel.endLoading(navController)
    }

    val context = LocalContext.current.applicationContext as MyApplication

    PreventBackPressed()

    LaunchedEffect(Unit){
        // 새로운 방 생성
        MyApplication.socket.on("createComplete", onCreateComplete)
        MyApplication.socket.emit("create")
        Log.d("socket-test", "emit create")


        /* 테스트 코드 */
//        Handler(Looper.getMainLooper())
//            .postDelayed({
//                onCreateComplete()
//            }, 4000)
    }

    Column(modifier = getBackgroundModifier(color = gray_9),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        LoadingCircle(
            loadingTitle = "${harmonyShareViewModel.getUserNickname()}님, 이제 궁합을\n확인하러 가볼까요?",
            loadingSubTitle = "상대방을 초대하고 함께\n실시간으로 궁합을 볼 수 있어요."
        )
    }
}