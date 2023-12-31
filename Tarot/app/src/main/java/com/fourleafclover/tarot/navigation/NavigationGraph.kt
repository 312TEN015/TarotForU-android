package com.fourleafclover.tarot.navigation

import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.fourleafclover.tarot.BottomNavigationBar
import com.fourleafclover.tarot.screen.HomeScreen
import com.fourleafclover.tarot.screen.InputScreen
import com.fourleafclover.tarot.screen.LoadingScreen
import com.fourleafclover.tarot.screen.MyTarotDetailScreen
import com.fourleafclover.tarot.screen.MyTarotScreen
import com.fourleafclover.tarot.screen.PagerOnBoarding
import com.fourleafclover.tarot.screen.PickTarotScreen
import com.fourleafclover.tarot.screen.ResultScreen

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
                PagerOnBoarding(navController)
            }
            composable(ScreenEnum.LoadingScreen.name) {
                LoadingScreen(navController)
            }
            composable(ScreenEnum.MyTarotDetailScreen.name) {
                MyTarotDetailScreen(navController)
            }
        }

    }


}
