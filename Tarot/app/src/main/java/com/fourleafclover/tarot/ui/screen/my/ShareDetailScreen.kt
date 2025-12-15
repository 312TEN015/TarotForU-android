package com.fourleafclover.tarot.ui.screen.my

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalView
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.fourleafclover.tarot.demo.ui.theme.backgroundColorScheme
import com.fourleafclover.tarot.demo.ui.theme.color.ColorSet
import com.fourleafclover.tarot.demo.ui.theme.textColorScheme
import com.fourleafclover.tarot.ui.component.AppBarPlain
import com.fourleafclover.tarot.ui.component.CardSlider
import com.fourleafclover.tarot.ui.component.backgroundModifier
import com.fourleafclover.tarot.ui.component.setStatusbarColor
import com.fourleafclover.tarot.ui.navigation.NavigateHomeOnBackPressed
import com.fourleafclover.tarot.ui.screen.fortune.viewModel.FortuneViewModel
import com.fourleafclover.tarot.ui.screen.my.viewmodel.ShareViewModel
import com.fourleafclover.tarot.ui.theme.TextB01M18
import com.fourleafclover.tarot.ui.theme.TextB02M16
import com.fourleafclover.tarot.ui.theme.TextB03M14
import com.fourleafclover.tarot.ui.theme.TextH01M26
import com.fourleafclover.tarot.ui.theme.TextH02M22

@Composable
@Preview
fun ShareDetailScreen(
    navController: NavHostController = rememberNavController(),
    fortuneViewModel: FortuneViewModel = hiltViewModel(),
    shareViewModel: ShareViewModel = hiltViewModel()
){
    val localContext = LocalContext.current
    val tarotSubjectData = fortuneViewModel.getPickedTopic(shareViewModel.sharedTarotResult.tarotType)
    setStatusbarColor(LocalView.current, MaterialTheme.backgroundColorScheme.secondaryBackgroundColor)

    NavigateHomeOnBackPressed(navController)

    Column(modifier = backgroundModifier)
    {

        AppBarPlain(navController = navController, title = "공유하기", backgroundColor = MaterialTheme.backgroundColorScheme.secondaryBackgroundColor, backButtonVisible = false)

        Column(modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState()))
        {
            Column(
                modifier = Modifier
                    .background(color = MaterialTheme.backgroundColorScheme.secondaryBackgroundColor)
                    .padding(top = 32.dp)
                    .wrapContentHeight()
                    .fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                TextB02M16(
                    text = tarotSubjectData.majorTopic,
                    color = MaterialTheme.textColorScheme.resultScreenSubTitleColor,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 16.dp),
                    textAlign = TextAlign.Center
                )

                val imoji = fortuneViewModel.getSubjectImoji(localContext, shareViewModel.sharedTarotResult.tarotType)
                TextH02M22(
                    text = "$imoji ${tarotSubjectData.majorQuestion} $imoji",
                    color = MaterialTheme.textColorScheme.resultScreenSubTitleColor,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 16.dp),
                    textAlign = TextAlign.Center
                )

                TextB03M14(
                    text = shareViewModel.sharedTarotResult.createdAt,
                    color = MaterialTheme.textColorScheme.resultScreenCreatedAtColor,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 32.dp),
                    textAlign = TextAlign.Center
                )
            }

            Box(modifier = Modifier.background(color = MaterialTheme.backgroundColorScheme.mainBackgroundColor)) {
                CardSlider(tarotResult = shareViewModel.sharedTarotResult, fortuneViewModel = fortuneViewModel)
            }

            Column(
                modifier = Modifier
                    .background(color = MaterialTheme.backgroundColorScheme.secondaryBackgroundColor)
                    .fillMaxWidth()
                    .padding(horizontal = 20.dp)
            ) {

                TextH01M26(
                    text = "타로 카드 종합 리딩",
                    color = MaterialTheme.textColorScheme.highlightTextColor,
                    modifier = Modifier.padding(top = 48.dp)
                )

                TextB01M18(
                    text = shareViewModel.sharedTarotResult.overallResult?.summary.toString(),
                    color = MaterialTheme.textColorScheme.titleTextColor,
                    modifier = Modifier.padding(top = 24.dp)
                )

                TextB02M16(
                    text = shareViewModel.sharedTarotResult.overallResult?.full.toString(),
                    color = MaterialTheme.textColorScheme.subTitleTextColor,
                    modifier = Modifier
                        .padding(top = 12.dp, bottom = 64.dp)
                        .wrapContentHeight()
                )
            }
        }
    }

}
