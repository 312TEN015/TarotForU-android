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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalView
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.fourleafclover.tarot.R
import com.fourleafclover.tarot.fortuneViewModel
import com.fourleafclover.tarot.myTarotViewModel
import com.fourleafclover.tarot.resultViewModel
import com.fourleafclover.tarot.sharedTarotResult
import com.fourleafclover.tarot.ui.component.AppBarPlain
import com.fourleafclover.tarot.ui.component.CardSlider
import com.fourleafclover.tarot.ui.component.HarmonyCardSlider
import com.fourleafclover.tarot.ui.component.backgroundModifier
import com.fourleafclover.tarot.ui.component.getBackgroundModifier
import com.fourleafclover.tarot.ui.component.setStatusbarColor
import com.fourleafclover.tarot.ui.theme.TextB01M18
import com.fourleafclover.tarot.ui.theme.TextB02M16
import com.fourleafclover.tarot.ui.theme.TextB03M14
import com.fourleafclover.tarot.ui.theme.TextButtonM16
import com.fourleafclover.tarot.ui.theme.TextH01M26
import com.fourleafclover.tarot.ui.theme.TextH02M22
import com.fourleafclover.tarot.ui.theme.backgroundColor_1
import com.fourleafclover.tarot.ui.theme.backgroundColor_2
import com.fourleafclover.tarot.ui.theme.gray_2
import com.fourleafclover.tarot.ui.theme.gray_3
import com.fourleafclover.tarot.ui.theme.gray_4
import com.fourleafclover.tarot.ui.theme.gray_5
import com.fourleafclover.tarot.ui.theme.gray_7
import com.fourleafclover.tarot.ui.theme.gray_8
import com.fourleafclover.tarot.ui.theme.highlightPurple
import com.fourleafclover.tarot.ui.theme.white

@Composable
@Preview
fun ShareHarmonyDetailScreen(navController: NavHostController = rememberNavController()){
    val localContext = LocalContext.current
    val tarotSubjectData = fortuneViewModel.getPickedTopic(sharedTarotResult.tarotType)
    setStatusbarColor(LocalView.current, backgroundColor_2)

    Column(modifier = getBackgroundModifier(backgroundColor_2))
    {

        AppBarPlain(navController = navController, title = "공유하기", backgroundColor = backgroundColor_2, backButtonVisible = false)

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
                    color = gray_2,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 16.dp),
                    textAlign = TextAlign.Center
                )

                val imoji = fortuneViewModel.getSubjectImoji(localContext, sharedTarotResult.tarotType)
                TextH02M22(
                    text = "$imoji ${tarotSubjectData.majorQuestion} $imoji",
                    color = gray_2,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 16.dp),
                    textAlign = TextAlign.Center
                )

                TextB03M14(
                    text = "${sharedTarotResult.createdAt} • {별명}님과 본 궁합",
                    color = gray_4,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 32.dp),
                    textAlign = TextAlign.Center
                )
            }


            Column(
                modifier = Modifier
                    .padding(horizontal = 20.dp)
                    .background(color = gray_8, shape = RoundedCornerShape(10.dp))
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
                            color = if (resultViewModel.isMyTab()) white else gray_7
                        )
                        .padding(vertical = 8.dp, horizontal = 16.dp)
                        .weight(1f),
                        contentAlignment = Alignment.Center) {

                        TextB03M14(
                            color = if (resultViewModel.isMyTab()) gray_7 else gray_5,
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
                            color = if (resultViewModel.isMyTab()) gray_7 else white
                        )
                        .padding(vertical = 8.dp, horizontal = 16.dp)
                        .weight(1f),
                        contentAlignment = Alignment.Center) {

                        TextB03M14(
                            color = if (resultViewModel.isMyTab()) gray_5 else gray_7,
                            text = "상대방 카드",
                            textAlign = TextAlign.Center
                        )
                    }
                }


                HarmonyCardSlider(outsideHorizontalPadding = 40.dp)

            }


            OverallResult()

        }
    }

}

@Composable
private fun OverallResult() {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 20.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        TextH01M26(
            text = "타로 카드 종합 리딩",
            color = highlightPurple,
            modifier = Modifier
                .padding(top = 48.dp)
                .fillMaxWidth()
        )

        TextB01M18(
            text = sharedTarotResult.overallResult?.summary.toString(),
            color = white,
            modifier = Modifier
                .padding(top = 24.dp)
                .fillMaxWidth()
        )

        TextB02M16(
            text = sharedTarotResult.overallResult?.full.toString(),
            color = gray_3,
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 12.dp, bottom = 64.dp)
                .wrapContentHeight()
        )
    }
}
