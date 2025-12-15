package com.fourleafclover.tarot.ui.screen.harmony

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.fourleafclover.tarot.MyApplication
import com.fourleafclover.tarot.R
import com.fourleafclover.tarot.demo.ui.theme.backgroundColorScheme
import com.fourleafclover.tarot.demo.ui.theme.textColorScheme
import com.fourleafclover.tarot.ui.component.AppBarPlain
import com.fourleafclover.tarot.ui.component.RoomDeletedDialog
import com.fourleafclover.tarot.ui.component.VerticalYesNoDialog
import com.fourleafclover.tarot.ui.component.getBackgroundModifier
import com.fourleafclover.tarot.ui.component.getOutlinedRectangleModifier
import com.fourleafclover.tarot.ui.navigation.ScreenEnum
import com.fourleafclover.tarot.ui.navigation.navigateInclusive
import com.fourleafclover.tarot.ui.screen.harmony.viewmodel.HarmonyViewModel
import com.fourleafclover.tarot.ui.screen.harmony.viewmodel.RoomCreateViewModel
import com.fourleafclover.tarot.ui.theme.TextB03M14
import com.fourleafclover.tarot.ui.theme.TextH02M22
import com.fourleafclover.tarot.ui.theme.TextH03SB18

@Preview
@Composable
fun RoomCreateScreen(
    navController: NavHostController = rememberNavController(),
    roomCreateViewModel: RoomCreateViewModel = hiltViewModel(),
    harmonyViewModel: HarmonyViewModel = hiltViewModel()
) {

    OpenRoomDeletedDialog(navController = navController, roomCreateViewModel = roomCreateViewModel)
    OpenRoomExistDialog(
        navController = navController,
        roomCreateViewModel = roomCreateViewModel,
        harmonyViewModel = harmonyViewModel
    )

    Column(modifier = getBackgroundModifier(MaterialTheme.backgroundColorScheme.mainBackgroundColor )) {
        AppBarPlain(
            navController,
            "",
            backgroundColor = MaterialTheme.backgroundColorScheme.mainBackgroundColor ,
            backButtonResource = R.drawable.arrow_left_white
        )

        Column(
            modifier = getBackgroundModifier(MaterialTheme.backgroundColorScheme.mainBackgroundColor )
                .padding(horizontal = 20.dp)
                .verticalScroll(rememberScrollState())
        ) {

            TextH02M22(
                text = "초대방을 만들고, 궁합을 함께 볼 상대방을 초대해보세요.",
                modifier = Modifier.padding(vertical = 24.dp),
                color = MaterialTheme.textColorScheme.titleTextColor
            )

            Box(
                modifier = Modifier
                    .padding(bottom = 34.dp)
                    .background(color = MaterialTheme.textColorScheme.titleTextColor , shape = RoundedCornerShape(10.dp))
                    .wrapContentHeight()
                    .fillMaxWidth()
                    .clickable {
                        // 소켓 연결하기
                        MyApplication.connectSocket()
                        harmonyViewModel.setIsRoomOwner(true)
                        roomCreateViewModel.checkRoomExist(navController, harmonyViewModel)
                    }
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp, vertical = 44.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {

                    TextH03SB18(text = "초대방 생성하기", modifier = Modifier)

                    Image(
                        painter = painterResource(id = R.drawable.arrow_right_black),
                        contentDescription = null,
                        alignment = Alignment.CenterEnd
                    )
                }
            }

            Column(
                modifier = getOutlinedRectangleModifier(
                    borderColor = MaterialTheme.backgroundColorScheme.partnerChatItemBackgroundColor,
                    fillColor = Color.Transparent,
                    cornerRadius = 10.dp
                )
                    .fillMaxWidth()
                    .wrapContentHeight()
                    .padding(start = 28.dp, top = 24.dp, end = 28.dp, bottom = 24.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {

                TextB03M14(
                    text = "궁합 운세 보기 3단계\uD83D\uDD2E",
                    modifier = Modifier
                        .padding(bottom = 8.dp)
                        .background(
                            color = Color(0x20B479FF),
                            shape = RoundedCornerShape(8.dp)
                        )
                        .padding(horizontal = 10.dp, vertical = 6.dp),
                    color = MaterialTheme.textColorScheme.titleTextColor
                )

                DescriptionStep(
                    number = "1",
                    title = "초대방을 생성해주세요.",
                    imageResourceId = R.drawable.pencil
                )
                DescriptionStep(
                    number = "2",
                    title = "궁합을 함께 볼 상대방에게 \n초대방 링크를 전달해요.",
                    imageResourceId = R.drawable.unlink
                )
                DescriptionStep(
                    number = "3",
                    title = "상대방이 초대방에 입장하면 \n함께 궁합 보기를 시작해요!",
                    imageResourceId = R.drawable.happy_outline
                )

            }


        }
    }


}

@Composable
fun DescriptionStep(
    number: String,
    title: String,
    imageResourceId: Int
) {

    Row(
        modifier = Modifier
            .padding(bottom = 16.dp)
            .padding(vertical = 8.dp, horizontal = 10.dp)
            .fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {

        Row {
            TextB03M14(
                text = number,
                modifier = Modifier
                    .size(22.dp)
                    .background(
                        color = MaterialTheme.textColorScheme.onDialogContentColor,
                        shape = CircleShape
                    ),
                color = MaterialTheme.textColorScheme.titleTextColor,
                textAlign = TextAlign.Center,
            )

            TextB03M14(
                text = title,
                modifier = Modifier.padding(start = 8.dp),
                color = MaterialTheme.textColorScheme.titleTextColor
            )
        }

        Image(
            modifier = Modifier
                .background(shape = CircleShape, color = MaterialTheme.backgroundColorScheme.secondaryBackgroundColor)
                .padding(14.dp),
            painter = painterResource(id = imageResourceId),
            contentDescription = null,
            alignment = Alignment.Center,
        )

    }
}


@Composable
fun OpenRoomExistDialog(
    navController: NavHostController,
    roomCreateViewModel: RoomCreateViewModel,
    harmonyViewModel: HarmonyViewModel
) {

    if (roomCreateViewModel.openRoomExistDialog.value) {
        Dialog(onDismissRequest = { roomCreateViewModel.closeRoomExistDialog() }) {
            VerticalYesNoDialog(
                onClickNo = {
                    if (roomCreateViewModel.isRoomExpired.value) {
                        roomCreateViewModel.openRoomDeletedDialog()
                    } else {
                        roomCreateViewModel.closeRoomExistDialog()
                        // 기존 방으로 입장하기
                        harmonyViewModel.enterExistingRoom()
                        navigateInclusive(navController, ScreenEnum.RoomNicknameScreen.name)
                    }
                },
                onClickClose = { roomCreateViewModel.closeRoomExistDialog() },
                onClickOk = {
                    roomCreateViewModel.closeRoomExistDialog()
                    // 새로운 방 입장하기
                    roomCreateViewModel.isRoomExpired.value = false
                    navigateInclusive(navController, ScreenEnum.RoomGenderScreen.name)
                })
        }
    }

}

@Composable
fun OpenRoomDeletedDialog(
    navController: NavHostController,
    roomCreateViewModel: RoomCreateViewModel
) {

    if (roomCreateViewModel.openRoomDeletedDialog.value) {
        Dialog(onDismissRequest = { roomCreateViewModel.closeRoomDeletedDialog() }) {
            RoomDeletedDialog(
                onClickClose = { roomCreateViewModel.closeRoomDeletedDialog() },
                onClickOk = { roomCreateViewModel.closeRoomDeletedDialog() }
            )
        }
    }

}