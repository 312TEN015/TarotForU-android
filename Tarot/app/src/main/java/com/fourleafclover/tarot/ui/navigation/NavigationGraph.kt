package com.fourleafclover.tarot.ui.navigation

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.material.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.LocalViewModelStoreOwner
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.compose.runtime.rememberCoroutineScope
import androidx.navigation.compose.navigation
import com.fourleafclover.tarot.MainActivity
import com.fourleafclover.tarot.demo.ui.theme.backgroundColorScheme
import com.fourleafclover.tarot.demo.viewmodel.DemoViewModel
import com.fourleafclover.tarot.ui.component.BottomNavigationBar
import com.fourleafclover.tarot.ui.screen.fortune.InputScreen
import com.fourleafclover.tarot.ui.screen.fortune.PickTarotScreen
import com.fourleafclover.tarot.ui.screen.fortune.TarotResultScreen
import com.fourleafclover.tarot.ui.screen.fortune.viewModel.PickTarotViewModel
import com.fourleafclover.tarot.ui.screen.fortune.viewModel.QuestionInputViewModel
import com.fourleafclover.tarot.ui.screen.harmony.HarmonyResultScreen
import com.fourleafclover.tarot.ui.screen.harmony.RoomChatScreen
import com.fourleafclover.tarot.ui.screen.harmony.RoomCreateScreen
import com.fourleafclover.tarot.ui.screen.harmony.RoomEnteringScreen
import com.fourleafclover.tarot.ui.screen.harmony.RoomGenderScreen
import com.fourleafclover.tarot.ui.screen.harmony.RoomNicknameScreen
import com.fourleafclover.tarot.ui.screen.harmony.RoomShareScreen
import com.fourleafclover.tarot.ui.screen.harmony.viewmodel.HarmonyViewModel
import com.fourleafclover.tarot.ui.screen.harmony.viewmodel.LoadingViewModel
import com.fourleafclover.tarot.ui.screen.harmony.viewmodel.ResultViewModel
import com.fourleafclover.tarot.ui.screen.loading.LoadingScreen
import com.fourleafclover.tarot.ui.screen.loading.RoomCreateLoadingScreen
import com.fourleafclover.tarot.ui.screen.loading.RoomInviteLoadingScreen
import com.fourleafclover.tarot.ui.screen.main.DialogViewModel
import com.fourleafclover.tarot.ui.screen.main.HomeScreen
import com.fourleafclover.tarot.ui.screen.main.PagerOnBoarding
import com.fourleafclover.tarot.ui.screen.my.MyTarotDetailScreen
import com.fourleafclover.tarot.ui.screen.my.MyTarotHarmonyDetail
import com.fourleafclover.tarot.ui.screen.my.MyTarotScreen
import com.fourleafclover.tarot.ui.screen.my.ShareDetailScreen
import com.fourleafclover.tarot.ui.screen.my.ShareHarmonyDetailScreen
import com.fourleafclover.tarot.ui.screen.my.viewmodel.MyTarotViewModel
import com.fourleafclover.tarot.ui.screen.my.viewmodel.ShareViewModel
import com.fourleafclover.tarot.utils.receiveShareRequest

@Composable
fun NavigationHost(
    startDestination: String = ScreenEnum.HomeScreen.name,
    dialogData: DemoViewModel.DemoDialogData? = null,
) {
    val navController = rememberNavController()
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route
    val activity = LocalViewModelStoreOwner.current as MainActivity

    Scaffold(
        bottomBar = {
            if (currentRoute == ScreenEnum.HomeScreen.name || currentRoute == ScreenEnum.MyTarotScreen.name) {
                BottomNavigationBar(navController = navController)
            }
        },
        modifier = Modifier
            .background(color = MaterialTheme.backgroundColorScheme.mainBackgroundColor)
            .systemBarsPadding()
            .imePadding()
    ) { innerPadding -> innerPadding

        NavHost(navController = navController, startDestination = startDestination, route = NavGraphRoute.ROOT) {
            composable(ScreenEnum.HomeScreen.name) {

                val questionInputViewModel: QuestionInputViewModel = navGraphViewModel(navController)
                val pickTarotViewModel: PickTarotViewModel = navGraphViewModel(navController)
                val resultViewModel: ResultViewModel = navGraphViewModel(navController)

                val dialogViewModel: DialogViewModel = navGraphViewModel(navController)
                val shareViewModel: ShareViewModel = navGraphViewModel(navController)
                val loadingViewModel: LoadingViewModel = navGraphViewModel(navController)

                val harmonyViewModel: HarmonyViewModel = navGraphViewModel(navController)

                val myTarotViewModel: MyTarotViewModel = navGraphViewModel(navController)
                val demoViewModel: DemoViewModel = navGraphViewModel<DemoViewModel>(navController)
                    .apply {
                        LaunchedEffect(Unit) {
                            dialogData?.let { setDemoDialog(dialogData) }
                        }
                    }

                val coroutineScope = rememberCoroutineScope()
                LaunchedEffect(Unit) {

                    questionInputViewModel.clear()
                    pickTarotViewModel.clear()
                    resultViewModel.clear()
                    dialogViewModel.clear()
                    shareViewModel.clear()
                    loadingViewModel.clear()
                    harmonyViewModel.clear()
                    myTarotViewModel.clear()

                    // 공유하기 확인
                    if (activity.intent != null) {
                        receiveShareRequest(activity, navController, shareViewModel, loadingViewModel, harmonyViewModel, coroutineScope)
                    }
                }

                HomeScreen(
                    activity,
                    navController
                    )
            }
            composable(ScreenEnum.MyTarotScreen.name) {
                MyTarotScreen(
                    navController
                )
            }
            composable(ScreenEnum.InputScreen.name) {
                InputScreen(
                    navController
                )
            }
            composable(ScreenEnum.PickTarotScreen.name) {
                PickTarotScreen(
                    navController
                    )
            }
            composable(ScreenEnum.ResultScreen.name) {
                TarotResultScreen(
                    navController
                    )
            }
            composable(ScreenEnum.OnBoardingScreen.name) {
                PagerOnBoarding(navController)
            }
            composable(ScreenEnum.LoadingScreen.name) {
                LoadingScreen(
                    navController
                    )
            }
            composable(ScreenEnum.MyTarotDetailScreen.name) {
                MyTarotDetailScreen(
                    navController
                    )
            }
            composable(ScreenEnum.ShareDetailScreen.name) {
                ShareDetailScreen(
                    navController
                )
            }
            composable(ScreenEnum.ShareHarmonyDetailScreen.name) {
                ShareHarmonyDetailScreen(
                    navController
                )
            }
            navigation(
                startDestination = ScreenEnum.RoomCreateScreen.name,
                route = NavGraphRoute.HARMONY
            ) {
                composable(ScreenEnum.RoomCreateScreen.name) { RoomCreateScreen(navController) }
                composable(ScreenEnum.RoomGenderScreen.name) { RoomGenderScreen(navController) }
                composable(ScreenEnum.RoomNicknameScreen.name) { RoomNicknameScreen(navController) }
                composable(ScreenEnum.RoomCreateLoadingScreen.name) { RoomCreateLoadingScreen(navController) }
                composable(ScreenEnum.RoomShareScreen.name) { RoomShareScreen(navController) }
                composable(ScreenEnum.RoomInviteLoadingScreen.name) { RoomInviteLoadingScreen(navController) }
                composable(ScreenEnum.RoomEnteringScreen.name) { RoomEnteringScreen(navController) }
                composable(ScreenEnum.RoomChatScreen.name) { RoomChatScreen(navController) }
                composable(ScreenEnum.RoomResultScreen.name) { HarmonyResultScreen(navController) }
            }
            composable(ScreenEnum.MyTarotHarmonyDetailScreen.name) {
                MyTarotHarmonyDetail(
                    navController
                )
            }
        }

    }


}
