package com.fourleafclover.tarot.ui.navigation

import android.content.Intent
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.fourleafclover.tarot.MyApplication
import com.fourleafclover.tarot.ui.component.BottomNavigationBar
import com.fourleafclover.tarot.ui.screen.HomeScreen
import com.fourleafclover.tarot.ui.screen.InputScreen
import com.fourleafclover.tarot.ui.screen.LoadingScreen
import com.fourleafclover.tarot.ui.screen.MyTarotDetailScreen
import com.fourleafclover.tarot.ui.screen.MyTarotScreen
import com.fourleafclover.tarot.ui.screen.PagerOnBoarding
import com.fourleafclover.tarot.ui.screen.PickTarotScreen
import com.fourleafclover.tarot.ui.screen.ResultScreen
import com.fourleafclover.tarot.ui.screen.ShareDetailScreen

@Composable
fun NavigationHost() {
    val navController = rememberNavController()
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route

    Scaffold(bottomBar = {
        if (currentRoute == ScreenEnum.HomeScreen.name || currentRoute == ScreenEnum.MyTarotScreen.name)
            BottomNavigationBar(navController = navController)
    }
    ) { innerPadding -> innerPadding

        NavHost(navController = navController, startDestination = ScreenEnum.OnBoardingScreen.name) {
            composable(ScreenEnum.HomeScreen.name) {
                HomeScreen(navController)
            }
            composable(ScreenEnum.MyTarotScreen.name) {
                MyTarotScreen(navController)
            }
            composable(ScreenEnum.InputScreen.name) {
                InputScreen(navController)
            }
            composable(ScreenEnum.PickTarotScreen.name) {
                PickTarotScreen(navController)
            }
            composable(ScreenEnum.ResultScreen.name) {
                ResultScreen(navController)
            }
            composable(ScreenEnum.OnBoardingScreen.name) {
                if (MyApplication.prefs.isOnBoardingComplete()){
                    navigateInclusive(navController, ScreenEnum.HomeScreen.name)
                }else{
                    PagerOnBoarding(navController)
                }
            }
            composable(ScreenEnum.LoadingScreen.name) {
                LoadingScreen(navController)
            }
            composable(ScreenEnum.MyTarotDetailScreen.name) {
                MyTarotDetailScreen(navController)
            }
            composable(ScreenEnum.ShareDetailScreen.name) {
                ShareDetailScreen(navController)
            }
        }

    }


}
