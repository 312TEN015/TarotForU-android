package com.fourleafclover.tarot.demo.ui.screen.loading

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.fourleafclover.tarot.SubjectHarmony
import com.fourleafclover.tarot.demo.ui.theme.backgroundColorScheme
import com.fourleafclover.tarot.demo.ui.theme.color.ColorSet
import com.fourleafclover.tarot.demo.ui.theme.textColorScheme
import com.fourleafclover.tarot.ui.component.AppBarCloseOnRoomInviteWithDialog
import com.fourleafclover.tarot.ui.component.LoadingCircle
import com.fourleafclover.tarot.ui.component.ShareLinkOrCopy
import com.fourleafclover.tarot.ui.component.getBackgroundModifier
import com.fourleafclover.tarot.ui.navigation.PreventBackPressed
import com.fourleafclover.tarot.ui.navigation.navGraphViewModel
import com.fourleafclover.tarot.ui.screen.harmony.emitJoin
import com.fourleafclover.tarot.ui.screen.harmony.setOnJoin
import com.fourleafclover.tarot.ui.screen.harmony.viewmodel.ChatViewModel
import com.fourleafclover.tarot.ui.screen.harmony.viewmodel.HarmonyViewModel
import com.fourleafclover.tarot.ui.screen.harmony.viewmodel.LoadingViewModel
import com.fourleafclover.tarot.ui.screen.main.DialogViewModel
import com.fourleafclover.tarot.ui.theme.TextB03M14
import kotlinx.coroutines.delay

// 추후 로딩 화면 컴포넌트화 하기
@Composable
@Preview
fun DemoRoomInviteLoadingScreen(
    navController: NavHostController = rememberNavController()
) {

    val dialogViewModel = navGraphViewModel<DialogViewModel>(navController)
    val chatViewModel = navGraphViewModel<ChatViewModel>(navController)
    val loadingViewModel = navGraphViewModel<LoadingViewModel>(navController)
    val harmonyViewModel = navGraphViewModel<HarmonyViewModel>(navController)

    if (!loadingViewModel.isLoading.value) {
        loadingViewModel.endLoading(navController)
    }

    PreventBackPressed()

    LaunchedEffect(Unit) {
        delay(2000)
        val demoPartnerNickname = "아지"
        harmonyViewModel.setPartnerNickname(demoPartnerNickname)
        chatViewModel.startChat(harmonyViewModel.getUserNickname(), demoPartnerNickname)
        loadingViewModel.updateLoadingState(false)
    }


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
                .padding(bottom = 24.dp),
            horizontalAlignment = Alignment.CenterHorizontally,

            ) {


            LoadingCircle(
                modifier = Modifier.weight(1f),
                "상대방을 기다리는 중입니다...",
                "1시간 안에 모두 입장하지 않으면 초대방이 사라져요!"
            )

            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {

                TextB03M14(
                    text = "상대방이 계속 들어오지 않는다면?\n한번 더 초대 링크를 공유해보세요!",
                    color = MaterialTheme.textColorScheme.onChatGuidBoxColor,
                    textAlign = TextAlign.Center
                )

                ShareLinkOrCopy(harmonyViewModel)

            }


        }
    }

}