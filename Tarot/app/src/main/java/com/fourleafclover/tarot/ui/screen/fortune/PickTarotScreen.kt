
package com.fourleafclover.tarot.ui.screen.fortune

import android.content.Context
import android.util.Log
import androidx.compose.animation.core.animateIntOffsetAsState
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.MaterialTheme
import androidx.compose.material3.Button
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.graphics.PathEffect
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.fourleafclover.tarot.MyApplication
import com.fourleafclover.tarot.R
import com.fourleafclover.tarot.demo.ui.component.secondaryButtonColors
import com.fourleafclover.tarot.demo.ui.theme.backgroundColorScheme
import com.fourleafclover.tarot.demo.ui.theme.textColorScheme
import com.fourleafclover.tarot.ui.component.AppBarCloseWithDialog
import com.fourleafclover.tarot.ui.component.backgroundModifier
import com.fourleafclover.tarot.ui.navigation.ScreenEnum
import com.fourleafclover.tarot.ui.screen.fortune.viewModel.FortuneViewModel
import com.fourleafclover.tarot.ui.screen.fortune.viewModel.PickTarotViewModel
import com.fourleafclover.tarot.ui.screen.harmony.viewmodel.LoadingViewModel
import com.fourleafclover.tarot.ui.screen.main.DialogViewModel
import com.fourleafclover.tarot.ui.theme.TextButtonM16
import com.fourleafclover.tarot.ui.theme.TextCaptionM12
import com.fourleafclover.tarot.ui.theme.TextH02M22
import kotlin.math.roundToInt


@Preview
@Composable
fun PickTarotScreen(
    navController: NavHostController = rememberNavController(),
    loadingViewModel: LoadingViewModel = hiltViewModel(),
    fortuneViewModel: FortuneViewModel = hiltViewModel(),
    pickTarotViewModel: PickTarotViewModel = hiltViewModel(),
    dialogViewModel: DialogViewModel = hiltViewModel()
) {
    val localContext = LocalContext.current
    var showIndicator by remember { mutableStateOf(MyApplication.prefs.isPickCardIndicateComplete()) }

    Box(modifier = backgroundModifier) {

        Column {

            AppBarCloseWithDialog(
                navController = navController,
                pickedTopicTemplate = fortuneViewModel.pickedTopicState.value.topicSubjectData,
                backgroundColor = MaterialTheme.backgroundColorScheme.inputScreenBackgroundColor,
                dialogViewModel = dialogViewModel
            )

            // "n번째 카드를 골라주세요."
            TextH02M22(
                text = pickTarotViewModel.getDirectionText(pickTarotViewModel.pickSequence.value),
                color = MaterialTheme.textColorScheme.titleTextColor,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 20.dp)
                    .padding(top = 32.dp, bottom = 28.dp)
            )

            Column(modifier = Modifier.fillMaxSize()) {

                Column(modifier = Modifier.weight(1f)) {

                    Row(modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(space = 24.dp, alignment = Alignment.CenterHorizontally))
                    {

                        // "첫번째 카드"
                        CardBlank(
                            context = localContext,
                            sequence = 1,
                            pickTarotViewModel.pickedCardNumberState.value.firstCardNumber,
                            fortuneViewModel,
                            pickTarotViewModel
                        )

                        // "두번째 카드"
                        CardBlank(
                            context = localContext,
                            sequence = 2,
                            pickTarotViewModel.pickedCardNumberState.value.secondCardNumber,
                            fortuneViewModel,
                            pickTarotViewModel
                        )

                        // "세번째 카드"
                        CardBlank(
                            context = localContext,
                            sequence = 3,
                            pickTarotViewModel.pickedCardNumberState.value.thirdCardNumber,
                            fortuneViewModel,
                            pickTarotViewModel
                        )
                    }


                    // 카드덱
                    Column(modifier = Modifier.weight(1f),
                        verticalArrangement = Arrangement.Bottom) {

                        CardDeck(pickTarotViewModel)

                        // 인디케이터
                        Image(painter = painterResource(id = R.drawable.tarot_pick_indicator),
                            contentDescription = "",
                            modifier = Modifier
                                .wrapContentSize()
                                .align(Alignment.CenterHorizontally)
                                .padding(top = 24.dp, bottom = 36.dp),
                            alpha = if(showIndicator) 1f else 0f
                        )

                    }

                }


                Button(
                    onClick = {
                        pickTarotViewModel.setPickedCard(pickTarotViewModel.pickSequence.value)
                        if (pickTarotViewModel.pickSequence.value == 3) {
                            loadingViewModel.startLoading(navController, ScreenEnum.LoadingScreen, ScreenEnum.ResultScreen)
                        }else{
                            pickTarotViewModel.moveToNextSequence()
                        }

                        pickTarotViewModel.resetNowSelectedCardIdx()
                    },
                    shape = RoundedCornerShape(10.dp),
                    modifier = Modifier
                        .wrapContentHeight()
                        .fillMaxWidth()
                        .padding(bottom = 34.dp)
                        .padding(horizontal = 20.dp),
                    colors = secondaryButtonColors(),
                    enabled = pickTarotViewModel.isCompleteButtonEnabled()
                ) {
                    TextButtonM16(
                        text = "선택완료",
                        modifier = Modifier.padding(vertical = 8.dp),
                        color = if (pickTarotViewModel.isCompleteButtonEnabled()) MaterialTheme.textColorScheme.onActivePrimaryButtonColor
                        else MaterialTheme.textColorScheme.onDisabledButtonColor)
                }
            }
        }

        if (showIndicator)
            Box(modifier = Modifier
                .fillMaxSize()
                .clickable {
                    showIndicator = false
                    MyApplication.prefs.setPickCardIndicateComplete()
                })
    }
}

@Composable
fun CardBlank(
    context: Context,
    sequence: Int,
    finalCardNumber: Int?,
    fortuneViewModel: FortuneViewModel,
    pickTarotViewModel: PickTarotViewModel
){

    val dash = Stroke(width = 3f, pathEffect = PathEffect.dashPathEffect(floatArrayOf(10f, 10f)))
    val dashColor = MaterialTheme.textColorScheme.onCardBlankGuidBoxColor
    val isCardConfirmed = finalCardNumber != -1

    fun getAlpha(isCardConfirmed: Boolean) = if (isCardConfirmed) 1f else 0f

    Box(modifier = Modifier
        .width(54.dp)
        .drawBehind {
            drawRoundRect(
                color = dashColor,
                style = dash,
                alpha = getAlpha(!isCardConfirmed)
            )
        }
    ) {

        Image(painter = painterResource(
            id = if ((isCardConfirmed)) {
                fortuneViewModel.getCardImageId(context, pickTarotViewModel.getCardNumber(sequence).toString())
            } else {
                R.drawable.tarot_front
            }),
            contentDescription = null,
            modifier = Modifier,
            alpha = getAlpha((isCardConfirmed)))

        TextCaptionM12(text = pickTarotViewModel.getCardBlankText(sequence),
            color = dashColor,
            modifier = Modifier
                .wrapContentSize()
                .align(Alignment.Center)
                .alpha(getAlpha(!isCardConfirmed)),
            textAlign = TextAlign.Center)

    }
}

@Composable
fun CardDeck(pickTarotViewModel: PickTarotViewModel){
    val pxToMove = with(LocalDensity.current) { -30.dp.toPx().roundToInt() }

    LazyRow(
        modifier = Modifier.wrapContentWidth(),
        verticalAlignment = Alignment.Bottom,
        horizontalArrangement = Arrangement.spacedBy((-46).dp)
    ) {

        items(pickTarotViewModel.cards.size) { index ->

            val offset by animateIntOffsetAsState(
                targetValue = if (pickTarotViewModel.nowSelectedCardIdx.value == index) {
                    IntOffset(0, pxToMove)
                } else {
                    IntOffset.Zero
                },
                label = "offset"
            )


            Image(painter = painterResource(id = R.drawable.tarot_front),
                contentDescription = "$index",
                modifier = Modifier
                    .width(80.dp)
                    .offset {
                        offset
                    }
                    .clickable(
                        interactionSource = remember { MutableInteractionSource() },
                        indication = null
                    ) {
                        Log.d("", "nowSelected: $index")
                        pickTarotViewModel.setNowSelectedCardIdx(index)
                    })
        }

    }
}
