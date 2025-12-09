package com.fourleafclover.tarot.ui.navigation

import android.util.Log
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.lifecycle.viewmodel.compose.LocalViewModelStoreOwner
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.fourleafclover.tarot.MainActivity
import com.fourleafclover.tarot.MyApplication
import com.fourleafclover.tarot.demo.viewmodel.DemoViewModel
import com.fourleafclover.tarot.ui.component.BottomNavigationBar
import com.fourleafclover.tarot.ui.screen.fortune.InputScreen
import com.fourleafclover.tarot.ui.screen.fortune.PickTarotScreen
import com.fourleafclover.tarot.ui.screen.fortune.TarotResultScreen
import com.fourleafclover.tarot.ui.screen.fortune.viewModel.FortuneViewModel
import com.fourleafclover.tarot.ui.screen.fortune.viewModel.PickTarotViewModel
import com.fourleafclover.tarot.ui.screen.fortune.viewModel.QuestionInputViewModel
import com.fourleafclover.tarot.ui.screen.harmony.HarmonyResultScreen
import com.fourleafclover.tarot.ui.screen.harmony.RoomChatScreen
import com.fourleafclover.tarot.ui.screen.harmony.RoomCreateScreen
import com.fourleafclover.tarot.ui.screen.harmony.RoomEnteringScreen
import com.fourleafclover.tarot.ui.screen.harmony.RoomGenderScreen
import com.fourleafclover.tarot.ui.screen.harmony.RoomNicknameScreen
import com.fourleafclover.tarot.ui.screen.harmony.RoomShareScreen
import com.fourleafclover.tarot.ui.screen.harmony.viewmodel.ChatViewModel
import com.fourleafclover.tarot.ui.screen.harmony.viewmodel.GenderViewModel
import com.fourleafclover.tarot.ui.screen.harmony.viewmodel.HarmonyViewModel
import com.fourleafclover.tarot.ui.screen.harmony.viewmodel.LoadingViewModel
import com.fourleafclover.tarot.ui.screen.harmony.viewmodel.NicknameViewModel
import com.fourleafclover.tarot.ui.screen.harmony.viewmodel.ResultViewModel
import com.fourleafclover.tarot.ui.screen.harmony.viewmodel.RoomCreateViewModel
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
import com.fourleafclover.tarot.utils.LogTags
import com.fourleafclover.tarot.utils.receiveShareRequest

@Composable
fun NavigationHost() {
    val navController = rememberNavController()
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route

    val activity = LocalViewModelStoreOwner.current as MainActivity
    val fortuneViewModel: FortuneViewModel = viewModel(activity)
    val questionInputViewModel: QuestionInputViewModel = viewModel(activity)
    val pickTarotViewModel: PickTarotViewModel = viewModel(activity)
    val resultViewModel: ResultViewModel = viewModel(activity)

    val dialogViewModel: DialogViewModel = viewModel(activity)
    val shareViewModel: ShareViewModel = viewModel(activity)
    val loadingViewModel: LoadingViewModel = viewModel(activity)

    val harmonyViewModel: HarmonyViewModel = viewModel(activity)
    val roomCreateViewModel: RoomCreateViewModel = viewModel(activity)
    val genderViewModel: GenderViewModel = viewModel(activity)
    val nicknameViewModel: NicknameViewModel = viewModel(activity)
    val chatViewModel: ChatViewModel = viewModel(activity)

    val myTarotViewModel: MyTarotViewModel = viewModel(activity)
    val demoViewModel: DemoViewModel = viewModel(activity)

    Scaffold(bottomBar = {
        if (currentRoute == ScreenEnum.HomeScreen.name || currentRoute == ScreenEnum.MyTarotScreen.name)
            BottomNavigationBar(navController = navController, myTarotViewModel)
    }
    ) { innerPadding -> innerPadding

        NavHost(navController = navController, startDestination = ScreenEnum.OnBoardingScreen.name) {
            composable(ScreenEnum.HomeScreen.name) {

                LaunchedEffect(Unit) {
                    questionInputViewModel.clear()
                    pickTarotViewModel.clear()
                    resultViewModel.clear()
                    dialogViewModel.clear()
                    shareViewModel.clear()
                    loadingViewModel.clear()
                    harmonyViewModel.clear()
                    roomCreateViewModel.clear()
                    genderViewModel.clear()
                    nicknameViewModel.clear()
                    chatViewModel.clear()
                    myTarotViewModel.clear()

                    // 공유하기 확인
                    if (activity.intent != null) {
                        receiveShareRequest(activity, navController, shareViewModel, loadingViewModel, harmonyViewModel)
                    }
                }

                HomeScreen(
                    activity,
                    navController,
                    harmonyViewModel,
                    shareViewModel,
                    dialogViewModel,
                    loadingViewModel,
                    fortuneViewModel,
                    demoViewModel
                    )
            }
            composable(ScreenEnum.MyTarotScreen.name) {
                MyTarotScreen(
                    navController,
                    myTarotViewModel,
                    fortuneViewModel
                )
            }
            composable(ScreenEnum.InputScreen.name) {
                InputScreen(
                    navController,
                    fortuneViewModel,
                    questionInputViewModel,
                    dialogViewModel
                )
            }
            composable(ScreenEnum.PickTarotScreen.name) {
                PickTarotScreen(
                    navController,
                    loadingViewModel,
                    fortuneViewModel,
                    pickTarotViewModel,
                    dialogViewModel
                    )
            }
            composable(ScreenEnum.ResultScreen.name) {
                TarotResultScreen(
                    navController,
                    fortuneViewModel,
                    resultViewModel,
                    harmonyViewModel
                    )
            }
            composable(ScreenEnum.OnBoardingScreen.name) {
                if (MyApplication.prefs.isOnBoardingComplete()){
                    navigateInclusive(navController, ScreenEnum.HomeScreen.name)
                }else{
                    PagerOnBoarding(navController)
                }
            }
            composable(ScreenEnum.LoadingScreen.name) {
                LoadingScreen(
                    navController,
                    loadingViewModel,
                    resultViewModel,
                    fortuneViewModel,
                    pickTarotViewModel,
                    questionInputViewModel,
                    demoViewModel
                    )
            }
            composable(ScreenEnum.MyTarotDetailScreen.name) {
                MyTarotDetailScreen(
                    navController,
                    fortuneViewModel,
                    myTarotViewModel,
                    harmonyViewModel
                    )
            }
            composable(ScreenEnum.ShareDetailScreen.name) {
                ShareDetailScreen(
                    navController,
                    fortuneViewModel,
                    shareViewModel
                )
            }
            composable(ScreenEnum.ShareHarmonyDetailScreen.name) {
                ShareHarmonyDetailScreen(
                    navController,
                    fortuneViewModel,
                    shareViewModel
                )
            }
            composable(ScreenEnum.RoomCreateScreen.name) {
                RoomCreateScreen(
                    navController,
                    roomCreateViewModel,
                    harmonyViewModel
                )
            }
            composable(ScreenEnum.RoomGenderScreen.name) {
                RoomGenderScreen(
                    navController,
                    genderViewModel,
                    harmonyViewModel,
                    dialogViewModel
                    )
            }
            composable(ScreenEnum.RoomNicknameScreen.name) {
                RoomNicknameScreen(
                    navController,
                    nicknameViewModel,
                    harmonyViewModel,
                    loadingViewModel,
                    dialogViewModel
                )
            }
            composable(ScreenEnum.RoomCreateLoadingScreen.name) {
                RoomCreateLoadingScreen(
                    navController,
                    harmonyViewModel,
                    loadingViewModel
                    )
            }
            composable(ScreenEnum.RoomShareScreen.name) {
                RoomShareScreen(
                    navController,
                    loadingViewModel,
                    harmonyViewModel,
                    dialogViewModel
                )
            }
            composable(ScreenEnum.RoomInviteLoadingScreen.name) {
                RoomInviteLoadingScreen(
                    navController,
                    harmonyViewModel,
                    loadingViewModel,
                    chatViewModel,
                    dialogViewModel
                    )
            }
            composable(ScreenEnum.RoomEnteringScreen.name) {
                RoomEnteringScreen(navController)
            }
            composable(ScreenEnum.RoomChatScreen.name) {
                RoomChatScreen(
                    navController,
                    harmonyViewModel,
                    chatViewModel,
                    fortuneViewModel,
                    pickTarotViewModel,
                    resultViewModel,
                    loadingViewModel,
                    dialogViewModel
                    )
            }
            composable(ScreenEnum.RoomResultScreen.name) {
                HarmonyResultScreen(
                    navController,
                    harmonyViewModel,
                    fortuneViewModel,
                    resultViewModel
                )
            }
            composable(ScreenEnum.MyTarotHarmonyDetailScreen.name) {
                MyTarotHarmonyDetail(
                    navController,
                    fortuneViewModel,
                    myTarotViewModel
                )
            }
        }

    }


}
