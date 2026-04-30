package com.fourleafclover.tarot.ui.screen.loading

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.fourleafclover.tarot.MyApplication
import com.fourleafclover.tarot.demo.ui.theme.backgroundColorScheme
import com.fourleafclover.tarot.ui.component.LoadingCircle
import com.fourleafclover.tarot.ui.component.getBackgroundModifier
import com.fourleafclover.tarot.ui.navigation.PreventBackPressed
import com.fourleafclover.tarot.ui.navigation.ScreenEnum
import com.fourleafclover.tarot.ui.navigation.navGraphViewModel
import com.fourleafclover.tarot.ui.screen.fortune.viewModel.FortuneViewModel
import com.fourleafclover.tarot.ui.screen.fortune.viewModel.PickTarotViewModel
import com.fourleafclover.tarot.ui.screen.fortune.viewModel.QuestionInputViewModel
import com.fourleafclover.tarot.ui.screen.harmony.viewmodel.LoadingViewModel
import com.fourleafclover.tarot.ui.screen.harmony.viewmodel.ResultViewModel
import kotlinx.coroutines.delay


@Composable
@Preview
fun LoadingScreen(
    navController: NavHostController = rememberNavController()
) {

    val loadingViewModel = navGraphViewModel<LoadingViewModel>(navController)
    val resultViewModel = navGraphViewModel<ResultViewModel>(navController)
    val fortuneViewModel = navGraphViewModel<FortuneViewModel>(navController)
    val pickTarotViewModel = navGraphViewModel<PickTarotViewModel>(navController)
    val questionInputViewModel = navGraphViewModel<QuestionInputViewModel>(navController)

    PreventBackPressed()

    LaunchedEffect(Unit) {
        when (loadingViewModel.destination) {
            ScreenEnum.ResultScreen -> {
                val ok = resultViewModel.fetchTarotResult(
                    pickTarotViewModel,
                    questionInputViewModel,
                    fortuneViewModel.pickedTopicState.value.topicNumber
                )
                if (!ok) {
                    MyApplication.toastUtil.makeShortToast("네트워크 상태를 확인 후 다시 시도해 주세요.")
                    loadingViewModel.changeDestination(ScreenEnum.HomeScreen)
                }
                loadingViewModel.endLoading(navController)
            }
            ScreenEnum.RoomResultScreen -> {
                // Match result is fetched by ChatViewModel via repository events.
                // Wait for the ready flag, then add a brief beat to keep the loading animation.
                while (!resultViewModel.isMatchResultPrepared.value) {
                    delay(200)
                }
                delay(4000)
                loadingViewModel.endLoading(navController)
            }
            else -> {}
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.backgroundColorScheme.secondaryBackgroundColor),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        LoadingCircle(
            loadingTitle = "선택하신 카드의 의미를\n열심히 해석하고 있어요!",
            loadingSubTitle = "잠시만 기다려주세요"
        )
    }
}
