package com.fourleafclover.tarot

import android.app.Application
import com.fourleafclover.tarot.utils.PreferenceUtil
import com.fourleafclover.tarot.utils.ToastUtil
import com.google.firebase.Firebase
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.firestore
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class MyApplication : Application() {

    companion object {
        lateinit var prefs: PreferenceUtil
        lateinit var toastUtil: ToastUtil
        lateinit var firestore: FirebaseFirestore
    }

    override fun onCreate() {
        super.onCreate()
        toastUtil = ToastUtil(applicationContext)
        prefs = PreferenceUtil(applicationContext)
        firestore = Firebase.firestore
    }
}
