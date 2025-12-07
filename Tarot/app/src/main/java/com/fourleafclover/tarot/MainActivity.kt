package com.fourleafclover.tarot

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import androidx.core.splashscreen.SplashScreen
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import com.fourleafclover.tarot.MyApplication.Companion.firestore
import com.fourleafclover.tarot.MyApplication.Companion.tarotService
import com.fourleafclover.tarot.demo.viewmodel.DemoViewModel
import com.fourleafclover.tarot.network.PrettyJsonLogger
import com.fourleafclover.tarot.network.TarotService
import com.fourleafclover.tarot.ui.navigation.NavigationHost
import com.fourleafclover.tarot.ui.screen.main.DialogViewModel
import com.fourleafclover.tarot.ui.theme.TarotTheme
import com.fourleafclover.tarot.utils.LogTags
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    private lateinit var splashScreen: SplashScreen
    private val splashViewModel: SplashViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        splashScreen = installSplashScreen()
        super.onCreate(savedInstanceState)
        splashScreen.setKeepOnScreenCondition{
            splashViewModel.isLoading.value
        }

        // ATTENTION: This was auto-generated to handle app links.
        val appLinkIntent: Intent = intent
        val appLinkAction: String? = appLinkIntent.action
        val appLinkData: Uri? = appLinkIntent.data

        if (appLinkData != null){
            Log.d("", appLinkAction.toString())
            Log.d("", appLinkData.toString())
            Log.d("", appLinkData.getQueryParameter("resultId").toString())
        }

        setServer()

        setContent {
            TarotTheme {
                NavigationHost()
            }
        }
    }

    @Preview
    @Composable
    fun HomePreview(){
        TarotTheme {
            NavigationHost()
        }
    }

    private fun setServer() = CoroutineScope(Dispatchers.IO).launch {
        val demoViewModel by viewModels<DemoViewModel>()
        val isDemo = checkIsDemo(demoViewModel)
        if (isDemo) {
            checkDemoDialogData(demoViewModel)
        } else {
            initServerSettings()
        }
    }

    private suspend fun checkIsDemo(demoViewModel: DemoViewModel) = suspendCoroutine<Boolean> {
        firestore
            .collection("properties")
            .document("is_demo")
            .get()
            .addOnSuccessListener { document ->
                if (document != null) {
                    Log.d(LogTags.firestore, "DocumentSnapshot data: ${document.data}")
                    demoViewModel.setIsDemo(document.data?.get("isDemo").toString().toBoolean())
                    it.resume(true)
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

    private suspend fun checkDemoDialogData(demoViewModel: DemoViewModel) = suspendCoroutine<Boolean> {
        firestore
            .collection("properties")
            .document("demo_popup")
            .get()
            .addOnSuccessListener { document ->
                if (document != null) {
                    Log.d(LogTags.firestore, "DocumentSnapshot data: ${document.data}")
                    demoViewModel.setDemoDialog(
                        visibility = document.data?.get("visibility").toString().toBoolean(),
                        title = document.data?.get("title").toString(),
                        content = document.data?.get("content").toString()
                    )
                    it.resume(true)
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

    private fun initServerSettings() {
        val logging = HttpLoggingInterceptor(PrettyJsonLogger()).apply {
            // 요청과 응답의 본문 내용까지 로그에 포함
            level = HttpLoggingInterceptor.Level.BODY
        }

        // 서버 초기화
        val okHttpClient = OkHttpClient().newBuilder()
            .connectTimeout(30, TimeUnit.SECONDS)
            .readTimeout(30, TimeUnit.SECONDS)
            .writeTimeout(30, TimeUnit.SECONDS)
            .addInterceptor(logging)
            .build()

        val retrofit = Retrofit.Builder()
            .baseUrl(BuildConfig.BASE_URL)
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        tarotService = retrofit.create(TarotService::class.java)
    }


}



