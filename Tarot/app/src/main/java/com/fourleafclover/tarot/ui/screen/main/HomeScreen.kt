package com.fourleafclover.tarot.ui.screen.main

import android.app.Activity
import android.content.Context
import android.content.ContextWrapper
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalView
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.fourleafclover.tarot.R
import com.fourleafclover.tarot.demo.viewmodel.DemoViewModel
import com.fourleafclover.tarot.ui.component.DemoAppNoticeDialog
import com.fourleafclover.tarot.ui.component.getBackgroundModifier
import com.fourleafclover.tarot.ui.component.setStatusbarColor
import com.fourleafclover.tarot.ui.navigation.FinishOnBackPressed
import com.fourleafclover.tarot.ui.navigation.ScreenEnum
import com.fourleafclover.tarot.ui.navigation.navigateSaveState
import com.fourleafclover.tarot.ui.screen.fortune.viewModel.FortuneViewModel
import com.fourleafclover.tarot.ui.screen.fortune.viewModel.PickTarotViewModel
import com.fourleafclover.tarot.ui.screen.fortune.viewModel.QuestionInputViewModel
import com.fourleafclover.tarot.ui.screen.harmony.viewmodel.ChatViewModel
import com.fourleafclover.tarot.ui.screen.harmony.viewmodel.GenderViewModel
import com.fourleafclover.tarot.ui.screen.harmony.viewmodel.HarmonyViewModel
import com.fourleafclover.tarot.ui.screen.harmony.viewmodel.LoadingViewModel
import com.fourleafclover.tarot.ui.screen.harmony.viewmodel.NicknameViewModel
import com.fourleafclover.tarot.ui.screen.harmony.viewmodel.ResultViewModel
import com.fourleafclover.tarot.ui.screen.harmony.viewmodel.RoomCreateViewModel
import com.fourleafclover.tarot.ui.screen.my.viewmodel.MyTarotViewModel
import com.fourleafclover.tarot.ui.screen.my.viewmodel.ShareViewModel
import com.fourleafclover.tarot.ui.theme.TextB02M16
import com.fourleafclover.tarot.ui.theme.TextH01M26
import com.fourleafclover.tarot.ui.theme.backgroundColor_2
import com.fourleafclover.tarot.ui.theme.gray_3
import com.fourleafclover.tarot.ui.theme.white
import com.fourleafclover.tarot.utils.receiveShareRequest

fun Context.findActivity(): Activity? = when (this) {
    is Activity -> this
    is ContextWrapper -> baseContext.findActivity()
    else -> null
}

@Composable
fun HomeScreen(
    activity: Activity,
    navController: NavHostController = rememberNavController(),
    harmonyViewModel: HarmonyViewModel,
    shareViewModel: ShareViewModel,
    dialogViewModel: DialogViewModel,
    loadingViewModel: LoadingViewModel,
    fortuneViewModel: FortuneViewModel,
    demoViewModel: DemoViewModel
) {



    // 상태바 초기화
    setStatusbarColor(LocalView.current, backgroundColor_2)

    // 뒤로가기 시 행동
    FinishOnBackPressed()

    // 데모앱 안내문
    if (demoViewModel.isDemo && demoViewModel.demoDialogData.visibility) {
        Dialog(onDismissRequest = { demoViewModel.setDemoDialogVisibility(false) }) {
            DemoAppNoticeDialog(demoViewModel) { demoViewModel.setDemoDialogVisibility(false) }
        }
    }

    Column(modifier = getBackgroundModifier(backgroundColor_2)
        .padding(horizontal = 20.dp)
        .padding(bottom = 60.dp)
        .verticalScroll(rememberScrollState())) {

        TextH01M26(
            text = "타로 카드를 뽑고\n운세를 확인해보세요!",
            modifier = Modifier.padding(top = 26.dp, bottom = 24.dp),
            color = white
        )

        Column {
            TextB02M16(
                text = "주제별 운세",
                color = gray_3,
                modifier = Modifier.padding(bottom = 6.dp)
            )

            Row(Modifier.padding(bottom = 32.dp)) {
                Column(modifier = Modifier
                    .padding(end = 4.dp)
                    .weight(1f)) {
                    Image(modifier = Modifier
                        .padding(bottom = 8.dp)
                        .clickable {
                            fortuneViewModel.setPickedTopic(0)
                            navigateSaveState(navController, ScreenEnum.InputScreen.name)
                        }, painter = painterResource(id = R.drawable.category_love), contentDescription = "연애운")
                    Image(painter = painterResource(id = R.drawable.category_dream),
                        contentDescription = "소망운",
                        modifier = Modifier.clickable {
                            fortuneViewModel.setPickedTopic(2)
                            navigateSaveState(navController, ScreenEnum.InputScreen.name)
                        }
                    )
                }

                Column(modifier = Modifier
                    .padding(start = 4.dp)
                    .weight(1f)) {
                    Image(modifier = Modifier
                        .padding(bottom = 8.dp)
                        .clickable {
                            fortuneViewModel.setPickedTopic(1)
                            navigateSaveState(navController, ScreenEnum.InputScreen.name)
                        },
                        painter = painterResource(id = R.drawable.category_study),
                        contentDescription = "학업운")
                    Image(painter = painterResource(id = R.drawable.category_job),
                        contentDescription = "취업운",
                        modifier = Modifier.clickable {
                            fortuneViewModel.setPickedTopic(3)
                            navigateSaveState(navController, ScreenEnum.InputScreen.name)
                        }
                    )
                }
            }

            Column(modifier = Modifier.padding(bottom = 42.dp)) {
                TextB02M16(
                    text = "오늘의 운세",
                    color = gray_3,
                    modifier = Modifier.padding(bottom = 6.dp)
                )

                Image(painter = painterResource(id = R.drawable.category_today),
                    contentDescription = "오늘의 운세",
                    Modifier.clickable {
                        fortuneViewModel.setPickedTopic(4)
                        navigateSaveState(navController, ScreenEnum.PickTarotScreen.name)
                    })

            }

            Column(modifier = Modifier.padding(bottom = 42.dp)) {
                TextB02M16(
                    text = "궁합 운세",
                    color = gray_3,
                    modifier = Modifier.padding(bottom = 6.dp)
                )

                Image(painter = painterResource(id = R.drawable.category_harmony),
                    contentDescription = "궁합 운세",
                    Modifier.clickable {
                        fortuneViewModel.setPickedTopic(5)
                        navigateSaveState(navController, ScreenEnum.RoomCreateScreen.name)
                    })

            }




        }
    }

}