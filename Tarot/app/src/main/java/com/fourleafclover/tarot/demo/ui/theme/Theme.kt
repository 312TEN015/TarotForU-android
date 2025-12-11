package com.fourleafclover.tarot.demo.ui.theme

import android.app.Activity
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocal
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.ReadOnlyComposable
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalView
import androidx.core.view.WindowCompat
import com.fourleafclover.tarot.demo.ui.theme.color.ColorSet
import com.fourleafclover.tarot.demo.ui.theme.color.TarotColors
import com.fourleafclover.tarot.ui.theme.gray_9

private val LocalColors = staticCompositionLocalOf { ColorSet.Default.lightColors }

@Composable
fun TarotTheme(
    tarotColors: ColorSet,
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    val colors = tarotColors.lightColors

    val view = LocalView.current
    if (!view.isInEditMode) {
        SideEffect {
            val window = (view.context as Activity).window
            window.statusBarColor = gray_9.toArgb()
            window.navigationBarColor = gray_9.toArgb()
            WindowCompat.getInsetsController(window, view).isAppearanceLightStatusBars = darkTheme
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

val MaterialTheme.colorScheme: TarotColors
    @Composable
    @ReadOnlyComposable
    get() = LocalColors.current