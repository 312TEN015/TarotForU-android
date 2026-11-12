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
import com.fourleafclover.tarot.data.repository.DemoMode
import com.fourleafclover.tarot.demo.ui.theme.TarotTheme
import com.fourleafclover.tarot.demo.viewmodel.DemoViewModel
import com.fourleafclover.tarot.network.TarotService
import com.fourleafclover.tarot.ui.navigation.NavigationHost
import com.fourleafclover.tarot.ui.navigation.ScreenEnum
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.suspendCancellableCoroutine
import kotlinx.coroutines.withContext
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import javax.inject.Inject
import kotlin.coroutines.resume

var LocalIsDemo = compositionLocalOf { false }


@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    private lateinit var splashScreen: SplashScreen
    private val splashViewModel: SplashViewModel by viewModels()

    @Inject lateinit var demoMode: DemoMode
    @Inject lateinit var tarotService: TarotService

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
            val config = fetchDemoConfig()
            val isDemo = config?.isDemo ?: false

            // IMPORTANT: set demo flag and results before any ViewModel injection runs.
            demoMode.isDemo = isDemo
            config?.demoResults?.single?.let { demoMode.demoSingleResult = it }
            config?.demoResults?.harmony?.let { demoMode.demoHarmonyResult = it }

            val dialogData = if (isDemo) config?.demoPopup?.let {
                DemoViewModel.DemoDialogData(
                    visibility = it.visibility,
                    title = it.title,
                    content = it.content
                )
            } else null

            withContext(Dispatchers.Main) {
                setContent(isDemo, dialogData)
            }
        }

    private suspend fun fetchDemoConfig() = suspendCancellableCoroutine { cont ->
        val call = tarotService.getDemoConfig()
        cont.invokeOnCancellation { call.cancel() }
        call.enqueue(object : Callback<com.fourleafclover.tarot.data.DemoConfigDto> {
            override fun onResponse(
                call: Call<com.fourleafclover.tarot.data.DemoConfigDto>,
                response: Response<com.fourleafclover.tarot.data.DemoConfigDto>
            ) {
                cont.resume(response.body())
            }
            override fun onFailure(call: Call<com.fourleafclover.tarot.data.DemoConfigDto>, t: Throwable) {
                Log.w("MainActivity", "demo config fetch failed: ${t.message}")
                cont.resume(null)
            }
        })
    }
}
