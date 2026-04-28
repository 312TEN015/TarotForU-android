package com.fourleafclover.tarot.ui.screen.harmony.viewmodel

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.fourleafclover.tarot.MyApplication
import com.fourleafclover.tarot.data.repository.RoomEvent
import com.fourleafclover.tarot.data.repository.TarotRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.filterIsInstance
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.launch
import java.util.Date
import javax.inject.Inject

@HiltViewModel
class HarmonyViewModel @Inject constructor(
    private val repository: TarotRepository
) : ViewModel() {

    private var userNickname = mutableStateOf("")
    private var partnerNickname = mutableStateOf("")

    private var _roomId = mutableStateOf("")
    val roomId get() = _roomId

    private var _createdRoomId = mutableStateOf("")
    val createdRoomId get() = _createdRoomId

    private var _invitedRoomId = mutableStateOf("")
    val invitedRoomId get() = _invitedRoomId

    private var _isRoomOwner = mutableStateOf(false)
    val isRoomOwner get() = _isRoomOwner

    var dynamicLink = ""
    var shortLink = ""

    fun clear() {
        userNickname.value = ""
        partnerNickname.value = ""
        _roomId.value = ""
        _createdRoomId.value = ""
        _invitedRoomId.value = ""
        _isRoomOwner.value = false
        dynamicLink = ""
        shortLink = ""
    }

    fun setIsRoomOwner(isRoomOwner: Boolean) {
        _isRoomOwner.value = isRoomOwner
    }

    fun getOwnerNickname(): String =
        if (isRoomOwner.value) userNickname.value else partnerNickname.value

    fun getInviteeNickname(): String =
        if (!isRoomOwner.value) userNickname.value else partnerNickname.value

    fun setUserNickname(nickname: String) { userNickname.value = nickname }
    fun getUserNickname() = userNickname.value
    fun setPartnerNickname(nickname: String) { partnerNickname.value = nickname }
    fun getPartnerNickname() = partnerNickname.value

    fun enterExistingRoom() {
        _roomId.value = MyApplication.prefs.getHarmonyRoomId()
        _createdRoomId.value = MyApplication.prefs.getHarmonyRoomId()
    }

    fun createNewRoom(newRoomId: String) {
        _roomId.value = newRoomId
        _createdRoomId.value = newRoomId
        _isRoomOwner.value = true

        val mDate = Date(System.currentTimeMillis())
        MyApplication.prefs.saveHarmonyRoomCreatedAt(mDate.toString())
        MyApplication.prefs.saveHarmonyRoomId(newRoomId)
    }

    fun deleteRoom() {
        val currentRoomId = _roomId.value
        _roomId.value = ""

        if (_isRoomOwner.value) {
            _createdRoomId.value = ""
            MyApplication.prefs.saveHarmonyRoomId("")
            MyApplication.prefs.saveHarmonyRoomCreatedAt("")
        } else {
            _invitedRoomId.value = ""
        }

        _isRoomOwner.value = false

        if (currentRoomId.isNotEmpty()) {
            viewModelScope.launch { repository.exitRoom(currentRoomId) }
        }
        repository.disconnect()
    }

    fun enterInvitedRoom(invitedRoomId: String) {
        _roomId.value = invitedRoomId
        _invitedRoomId.value = invitedRoomId
    }

    /** Suspends until repository confirms room creation, then updates this VM's state. */
    suspend fun createRoomAndAwait() {
        repository.connect()
        val event = repository.observeRoomEvents()
            .filterIsInstance<RoomEvent.Created>()
            .onStart { repository.createRoom() }
            .first()
        createNewRoom(event.roomId)
    }

    /** Suspends until both clients have joined and the partner nickname is known. */
    suspend fun joinRoomAndAwait(): String {
        repository.connect()
        val event = repository.observeRoomEvents()
            .filterIsInstance<RoomEvent.Joined>()
            .onStart {
                repository.joinRoom(
                    roomId = _roomId.value,
                    nickname = userNickname.value
                )
            }
            .first()
        val partner = event.nicknames.find { it != userNickname.value } ?: " "
        setPartnerNickname(partner)
        return partner
    }
}
