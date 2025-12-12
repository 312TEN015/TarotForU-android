package com.fourleafclover.tarot.demo.ui.theme.color

import androidx.compose.material.lightColors

sealed class ColorSet {
    open lateinit var tarotLightColors: TarotColors
    open lateinit var darkColors: TarotColors

    object Default : ColorSet() {
        override var tarotLightColors = TarotColors(
            material = lightColors(
                primary = primary,
                primaryVariant = gray_3,
                secondary = secondary,
                secondaryVariant = secondaryVariant,
                surface = gray_9,
                onSurface = gray_0,
                background = gray_9,
                onBackground = gray_0,
                error = error,
                onError = onError,
                onPrimary = gray_0
            ),
            backgroundColor = BackgroundColor(
                mainBackgroundColor = gray_9,
                splashBackgroundColor = gray_9,
                onBoardingBackgroundColor = gray_9,
                activePrimaryButtonBackgroundColor = gray_9,
                activeSecondaryButtonBackgroundColor = secondary,
                disabledButtonBackgroundColor = gray_6,
                dialogButtonYesBackgroundColor = gray_9,
                dialogButtonNoBackgroundColor = gray_2,
                dialogBackgroundColor = gray_0,

                myTarotItemBackgroundColor = gray_7,

                questionTextFieldBackgroundColor = gray_9,
                inputScreenBackgroundColor = gray_8,

                myChatItemBackgroundColor = secondaryVariant,
                partnerChatItemBackgroundColor = gray_7,
                chatGuidBoxColor = gray_8,

                cardBlankGuidBoxColor = gray_4,

                cardSliderBackgroundColor = gray_8,

                activeTabColor = gray_0,
                inactiveTabColor = gray_7,

                genderActiveButtonBackgroundColor = primaryVariant,
                genderInactiveButtonBackgroundColor = gray_7
            ),
            textColor = TextColor(
                titleTextColor = gray_0,
                subTitleTextColor = gray_3,

                onActivePrimaryButtonColor = gray_0,
                onActiveSecondaryButtonColor = gray_1,
                onDisabledButtonColor = gray_5,
                onDialogYesButtonColor = gray_0,
                onDialogNoButtonColor = gray_8,
                onDialogTitleColor = gray_8,
                onDialogContentColor = gray_6,

                questionNumberColor = secondary,
                questionTitleColor = gray_3,
                answerHintColor = gray_3,

                onPartnerChatItemColor = gray_1,
                onMyChatItemColor = gray_8,
                onChatGuidBoxColor = gray_5,

                onCardBlankGuidBoxColor = gray_4,

                onActiveTabColor = gray_7,
                onInactiveTabColor = gray_5
            ),
            love = primaryLove,
            study = primaryStudy,
            dream = primaryDream,
            job = primaryJob
        )

        //override var darkColors = TarotColors()
    }

}