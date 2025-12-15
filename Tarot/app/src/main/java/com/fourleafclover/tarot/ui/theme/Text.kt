package com.fourleafclover.tarot.ui.theme

import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.PlatformTextStyle
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.fourleafclover.tarot.R
import com.fourleafclover.tarot.demo.ui.theme.color.gray_9

val Pretendard = FontFamily(
    Font(R.font.pretendard_semibold, FontWeight.Bold, FontStyle.Normal),
    Font(R.font.pretendard_medium, FontWeight.Medium, FontStyle.Normal),
    Font(R.font.pretendard_regular, FontWeight.Normal, FontStyle.Normal)
)

@Composable
fun toSp(dp: Dp) = with(LocalDensity.current) { dp.toSp() }

@Composable
fun getTextStyle(fontSize: Int, fontWeight: FontWeight, color: Color = Color.White): TextStyle {
    return TextStyle(
        fontFamily = Pretendard,
        fontSize = toSp(fontSize.dp),
        color = color,
        fontWeight = fontWeight,
        platformStyle = PlatformTextStyle(
            includeFontPadding = true
        )
    )
}

@Composable
fun getLineHeight(lineHeight: Int) : TextUnit {
    return with(LocalDensity.current) {
        // 밀도별로 dp 값을 조정
        val adjustedSpacing = when {
            density <= 1.0 -> lineHeight / 3 * 2          // ldpi (저밀도)
            else -> lineHeight
        }

        (adjustedSpacing.toFloat().dp).toSp()
    }
}

@Composable
fun TextH01M26(text: String,
               modifier: Modifier = Modifier,
               color: Color = gray_9,
               textAlign: TextAlign = TextAlign.Left){
    return Text(
        text = text,
        modifier = modifier,
        lineHeight = getLineHeight(lineHeight = 34),
        style = getTextStyle(fontSize = 26, fontWeight = FontWeight.Medium, color = color),
        textAlign = textAlign
    )
}

@Composable
fun TextH02M22(text: String,
               modifier: Modifier = Modifier,
               color: Color = gray_9,
               textAlign: TextAlign = TextAlign.Left){
    return Text(
        text = text,
        modifier = modifier,
        lineHeight = getLineHeight(lineHeight = 34),
        style = getTextStyle(fontSize = 22, fontWeight = FontWeight.Medium, color = color),
        textAlign = textAlign
    )
}

@Composable
fun TextH03SB18(text: String,
               modifier: Modifier = Modifier,
               color: Color = gray_9,
                textAlign: TextAlign = TextAlign.Left){
    return Text(
        text = text,
        modifier = modifier,
        lineHeight = getLineHeight(lineHeight = 24),
        style = getTextStyle(fontSize = 18, fontWeight = FontWeight.SemiBold, color = color),
        textAlign = textAlign
    )
}

@Composable
fun TextB01M18(text: String,
               modifier: Modifier = Modifier,
               color: Color = gray_9,
               textAlign: TextAlign = TextAlign.Left){
    return Text(
        text = text,
        modifier = modifier,
        lineHeight = getLineHeight(lineHeight = 28),
        style = getTextStyle(fontSize = 18, fontWeight = FontWeight.Medium, color = color),
        textAlign = textAlign
    )
}

@Composable
fun TextB02M16(text: String,
               modifier: Modifier = Modifier,
               color: Color = gray_9,
               textAlign: TextAlign = TextAlign.Left,
               overflow: TextOverflow = TextOverflow.Visible){
    return Text(
        text = text,
        modifier = modifier,
        lineHeight = getLineHeight(lineHeight = 28),
        style = getTextStyle(fontSize = 16, fontWeight = FontWeight.Medium, color = color),
        textAlign = textAlign,
        overflow = overflow
    )
}

@Composable
fun TextB03M14(text: String,
               modifier: Modifier = Modifier,
               color: Color = gray_9,
               textAlign: TextAlign = TextAlign.Left){
    return Text(
        text = text,
        modifier = modifier,
        lineHeight = getLineHeight(lineHeight = 20),
        style = getTextStyle(fontSize = 14, fontWeight = FontWeight.Medium, color = color),
        textAlign = textAlign,
        letterSpacing = toSp(dp = (-0.2).dp)
    )
}

@Composable
fun TextB04M12(text: String,
               modifier: Modifier = Modifier,
               color: Color = gray_9,
               textAlign: TextAlign = TextAlign.Left){
    return Text(
        text = text,
        modifier = modifier,
        lineHeight = getLineHeight(lineHeight = 16),
        style = getTextStyle(fontSize = 12, fontWeight = FontWeight.Medium, color = color),
        textAlign = textAlign
    )
}

@Composable
fun TextCaptionM12(text: String,
                   modifier: Modifier = Modifier,
                   color: Color = gray_9,
                   textAlign: TextAlign = TextAlign.Left){
    return Text(
        text = text,
        modifier = modifier,
        lineHeight = getLineHeight(lineHeight = 20),
        style = getTextStyle(fontSize = 12, fontWeight = FontWeight.Medium, color = color),
        textAlign = textAlign
    )
}

@Composable
fun TextButtonM16(text: String,
                  modifier: Modifier = Modifier,
                  color: Color = gray_9,
                  textAlign: TextAlign = TextAlign.Left){
    return Text(
        text = text,
        modifier = modifier,
        lineHeight = getLineHeight(lineHeight = 26),
        style = getTextStyle(fontSize = 16, fontWeight = FontWeight.Medium, color = color),
        textAlign = textAlign
    )
}

@Composable
fun TextButtonSB16(text: String,
                  modifier: Modifier = Modifier,
                  color: Color = gray_9,
                  textAlign: TextAlign = TextAlign.Left){
    return Text(
        text = text,
        modifier = modifier,
        lineHeight = getLineHeight(lineHeight = 26),
        style = getTextStyle(fontSize = 16, fontWeight = FontWeight.SemiBold, color = color),
        textAlign = textAlign
    )
}

@Composable
fun TextButtonM18(text: String,
                  modifier: Modifier = Modifier,
                  color: Color = gray_9,
                  textAlign: TextAlign = TextAlign.Left){
    return Text(
        text = text,
        modifier = modifier,
        style = getTextStyle(fontSize = 18, fontWeight = FontWeight.Medium, color = color),
        textAlign = textAlign
    )
}