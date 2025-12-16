package com.fourleafclover.tarot.demo.ui.screen.loading

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.fourleafclover.tarot.demo.ui.theme.backgroundColorScheme
import com.fourleafclover.tarot.demo.ui.theme.color.ColorSet
import com.fourleafclover.tarot.ui.component.LoadingCircle
import com.fourleafclover.tarot.ui.component.getBackgroundModifier
import com.fourleafclover.tarot.ui.navigation.PreventBackPressed
import com.fourleafclover.tarot.ui.navigation.navGraphViewModel
import com.fourleafclover.tarot.ui.screen.harmony.emitCreate
import com.fourleafclover.tarot.ui.screen.harmony.setOnCreateComplete
import com.fourleafclover.tarot.ui.screen.harmony.viewmodel.HarmonyViewModel
import com.fourleafclover.tarot.ui.screen.harmony.viewmodel.LoadingViewModel
import kotlinx.coroutines.delay

// 추후 로딩 화면 컴포넌트화 하기
@Composable
@Preview
fun DemoRoomCreateLoadingScreen(
    navController: NavHostController = rememberNavController()
){

    val loadingViewModel = navGraphViewModel<LoadingViewModel>(navController)
    val harmonyViewModel = navGraphViewModel<HarmonyViewModel>(navController)

    if (!loadingViewModel.isLoading.value) {
        loadingViewModel.endLoading(navController)
    }

    PreventBackPressed()

    LaunchedEffect(Unit){
        // 새로운 방 생성
        delay(2000)
        loadingViewModel.updateLoadingState(false)
    }

    Column(modifier = getBackgroundModifier(color = MaterialTheme.backgroundColorScheme.mainBackgroundColor),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        LoadingCircle(
            loadingTitle = "${harmonyViewModel.getUserNickname()}님, 이제 궁합을\n확인하러 가볼까요?",
            loadingSubTitle = "상대방을 초대하고 함께\n실시간으로 궁합을 볼 수 있어요."
        )
    }
}