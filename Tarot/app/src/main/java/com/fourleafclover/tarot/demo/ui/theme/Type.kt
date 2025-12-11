package com.fourleafclover.tarot.demo.ui.theme

import androidx.compose.material.Typography
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.fourleafclover.tarot.R

private val pretendardBold = FontFamily(
    Font(R.font.pretendard_semibold, FontWeight.Bold)
)
private val pretendardNormal = FontFamily(
    Font(R.font.pretendard_medium, FontWeight.Normal)
)
private val pretendardThin = FontFamily(
    Font(R.font.pretendard_regular, FontWeight.Thin)
)

@Composable
fun toSp(dp: Dp) = with(LocalDensity.current) { dp.toSp() }

var Typography = Typography(defaultFontFamily = pretendardNormal)
