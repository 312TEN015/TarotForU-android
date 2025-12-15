package com.fourleafclover.tarot.ui.screen.my

import android.content.Context
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
import androidx.compose.foundation.shape.RoundedCornerShape
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
import com.fourleafclover.tarot.ui.component.HarmonyCardSlider
import com.fourleafclover.tarot.ui.component.getBackgroundModifier
import com.fourleafclover.tarot.ui.component.setStatusbarColor
import com.fourleafclover.tarot.ui.navigation.NavigateHomeOnBackPressed
import com.fourleafclover.tarot.ui.navigation.navGraphViewModel
import com.fourleafclover.tarot.ui.screen.fortune.viewModel.FortuneViewModel
import com.fourleafclover.tarot.ui.screen.my.viewmodel.ShareViewModel
import com.fourleafclover.tarot.ui.theme.TextB01M18
import com.fourleafclover.tarot.ui.theme.TextB02M16
import com.fourleafclover.tarot.ui.theme.TextB03M14
import com.fourleafclover.tarot.ui.theme.TextH01M26
import com.fourleafclover.tarot.ui.theme.TextH02M22

@Composable
@Preview
fun ShareHarmonyDetailScreen(
    navController: NavHostController = rememberNavController()){

    val shareViewModel = navGraphViewModel<ShareViewModel>(navController)
    val fortuneViewModel = navGraphViewModel<FortuneViewModel>(navController)

    val localContext = LocalContext.current
    val tarotSubjectData = fortuneViewModel.getPickedTopic(shareViewModel.sharedTarotResult.tarotType)
    setStatusbarColor(LocalView.current, MaterialTheme.backgroundColorScheme.mainBackgroundColor)

    NavigateHomeOnBackPressed(navController)

    Column(modifier = getBackgroundModifier(MaterialTheme.backgroundColorScheme.mainBackgroundColor))
    {

        AppBarPlain(navController = navController, title = "공유하기", backgroundColor = MaterialTheme.backgroundColorScheme.mainBackgroundColor, backButtonVisible = false)

        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())

        ) {
            Column(
                modifier = Modifier
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


            Column(
                modifier = Modifier
                    .padding(horizontal = 20.dp)
                    .background(color = MaterialTheme.backgroundColorScheme.secondaryBackgroundColor, shape = RoundedCornerShape(10.dp))
                    .padding(vertical = 24.dp, horizontal = 20.dp)
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 36.dp),
                    horizontalArrangement = Arrangement.Center
                ) {
                    Box(modifier = Modifier
                        .clickable {
                            shareViewModel.roomOwnerTab()
                        }
                        .padding(end = 4.dp)
                        .background(
                            shape = RoundedCornerShape(6.dp),
                            color = if (shareViewModel.isRoomOwnerTab()) MaterialTheme.backgroundColorScheme.activeTabColor
                            else MaterialTheme.backgroundColorScheme.inactiveTabColor
                        )
                        .padding(vertical = 8.dp, horizontal = 16.dp)
                        .weight(1f),
                        contentAlignment = Alignment.Center) {

                        TextB03M14(
                            color = if (shareViewModel.isRoomOwnerTab()) MaterialTheme.textColorScheme.onActiveTabColor
                            else MaterialTheme.textColorScheme.onInactiveTabColor,
                            text = "${shareViewModel.sharedTarotResult.overallResult?.firstUser}님의 카드",
                            textAlign = TextAlign.Center
                        )
                    }

                    Box(modifier = Modifier
                        .clickable {
                            shareViewModel.roomInviteeTab()
                        }
                        .padding(start = 4.dp)
                        .background(
                            shape = RoundedCornerShape(6.dp),
                            color = if (shareViewModel.isRoomOwnerTab()) MaterialTheme.backgroundColorScheme.inactiveTabColor
                            else MaterialTheme.backgroundColorScheme.activeTabColor
                        )
                        .padding(vertical = 8.dp, horizontal = 16.dp)
                        .weight(1f),
                        contentAlignment = Alignment.Center) {

                        TextB03M14(
                            color = if (shareViewModel.isRoomOwnerTab()) MaterialTheme.textColorScheme.onInactiveTabColor
                            else MaterialTheme.textColorScheme.onActiveTabColor,
                            text = "${shareViewModel.sharedTarotResult.overallResult?.secondUser}님의 카드",
                            textAlign = TextAlign.Center
                        )
                    }
                }


                HarmonyCardSlider(
                    outsideHorizontalPadding = 40.dp,
                    cardImageList = getSliderList(LocalContext.current, fortuneViewModel, shareViewModel),
                    firstCardResults = shareViewModel.roomOwnerCardResults,
                    secondCardResults = shareViewModel.inviteeCardResults,
                    isFirstTab = shareViewModel.isRoomOwnerTab()
                )

            }


            OverallResult(shareViewModel)

        }
    }

}

private fun getSliderList(context: Context, fortuneViewModel: FortuneViewModel, shareViewModel: ShareViewModel) : ArrayList<Int> {
    val sliderList: ArrayList<Int> = arrayListOf(0, 0, 0)
    if (shareViewModel.isRoomOwnerTab()) {
        sliderList[0] = fortuneViewModel.getCardImageId(context, shareViewModel.roomOwnerCardNumbers[0].toString())
        sliderList[1] = fortuneViewModel.getCardImageId(context, shareViewModel.roomOwnerCardNumbers[1].toString())
        sliderList[2] = fortuneViewModel.getCardImageId(context, shareViewModel.roomOwnerCardNumbers[2].toString())
    }else {
        sliderList[0] = fortuneViewModel.getCardImageId(context, shareViewModel.inviteeCardNumbers[0].toString())
        sliderList[1] = fortuneViewModel.getCardImageId(context, shareViewModel.inviteeCardNumbers[1].toString())
        sliderList[2] = fortuneViewModel.getCardImageId(context, shareViewModel.inviteeCardNumbers[2].toString())
    }

    return sliderList
}

@Composable
private fun OverallResult(shareViewModel: ShareViewModel) {

    Column(
        modifier = Modifier
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
            text = shareViewModel.sharedTarotResult.overallResult?.summary.toString(),
            color = MaterialTheme.textColorScheme.titleTextColor,
            modifier = Modifier
                .padding(top = 24.dp)
                .fillMaxWidth()
        )

        TextB02M16(
            text = shareViewModel.sharedTarotResult.overallResult?.full.toString(),
            color = MaterialTheme.textColorScheme.subTitleTextColor,
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 12.dp, bottom = 64.dp)
                .wrapContentHeight()
        )
    }
}

