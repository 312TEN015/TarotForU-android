package com.fourleafclover.tarot.ui.screen.harmony

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.fourleafclover.tarot.LocalIsDemo
import com.fourleafclover.tarot.SubjectHarmony
import com.fourleafclover.tarot.demo.ui.theme.backgroundColorScheme
import com.fourleafclover.tarot.demo.ui.theme.color.gray_7
import com.fourleafclover.tarot.demo.ui.theme.colorScheme
import com.fourleafclover.tarot.demo.ui.theme.textColorScheme
import com.fourleafclover.tarot.ui.component.AppBarCloseOnRoomCreateWithDialog
import com.fourleafclover.tarot.ui.component.ButtonNext
import com.fourleafclover.tarot.ui.component.ButtonText
import com.fourleafclover.tarot.ui.component.getBackgroundModifier
import com.fourleafclover.tarot.ui.navigation.NavGraphRoute
import com.fourleafclover.tarot.ui.navigation.PreventBackPressed
import com.fourleafclover.tarot.ui.navigation.ScreenEnum
import com.fourleafclover.tarot.ui.navigation.navGraphViewModel
import com.fourleafclover.tarot.ui.screen.harmony.viewmodel.HarmonyViewModel
import com.fourleafclover.tarot.ui.screen.harmony.viewmodel.LoadingViewModel
import com.fourleafclover.tarot.ui.screen.harmony.viewmodel.NicknameViewModel
import com.fourleafclover.tarot.ui.screen.main.DialogViewModel
import com.fourleafclover.tarot.ui.theme.TextB02M16
import com.fourleafclover.tarot.ui.theme.TextB03M14
import com.fourleafclover.tarot.ui.theme.TextCaptionM12
import com.fourleafclover.tarot.ui.theme.TextH02M22
import com.fourleafclover.tarot.ui.theme.getTextStyle

private const val DEMO_NICKNAME = "해달"

@Preview
@Composable
fun RoomNicknameScreen(
    navController: NavHostController = rememberNavController()
) {

    val isDemo = LocalIsDemo.current
    val dialogViewModel = navGraphViewModel<DialogViewModel>(navController)
    val loadingViewModel = navGraphViewModel<LoadingViewModel>(navController)
    val nicknameViewModel = navGraphViewModel<NicknameViewModel>(navController, NavGraphRoute.HARMONY)
    val harmonyViewModel = navGraphViewModel<HarmonyViewModel>(navController)

    PreventBackPressed()

    LaunchedEffect(Unit) {
        if (isDemo) nicknameViewModel.updateNickname(DEMO_NICKNAME)
    }

    Column(modifier = getBackgroundModifier(MaterialTheme.backgroundColorScheme.mainBackgroundColor)) {
        AppBarCloseOnRoomCreateWithDialog(
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
                .padding(top = 24.dp)
        ) {

            Column(modifier = Modifier.weight(1f)) {
                TextB02M16(text = "그 사람과의 운명을 확인해봐요.", color = MaterialTheme.textColorScheme.subTitleTextColor)
                TextH02M22(text = "궁합을 보러 갈 준비가 되셨나요?", color = MaterialTheme.textColorScheme.titleTextColor)

                Row(
                    modifier = Modifier
                        .padding(top = 24.dp)
                        .background(shape = RoundedCornerShape(10.dp), color = gray_7)
                        .padding(end = 16.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {

                    TextField(
                        modifier = Modifier.weight(1f),
                        textStyle = getTextStyle(
                            fontSize = 16,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.textColorScheme.titleTextColor
                        ),
                        enabled = !isDemo,
                        onValueChange = { newText -> nicknameViewModel.updateNickname(newText) },
                        value = nicknameViewModel.nickname.value,
                        placeholder = {
                            TextB03M14(
                                text = "별명을 입력해주세요.",
                                color = MaterialTheme.textColorScheme.onDisabledButtonColor
                            )
                        },
                        singleLine = true,
                        colors = TextFieldDefaults.colors(
                            cursorColor = MaterialTheme.textColorScheme.titleTextColor,
                            focusedTextColor = MaterialTheme.textColorScheme.textFieldPlaceHolderColor,
                            focusedContainerColor = Color.Transparent,
                            unfocusedContainerColor = Color.Transparent,
                            disabledContainerColor = Color.Transparent,
                            focusedIndicatorColor = Color.Transparent,
                            disabledIndicatorColor = Color.Transparent,
                            unfocusedIndicatorColor = Color.Transparent
                        )
                    )

                    Row(modifier = Modifier) {
                        Text(
                            text = "${nicknameViewModel.getNicknameLength()}", style = getTextStyle(
                                fontSize = 14,
                                fontWeight = FontWeight.Medium,
                                color = MaterialTheme.colorScheme.secondary,
                            )
                        )
                        Text(
                            text = "/10", style = getTextStyle(
                                fontSize = 14,
                                fontWeight = FontWeight.Medium,
                                color = MaterialTheme.textColorScheme.onDialogContentColor,
                            )
                        )
                    }
                }

                TextCaptionM12(
                    modifier = Modifier
                        .padding(top = 8.dp, start = 16.dp, end = 16.dp)
                        .alpha(if (nicknameViewModel.isCaptionVisible.value) 1f else 0f),
                    text = "최대 10글자를 입력할 수 있어요.",
                    color = MaterialTheme.colorScheme.onError
                )
            }

            ButtonNext(
                onClick = {
                    harmonyViewModel.setUserNickname(nicknameViewModel.nickname.value)

                    if (harmonyViewModel.roomId.value.isEmpty()) {
                        loadingViewModel.startLoading(
                            navController,
                            ScreenEnum.RoomCreateLoadingScreen,
                            ScreenEnum.RoomShareScreen
                        )
                    } else {
                        loadingViewModel.startLoading(
                            navController,
                            ScreenEnum.RoomInviteLoadingScreen,
                            ScreenEnum.RoomEnteringScreen
                        )
                    }
                },
                enabled = { nicknameViewModel.isCompleted() },
                content = { ButtonText(isEnabled = nicknameViewModel.isCompleted(), text = "입력완료") }
            )
        }
    }
}
