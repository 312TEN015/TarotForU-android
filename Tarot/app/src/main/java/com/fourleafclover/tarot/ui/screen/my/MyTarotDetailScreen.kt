package com.fourleafclover.tarot.ui.screen.my

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
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
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.fourleafclover.tarot.R
import com.fourleafclover.tarot.demo.ui.theme.backgroundColorScheme
import com.fourleafclover.tarot.demo.ui.theme.color.ColorSet
import com.fourleafclover.tarot.demo.ui.theme.textColorScheme
import com.fourleafclover.tarot.ui.component.AppBarPlain
import com.fourleafclover.tarot.ui.component.CardSlider
import com.fourleafclover.tarot.ui.component.backgroundModifier
import com.fourleafclover.tarot.ui.component.setStatusbarColor
import com.fourleafclover.tarot.ui.navigation.navGraphViewModel
import com.fourleafclover.tarot.ui.screen.fortune.getCardSliderImage
import com.fourleafclover.tarot.ui.screen.fortune.viewModel.FortuneViewModel
import com.fourleafclover.tarot.ui.screen.harmony.viewmodel.HarmonyViewModel
import com.fourleafclover.tarot.ui.screen.my.viewmodel.MyTarotViewModel
import com.fourleafclover.tarot.ui.theme.TextB01M18
import com.fourleafclover.tarot.ui.theme.TextB02M16
import com.fourleafclover.tarot.ui.theme.TextB03M14
import com.fourleafclover.tarot.ui.theme.TextButtonM16
import com.fourleafclover.tarot.ui.theme.TextH01M26
import com.fourleafclover.tarot.ui.theme.TextH02M22
import com.fourleafclover.tarot.utils.ShareActionType
import com.fourleafclover.tarot.utils.ShareLinkType
import com.fourleafclover.tarot.utils.setDynamicLink

@Composable
@Preview
fun MyTarotDetailScreen(
    navController: NavHostController = rememberNavController(),
) {
    val myTarotViewModel = navGraphViewModel<MyTarotViewModel>(navController)
    val fortuneViewModel = navGraphViewModel<FortuneViewModel>(navController)
    val harmonyViewModel = navGraphViewModel<HarmonyViewModel>(navController)

    val localContext = LocalContext.current
    val tarotSubjectData = fortuneViewModel.getPickedTopic(myTarotViewModel.selectedTarotResult.tarotType)
    setStatusbarColor(LocalView.current, MaterialTheme.backgroundColorScheme.secondaryBackgroundColor)

    Column(modifier = backgroundModifier)
    {

        AppBarPlain(
            navController = navController,
            title = "MY 타로",
            backgroundColor = MaterialTheme.backgroundColorScheme.secondaryBackgroundColor,
            backButtonVisible = true
        )

        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
        )
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

                val imoji = fortuneViewModel.getSubjectImoji(
                    localContext,
                    myTarotViewModel.selectedTarotResult.tarotType
                )
                TextH02M22(
                    text = "$imoji ${tarotSubjectData.majorQuestion} $imoji",
                    color = MaterialTheme.textColorScheme.resultScreenSubTitleColor,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 16.dp),
                    textAlign = TextAlign.Center
                )

                TextB03M14(
                    text = myTarotViewModel.selectedTarotResult.createdAt,
                    color = MaterialTheme.textColorScheme.resultScreenCreatedAtColor,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 32.dp),
                    textAlign = TextAlign.Center
                )
            }


            Box(modifier = Modifier.background(color = MaterialTheme.backgroundColorScheme.mainBackgroundColor)) {
                val result = myTarotViewModel.selectedTarotResult
                CardSlider(
                    tarotResult = result,
                    cardImageList = getCardSliderImage(
                        LocalContext.current,
                        result.cards,
                        fortuneViewModel
                    )
                )
            }

            Column(
                modifier = Modifier
                    .background(color = MaterialTheme.backgroundColorScheme.secondaryBackgroundColor)
                    .fillMaxWidth()
                    .padding(horizontal = 20.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {

                TextH01M26(
                    text = "타로 카드 종합 리딩",
                    color = MaterialTheme.textColorScheme.highlightTextColor,
                    modifier = Modifier
                        .padding(top = 48.dp)
                        .fillMaxWidth()
                )

                TextB01M18(
                    text = myTarotViewModel.selectedTarotResult.overallResult?.summary.toString(),
                    color = MaterialTheme.textColorScheme.titleTextColor,
                    modifier = Modifier
                        .padding(top = 24.dp)
                        .fillMaxWidth()
                )

                TextB02M16(
                    text = myTarotViewModel.selectedTarotResult.overallResult?.full.toString(),
                    color = MaterialTheme.textColorScheme.subTitleTextColor,
                    modifier = Modifier
                        .padding(top = 12.dp, bottom = 64.dp)
                        .wrapContentHeight()
                )

                Row(
                    modifier = Modifier
                        .padding(vertical = 16.dp, horizontal = 16.dp)
                        .padding(bottom = 45.dp)
                        .clickable {
                            setDynamicLink(
                                localContext,
                                myTarotViewModel.selectedTarotResult.tarotId,
                                ShareLinkType.MY,
                                ShareActionType.OPEN_SHEET,
                                harmonyViewModel
                            )
                        },
                    horizontalArrangement = Arrangement.Center
                )
                {
                    Image(
                        painter = painterResource(id = R.drawable.share),
                        contentDescription = null,
                        modifier = Modifier.padding(end = 3.dp)
                    )
                    TextButtonM16(
                        text = "공유하기",
                        color = MaterialTheme.textColorScheme.subTitleTextColor
                    )
                }
            }
        }
    }

}
