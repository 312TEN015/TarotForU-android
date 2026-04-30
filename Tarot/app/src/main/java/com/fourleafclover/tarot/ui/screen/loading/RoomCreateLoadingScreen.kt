package com.fourleafclover.tarot.ui.screen.loading

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.fourleafclover.tarot.demo.ui.theme.backgroundColorScheme
import com.fourleafclover.tarot.ui.component.LoadingCircle
import com.fourleafclover.tarot.ui.component.getBackgroundModifier
import com.fourleafclover.tarot.ui.navigation.PreventBackPressed
import com.fourleafclover.tarot.ui.navigation.navGraphViewModel
import com.fourleafclover.tarot.ui.screen.harmony.viewmodel.HarmonyViewModel
import com.fourleafclover.tarot.ui.screen.harmony.viewmodel.LoadingViewModel

@Composable
@Preview
fun RoomCreateLoadingScreen(
    navController: NavHostController = rememberNavController()
) {

    val loadingViewModel = navGraphViewModel<LoadingViewModel>(navController)
    val harmonyViewModel = navGraphViewModel<HarmonyViewModel>(navController)

    PreventBackPressed()

    LaunchedEffect(Unit) {
        harmonyViewModel.createRoomAndAwait()
        loadingViewModel.endLoading(navController)
    }

    Column(
        modifier = getBackgroundModifier(color = MaterialTheme.backgroundColorScheme.mainBackgroundColor),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        LoadingCircle(
            loadingTitle = "${harmonyViewModel.getUserNickname()}님, 이제 궁합을\n확인하러 가볼까요?",
            loadingSubTitle = "상대방을 초대하고 함께\n실시간으로 궁합을 볼 수 있어요."
        )
    }
}
