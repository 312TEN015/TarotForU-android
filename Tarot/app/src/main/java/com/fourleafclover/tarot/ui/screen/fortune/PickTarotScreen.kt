
package com.fourleafclover.tarot.ui.screen.fortune

import android.content.Context
import android.util.Log
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.animateIntOffsetAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
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
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.MaterialTheme
import androidx.compose.material3.Button
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.graphics.PathEffect
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.layout.LayoutCoordinates
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.layout.positionInRoot
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.toSize
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.fourleafclover.tarot.MyApplication
import com.fourleafclover.tarot.R
import com.fourleafclover.tarot.demo.ui.component.SecondaryButtonColors
import com.fourleafclover.tarot.demo.ui.theme.backgroundColorScheme
import com.fourleafclover.tarot.demo.ui.theme.textColorScheme
import com.fourleafclover.tarot.ui.component.AppBarCloseWithDialog
import com.fourleafclover.tarot.ui.component.backgroundModifier
import com.fourleafclover.tarot.ui.navigation.ScreenEnum
import com.fourleafclover.tarot.ui.navigation.navGraphViewModel
import com.fourleafclover.tarot.ui.screen.fortune.viewModel.FortuneViewModel
import com.fourleafclover.tarot.ui.screen.fortune.viewModel.PickTarotViewModel
import com.fourleafclover.tarot.ui.screen.harmony.viewmodel.LoadingViewModel
import com.fourleafclover.tarot.ui.screen.harmony.viewmodel.LoadingViewModel_Factory
import com.fourleafclover.tarot.ui.screen.main.DialogViewModel
import com.fourleafclover.tarot.ui.theme.TextButtonM16
import com.fourleafclover.tarot.ui.theme.TextCaptionM12
import com.fourleafclover.tarot.ui.theme.TextH02M22
import kotlin.math.roundToInt


@Preview
@Composable
fun PickTarotScreen(
    navController: NavHostController = rememberNavController(),
) {
    val loadingViewModel = navGraphViewModel<LoadingViewModel>(navController)
    val fortuneViewModel = navGraphViewModel<FortuneViewModel>(navController)
    val pickTarotViewModel = navGraphViewModel<PickTarotViewModel>(navController)
    val dialogViewModel = navGraphViewModel<DialogViewModel>(navController)

    val localContext = LocalContext.current
    var showIndicator by remember { mutableStateOf(MyApplication.prefs.isPickCardIndicateComplete()) }
    // 뽑은 카드가 날아가 놓일 세 칸의 화면상 위치
    val slotBounds = remember { mutableStateListOf<Rect?>(null, null, null) }

    Box(modifier = backgroundModifier) {

        Column {

            AppBarCloseWithDialog(
                navController = navController,
                pickedTopicTemplate = fortuneViewModel.pickedTopicState.value.topicSubjectData,
                backgroundColor = MaterialTheme.backgroundColorScheme.secondaryBackgroundColor,
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
                            pickTarotViewModel,
                            onPositioned = { slotBounds[0] = it }
                        )

                        // "두번째 카드"
                        CardBlank(
                            context = localContext,
                            sequence = 2,
                            pickTarotViewModel.pickedCardNumberState.value.secondCardNumber,
                            fortuneViewModel,
                            pickTarotViewModel,
                            onPositioned = { slotBounds[1] = it }
                        )

                        // "세번째 카드"
                        CardBlank(
                            context = localContext,
                            sequence = 3,
                            pickTarotViewModel.pickedCardNumberState.value.thirdCardNumber,
                            fortuneViewModel,
                            pickTarotViewModel,
                            onPositioned = { slotBounds[2] = it }
                        )
                    }


                    // 카드덱
                    Column(modifier = Modifier.weight(1f),
                        verticalArrangement = Arrangement.Bottom) {

                        CardDeck(
                            pickTarotViewModel,
                            leaveTarget = slotBounds.getOrNull(pickTarotViewModel.pickSequence.value - 1),
                            onCardPicked = {
                            pickTarotViewModel.setPickedCard(pickTarotViewModel.pickSequence.value)
                            if (pickTarotViewModel.pickSequence.value == 3) {
                                loadingViewModel.startLoading(navController, ScreenEnum.LoadingScreen, ScreenEnum.ResultScreen)
                            } else {
                                pickTarotViewModel.moveToNextSequence()
                            }

                            pickTarotViewModel.resetNowSelectedCardIdx()
                        })

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
                    // 카드가 덱에서 빠져나가는 애니메이션이 끝나면 CardDeck의 onCardPicked가 불린다
                    onClick = { pickTarotViewModel.startPickAnimation() },
                    shape = RoundedCornerShape(10.dp),
                    modifier = Modifier
                        .wrapContentHeight()
                        .fillMaxWidth()
                        .padding(bottom = 34.dp)
                        .padding(horizontal = 20.dp),
                    colors = SecondaryButtonColors(),
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
    pickTarotViewModel: PickTarotViewModel,
    onPositioned: (Rect) -> Unit = {}
){

    val dash = Stroke(width = 3f, pathEffect = PathEffect.dashPathEffect(floatArrayOf(10f, 10f)))
    val dashColor = MaterialTheme.textColorScheme.onCardBlankGuidBoxColor
    val isCardConfirmed = finalCardNumber != -1

    fun getAlpha(isCardConfirmed: Boolean) = if (isCardConfirmed) 1f else 0f

    Box(modifier = Modifier
        .width(54.dp)
        .onGloballyPositioned { onPositioned(it.rectInRoot()) }
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

/**
 * @param leaveTarget 뽑힌 카드가 날아가 놓일 자리(루트 기준 좌표). 주면 카드가 그 자리까지
 *   작아지며 날아가고, null이면 제자리에서 위로 올라가며 투명해진다.
 * @param onCardPicked 선택완료 후 카드가 덱을 떠나는 애니메이션이 끝났을 때 불린다.
 *   여기서 [PickTarotViewModel.setPickedCard]로 카드를 실제로 덱에서 빼야 한다.
 */
@OptIn(ExperimentalFoundationApi::class)
@Composable
fun CardDeck(
    pickTarotViewModel: PickTarotViewModel,
    leaveTarget: Rect? = null,
    onCardPicked: () -> Unit
){
    val pxToMove = with(LocalDensity.current) { -30.dp.toPx().roundToInt() }
    val leavePx = with(LocalDensity.current) { DECK_CARD_LEAVE_DISTANCE.toPx() }

    // 뽑힌 카드가 덱을 떠나는 진행도 (0 = 제자리, 1 = 다 떠남)
    val leaveProgress = remember { Animatable(0f) }
    // 지금 선택된 카드의 화면상 위치. 날아갈 때의 출발점으로 쓴다.
    var selectedCardBounds by remember { mutableStateOf<Rect?>(null) }
    var deckOrigin by remember { mutableStateOf(Offset.Zero) }
    val leavingCardIdx = pickTarotViewModel.leavingCardIdx.value
    val currentOnCardPicked by rememberUpdatedState(onCardPicked)

    LaunchedEffect(leavingCardIdx) {
        leaveProgress.snapTo(0f)
        if (leavingCardIdx != -1) {
            leaveProgress.animateTo(
                1f,
                tween(
                    if (leaveTarget != null) DECK_CARD_FLY_MS else DECK_CARD_LEAVE_MS,
                    easing = FastOutSlowInEasing
                )
            )
            currentOnCardPicked()
        }
    }

    BoxWithConstraints(
        modifier = Modifier
            .fillMaxWidth()
            .onGloballyPositioned { deckOrigin = it.positionInRoot() }
    ) {
        val density = LocalDensity.current
        val cardCount = pickTarotViewModel.cards.size
        val deckWidth = if (cardCount == 0) 0.dp else DECK_CARD_STEP * (cardCount - 1) + DECK_CARD_WIDTH

        // 덱이 화면보다 넓으면 가운데 카드가 화면 중앙에 오도록 스크롤된 상태로 시작해서
        // 양옆으로 넘겨볼 수 있게 하고, 화면보다 좁으면 여백을 줘서 가운데에 둔다.
        val overflowPx = with(density) { (deckWidth - maxWidth).toPx() }
        val stepPx = with(density) { DECK_CARD_STEP.toPx() }
        val startScrollPx = (overflowPx / 2f).coerceAtLeast(0f)
        val listState = rememberLazyListState(
            initialFirstVisibleItemIndex = (startScrollPx / stepPx).toInt(),
            initialFirstVisibleItemScrollOffset = (startScrollPx % stepPx).roundToInt()
        )
        val sidePadding = ((maxWidth - deckWidth) / 2).coerceAtLeast(0.dp)

    LazyRow(
        state = listState,
        modifier = Modifier.fillMaxWidth(),
        contentPadding = PaddingValues(horizontal = sidePadding),
        verticalAlignment = Alignment.Bottom,
        horizontalArrangement = Arrangement.spacedBy(DECK_CARD_STEP - DECK_CARD_WIDTH)
    ) {

        // 카드 번호를 key로 줘서, 한 장이 빠지면 나머지 카드가 빈자리를 메우며 움직이게 한다
        items(pickTarotViewModel.cards.size, key = { pickTarotViewModel.cards[it] }) { index ->

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
                    .width(DECK_CARD_WIDTH)
                    .animateItemPlacement()
                    .offset {
                        offset
                    }
                    .onGloballyPositioned {
                        if (index == pickTarotViewModel.nowSelectedCardIdx.value) {
                            selectedCardBounds = it.rectInRoot()
                        }
                    }
                    .graphicsLayer {
                        // 덱을 떠나는 카드는 아래의 덱 밖 복사본이 대신 그린다
                        if (index == leavingCardIdx) alpha = 0f
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

        // LazyRow는 위아래로 30dp 넘게 벗어난 그림을 잘라내기 때문에,
        // 덱을 떠나는 카드는 LazyRow 밖에 같은 자리의 복사본을 올려서 움직인다.
        val from = selectedCardBounds
        if (leavingCardIdx != -1 && from != null && from.width > 0f) {
            Image(
                painter = painterResource(id = R.drawable.tarot_front),
                contentDescription = null,
                modifier = Modifier
                    .offset {
                        IntOffset(
                            (from.left - deckOrigin.x).roundToInt(),
                            (from.top - deckOrigin.y).roundToInt()
                        )
                    }
                    .width(DECK_CARD_WIDTH)
                    .graphicsLayer {
                        val progress = leaveProgress.value
                        if (leaveTarget != null) {
                            // 뽑은 카드 칸까지 작아지면서 날아간다
                            translationX = (leaveTarget.center.x - from.center.x) * progress
                            translationY = (leaveTarget.center.y - from.center.y) * progress
                            val scale = 1f + (leaveTarget.width / from.width - 1f) * progress
                            scaleX = scale
                            scaleY = scale
                        } else {
                            alpha = 1f - progress
                            translationY = -leavePx * progress
                        }
                    }
            )
        }
    }
}

private val DECK_CARD_WIDTH = 80.dp
// 겹쳐 놓은 카드 한 장이 차지하는 가로 간격
private val DECK_CARD_STEP = 34.dp
// 날아갈 자리가 없을 때: 뽑힌 카드가 사라지면서 위로 올라가는 거리와 시간
private val DECK_CARD_LEAVE_DISTANCE = 60.dp
private const val DECK_CARD_LEAVE_MS = 450
// 뽑힌 카드가 뽑은 카드 칸까지 날아가는 시간
private const val DECK_CARD_FLY_MS = 550

private fun LayoutCoordinates.rectInRoot(): Rect = Rect(positionInRoot(), size.toSize())
