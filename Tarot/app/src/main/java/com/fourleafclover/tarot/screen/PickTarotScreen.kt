
package com.fourleafclover.tarot.screen

import android.util.Log
import androidx.compose.animation.core.animateIntOffsetAsState
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateListOf
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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.fourleafclover.tarot.AppBarClose
import com.fourleafclover.tarot.R
import com.fourleafclover.tarot.backgroundModifier
import com.fourleafclover.tarot.data.getCardImageId
import com.fourleafclover.tarot.data.getPickedTopic
import com.fourleafclover.tarot.data.pickedTopicNumber
import com.fourleafclover.tarot.data.tarotInputDto
import com.fourleafclover.tarot.navigation.ScreenEnum
import com.fourleafclover.tarot.ui.theme.getTextStyle
import com.fourleafclover.tarot.ui.theme.gray_1
import com.fourleafclover.tarot.ui.theme.gray_4
import com.fourleafclover.tarot.ui.theme.gray_5
import com.fourleafclover.tarot.ui.theme.gray_6
import com.fourleafclover.tarot.ui.theme.gray_8
import com.fourleafclover.tarot.ui.theme.gray_9
import com.fourleafclover.tarot.ui.theme.highligtPurple
import com.fourleafclover.tarot.ui.theme.white
import kotlin.math.roundToInt


@Preview
@Composable
fun PickTarotScreen(navController: NavHostController = rememberNavController()) {
    val localContext = LocalContext.current

    Column(modifier = backgroundModifier)
    {

        AppBarClose(navController = navController,
            pickedTopicTemplate = getPickedTopic(pickedTopicNumber),
            gray_8)


        val dash = Stroke(width = 3f, pathEffect = PathEffect.dashPathEffect(floatArrayOf(10f, 10f)))
        var cardNumber by remember { mutableIntStateOf(1) }
        var cardSelected by remember { mutableStateOf(false) }

        var firstCardPicked by remember { mutableStateOf(false) }
        var secondCardPicked by remember { mutableStateOf(false) }
        var thirdCardPicked by remember { mutableStateOf(false) }

        var firstCardNumber by remember { mutableIntStateOf(-1) }
        var secondCardNumber by remember { mutableIntStateOf(-1) }
        var thirdCardNumber by remember { mutableIntStateOf(-1) }

        var nowSelected by remember { mutableIntStateOf(-1) }
        
        val pxToMove = with(LocalDensity.current) { -46.dp.toPx().roundToInt() }

        val randomCards = (0..21).toMutableList().shuffled()
        val cards = remember { mutableStateListOf(0,1,2,3,4,5,6,7,8,9,10,11,12,13,14,15,16,17,18,19,20,21) }
        val entireCards = arrayListOf<Int>(0,1,2,3,4,5,6,7,8,9,10,11,12,13,14,15,16,17,18,19,20,21)


        Text(
            text = if (cardNumber == 1) "첫 번째 카드를 골라주세요."
            else if(cardNumber == 2) "두 번째 카드를 골라주세요."
            else "세 번째 카드를 골라주세요.",
            style = getTextStyle(22, FontWeight.Medium, white),
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 20.dp, vertical = 32.dp)
        )

        Column(modifier = Modifier.fillMaxSize()) {

            Column(modifier = Modifier.weight(1f)) {

                Row(modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 32.dp),
                    horizontalArrangement = Arrangement.Center)
                {

                    Box(modifier = Modifier
                        .width(54.dp)
                        .drawBehind {
                            drawRoundRect(
                                color = gray_4,
                                style = dash,
                                alpha = if (firstCardPicked) 0f else 1f
                            )
                        }) {

                        Image(painter = painterResource(
                            id = if (firstCardPicked) getCardImageId(localContext, firstCardNumber.toString())
                            else R.drawable.tarot_front),
                            contentDescription = null,
                            modifier = Modifier,
                            alpha = if(firstCardPicked) 1f else 0f)
                        Text(text = "첫번째\n 카드",
                            style = getTextStyle(fontSize = 12, fontWeight = FontWeight.Normal, color = gray_4),
                            modifier = Modifier
                                .wrapContentSize()
                                .align(Alignment.Center)
                                .alpha(if (firstCardPicked) 0f else 1f),
                            textAlign = TextAlign.Center)

                    }

                    Box(modifier = Modifier
                        .padding(horizontal = 24.dp)
                        .width(54.dp)
                        .drawBehind {
                            drawRoundRect(
                                color = gray_4,
                                style = dash,
                                alpha = if (secondCardPicked) 0f else 1f
                            )
                        }) {

                        Image(painter = painterResource(
                            id = if (secondCardPicked) getCardImageId(localContext, secondCardNumber.toString())
                            else R.drawable.tarot_front),
                            contentDescription = null,
                            modifier = Modifier,
                            alpha = if(secondCardPicked) 1f else 0f)
                        Text(text = "두번째\n 카드",
                            style = getTextStyle(fontSize = 12, fontWeight = FontWeight.Normal, color = gray_4),
                            modifier = Modifier
                                .wrapContentSize()
                                .align(Alignment.Center)
                                .alpha(if (secondCardPicked) 0f else 1f),
                            textAlign = TextAlign.Center)

                    }

                    Box(modifier = Modifier
                        .width(54.dp)
                        .drawBehind {
                            drawRoundRect(
                                color = gray_4,
                                style = dash,
                                alpha = if (thirdCardPicked) 0f else 1f
                            )
                        }) {

                        Image(painter = painterResource(
                            id = if (thirdCardPicked) getCardImageId(localContext, thirdCardNumber.toString())
                            else R.drawable.tarot_front),
                            contentDescription = null,
                            modifier = Modifier,
                            alpha = if(thirdCardPicked) 1f else 0f)
                        Text(text = "세번째\n 카드",
                            style = getTextStyle(fontSize = 12, fontWeight = FontWeight.Normal, color = gray_4),
                            modifier = Modifier
                                .wrapContentSize()
                                .align(Alignment.Center)
                                .alpha(if (thirdCardPicked) 0f else 1f),
                            textAlign = TextAlign.Center)

                    }
                }


                LazyRow(
                    modifier = Modifier
                        .weight(1f)
                        .wrapContentWidth()
                        .fillMaxHeight(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy((-46).dp)
                ) {

                    items(cards.size) { index ->

                        val offset by animateIntOffsetAsState(
                            targetValue = if (nowSelected == index) {
                                IntOffset(0, pxToMove)
                            }  else {
                                IntOffset.Zero
                            },
                            label = "offset"
                        )


                        Image(painter = painterResource(id = R.drawable.tarot_front),
                            contentDescription = "$index",
                            modifier = Modifier
                                .width(88.dp)
                                .offset {
                                    offset
                                }
                                .clickable(
                                    interactionSource = remember { MutableInteractionSource() },
                                    indication = null
                                ) {
                                    Log.d("", "nowSelected: $index")
                                    cardSelected = true
                                    nowSelected = index
                                })
                    }

                }

            }


            Button(
                onClick = {
                    if (cardNumber == 1){
                        firstCardPicked = true
                        cardSelected = false
                        firstCardNumber = cards[nowSelected]
                        cards.remove(firstCardNumber)
                        cardNumber = 2
                    }
                    else if (cardNumber == 2){
                        secondCardPicked = true
                        cardSelected = false
                        secondCardNumber = cards[nowSelected]
                        cards.remove(secondCardNumber)
                        cardNumber = 3

                    }
                    else if (cardNumber == 3) {
                        thirdCardNumber = cards[nowSelected]
                        cards.remove(thirdCardNumber)
                        thirdCardPicked = true

                        tarotInputDto.cards = arrayListOf(firstCardNumber, secondCardNumber, thirdCardNumber)

                        navController.navigate(ScreenEnum.LoadingScreen.name) {
                            navController.graph.startDestinationRoute?.let {
                                popUpTo(it) { saveState = true }
                            }
                            launchSingleTop = true
                            restoreState = true
                        }
                    }

                    Log.d("", "${cards.size}, {${cards.joinToString (",")}}")
//                    Log.d("", "cardNum: $cardNumber, cardVal: ${entireCards[cards[nowSelected]]}, 1: $firstCardIndex, 2: $secondCardIndex, 3: $thirdCardIndex")
                    nowSelected = -1

                },
                shape = RoundedCornerShape(10.dp),
                modifier = Modifier
                    .wrapContentHeight()
                    .fillMaxWidth()
                    .padding(bottom = 49.dp)
                    .padding(horizontal = 20.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = highligtPurple,
                    contentColor = gray_1,
                    disabledContainerColor = gray_5,
                    disabledContentColor = gray_6
                ),
                enabled = cardSelected
            ) {
                Text(text = "선택완료", modifier = Modifier.padding(vertical = 8.dp),
                    style = getTextStyle(
                        fontSize = 16,
                        fontWeight = FontWeight.Medium
                    ))
            }
        }
    }
}