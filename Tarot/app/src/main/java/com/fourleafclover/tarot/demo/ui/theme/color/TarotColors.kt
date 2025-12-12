package com.fourleafclover.tarot.demo.ui.theme.color

import androidx.compose.material.Colors
import androidx.compose.ui.graphics.Color

data class TarotColors(
    val material: Colors,
    val backgroundColor: BackgroundColor,
    val textColor: TextColor,
    val love: Color = Color.White,
    val study: Color = Color.White,
    val dream: Color = Color.White,
    val job: Color = Color.White
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
    val partnerChatItemBackgroundColor: Color,
    val myChatItemBackgroundColor: Color,
    val myTarotItemBackgroundColor: Color,
    val questionTextFieldBackgroundColor: Color,
    val inputScreenBackgroundColor: Color,
    val chatGuidBoxColor: Color,
    val cardBlankGuidBoxColor: Color,
    val cardSliderBackgroundColor: Color,
    val activeTabColor: Color,
    val inactiveTabColor: Color,
    val genderActiveButtonBackgroundColor: Color,
    val genderInactiveButtonBackgroundColor: Color

)

data class TextColor (
    val titleTextColor: Color,
    val subTitleTextColor: Color,
    val onActivePrimaryButtonColor: Color,
    val onActiveSecondaryButtonColor: Color,
    val onDisabledButtonColor: Color,
    val onDialogYesButtonColor: Color,
    val onDialogNoButtonColor: Color,
    val onDialogTitleColor: Color,
    val onDialogContentColor: Color,
    val questionNumberColor: Color,
    val questionTitleColor: Color,
    val answerHintColor: Color,
    val onPartnerChatItemColor: Color,
    val onMyChatItemColor: Color,
    val onChatGuidBoxColor: Color,
    val onCardBlankGuidBoxColor: Color,
    val onActiveTabColor : Color,
    val onInactiveTabColor: Color
)