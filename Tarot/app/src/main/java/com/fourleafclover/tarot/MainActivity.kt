package com.fourleafclover.tarot

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.compositionLocalOf
import androidx.compose.ui.tooling.preview.Preview
import androidx.core.splashscreen.SplashScreen
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import com.fourleafclover.tarot.MyApplication.Companion.firestore
import com.fourleafclover.tarot.data.repository.DemoMode
import com.fourleafclover.tarot.demo.ui.theme.TarotTheme
import com.fourleafclover.tarot.demo.viewmodel.DemoViewModel
import com.fourleafclover.tarot.ui.navigation.NavigationHost
import com.fourleafclover.tarot.ui.navigation.ScreenEnum
import com.fourleafclover.tarot.utils.LogTags
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine

var LocalIsDemo = compositionLocalOf { false }


@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    private lateinit var splashScreen: SplashScreen
    private val splashViewModel: SplashViewModel by viewModels()

    @Inject lateinit var demoMode: DemoMode

    override fun onCreate(savedInstanceState: Bundle?) {
        splashScreen = installSplashScreen()
        super.onCreate(savedInstanceState)
        splashScreen.setKeepOnScreenCondition {
            splashViewModel.isLoading.value
        }

        // ATTENTION: This was auto-generated to handle app links.
        val appLinkIntent: Intent = intent
        val appLinkAction: String? = appLinkIntent.action
        val appLinkData: Uri? = appLinkIntent.data

        if (appLinkData != null) {
            Log.d("", appLinkAction.toString())
            Log.d("", appLinkData.toString())
            Log.d("", appLinkData.getQueryParameter("resultId").toString())
        }

        bootstrap { isDemo, dialogData ->
            val startDestination =
                if (MyApplication.prefs.isOnBoardingComplete()) ScreenEnum.HomeScreen.name
                else ScreenEnum.OnBoardingScreen.name

            setContent {
                CompositionLocalProvider(LocalIsDemo provides isDemo) {
                    TarotTheme {
                        NavigationHost(
                            startDestination = startDestination,
                            dialogData = dialogData,
                        )
                    }
                }
            }
        }
    }

    @Preview
    @Composable
    fun HomePreview() {
        TarotTheme {
            NavigationHost()
        }
    }

    private fun bootstrap(setContent: (isDemo: Boolean, dialogData: DemoViewModel.DemoDialogData?) -> Unit) =
        CoroutineScope(Dispatchers.IO).launch {
            val isDemo = checkIsDemo()
            // IMPORTANT: set demo flag before any ViewModel injection runs.
            demoMode.isDemo = isDemo

            val dialogData = if (isDemo) checkDemoDialogData() else null

            withContext(Dispatchers.Main) {
                setContent(isDemo, dialogData)
            }
        }

    private suspend fun checkIsDemo() = suspendCoroutine<Boolean> {
        firestore.collection("properties").document("is_demo").get()
            .addOnSuccessListener { document ->
                if (document != null) {
                    Log.d(LogTags.firestore, "DocumentSnapshot data: ${document.data}")
                    it.resume(document.data?.get("isDemo").toString().toBoolean())
                } else {
                    Log.d(LogTags.firestore, "No such document")
                    it.resume(false)
                }
            }
            .addOnFailureListener { exception ->
                Log.w(LogTags.firestore, "Error getting documents.", exception)
                it.resume(false)
            }
    }

    private suspend fun checkDemoDialogData() = suspendCoroutine<DemoViewModel.DemoDialogData?> {
        firestore.collection("properties").document("demo_popup").get()
            .addOnSuccessListener { document ->
                if (document != null) {
                    Log.d(LogTags.firestore, "DocumentSnapshot data: ${document.data}")
                    val result = DemoViewModel.DemoDialogData(
                        visibility = document.data?.get("visibility").toString().toBoolean(),
                        title = document.data?.get("title").toString(),
                        content = document.data?.get("content").toString()
                    )
                    it.resume(result)
                } else {
                    Log.d(LogTags.firestore, "No such document")
                    it.resume(null)
                }
            }
            .addOnFailureListener { exception ->
                Log.w(LogTags.firestore, "Error getting documents.", exception)
                it.resume(null)
            }
    }
}
