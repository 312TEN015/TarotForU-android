package com.fourleafclover.tarot

import android.content.ActivityNotFoundException
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
import com.fourleafclover.tarot.data.NoticeConfigDto
import com.fourleafclover.tarot.data.repository.DemoMode
import com.fourleafclover.tarot.demo.ui.theme.TarotTheme
import com.fourleafclover.tarot.demo.viewmodel.DemoViewModel
import com.fourleafclover.tarot.network.TarotService
import com.fourleafclover.tarot.notice.AppNotice
import com.fourleafclover.tarot.notice.BlockingNoticeScreen
import com.fourleafclover.tarot.notice.NoticeFetchResult
import com.fourleafclover.tarot.notice.resolveAppNotice
import com.fourleafclover.tarot.ui.navigation.NavigationHost
import com.fourleafclover.tarot.ui.navigation.ScreenEnum
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import kotlinx.coroutines.suspendCancellableCoroutine
import kotlinx.coroutines.withContext
import kotlinx.coroutines.withTimeoutOrNull
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

        bootstrap { isDemo, dialogData, notice ->
            // 점검 중이거나 업데이트가 필요하면 앱 화면 대신 닫을 수 없는 공지만 띄운다
            if (notice != null) {
                setContent {
                    TarotTheme {
                        BlockingNoticeScreen(notice = notice, onConfirm = { onNoticeConfirmed(notice) })
                    }
                }
                return@bootstrap
            }

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

    private fun onNoticeConfirmed(notice: AppNotice) {
        when (notice) {
            is AppNotice.Maintenance -> finishAffinity()
            is AppNotice.ForceUpdate -> openStore()
        }
    }

    /** Play 스토어의 앱 페이지를 연다. 스토어 앱이 없으면 웹 페이지로 연다. */
    private fun openStore() {
        try {
            startActivity(
                Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=$packageName"))
                    .setPackage("com.android.vending")
            )
        } catch (e: ActivityNotFoundException) {
            runCatching {
                startActivity(
                    Intent(
                        Intent.ACTION_VIEW,
                        Uri.parse("https://play.google.com/store/apps/details?id=$packageName")
                    )
                )
            }
        }
    }

    private fun bootstrap(
        setContent: (isDemo: Boolean, dialogData: DemoViewModel.DemoDialogData?, notice: AppNotice?) -> Unit
    ) =
        CoroutineScope(Dispatchers.IO).launch {
            val noticeResult = async { fetchNoticeConfig() }
            val demoConfig = async { fetchDemoConfig() }

            val notice = resolveAppNotice(noticeResult.await(), BuildConfig.VERSION_CODE)
            if (notice != null) {
                // 앱을 쓸 수 없는 상태라 데모 설정은 기다리지 않는다
                demoConfig.cancel()
                withContext(Dispatchers.Main) {
                    setContent(false, null, notice)
                }
                return@launch
            }

            val config = demoConfig.await()
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
                setContent(isDemo, dialogData, null)
            }
        }

    /**
     * 공지 설정을 받아온다. 서버가 [NOTICE_TIMEOUT_MS] 안에 답하지 않거나 5xx로 답하면
     * 서버를 쓸 수 없는 상태로 본다. (서버 앞단의 프록시는 서버가 죽으면 502를 준다)
     */
    private suspend fun fetchNoticeConfig(): NoticeFetchResult =
        withTimeoutOrNull(NOTICE_TIMEOUT_MS) {
            suspendCancellableCoroutine { cont ->
                val call = tarotService.getNoticeConfig()
                cont.invokeOnCancellation { call.cancel() }
                call.enqueue(object : Callback<NoticeConfigDto> {
                    override fun onResponse(call: Call<NoticeConfigDto>, response: Response<NoticeConfigDto>) {
                        cont.resume(
                            if (response.code() >= 500) NoticeFetchResult.Unreachable
                            else NoticeFetchResult.Reachable(response.body())
                        )
                    }
                    override fun onFailure(call: Call<NoticeConfigDto>, t: Throwable) {
                        Log.w("MainActivity", "notice config fetch failed: ${t.message}")
                        cont.resume(NoticeFetchResult.Unreachable)
                    }
                })
            }
        } ?: NoticeFetchResult.Unreachable

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

// 시작 화면에서 오래 멈추지 않도록 공지 조회는 짧게 기다린다
private const val NOTICE_TIMEOUT_MS = 5_000L
