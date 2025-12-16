package com.fourleafclover.tarot.demo.ui.screen.harmony

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.fourleafclover.tarot.R
import com.fourleafclover.tarot.SubjectHarmony
import com.fourleafclover.tarot.demo.ui.theme.backgroundColorScheme
import com.fourleafclover.tarot.demo.ui.theme.color.gray_5
import com.fourleafclover.tarot.demo.ui.theme.color.gray_6
import com.fourleafclover.tarot.demo.ui.theme.textColorScheme
import com.fourleafclover.tarot.ui.component.AppBarCloseOnRoomInviteWithDialog
import com.fourleafclover.tarot.ui.component.ButtonNext
import com.fourleafclover.tarot.ui.component.ButtonText
import com.fourleafclover.tarot.ui.component.ShareLinkOrCopy
import com.fourleafclover.tarot.ui.component.getBackgroundModifier
import com.fourleafclover.tarot.ui.navigation.PreventBackPressed
import com.fourleafclover.tarot.ui.navigation.ScreenEnum
import com.fourleafclover.tarot.ui.navigation.navGraphViewModel
import com.fourleafclover.tarot.ui.screen.harmony.viewmodel.HarmonyViewModel
import com.fourleafclover.tarot.ui.screen.harmony.viewmodel.LoadingViewModel
import com.fourleafclover.tarot.ui.screen.main.DialogViewModel
import com.fourleafclover.tarot.ui.theme.TextB02M16
import com.fourleafclover.tarot.ui.theme.TextB04M12
import com.fourleafclover.tarot.ui.theme.TextH02M22

@Preview
@Composable
fun DemoRoomShareScreen(
    navController: NavHostController = rememberNavController()
) {

    val loadingViewModel = navGraphViewModel<LoadingViewModel>(navController)
    val dialogViewModel = navGraphViewModel<DialogViewModel>(navController)
    val harmonyViewModel = navGraphViewModel<HarmonyViewModel>(navController)

    PreventBackPressed()


    Column(modifier = getBackgroundModifier(MaterialTheme.backgroundColorScheme.mainBackgroundColor)) {
        AppBarCloseOnRoomInviteWithDialog(
            navController = navController,
            pickedTopicTemplate = SubjectHarmony,
            backgroundColor = MaterialTheme.backgroundColorScheme.mainBackgroundColor,
            isTitleVisible = false,
            harmonyViewModel = harmonyViewModel,
            dialogViewModel = dialogViewModel
        )

        Column(
            modifier = getBackgroundModifier(MaterialTheme.backgroundColorScheme.mainBackgroundColor)
                .padding(horizontal = 20.dp)
                .verticalScroll(rememberScrollState())
        ) {

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Image(
                    painter = painterResource(id = R.drawable.letter),
                    contentDescription = null,
                    modifier = Modifier.padding(top = 45.dp, bottom = 74.dp)
                )

                TextH02M22(
                    text = "궁합 초대 방이 생성되었어요!",
                    color = MaterialTheme.textColorScheme.titleTextColor,
                    modifier = Modifier.padding(bottom = 8.dp),
                    textAlign = TextAlign.Center
                )
                TextB02M16(
                    text = "궁합을 볼 상대방에게 초대 링크를 전달하고,\n함께 궁합 운세 보기를 시작해요!",
                    color = gray_5,
                    modifier = Modifier.padding(bottom = 40.dp),
                    textAlign = TextAlign.Center
                )

                ShareLinkOrCopy(harmonyViewModel)

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 16.dp),
                    horizontalArrangement = Arrangement.Center
                ) {
                    Image(
                        painter = painterResource(id = R.drawable.alert),
                        contentDescription = null
                    )
                    TextB04M12(
                        text = "1시간 안에 모두 입장하지 않으면 초대방이 사라져요!",
                        color = gray_6,
                        modifier = Modifier.padding(start = 8.dp)
                    )
                }


            }

            ButtonNext(
                onClick = {
                    loadingViewModel.startLoading(
                        navController,
                        ScreenEnum.RoomInviteLoadingScreen,
                        ScreenEnum.RoomEnteringScreen
                    )
                },
                enabled = { true },
                content = { ButtonText(true, "초대방 입장") }
            )
        }


    }
}

