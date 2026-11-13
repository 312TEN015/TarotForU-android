package com.fourleafclover.tarot.notice

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.MaterialTheme
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.fourleafclover.tarot.R
import com.fourleafclover.tarot.demo.ui.theme.backgroundColorScheme
import com.fourleafclover.tarot.demo.ui.theme.textColorScheme
import com.fourleafclover.tarot.ui.theme.TextB03M14
import com.fourleafclover.tarot.ui.theme.TextButtonM16

/**
 * 점검 안내나 강제 업데이트처럼 앱을 쓸 수 없을 때, 앱 화면 대신 띄우는 화면.
 * 팝업은 뒤로가기나 바깥 터치로 닫히지 않고, 확인 버튼으로만 다음 동작을 한다.
 */
@Composable
fun BlockingNoticeScreen(notice: AppNotice, onConfirm: () -> Unit) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.backgroundColorScheme.mainBackgroundColor)
    )

    Dialog(
        onDismissRequest = { /* 닫을 수 없는 팝업 */ },
        properties = DialogProperties(dismissOnBackPress = false, dismissOnClickOutside = false)
    ) {
        NoticeDialog(title = notice.title, content = notice.content, onClickOk = onConfirm)
    }
}

@Composable
private fun NoticeDialog(title: String, content: String, onClickOk: () -> Unit) {
    Surface(
        shape = RoundedCornerShape(size = 10.dp),
        color = MaterialTheme.backgroundColorScheme.dialogBackgroundColor
    ) {
        Column(
            modifier = Modifier
                .wrapContentHeight()
                .width(288.dp)
                .padding(start = 16.dp, top = 24.dp, end = 16.dp, bottom = 16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Image(
                painter = painterResource(id = R.drawable.alert_red),
                contentDescription = null,
                modifier = Modifier
                    .size(48.dp)
                    .padding(bottom = 8.dp)
            )

            TextButtonM16(
                text = title,
                color = MaterialTheme.textColorScheme.onDialogTitleColor,
                textAlign = TextAlign.Center,
                modifier = Modifier.padding(vertical = 8.dp)
            )

            if (content.isNotBlank()) {
                TextB03M14(
                    text = content,
                    color = MaterialTheme.textColorScheme.onDialogContentColor,
                    textAlign = TextAlign.Center
                )
            }

            Button(
                onClick = onClickOk,
                shape = RoundedCornerShape(6.dp),
                modifier = Modifier
                    .padding(top = 24.dp)
                    .fillMaxWidth()
                    .wrapContentHeight(),
                colors = ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.backgroundColorScheme.dialogButtonYesBackgroundColor,
                    contentColor = MaterialTheme.textColorScheme.onDialogYesButtonColor
                ),
                contentPadding = PaddingValues(0.dp)
            ) {
                TextButtonM16(
                    text = "확인",
                    modifier = Modifier.padding(vertical = 9.dp),
                    color = MaterialTheme.textColorScheme.onDialogYesButtonColor
                )
            }
        }
    }
}
