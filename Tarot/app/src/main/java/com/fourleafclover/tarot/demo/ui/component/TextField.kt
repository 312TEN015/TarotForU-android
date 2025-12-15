package com.fourleafclover.tarot.demo.ui.component

import androidx.compose.material.MaterialTheme
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.TextFieldColors
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import com.fourleafclover.tarot.demo.ui.theme.backgroundColorScheme
import com.fourleafclover.tarot.demo.ui.theme.color.ColorSet
import com.fourleafclover.tarot.demo.ui.theme.textColorScheme

@Composable
fun TextFieldColors(): TextFieldColors {
    return TextFieldDefaults.colors (
        cursorColor = MaterialTheme.textColorScheme.highlightTextColor,
        focusedTextColor = MaterialTheme.textColorScheme.textFieldTextColor,
        focusedContainerColor = MaterialTheme.backgroundColorScheme.secondaryBackgroundColor,
        unfocusedContainerColor = MaterialTheme.backgroundColorScheme.secondaryBackgroundColor,
        disabledContainerColor = MaterialTheme.backgroundColorScheme.secondaryBackgroundColor,
        focusedIndicatorColor = Color.Transparent,
        disabledIndicatorColor = Color.Transparent,
        unfocusedIndicatorColor = Color.Transparent
    )
}