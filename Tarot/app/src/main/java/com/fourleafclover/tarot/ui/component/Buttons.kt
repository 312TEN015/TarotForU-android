package com.fourleafclover.tarot.ui.component

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.fourleafclover.tarot.demo.ui.theme.backgroundColorScheme
import com.fourleafclover.tarot.demo.ui.theme.color.ColorSet
import com.fourleafclover.tarot.demo.ui.theme.textColorScheme
import com.fourleafclover.tarot.ui.theme.TextButtonSB16

@Preview
@Composable
fun ButtonNext(
    onClick: () -> Unit = {},
    enabled: () -> Boolean = { false },
    content:  @Composable () -> Unit = {}
) {
    Box(
        modifier = Modifier
            .padding(bottom = 16.dp)
            .background(
                shape = RoundedCornerShape(10.dp),
                color = if (enabled()) MaterialTheme.backgroundColorScheme.activeSecondaryButtonBackgroundColor
                else MaterialTheme.backgroundColorScheme.disabledButtonBackgroundColor
            )
            .clickable { if (enabled()) { onClick() } }
            .wrapContentHeight()
            .fillMaxWidth(),
        contentAlignment = Alignment.Center
    ) {
        content()
    }
}

@Composable
fun ButtonText(isEnabled: Boolean, text: String = "선택완료", paddingVertical: Dp = 15.dp){
    TextButtonSB16(
        text = text,
        modifier = Modifier.padding(vertical = paddingVertical),
        color = if (isEnabled) MaterialTheme.textColorScheme.onActiveSecondaryButtonColor
        else MaterialTheme.textColorScheme.onDisabledButtonColor
    )
}