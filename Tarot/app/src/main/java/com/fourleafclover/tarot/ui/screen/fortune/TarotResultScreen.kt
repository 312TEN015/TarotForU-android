package com.fourleafclover.tarot.ui.screen.fortune

import android.app.Activity
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
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.currentCompositionLocalContext
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.fourleafclover.tarot.MyApplication
import com.fourleafclover.tarot.R
import com.fourleafclover.tarot.demo.data.demoTarotResult
import com.fourleafclover.tarot.demo.ui.component.PrimaryButtonColors
import com.fourleafclover.tarot.demo.ui.component.SecondaryButtonColors
import com.fourleafclover.tarot.demo.ui.theme.backgroundColorScheme
import com.fourleafclover.tarot.demo.ui.theme.textColorScheme
import com.fourleafclover.tarot.demo.viewmodel.DemoViewModel
import com.fourleafclover.tarot.ui.component.CardSlider
import com.fourleafclover.tarot.ui.component.ControlDialog
import com.fourleafclover.tarot.ui.component.appBarModifier
import com.fourleafclover.tarot.ui.component.backgroundModifier
import com.fourleafclover.tarot.ui.screen.fortune.viewModel.FortuneViewModel
import com.fourleafclover.tarot.ui.screen.harmony.viewmodel.HarmonyViewModel
import com.fourleafclover.tarot.ui.screen.harmony.viewmodel.ResultViewModel
import com.fourleafclover.tarot.ui.theme.TextB01M18
import com.fourleafclover.tarot.ui.theme.TextB02M16
import com.fourleafclover.tarot.ui.theme.TextButtonM16
import com.fourleafclover.tarot.ui.theme.TextH01M26
import com.fourleafclover.tarot.ui.theme.TextH02M22
import com.fourleafclover.tarot.ui.theme.getTextStyle
import com.fourleafclover.tarot.utils.ShareActionType
import com.fourleafclover.tarot.utils.ShareLinkType
import com.fourleafclover.tarot.utils.setDynamicLink

@Composable
@Preview
fun TarotResultScreen(
    navController: NavHostController = rememberNavController(),
    fortuneViewModel: FortuneViewModel = hiltViewModel(),
    resultViewModel: ResultViewModel = hiltViewModel(),
    harmonyViewModel: HarmonyViewModel = hiltViewModel(),
    demoViewModel: DemoViewModel = hiltViewModel()
){

    Column(modifier = backgroundModifier.verticalScroll(rememberScrollState()))
    {

        ControlDialog(navController, resultViewModel)


        Box(modifier = appBarModifier
            .background(color = MaterialTheme.backgroundColorScheme.secondaryBackgroundColor)
            .padding(top = 10.dp, bottom = 10.dp),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = fortuneViewModel.pickedTopicState.value.topicSubjectData.majorTopic,
                style = getTextStyle(16, FontWeight.Medium, MaterialTheme.textColorScheme.titleTextColor),
                modifier = Modifier
                    .fillMaxWidth()
                    .align(Alignment.Center),
                textAlign = TextAlign.Center
            )

            Image(
                painter = painterResource(id = R.drawable.cancel),
                contentDescription = "닫기버튼",
                modifier = Modifier
                    .align(Alignment.CenterEnd)
                    .clickable { resultViewModel.openCloseDialog() }
                    .padding(end = 20.dp)
                    .size(28.dp),
                alignment = Alignment.Center
            )
        }

        Column(modifier = Modifier) {

            TextH02M22(
                text = "선택하신 카드는\n이런 의미를 담고 있어요.",
                color = MaterialTheme.textColorScheme.titleTextColor,
                modifier = Modifier
                    .background(color = MaterialTheme.backgroundColorScheme.secondaryBackgroundColor)
                    .padding(horizontal = 20.dp, vertical = 32.dp)
                    .fillMaxWidth()
            )

            Box(modifier = Modifier.background(color = MaterialTheme.backgroundColorScheme.cardSliderBackgroundColor)){
                CardSlider(tarotResult = resultViewModel.tarotResult.value, fortuneViewModel = fortuneViewModel)
            }


            OverallResult(resultViewModel, harmonyViewModel, demoViewModel)

        }

    }
}


@Composable
private fun OverallResult(resultViewModel: ResultViewModel, harmonyViewModel: HarmonyViewModel, demoViewModel: DemoViewModel){

    Column(
        modifier = Modifier
            .background(color = MaterialTheme.backgroundColorScheme.secondaryBackgroundColor)
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
            text = resultViewModel.tarotResult.value.overallResult?.summary.toString(),
            color = MaterialTheme.textColorScheme.titleTextColor,
            modifier = Modifier
                .padding(top = 24.dp)
                .fillMaxWidth()
        )

        TextB02M16(
            text = resultViewModel.tarotResult.value.overallResult?.full.toString(),
            color = MaterialTheme.textColorScheme.subTitleTextColor,
            modifier = Modifier
                .padding(top = 12.dp, bottom = 64.dp)
                .wrapContentHeight()
        )

        Button(
            onClick = {
                // 타로 결과 id 저장
                saveToMyTarot(resultViewModel, demoViewModel.isDemo)
            },
            shape = RoundedCornerShape(10.dp),
            enabled = !resultViewModel.saveState.value,
            modifier = Modifier
                .wrapContentHeight()
                .fillMaxWidth()
                .padding(bottom = 8.dp),
            colors = SecondaryButtonColors()
        ) {

            if (resultViewModel.saveState.value){
                Image(painter = painterResource(id = R.drawable.check_filled_disabled),
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
                else MaterialTheme.textColorScheme.onDisabledButtonColor,
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

        Row(modifier = Modifier
            .padding(vertical = 16.dp, horizontal = 16.dp)
            .padding(bottom = 45.dp)
            .clickable {
                setDynamicLink(
                    localContext,
                    resultViewModel.tarotResult.value.tarotId,
                    ShareLinkType.MY,
                    ShareActionType.OPEN_SHEET,
                    harmonyViewModel
                )
            },
            horizontalArrangement = Arrangement.Center)
        {
            Image(painter = painterResource(id = R.drawable.share),
                contentDescription = null,
                modifier = Modifier.padding(end = 3.dp))
            TextButtonM16(
                text = "공유하기",
                color = MaterialTheme.textColorScheme.subTitleTextColor
            )
        }
    }
}


fun saveToMyTarot(resultViewModel: ResultViewModel, isDemo: Boolean) {
    if (isDemo) {
        MyApplication.prefs.addTarotResult(demoTarotResult.tarotId)
    } else {
        MyApplication.prefs.addTarotResult(resultViewModel.tarotResult.value.tarotId)
    }
    resultViewModel.saveResult()
    resultViewModel.openCompleteDialog()
}


