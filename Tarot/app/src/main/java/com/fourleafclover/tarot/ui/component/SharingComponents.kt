package com.fourleafclover.tarot.ui.component

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.fourleafclover.tarot.R
import com.fourleafclover.tarot.demo.ui.theme.backgroundColorScheme
import com.fourleafclover.tarot.demo.ui.theme.color.ColorSet
import com.fourleafclover.tarot.demo.ui.theme.color.gray_7
import com.fourleafclover.tarot.demo.ui.theme.textColorScheme
import com.fourleafclover.tarot.ui.screen.harmony.viewmodel.HarmonyViewModel
import com.fourleafclover.tarot.ui.theme.TextB02M16
import com.fourleafclover.tarot.utils.ShareActionType
import com.fourleafclover.tarot.utils.ShareLinkType
import com.fourleafclover.tarot.utils.setDynamicLink

@Composable
fun ShareLinkOrCopy(harmonyViewModel: HarmonyViewModel) {
    val localContext = LocalContext.current

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 24.dp),
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        HowToShareButton(
            modifier = Modifier.weight(1f),
            iconResource = R.drawable.share_g2,
            text = "초대 링크 공유",
            onClick = {
                setDynamicLink(
                    localContext,
                    harmonyViewModel.roomId.value,
                    ShareLinkType.HARMONY,
                    ShareActionType.OPEN_SHEET,
                    harmonyViewModel
                )
            })
        HowToShareButton(
            modifier = Modifier.weight(1f),
            iconResource = R.drawable.unlink_g2,
            text = "초대 링크 복사",
            onClick = {
                setDynamicLink(
                    localContext,
                    harmonyViewModel.roomId.value,
                    ShareLinkType.HARMONY,
                    ShareActionType.COPY_LINK,
                    harmonyViewModel
                )
            })
    }
}

@Composable
fun HowToShareButton(
    modifier: Modifier = Modifier,
    iconResource: Int = R.drawable.share,
    text: String = "초대 링크 공유",
    onClick: () -> Unit = {}
) {
    Box(
        modifier = getOutlinedRectangleModifier(
            borderColor = gray_7,
            fillColor = MaterialTheme.backgroundColorScheme.activePrimaryButtonBackgroundColor,
            cornerRadius = 10.dp
        )
            .padding(vertical = 15.dp)
            .clickable { onClick() }
            .then(modifier),
        contentAlignment = Alignment.Center
    ) {
        Row(horizontalArrangement = Arrangement.Center) {
            Image(
                modifier = Modifier
                    .padding(end = 4.dp)
                    .size(20.dp),
                painter = painterResource(id = iconResource),
                contentDescription = null
            )
            TextB02M16(text = text, color = MaterialTheme.textColorScheme.resultScreenSubTitleColor)
        }
    }
}