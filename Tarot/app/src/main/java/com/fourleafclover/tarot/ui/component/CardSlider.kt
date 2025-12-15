package com.fourleafclover.tarot.ui.component

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.PageSize
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.fourleafclover.tarot.data.CardResultData
import com.fourleafclover.tarot.data.TarotOutputDto
import com.fourleafclover.tarot.demo.ui.theme.backgroundColorScheme
import com.fourleafclover.tarot.demo.ui.theme.textColorScheme
import com.fourleafclover.tarot.ui.theme.TextB02M16
import com.fourleafclover.tarot.ui.theme.TextB04M12
import kotlin.math.absoluteValue

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun CardSlider(
    modifier: Modifier = Modifier,
    tarotResult: TarotOutputDto,
    cardImageList: MutableList<Int>
) {

    if (cardImageList.contains(0)) return

    val pagerState = rememberPagerState(
        initialPage = 0,
        initialPageOffsetFraction = 0f
    ) {
        3
    }

    Column(
        modifier = Modifier
            .padding(top = 48.dp),
        horizontalAlignment = Alignment.CenterHorizontally, verticalArrangement = Arrangement.Center
    ) {
        Row(
            modifier = modifier
                .fillMaxWidth()
                .padding(bottom = 32.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center
        ) {

            val itemWidth by remember { mutableStateOf(140.dp) }
            val screenWidth = LocalConfiguration.current.screenWidthDp.dp
            val horizontalPadding by remember { mutableStateOf(screenWidth / 2 - itemWidth / 2) }

            HorizontalPager(
                modifier = modifier.weight(1f),
                state = pagerState,
                pageSpacing = 24.dp,
                userScrollEnabled = true,
                reverseLayout = false,
                contentPadding = PaddingValues(horizontal = horizontalPadding),
                beyondBoundsPageCount = 0,
                pageSize = PageSize.Fixed(itemWidth),
                key = null,
                pageContent = { page ->
                    val pageOffset = (pagerState.currentPage - page) + pagerState.currentPageOffsetFraction

                    val scaleFactor = 0.75f + (1f - 0.75f) * (1f - pageOffset.absoluteValue)

                    Box(modifier = modifier
                        .graphicsLayer {
                            scaleX = scaleFactor
                            scaleY = scaleFactor
                        }
                        .alpha(
                            scaleFactor.coerceIn(0f, 1f)
                        )) {

                        val painter = painterResource(id = cardImageList[page])
                        val imageRatio = painter.intrinsicSize.width / painter.intrinsicSize.height
                        Image(
                            painter = painter,
                            contentDescription = null,
                            modifier = Modifier
                                .width(itemWidth)
                                .aspectRatio(imageRatio)
                        )
                    }

                }
            )

        }

        DotsIndicator(
            totalDots = cardImageList.size,
            selectedIndex = pagerState.currentPage
        )

        Column(
            modifier = Modifier
                .padding(horizontal = 20.dp)
                .padding(top = 32.dp, bottom = 48.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        )
        {
            TextB02M16(
                text = if (pagerState.currentPage == 0) "첫번째 카드"
                else if (pagerState.currentPage == 1) "두번째 카드"
                else "세번째 카드",
                color = MaterialTheme.textColorScheme.highlightTextColor,
                modifier = Modifier.padding(bottom = 12.dp)
            )


            LazyRow(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.Center
            ) {

                items(3) {

                    TextB04M12(
                        text = "# ${
                            tarotResult.cardResults?.get(pagerState.currentPage)?.keywords?.get(
                                it
                            )
                        }",
                        color = MaterialTheme.textColorScheme.resultScreenCreatedAtColor,
                        modifier = Modifier
                            .padding(end = 8.dp)
                            .background(color = MaterialTheme.backgroundColorScheme.secondaryBackgroundColor, shape = RoundedCornerShape(6.dp))
                            .padding(horizontal = 8.dp, vertical = 4.dp)
                    )

                }
            }

            TextB02M16(
                text = "${tarotResult.cardResults?.get(pagerState.currentPage)?.description}",
                color = MaterialTheme.textColorScheme.subTitleTextColor,
                textAlign = TextAlign.Center,
                overflow = TextOverflow.Visible,
                modifier = Modifier
                    .padding(top = 16.dp)
                    .height(56.dp)
            )
        }
    }
}

@Composable
fun DotsIndicator(
    modifier: Modifier = Modifier,
    totalDots: Int,
    selectedIndex: Int,
    selectedColor: Color = MaterialTheme.textColorScheme.titleTextColor,
    unSelectedColor: Color = MaterialTheme.backgroundColorScheme.dotIndicatorColor,
    dotSize: Dp = 6.dp
) {
    LazyRow(
        modifier = modifier
            .wrapContentWidth()
            .wrapContentHeight()
    ) {
        items(totalDots) { index ->
            Box(
                modifier = modifier
                    .width(if (index == selectedIndex) 18.dp else dotSize)
                    .height(dotSize)
                    .background(
                        color = if (index == selectedIndex) selectedColor else unSelectedColor,
                        shape = RoundedCornerShape(4.dp)
                    )
            )

            if (index != totalDots - 1) {
                Spacer(modifier = Modifier.padding(horizontal = 4.dp))
            }
        }
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun HarmonyCardSlider(
    modifier: Modifier = Modifier,
    outsideHorizontalPadding: Dp = 0.dp,
    cardImageList: MutableList<Int> = arrayListOf(0, 0, 0),
    firstCardResults: List<CardResultData>,
    secondCardResults: List<CardResultData>,
    isFirstTab: Boolean
) {

    val pagerState = rememberPagerState(
        initialPage = 0,
        initialPageOffsetFraction = 0f
    ) {
        3
    }

    Column(
        modifier = Modifier.padding(bottom = 24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {

        TextB02M16(
            text = if (pagerState.currentPage == 0) "첫번째 카드"
            else if (pagerState.currentPage == 1) "두번째 카드"
            else "세번째 카드",
            color = MaterialTheme.textColorScheme.highlightTextColor,
            modifier = Modifier.padding(bottom = 16.dp)
        )

        Row(
            modifier = modifier.padding(bottom = 24.dp),
            horizontalArrangement = Arrangement.Center
        ) {

            val itemWidth by remember { mutableStateOf(100.dp) }
            val screenWidth = LocalConfiguration.current.screenWidthDp.dp - (outsideHorizontalPadding * 2)
            val horizontalPadding by remember { mutableStateOf(screenWidth / 2 - itemWidth / 2) }

            HorizontalPager(
                modifier = modifier.weight(1f),
                state = pagerState,
                pageSpacing = 4.dp,
                userScrollEnabled = true,
                reverseLayout = false,
                contentPadding = PaddingValues(horizontal = horizontalPadding),
                beyondBoundsPageCount = 0,
                pageSize = PageSize.Fixed(itemWidth),
                key = null,
                pageContent = { page ->
                    val pageOffset =
                        (pagerState.currentPage - page) + pagerState.currentPageOffsetFraction

                    val scaleFactor = 0.65f + (1f - 0.65f) * (1f - pageOffset.absoluteValue)

                    Box(modifier = modifier
                        .graphicsLayer {
                            scaleX = scaleFactor
                            scaleY = scaleFactor
                        }
                        .alpha(
                            scaleFactor.coerceIn(0f, 1f)
                        )) {

                        val painter = painterResource(id = cardImageList[page])
                        val imageRatio = painter.intrinsicSize.width / painter.intrinsicSize.height
                        Image(
                            painter = painter,
                            contentDescription = null,
                            modifier = Modifier
                                .width(itemWidth)
                                .aspectRatio(imageRatio)
                        )
                    }

                }
            )

        }

        DotsIndicator(
            totalDots = cardImageList.size,
            selectedIndex = pagerState.currentPage
        )

        Column(
            modifier = Modifier
                .padding(horizontal = 20.dp)
                .padding(top = 16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        )
        {


            LazyRow(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.Center
            ) {

                items(3) {

                    TextB04M12(
                        text = "# ${
                            if (isFirstTab) {
                                firstCardResults[pagerState.currentPage].keywords[it]
                            } else {
                                secondCardResults[pagerState.currentPage].keywords[it]
                            }
                        }",
                        color = MaterialTheme.textColorScheme.resultScreenCreatedAtColor,
                        modifier = Modifier
                            .padding(end = 8.dp)
                            .background(color = MaterialTheme.backgroundColorScheme.mainBackgroundColor, shape = RoundedCornerShape(6.dp))
                            .padding(horizontal = 8.dp, vertical = 4.dp)
                    )

                }
            }

            TextB02M16(
                text = if (isFirstTab) {
                    firstCardResults[pagerState.currentPage].description
                } else {
                    secondCardResults[pagerState.currentPage].description
                },
                color = MaterialTheme.textColorScheme.subTitleTextColor,
                textAlign = TextAlign.Center,
                overflow = TextOverflow.Visible,
                modifier = Modifier
                    .padding(top = 12.dp)
                    .height(56.dp)
            )
        }
    }
}

