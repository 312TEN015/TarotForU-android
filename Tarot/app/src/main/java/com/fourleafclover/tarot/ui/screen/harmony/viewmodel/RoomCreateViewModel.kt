package com.fourleafclover.tarot.ui.screen.harmony.viewmodel

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.navigation.NavHostController
import com.fourleafclover.tarot.MyApplication
import com.fourleafclover.tarot.ui.navigation.ScreenEnum
import com.fourleafclover.tarot.ui.navigation.navigateInclusive
import dagger.hilt.android.lifecycle.HiltViewModel
import java.util.Date
import javax.inject.Inject

/** Manages dialog state for the room-create screen. Room creation itself lives in [HarmonyViewModel]. */
@HiltViewModel
class RoomCreateViewModel @Inject constructor() : ViewModel() {

    private var _openRoomDeletedDialog = mutableStateOf(false)
    val openRoomDeletedDialog = _openRoomDeletedDialog

    private var _openRoomExistDialog = mutableStateOf(false)
    val openRoomExistDialog = _openRoomExistDialog

    private var _isRoomExpired = mutableStateOf(false)
    val isRoomExpired = _isRoomExpired

    fun clear() {
        _openRoomDeletedDialog.value = false
        _openRoomExistDialog.value = false
        _isRoomExpired.value = false
    }

    fun openRoomDeletedDialog() { _openRoomDeletedDialog.value = true }
    fun openRoomExistDialog() { _openRoomExistDialog.value = true }
    fun closeRoomDeletedDialog() { _openRoomDeletedDialog.value = false }
    fun closeRoomExistDialog() { _openRoomExistDialog.value = false }

    fun checkRoomExist(navController: NavHostController, harmonyViewModel: HarmonyViewModel) {
        val roomId = MyApplication.prefs.getHarmonyRoomId()
        val createdAtString = MyApplication.prefs.getHarmonyRoomCreatedAt()

        if (roomId.isNotEmpty() && createdAtString.isNotEmpty()) {
            checkRoomCreatedAt(harmonyViewModel)
            openRoomExistDialog()
        } else {
            navigateInclusive(navController, ScreenEnum.RoomGenderScreen.name)
        }
    }

    private fun checkRoomCreatedAt(harmonyViewModel: HarmonyViewModel) {
        val createdAt = Date(MyApplication.prefs.getHarmonyRoomCreatedAt())
        val diffMilliseconds = System.currentTimeMillis() - createdAt.time
        val diffHours = diffMilliseconds / (60 * 60 * 1000)
        if (diffHours >= 1) {
            _isRoomExpired.value = true
            harmonyViewModel.deleteRoom()
        }
    }
}
