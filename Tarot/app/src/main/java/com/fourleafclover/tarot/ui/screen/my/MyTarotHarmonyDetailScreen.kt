package com.fourleafclover.tarot.ui.screen.my

import android.content.Context
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
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
import com.fourleafclover.tarot.ui.component.HarmonyCardSlider
import com.fourleafclover.tarot.ui.component.getBackgroundModifier
import com.fourleafclover.tarot.ui.component.setStatusbarColor
import com.fourleafclover.tarot.ui.navigation.navGraphViewModel
import com.fourleafclover.tarot.ui.screen.fortune.viewModel.FortuneViewModel
import com.fourleafclover.tarot.ui.screen.fortune.viewModel.PickTarotViewModel
import com.fourleafclover.tarot.ui.screen.harmony.viewmodel.ChatViewModel
import com.fourleafclover.tarot.ui.screen.harmony.viewmodel.HarmonyViewModel
import com.fourleafclover.tarot.ui.screen.harmony.viewmodel.LoadingViewModel
import com.fourleafclover.tarot.ui.screen.harmony.viewmodel.ResultViewModel
import com.fourleafclover.tarot.ui.screen.main.DialogViewModel
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
fun MyTarotHarmonyDetail(
    navController: NavHostController
) {

    val myTarotViewModel = navGraphViewModel<MyTarotViewModel>(navController)
    val fortuneViewModel = navGraphViewModel<FortuneViewModel>(navController)

    MyTarotHarmonyDetailPreview(navController, fortuneViewModel, myTarotViewModel)
}

@Preview
@Composable
fun MyTarotHarmonyDetailPreview(
    navController: NavHostController = rememberNavController(),
    fortuneViewModel: FortuneViewModel = hiltViewModel(),
    myTarotViewModel: MyTarotViewModel = hiltViewModel(),
    harmonyViewModel: HarmonyViewModel = hiltViewModel()
) {
    val localContext = LocalContext.current
    val tarotSubjectData = fortuneViewModel.getPickedTopic(5)
    setStatusbarColor(LocalView.current, MaterialTheme.backgroundColorScheme.mainBackgroundColor)

    Column(modifier = getBackgroundModifier(MaterialTheme.backgroundColorScheme.mainBackgroundColor).verticalScroll(rememberScrollState()))
    {

        AppBarPlain(navController = navController, title = "MY 타로", backgroundColor = MaterialTheme.backgroundColorScheme.mainBackgroundColor, backButtonVisible = true)

        Column(
            modifier = Modifier
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

                val imoji = fortuneViewModel.getSubjectImoji(localContext, myTarotViewModel.selectedTarotResult.tarotType)
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
                            myTarotViewModel.roomOwnerTab()
                        }
                        .padding(end = 4.dp)
                        .background(
                            shape = RoundedCornerShape(6.dp),
                            color = if (myTarotViewModel.isRoomOwnerTab()) MaterialTheme.backgroundColorScheme.activeTabColor
                            else MaterialTheme.backgroundColorScheme.inactiveTabColor
                        )
                        .padding(vertical = 8.dp, horizontal = 16.dp)
                        .weight(1f),
                        contentAlignment = Alignment.Center) {

                        TextB03M14(
                            color = if (myTarotViewModel.isRoomOwnerTab()) MaterialTheme.textColorScheme.onActiveTabColor
                            else MaterialTheme.textColorScheme.onInactiveTabColor,
                            text = "${myTarotViewModel.selectedTarotResult.overallResult?.firstUser}님의 카드",
                            textAlign = TextAlign.Center
                        )
                    }

                    Box(modifier = Modifier
                        .clickable {
                            myTarotViewModel.roomInviteeTab()
                        }
                        .padding(start = 4.dp)
                        .background(
                            shape = RoundedCornerShape(6.dp),
                            color = if (myTarotViewModel.isRoomOwnerTab()) MaterialTheme.backgroundColorScheme.inactiveTabColor
                            else MaterialTheme.backgroundColorScheme.activeTabColor
                        )
                        .padding(vertical = 8.dp, horizontal = 16.dp)
                        .weight(1f),
                        contentAlignment = Alignment.Center) {

                        TextB03M14(
                            color = if (myTarotViewModel.isRoomOwnerTab()) MaterialTheme.textColorScheme.onInactiveTabColor
                            else MaterialTheme.textColorScheme.onActiveTabColor,
                            text = "${myTarotViewModel.selectedTarotResult.overallResult?.secondUser}님의 카드",
                            textAlign = TextAlign.Center
                        )
                    }
                }


                HarmonyCardSlider(
                    outsideHorizontalPadding = 40.dp,
                    cardImageList = getSliderList(LocalContext.current, fortuneViewModel, myTarotViewModel),
                    firstCardResults = myTarotViewModel.roomOwnerCardResults,
                    secondCardResults = myTarotViewModel.inviteeCardResults,
                    isFirstTab = myTarotViewModel.isRoomOwnerTab()
                )

            }


            OverallResult(myTarotViewModel, harmonyViewModel)

        }

    }
}

private fun getSliderList(context: Context, fortuneViewModel: FortuneViewModel, myTarotViewModel: MyTarotViewModel) : ArrayList<Int> {
    val sliderList: ArrayList<Int> = arrayListOf(0, 0, 0)
    if (myTarotViewModel.isRoomOwnerTab()) {
        sliderList[0] = fortuneViewModel.getCardImageId(context, myTarotViewModel.roomOwnerCardNumbers[0].toString())
        sliderList[1] = fortuneViewModel.getCardImageId(context, myTarotViewModel.roomOwnerCardNumbers[1].toString())
        sliderList[2] = fortuneViewModel.getCardImageId(context, myTarotViewModel.roomOwnerCardNumbers[2].toString())
    }else {
        sliderList[0] = fortuneViewModel.getCardImageId(context, myTarotViewModel.inviteeCardNumbers[0].toString())
        sliderList[1] = fortuneViewModel.getCardImageId(context, myTarotViewModel.inviteeCardNumbers[1].toString())
        sliderList[2] = fortuneViewModel.getCardImageId(context, myTarotViewModel.inviteeCardNumbers[2].toString())
    }

    return sliderList
}


@Composable
private fun OverallResult(myTarotViewModel: MyTarotViewModel, harmonyViewModel: HarmonyViewModel) {

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 20.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        val localContext = LocalContext.current

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
                .fillMaxWidth()
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
