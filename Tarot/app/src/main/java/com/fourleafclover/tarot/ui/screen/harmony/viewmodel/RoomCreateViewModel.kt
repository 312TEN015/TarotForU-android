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

@HiltViewModel
class RoomCreateViewModel @Inject constructor(): ViewModel() {

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

    fun openRoomDeletedDialog() {
        _openRoomDeletedDialog.value = true
    }

    fun openRoomExistDialog() {
        _openRoomExistDialog.value = true
    }

    fun closeRoomDeletedDialog() {
        _openRoomDeletedDialog.value = false
    }

    fun closeRoomExistDialog() {
        _openRoomExistDialog.value = false
    }


    fun checkRoomExist(navController: NavHostController, harmonyViewModel: HarmonyViewModel) {

        val roomId = MyApplication.prefs.getHarmonyRoomId()
        val createdAtString = MyApplication.prefs.getHarmonyRoomCreatedAt()

        if (roomId.isNotEmpty() && createdAtString.isNotEmpty()) {
            // 방 생성 후 1시간 경과 체크
            checkRoomCreatedAt(harmonyViewModel)

            // 이미 생성하신 초대방이 있어요
            openRoomExistDialog()

        } else {
            // 새로 초대방 만들기
            navigateInclusive(navController, ScreenEnum.RoomGenderScreen.name)
        }
    }

    fun checkRoomCreatedAt(harmonyViewModel: HarmonyViewModel) {
        val createdAt = Date(MyApplication.prefs.getHarmonyRoomCreatedAt())
        val mNow = System.currentTimeMillis()
        val diffMilliseconds = Date(mNow).time - createdAt.time
        val diffHours = diffMilliseconds / (60 * 60 * 1000)
        if (diffHours >= 1) {
            // 1시간 지나서 사라짐
            _isRoomExpired.value = true
            harmonyViewModel.deleteRoom()
            MyApplication.closeSocket()
        }
    }

}