package com.fourleafclover.tarot.ui.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.ViewModel
import androidx.navigation.NavHostController

object NavGraphRoute {
    const val ROOT = "root_graph"
    const val HARMONY = "harmony_graph"
}

/**
 * 일반 forward 네비게이션. 현재 백 스택 위에 [screenName]을 push.
 * 같은 화면이 연달아 push되는 것만 막기 위해 launchSingleTop 사용.
 */
fun navigateSaveState(navController: NavHostController, screenName: String){
    navController.navigate(screenName) {
        launchSingleTop = true
    }
}


/**
 * [screenName]을 새로 push하면서, 백 스택에 이미 존재하던 동일 라우트와 그 위 항목을 모두 제거.
 * 결과적으로 [screenName]은 스택 최상단의 단일 인스턴스로 남는다.
 *
 * 주로 startDestination 화면(예: HomeScreen)으로 돌아가며 백 스택을 비우는 용도.
 * [screenName]이 스택에 없는 forward 전이에는 [navigateReplacing]을 쓸 것.
 */
fun navigateInclusive(navController: NavHostController, screenName: String){
    navController.navigate(screenName) {
        popUpTo(screenName) {  inclusive = true }
    }
}


/**
 * [screenName]으로 전진하면서 호출 시점의 현재 화면을 백 스택에서 제거.
 * 일회성 화면(예: LoadingScreen) 다음 화면으로 넘어갈 때 사용 — 뒤로가기로
 * 일회성 화면에 다시 진입하지 않도록 한다.
 */
fun navigateReplacing(navController: NavHostController, screenName: String){
    val currentRoute = navController.currentBackStackEntry?.destination?.route
    navController.navigate(screenName) {
        if (currentRoute != null) {
            popUpTo(currentRoute) { inclusive = true }
        }
    }
}

@Composable
inline fun <reified VM : ViewModel> navGraphViewModel(
    navController: NavHostController,
    route: String = NavGraphRoute.ROOT
): VM {
    // 화면이 처음 그려질 때 한 번만 조회한다. 현재 백 스택 항목이 바뀔 때마다 다시 조회하면,
    // 그래프를 빠져나가는 전환 애니메이션 중에 이미 pop된 그래프를 찾다가 크래시가 난다.
    val parentEntry = remember(navController, route) {
        navController.getBackStackEntry(route)
    }

    return hiltViewModel(parentEntry)
}