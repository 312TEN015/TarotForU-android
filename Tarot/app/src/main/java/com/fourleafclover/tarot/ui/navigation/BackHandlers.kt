package com.fourleafclover.tarot.ui.navigation

import android.app.Activity
import androidx.activity.compose.BackHandler
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableLongStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.NavHostController
import com.fourleafclover.tarot.MyApplication
import com.fourleafclover.tarot.ui.screen.main.DialogViewModel

@Composable
fun FinishOnBackPressed() {
    val context = LocalContext.current
    val backPressedTime = remember { mutableLongStateOf(0L) }

    BackHandler {
        val now = System.currentTimeMillis()
        if (now - backPressedTime.longValue <= 1000L) {
            (context as Activity).finish()
        } else {
            MyApplication.toastUtil.makeShortToast("한 번 더 누르시면 앱이 종료됩니다.")
        }
        backPressedTime.longValue = now
    }
}

@Composable
fun PreventBackPressed(){
    BackHandler { }
}

@Composable
fun NavigateHomeOnBackPressed(navController: NavHostController) {
    BackHandler {
        navigateInclusive(navController, ScreenEnum.HomeScreen.name)
    }
}

@Composable
fun OpenDialogOnBackPressed(dialogViewModel: DialogViewModel) {

    BackHandler {
        if (dialogViewModel.openDialog) dialogViewModel.closeDialog()
        else dialogViewModel.openDialog()
    }
}


