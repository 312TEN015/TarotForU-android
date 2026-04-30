package com.fourleafclover.tarot.ui.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.ViewModel
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState

object NavGraphRoute {
    const val ROOT = "root_graph"
    const val HARMONY = "harmony_graph"
}

// 백 스택에 저장
fun navigateSaveState(navController: NavHostController, screenName: String){
    navController.navigate(screenName) {
        popUpTo(screenName) {  inclusive = false }
    }
}


// 백 스택에 남기지 않음
fun navigateInclusive(navController: NavHostController, screenName: String){
    navController.navigate(screenName) {
        popUpTo(screenName) {  inclusive = true }
    }
}

@Composable
inline fun <reified VM : ViewModel> navGraphViewModel(
    navController: NavHostController,
    route: String = NavGraphRoute.ROOT
): VM {
    val navBackStackEntry by navController.currentBackStackEntryAsState()

    val parentEntry = remember(navBackStackEntry) {
        navController.getBackStackEntry(route)
    }

    return hiltViewModel(parentEntry)
}