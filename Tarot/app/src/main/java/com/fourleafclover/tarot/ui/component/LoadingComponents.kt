package com.fourleafclover.tarot.ui.component

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.fourleafclover.tarot.R
import com.fourleafclover.tarot.demo.ui.theme.color.ColorSet
import com.fourleafclover.tarot.demo.ui.theme.textColorScheme
import com.fourleafclover.tarot.ui.theme.TextB03M14
import com.fourleafclover.tarot.ui.theme.TextH02M22
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@Composable
@Preview
fun LoadingCircle(
    modifier: Modifier = Modifier,
    loadingTitle: String = "상대방을 기다리는 중입니다...",
    loadingSubTitle: String = "1시간 안에 모두 입장하지 않으면 초대방이 사라져요!"
) {
    // 로딩 화면 회전
    val rotation = remember { Animatable(0f) }

    LaunchedEffect(Unit) {
        rotation.animateTo(
            targetValue = 360f,
            animationSpec = infiniteRepeatable(
                animation = tween(3000, easing = LinearEasing),
                repeatMode = RepeatMode.Restart
            )
        )
    }

    Column(
        modifier = Modifier.then(modifier),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Image(
            painter = painterResource(id = R.drawable.loading),
            contentDescription = "",
            modifier = Modifier
                .wrapContentSize()
                .rotate(rotation.value)
        )
        TextH02M22(
            text = loadingTitle,
            color = MaterialTheme.textColorScheme.titleTextColor,
            modifier = Modifier.padding(bottom = 8.dp, top = 40.dp),
            textAlign = TextAlign.Center
        )
        TextB03M14(
            text = loadingSubTitle,
            color = MaterialTheme.textColorScheme.onDialogContentColor,
            textAlign = TextAlign.Center
        )
    }
}

data class LoadingTip(val title: String, val subTitle: String)

/**
 * 로딩 중 안내 문구를 순서대로 돌려가며 보여준다.
 * 각 문구는 투명한 상태에서 아래에서 위로 떠오르며 선명해지고, 잠시 머문 뒤 사라진다.
 */
@Composable
fun LoadingTipCircle(
    tips: List<LoadingTip>,
    modifier: Modifier = Modifier
) {
    val rotation = remember { Animatable(0f) }
    val alpha = remember { Animatable(0f) }
    // 1f = 아래로 내려간 상태, 0f = 제자리
    val rise = remember { Animatable(1f) }
    var index by remember { mutableStateOf(0) }

    LaunchedEffect(Unit) {
        rotation.animateTo(
            targetValue = 360f,
            animationSpec = infiniteRepeatable(
                animation = tween(3000, easing = LinearEasing),
                repeatMode = RepeatMode.Restart
            )
        )
    }

    LaunchedEffect(tips) {
        if (tips.isEmpty()) return@LaunchedEffect
        while (true) {
            rise.snapTo(1f)
            coroutineScope {
                launch { alpha.animateTo(1f, tween(TIP_FADE_IN_MS)) }
                launch { rise.animateTo(0f, tween(TIP_FADE_IN_MS)) }
            }
            delay(TIP_HOLD_MS)
            alpha.animateTo(0f, tween(TIP_FADE_OUT_MS))
            index = (index + 1) % tips.size
        }
    }

    val tip = tips.getOrNull(index) ?: return

    Column(
        modifier = Modifier.then(modifier),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Image(
            painter = painterResource(id = R.drawable.loading),
            contentDescription = "",
            modifier = Modifier
                .wrapContentSize()
                .rotate(rotation.value)
        )
        Column(
            modifier = Modifier
                .padding(top = 40.dp)
                .graphicsLayer {
                    this.alpha = alpha.value
                    translationY = rise.value * 16.dp.toPx()
                },
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            TextH02M22(
                text = tip.title,
                color = MaterialTheme.textColorScheme.titleTextColor,
                modifier = Modifier.padding(bottom = 8.dp),
                textAlign = TextAlign.Center
            )
            TextB03M14(
                text = tip.subTitle,
                color = MaterialTheme.textColorScheme.onDialogContentColor,
                textAlign = TextAlign.Center
            )
        }
    }
}

private const val TIP_FADE_IN_MS = 900
private const val TIP_FADE_OUT_MS = 400
private const val TIP_HOLD_MS = 3200L
