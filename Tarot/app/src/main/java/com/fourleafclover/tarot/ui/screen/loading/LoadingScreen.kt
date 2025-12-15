package com.fourleafclover.tarot.ui.screen.loading

import android.os.Handler
import android.os.Looper
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.fourleafclover.tarot.LocalIsDemo
import com.fourleafclover.tarot.demo.ui.theme.backgroundColorScheme
import com.fourleafclover.tarot.ui.component.LoadingCircle
import com.fourleafclover.tarot.ui.navigation.PreventBackPressed
import com.fourleafclover.tarot.ui.navigation.ScreenEnum
import com.fourleafclover.tarot.ui.navigation.navGraphViewModel
import com.fourleafclover.tarot.ui.screen.fortune.viewModel.FortuneViewModel
import com.fourleafclover.tarot.ui.screen.fortune.viewModel.PickTarotViewModel
import com.fourleafclover.tarot.ui.screen.fortune.viewModel.QuestionInputViewModel
import com.fourleafclover.tarot.ui.screen.harmony.viewmodel.LoadingViewModel
import com.fourleafclover.tarot.ui.screen.harmony.viewmodel.ResultViewModel
import com.fourleafclover.tarot.utils.getTarotResult
import kotlinx.coroutines.delay


@Composable
@Preview
fun LoadingScreen(
    navController: NavHostController = rememberNavController()
){

    val loadingViewModel = navGraphViewModel<LoadingViewModel>(navController)
    val resultViewModel = navGraphViewModel<ResultViewModel>(navController)
    val fortuneViewModel = navGraphViewModel<FortuneViewModel>(navController)
    val pickTarotViewModel = navGraphViewModel<PickTarotViewModel>(navController)
    val questionInputViewModel = navGraphViewModel<QuestionInputViewModel>(navController)
    val localContext = LocalContext.current
    val isDemo = LocalIsDemo.current

    if (!loadingViewModel.isLoading.value) {
        loadingViewModel.endLoading(navController)
    }

    PreventBackPressed()

    LaunchedEffect(Unit){
        if (loadingViewModel.destination == ScreenEnum.ResultScreen){

            if (isDemo) {
                delay(2500)
            }

            getTarotResult(
                localContext,
                resultViewModel = resultViewModel,
                pickTarotViewModel = pickTarotViewModel,
                loadingViewModel = loadingViewModel,
                questionInputViewModel = questionInputViewModel,
                isDemo,
                fortuneViewModel.pickedTopicState.value.topicNumber
            )
        }
        else if (loadingViewModel.destination == ScreenEnum.RoomResultScreen){
            if (resultViewModel.isMatchResultPrepared.value) {
                Handler(Looper.getMainLooper())
                    .postDelayed({
                        loadingViewModel.updateLoadingState(false)
                    }, 4000)
            }
        }
    }

    Column(modifier = Modifier
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