package com.fourleafclover.tarot.ui.component

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.fourleafclover.tarot.R
import com.fourleafclover.tarot.demo.viewmodel.DemoViewModel
import com.fourleafclover.tarot.ui.theme.TextB02M16
import com.fourleafclover.tarot.ui.theme.TextB03M14
import com.fourleafclover.tarot.ui.theme.TextButtonM16
import com.fourleafclover.tarot.ui.theme.gray_2
import com.fourleafclover.tarot.ui.theme.gray_6
import com.fourleafclover.tarot.ui.theme.gray_8
import com.fourleafclover.tarot.ui.theme.white

@Composable
@Preview
fun CloseDialog(onClickNo: () -> Unit = {}, onClickOk: () -> Unit = {}) {
    YesNoDialog("운세 보기를 중단하고\n나가시겠습니까?", onClickNo, onClickOk)
}

@Composable
@Preview
fun CloseOnChatDialog(onClickNo: () -> Unit = {}, onClickOk: () -> Unit = {}) {
    YesNoDialog("궁합 보기를 중단하고 나가시겠습니까?\n나간 초대방은 다시 들어올 수 없습니다.", onClickNo, onClickOk)
}

@Composable
@Preview
fun CloseOnRoomInviteDialog(onClickNo: () -> Unit = {}, onClickOk: () -> Unit = {}) {
    YesNoDialog("궁합 보기를 중단하고 나가시겠습니까?\n생성된 초대방이 삭제됩니다.", onClickNo, onClickOk)
}

@Composable
@Preview
fun CloseOnRoomCreateDialog(onClickNo: () -> Unit = {}, onClickOk: () -> Unit = {}) {
    YesNoDialog("궁합 보기를 중단하고 나가시겠습니까?", onClickNo, onClickOk)
}


@Composable
@Preview
fun CloseWithoutSaveDialog(onClickNo: () -> Unit = {}, onClickOk: () -> Unit = {}) {
    YesNoDialog("타로 결과를 저장하지 않고\n나가시겠습니까?", onClickNo, onClickOk)
}

@Composable
@Preview
fun DeleteTarotResultDialog(onClickNo: () -> Unit = {}, onClickOk: () -> Unit = {}) {
    YesNoDialog("해당 타로 기록을\n삭제하시겠습니까?", onClickNo, onClickOk)
}



@Preview
@Composable
fun SaveCompletedDialog(onClickOk: () -> Unit = {}) {

    Surface(modifier = Modifier,
        shape = RoundedCornerShape(size = 10.dp),
        color = white){

        Column(modifier = Modifier
            .wrapContentHeight()
            .width(288.dp)
            .padding(start = 16.dp, top = 24.dp, end = 16.dp, bottom = 16.dp),
            horizontalAlignment = Alignment.CenterHorizontally) {

            Image(painter = painterResource(id = R.drawable.check_filled),
                contentDescription = "",
                modifier = Modifier.size(32.dp))

            TextButtonM16(text = "타로 결과가 저장되었습니다!",
                color = gray_8,
                textAlign = TextAlign.Center,
                modifier = Modifier
                    .align(Alignment.CenterHorizontally)
                    .padding(vertical = 8.dp))

            TextB03M14(text = "저장된 타로는 마이페이지에서\n다시 볼 수 있어요.",
                color = gray_6,
                textAlign = TextAlign.Center,
                modifier = Modifier
                    .align(Alignment.CenterHorizontally)
                    .padding(bottom = 24.dp))

            Button(onClick = onClickOk,
                shape = RoundedCornerShape(6.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .wrapContentHeight()
                    .padding(end = 4.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = gray_8,
                    contentColor = white
                ),
                contentPadding = PaddingValues(0.dp)
            ) {
                TextButtonM16(
                    text = "확인",
                    modifier = Modifier.padding(vertical = 9.dp),
                    color = white
                )
            }

        }

    }

}


@Preview
@Composable
fun YesNoDialog(content: String = "운세 보기를 중단하고\n나가시겠습니까?", onClickNo: () -> Unit = {}, onClickOk: () -> Unit = {}) {

    Surface(modifier = Modifier,
        shape = RoundedCornerShape(size = 10.dp),
        color = white){

        Column(modifier = Modifier
            .width(288.dp)
            .wrapContentHeight()
            .padding(start = 16.dp, top = 40.dp, end = 16.dp, bottom = 16.dp)) {

            TextB02M16(text = content,
                color = gray_8,
                textAlign = TextAlign.Center,
                modifier = Modifier
                    .align(Alignment.CenterHorizontally)
                    .padding(bottom = 24.dp))

            Row {
                Button(onClick = onClickNo,
                    shape = RoundedCornerShape(6.dp),
                    modifier = Modifier
                        .weight(1f)
                        .padding(end = 4.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = gray_2,
                        contentColor = gray_8
                    ),
                    contentPadding = PaddingValues(0.dp)
                ) {
                    TextButtonM16(
                        text = "아니요",
                        modifier = Modifier.padding(vertical = 9.dp),
                        color = gray_8
                    )
                }

                Button(onClick = onClickOk,
                    shape = RoundedCornerShape(6.dp),
                    modifier = Modifier
                        .weight(1f)
                        .padding(start = 4.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = gray_8,
                        contentColor = gray_2
                    ),
                    contentPadding = PaddingValues(0.dp)
                ) {
                    TextButtonM16(
                        text = "네",
                        modifier = Modifier.padding(vertical = 9.dp),
                        color = gray_2
                    )
                }
            }

        }

    }

}


@Preview
@Composable
fun VerticalYesNoDialog(onClickNo: () -> Unit = {}, onClickOk: () -> Unit = {}, onClickClose: () -> Unit = {}) {

    Surface(modifier = Modifier,
        shape = RoundedCornerShape(size = 10.dp),
        color = white){

        Box(modifier = Modifier.width(288.dp), contentAlignment = Alignment.TopEnd){
            Image(
                painter = painterResource(id = R.drawable.cancel_black),
                contentDescription = null,
                modifier = Modifier
                    .padding(top = 8.dp, end = 8.dp)
                    .clickable { onClickClose() }
            )
        }

        Column(modifier = Modifier
            .wrapContentHeight()
            .width(288.dp)
            .padding(top = 24.dp, bottom = 16.dp),
            horizontalAlignment = Alignment.CenterHorizontally) {

            Column(
                modifier = Modifier.padding(start = 13.dp, end = 13.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {

                TextButtonM16(text = "이미 생성하신 초대방이 있어요!",
                    color = gray_8,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.padding(vertical = 8.dp))

                TextB03M14(text = "초대방을 새로 만들 경우, 기존 초대방은 사라져요.",
                    color = gray_6,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.padding(bottom = 24.dp))
            }

            Column(modifier = Modifier.padding(start = 16.dp, end = 16.dp)){

                Button(onClick = onClickOk,
                    shape = RoundedCornerShape(6.dp),
                    modifier = Modifier
                        .fillMaxWidth()
                        .wrapContentHeight()
                        .padding(end = 4.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = gray_8,
                        contentColor = white
                    ),
                    contentPadding = PaddingValues(0.dp)
                ) {
                    TextButtonM16(
                        text = "새로 만들기",
                        modifier = Modifier.padding(vertical = 9.dp),
                        color = white
                    )
                }

                Button(onClick = onClickNo,
                    shape = RoundedCornerShape(6.dp),
                    modifier = Modifier
                        .fillMaxWidth()
                        .wrapContentHeight()
                        .padding(end = 4.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = gray_2,
                        contentColor = gray_8
                    ),
                    contentPadding = PaddingValues(0.dp)
                ) {
                    TextButtonM16(
                        text = "기존 초대방 입장하기",
                        modifier = Modifier.padding(vertical = 9.dp),
                        color = gray_8
                    )
                }
            }


        }

    }

}



@Preview
@Composable
fun RoomDeletedDialog(onClickOk: () -> Unit = {}, onClickClose: () -> Unit = {}) {

    Surface(modifier = Modifier,
        shape = RoundedCornerShape(size = 10.dp),
        color = white){

        Box(modifier = Modifier.width(288.dp), contentAlignment = Alignment.TopEnd){
            Image(
                painter = painterResource(id = R.drawable.cancel_black),
                contentDescription = null,
                modifier = Modifier
                    .padding(top = 8.dp, end = 8.dp)
                    .clickable { onClickClose() }
            )
        }

        Column(modifier = Modifier
            .wrapContentHeight()
            .width(288.dp)
            .padding(start = 16.dp, top = 24.dp, end = 16.dp, bottom = 16.dp),
            horizontalAlignment = Alignment.CenterHorizontally) {

            Image(painter = painterResource(id = R.drawable.alert_red),
                contentDescription = "",
                modifier = Modifier
                    .size(48.dp)
                    .padding(bottom = 8.dp)
            )

            TextButtonM16(text = "1시간 안에 입장하지 않아서\n초대방이 사라졌어요.\uD83D\uDE22",
                color = gray_8,
                textAlign = TextAlign.Center,
                modifier = Modifier
                    .align(Alignment.CenterHorizontally)
                    .padding(bottom = 24.dp))

            Button(onClick = onClickOk,
                shape = RoundedCornerShape(6.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .wrapContentHeight()
                    .padding(end = 4.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = gray_8,
                    contentColor = white
                ),
                contentPadding = PaddingValues(0.dp)
            ) {
                TextButtonM16(
                    text = "확인",
                    modifier = Modifier.padding(vertical = 9.dp),
                    color = white
                )
            }

        }

    }

}

@Preview
@Composable
fun DemoAppNoticeDialog(demoViewModel: DemoViewModel = hiltViewModel(), onClickOk: () -> Unit = {}) {

    Surface(modifier = Modifier,
        shape = RoundedCornerShape(size = 10.dp),
        color = white){

        Column(modifier = Modifier
            .wrapContentHeight()
            .width(288.dp)
            .padding(start = 16.dp, top = 24.dp, end = 16.dp, bottom = 16.dp),
            horizontalAlignment = Alignment.CenterHorizontally) {

            Image(painter = painterResource(id = R.drawable.alert_red),
                contentDescription = "",
                modifier = Modifier
                    .size(48.dp)
                    .padding(bottom = 8.dp)
            )

            TextButtonM16(text = demoViewModel.demoDialogData.title,
                color = gray_8,
                textAlign = TextAlign.Center,
                modifier = Modifier
                    .align(Alignment.CenterHorizontally)
                    .padding(vertical = 8.dp))

            TextB03M14(text = demoViewModel.demoDialogData.content,
                color = gray_6,
                textAlign = TextAlign.Start,
                modifier = Modifier
                    .align(Alignment.CenterHorizontally)
                    .padding(bottom = 24.dp))

            Button(onClick = onClickOk,
                shape = RoundedCornerShape(6.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .wrapContentHeight()
                    .padding(end = 4.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = gray_8,
                    contentColor = white
                ),
                contentPadding = PaddingValues(0.dp)
            ) {
                TextButtonM16(
                    text = "확인",
                    modifier = Modifier.padding(vertical = 9.dp),
                    color = white
                )
            }

        }

    }

}