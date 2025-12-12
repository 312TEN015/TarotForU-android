package com.fourleafclover.tarot.demo.ui.component

import androidx.compose.material.MaterialTheme
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.ButtonDefaults
import androidx.compose.runtime.Composable
import com.fourleafclover.tarot.demo.ui.theme.backgroundColorScheme
import com.fourleafclover.tarot.demo.ui.theme.textColorScheme


@Composable
fun secondaryButtonColors(): ButtonColors {
    return ButtonDefaults.buttonColors(
        containerColor = MaterialTheme.backgroundColorScheme.activeSecondaryButtonBackgroundColor,
        contentColor = MaterialTheme.textColorScheme.onActiveSecondaryButtonColor,
        disabledContainerColor = MaterialTheme.backgroundColorScheme.disabledButtonBackgroundColor,
        disabledContentColor = MaterialTheme.textColorScheme.onDisabledButtonColor
    )
}

@Composable
fun primaryButtonColors(): ButtonColors {
    return ButtonDefaults.buttonColors(
        containerColor = MaterialTheme.backgroundColorScheme.activePrimaryButtonBackgroundColor,
        contentColor = MaterialTheme.textColorScheme.onActivePrimaryButtonColor,
        disabledContainerColor = MaterialTheme.backgroundColorScheme.disabledButtonBackgroundColor,
        disabledContentColor = MaterialTheme.textColorScheme.onDisabledButtonColor
    )
}