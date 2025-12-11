package com.fourleafclover.tarot.demo.ui.theme.color

import androidx.compose.material.Colors
import androidx.compose.ui.graphics.Color
import com.fourleafclover.tarot.ui.theme.white

data class TarotColors(
    val material: Colors,
    val backgroundColor: BackgroundColor,
    val textColor: TextColor,
    val love: Color = white,
    val study: Color = white,
    val dream: Color = white,
    val job: Color = white
) {
    val primary: Color get() = material.primary
    val primaryVariant: Color get() = material.primaryVariant
    val secondary: Color get() = material.secondary
    val secondaryVariant: Color get() = material.secondaryVariant
    val background: Color get() = material.background
    val surface: Color get() = material.surface
    val onPrimary: Color get() = material.onPrimary
    val onSecondary: Color get() = material.onSecondary
    val onBackground: Color get() = material.onBackground
    val onSurface: Color get() = material.onSurface
    val error: Color get() = material.error
    val onError: Color get() = material.onError
    val isLight: Boolean get() = material.isLight
}

data class BackgroundColor(
    val mainBackgroundColor: Color,
    val splashBackgroundColor: Color,
    val onBoardingBackgroundColor: Color,
    val activePrimaryButtonBackgroundColor: Color,
    val activeSecondaryButtonBackgroundColor: Color,
    val disabledButtonBackgroundColor: Color,
    val dialogButtonYesBackgroundColor: Color,
    val dialogButtonNoBackgroundColor: Color,
    val dialogBackgroundColor: Color,
    val myTarotItemBackgroundColor: Color,
    val questionTextFieldBackgroundColor: Color,
    val questionScreenBackgroundColor: Color
)

data class TextColor (
    val titleTextColor: Color,
    val subTitleTextColor: Color,
    val onActiveButtonColor: Color,
    val onDisabledButtonColor: Color,
    val onDialogYesButtonColor: Color,
    val onDialogNoButtonColor: Color,
    val onDialogTitleColor: Color,
    val onDialogContentColor: Color,
    val questionNumberColor: Color,
    val questionTitleColor: Color,
    val answerHintColor: Color,
)