package com.fourleafclover.tarot.ui.screen.harmony

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
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.MaterialTheme
import androidx.compose.material3.Button
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.fourleafclover.tarot.LocalIsDemo
import com.fourleafclover.tarot.MyApplication
import com.fourleafclover.tarot.R
import com.fourleafclover.tarot.SubjectHarmony
import com.fourleafclover.tarot.data.TarotOutputDto
import com.fourleafclover.tarot.demo.ui.component.PrimaryButtonColors
import com.fourleafclover.tarot.demo.ui.component.SecondaryButtonColors
import com.fourleafclover.tarot.demo.ui.theme.backgroundColorScheme
import com.fourleafclover.tarot.demo.ui.theme.textColorScheme
import com.fourleafclover.tarot.demo.viewmodel.DemoViewModel
import com.fourleafclover.tarot.ui.component.AppBarCloseTarotResult
import com.fourleafclover.tarot.ui.component.ControlDialog
import com.fourleafclover.tarot.ui.component.HarmonyCardSlider
import com.fourleafclover.tarot.ui.component.getBackgroundModifier
import com.fourleafclover.tarot.ui.navigation.navGraphViewModel
import com.fourleafclover.tarot.ui.screen.fortune.saveToMyTarot
import com.fourleafclover.tarot.ui.screen.fortune.viewModel.FortuneViewModel
import com.fourleafclover.tarot.ui.screen.harmony.viewmodel.HarmonyViewModel
import com.fourleafclover.tarot.ui.screen.harmony.viewmodel.ResultViewModel
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
fun HarmonyResultScreen(
    navController: NavHostController,
) {

    val demoViewModel = navGraphViewModel<DemoViewModel>(navController)
    val fortuneViewModel = navGraphViewModel<FortuneViewModel>(navController)
    val harmonyViewModel = navGraphViewModel<HarmonyViewModel>(navController)
    val resultViewModel = navGraphViewModel<ResultViewModel>(navController)

    HarmonyResultScreenPreview(navController, harmonyViewModel, fortuneViewModel, resultViewModel, demoViewModel)
}

@Preview
@Composable
fun HarmonyResultScreenPreview(
    navController: NavHostController = rememberNavController(),
    harmonyViewModel: HarmonyViewModel = hiltViewModel(),
    fortuneViewModel: FortuneViewModel = hiltViewModel(),
    resultViewModel: ResultViewModel = hiltViewModel(),
    demoViewModel: DemoViewModel = hiltViewModel()
) {

    LaunchedEffect(Unit) {
        harmonyViewModel.deleteRoom()
        MyApplication.closeSocket()
    }

    Column(modifier =
        getBackgroundModifier(MaterialTheme.backgroundColorScheme.mainBackgroundColor)
        .verticalScroll(rememberScrollState())
    ) {
        ControlDialog(navController, resultViewModel)

        AppBarCloseTarotResult(
            navController,
            SubjectHarmony,
            MaterialTheme.backgroundColorScheme.mainBackgroundColor,
            true,
            resultViewModel
        )

        Column(
            modifier = Modifier
        ) {

            TextH02M22(
                text = "${if (resultViewModel.isMyTab()) harmonyViewModel.getUserNickname() 
                else harmonyViewModel.getPartnerNickname() }님이\n선택하신 카드는\n이런 의미를 담고 있어요.",
                color = MaterialTheme.textColorScheme.titleTextColor,
                modifier = Modifier
                    .background(color = MaterialTheme.backgroundColorScheme.mainBackgroundColor)
                    .padding(horizontal = 20.dp, vertical = 32.dp)
                    .fillMaxWidth()
            )


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
                            resultViewModel.myTab()
                        }
                        .padding(end = 4.dp)
                        .background(
                            shape = RoundedCornerShape(6.dp),
                            color = if (resultViewModel.isMyTab()) MaterialTheme.backgroundColorScheme.activeTabColor
                            else MaterialTheme.backgroundColorScheme.inactiveTabColor
                        )
                        .padding(vertical = 8.dp, horizontal = 16.dp)
                        .weight(1f),
                        contentAlignment = Alignment.Center) {

                        TextB03M14(
                            color = if (resultViewModel.isMyTab()) MaterialTheme.textColorScheme.onActiveTabColor
                            else MaterialTheme.textColorScheme.onInactiveTabColor,
                            text = "내가 선택한 카드",
                            textAlign = TextAlign.Center
                        )
                    }

                    Box(modifier = Modifier
                        .clickable {
                            resultViewModel.partnerTab()
                        }
                        .padding(start = 4.dp)
                        .background(
                            shape = RoundedCornerShape(6.dp),
                            color = if (resultViewModel.isMyTab()) MaterialTheme.backgroundColorScheme.inactiveTabColor
                            else MaterialTheme.backgroundColorScheme.activeTabColor
                        )
                        .padding(vertical = 8.dp, horizontal = 16.dp)
                        .weight(1f),
                        contentAlignment = Alignment.Center) {

                        TextB03M14(
                            color = if (resultViewModel.isMyTab()) MaterialTheme.textColorScheme.onInactiveTabColor else MaterialTheme.textColorScheme.onActiveTabColor ,
                            text = "상대방 카드",
                            textAlign = TextAlign.Center
                        )
                    }
                }

                HarmonyCardSlider(
                    outsideHorizontalPadding = 40.dp,
                    cardImageList = getSliderList(LocalContext.current, fortuneViewModel, resultViewModel),
                    firstCardResults = resultViewModel.myCardResults,
                    secondCardResults = resultViewModel.partnerCardResults,
                    isFirstTab = resultViewModel.isMyTab()
                )
            }


            OverallResult(resultViewModel.tarotResult.value, resultViewModel, harmonyViewModel)

        }

    }
}

private fun getSliderList(context: Context, fortuneViewModel: FortuneViewModel, resultViewModel: ResultViewModel) : ArrayList<Int> {
    val sliderList: ArrayList<Int> = arrayListOf(0, 0, 0)
    if (resultViewModel.isMyTab()) {
        sliderList[0] = fortuneViewModel.getCardImageId(context, resultViewModel.myCardNumbers[0].toString())
        sliderList[1] = fortuneViewModel.getCardImageId(context, resultViewModel.myCardNumbers[1].toString())
        sliderList[2] = fortuneViewModel.getCardImageId(context, resultViewModel.myCardNumbers[2].toString())
    }else {
        sliderList[0] = fortuneViewModel.getCardImageId(context, resultViewModel.partnerCardNumbers[0].toString())
        sliderList[1] = fortuneViewModel.getCardImageId(context, resultViewModel.partnerCardNumbers[1].toString())
        sliderList[2] = fortuneViewModel.getCardImageId(context, resultViewModel.partnerCardNumbers[2].toString())
    }

    return sliderList
}

@Composable
private fun OverallResult(
    tarotOutputDto: TarotOutputDto,
    resultViewModel: ResultViewModel,
    harmonyViewModel: HarmonyViewModel) {

    val isDemo = LocalIsDemo.current

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
            text = tarotOutputDto.overallResult?.summary.toString(),
            color = MaterialTheme.textColorScheme.titleTextColor,
            modifier = Modifier
                .padding(top = 24.dp)
                .fillMaxWidth()
        )

        TextB02M16(
            text = tarotOutputDto.overallResult?.full.toString(),
            color = MaterialTheme.textColorScheme.subTitleTextColor,
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 12.dp, bottom = 64.dp)
                .wrapContentHeight()
        )

        Button(
            onClick = {
                // 타로 결과 id 저장
                saveToMyTarot(resultViewModel, isDemo)
            },
            shape = RoundedCornerShape(10.dp),
            enabled = !resultViewModel.saveState.value,
            modifier = Modifier
                .wrapContentHeight()
                .fillMaxWidth()
                .padding(bottom = 8.dp),
            colors = SecondaryButtonColors()
        ) {

            if (resultViewModel.saveState.value) {
                Image(
                    painter = painterResource(id = R.drawable.check_filled_disabled),
                    contentDescription = null,
                    modifier = Modifier
                        .size(22.dp)
                        .padding(end = 4.dp),
                    alignment = Alignment.Center
                )
            }

            TextButtonM16(
                text = if (resultViewModel.saveState.value) "저장 완료!" else "타로 저장하기",
                modifier = Modifier.padding(vertical = 8.dp),
                color = if (!resultViewModel.saveState.value) MaterialTheme.textColorScheme.onActivePrimaryButtonColor
                else MaterialTheme.textColorScheme.onDisabledButtonColor ,
            )
        }

        Button(
            onClick = { resultViewModel.openCloseDialog() },
            shape = RoundedCornerShape(10.dp),
            modifier = Modifier
                .wrapContentHeight()
                .fillMaxWidth(),
            colors = PrimaryButtonColors()
        ) {
            TextButtonM16(
                text = "홈으로 돌아가기",
                modifier = Modifier.padding(vertical = 8.dp),
                color = MaterialTheme.textColorScheme.onActiveSecondaryButtonColor
            )
        }

        Row(
            modifier = Modifier
                .padding(vertical = 16.dp, horizontal = 16.dp)
                .padding(bottom = 45.dp)
                .clickable {
                    setDynamicLink(
                        localContext,
                        tarotOutputDto.tarotId,
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
