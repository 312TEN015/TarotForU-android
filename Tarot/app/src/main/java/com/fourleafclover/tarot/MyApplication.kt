package com.fourleafclover.tarot

import android.app.Application
import android.util.Log
import com.fourleafclover.tarot.network.PrettyJsonLogger
import com.fourleafclover.tarot.network.TarotService
import com.fourleafclover.tarot.utils.LogTags
import com.fourleafclover.tarot.utils.PreferenceUtil
import com.fourleafclover.tarot.utils.ToastUtil
import com.google.firebase.Firebase
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Source
import com.google.firebase.firestore.firestore
import dagger.hilt.android.HiltAndroidApp
import io.socket.client.IO
import io.socket.client.Socket
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.Arrays
import java.util.concurrent.TimeUnit

@HiltAndroidApp
class MyApplication: Application() {




    companion object {
        lateinit var prefs: PreferenceUtil
        lateinit var tarotService: TarotService
        lateinit var socket: Socket
        lateinit var toastUtil: ToastUtil
        lateinit var firestore: FirebaseFirestore

        fun closeSocket() {
            if (socket != null) socket.close()
        }

        fun connectSocket(){
            try {
                val options = IO.Options().apply {
                    reconnection = false // 자동 재연결
                    reconnectionAttempts = 5 // 재연결 시도 횟수
                    reconnectionDelay = 2000 // 재연결 지연 시간
                    forceNew = false
                    transports = arrayOf("polling", "websocket")
                }

                socket = IO.socket(BuildConfig.BASE_URL, options)

                // 이벤트 리스너 설정
                socket.on(Socket.EVENT_CONNECT) { args ->
                    Log.d("SocketIO", "Current transport: connect")
                }
                socket.on(Socket.EVENT_DISCONNECT) { args ->
                    Log.d("SocketIO", "Current transport: connect disconnect")
                }
                socket.on(Socket.EVENT_CONNECT_ERROR) { args ->
                    Log.d("SocketIO", "Current transport: connect error ${Arrays.toString(args)}")
                }

            } catch (e: Exception) {
                e.printStackTrace()
            }

            if (!socket.connected()) socket.connect()
        }
    }


    override fun onCreate() {
        super.onCreate()
        toastUtil = ToastUtil(applicationContext)

        prefs = PreferenceUtil(applicationContext)
//        prefs.deleteAllTarotResults()
//        prefs.addTarotResult("QLILQXcNipQq87fH_i_mb")
//        prefs.addTarotResult("h4L3cNruvH2LB8grq5fHL")
//        prefs.addTarotResult("5UBpn2BedIwbVAe_gjVDr")
//        prefs.addTarotResult("NziRD6ZNAFOIhxuOcE0FB")
//        prefs.addTarotResult("1PWkCVrc3DZBuD8nkAcM7")
//        prefs.addTarotResult("303R-CSMhBq6VZc9ICH_3")
//        prefs.addTarotResult("jwWT2Fk6B4A705eZTo73T")
//        prefs.addTarotResult("xY_dTJvxSvjbJ_KN2-sTh")

        // 궁합
//        prefs.addTarotResult("ktNAAiCk3DVX7G-_hh5_C")
//        prefs.addTarotResult("A4Pz2ackc0XQlDGKRqHYx")


//        prefs.deleteIsPickCardIndicateComplete()

        firestore = Firebase.firestore.apply {
            this.collection("properties").document("W0rGvGYUQCFL3N82bVg1")
            .get()
            .addOnSuccessListener { document ->
                if (document != null) {
                    Log.d(LogTags.firestore, "DocumentSnapshot data: ${document.data}")
                    isDemo = document.data?.get("isDemo").toString().toBoolean()
                    if (!isDemo) initServerSettings()
                } else {
                    Log.d(LogTags.firestore, "No such document")
                }
            }
            .addOnFailureListener { exception ->
                Log.w(LogTags.firestore, "Error getting documents.", exception)
            }
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
