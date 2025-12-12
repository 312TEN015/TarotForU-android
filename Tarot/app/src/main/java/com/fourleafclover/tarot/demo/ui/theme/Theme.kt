package com.fourleafclover.tarot.demo.ui.theme

import android.app.Activity
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.ReadOnlyComposable
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.platform.LocalView
import androidx.core.view.WindowCompat
import com.fourleafclover.tarot.demo.ui.theme.color.BackgroundColor
import com.fourleafclover.tarot.demo.ui.theme.color.ColorSet
import com.fourleafclover.tarot.demo.ui.theme.color.TarotColors
import com.fourleafclover.tarot.demo.ui.theme.color.TextColor

private val LocalColors = staticCompositionLocalOf { ColorSet.Default.tarotLightColors }

val MaterialTheme.colorScheme: TarotColors
    @Composable
    @ReadOnlyComposable
    get() = LocalColors.current

val MaterialTheme.backgroundColorScheme: BackgroundColor
    @Composable
    @ReadOnlyComposable
    get() = LocalColors.current.backgroundColor

val MaterialTheme.textColorScheme: TextColor
    @Composable
    @ReadOnlyComposable
    get() = LocalColors.current.textColor

@Composable
fun TarotTheme(
    tarotColors: ColorSet = ColorSet.Default,
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    val colors = tarotColors.tarotLightColors

    val view = LocalView.current
    val window = (view.context as Activity).window

    SideEffect {
        WindowCompat.getInsetsController(window, view).apply {
            isAppearanceLightStatusBars = true
            isAppearanceLightNavigationBars = true
        }
    }

    CompositionLocalProvider(LocalColors provides colors) {
        MaterialTheme(
            colors = colors.material,
            typography = Typography,
            content = content
        )
    }

}