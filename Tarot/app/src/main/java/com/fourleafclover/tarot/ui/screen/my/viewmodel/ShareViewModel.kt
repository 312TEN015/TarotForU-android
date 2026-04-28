package com.fourleafclover.tarot.ui.screen.my.viewmodel

import android.util.Log
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import com.fourleafclover.tarot.data.CardResultData
import com.fourleafclover.tarot.data.TarotOutputDto
import com.fourleafclover.tarot.data.repository.TarotRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class ShareViewModel @Inject constructor(
    private val repository: TarotRepository
) : ViewModel() {

    private var _sharedTarotResult = mutableStateOf(
        TarotOutputDto("0", 0, arrayListOf(), "", arrayListOf(), null)
    )
    val sharedTarotResult get() = _sharedTarotResult.value

    private var isRoomOwnerTab = mutableStateOf(true)

    var roomOwnerCardResults: List<CardResultData> =
        arrayListOf(CardResultData(), CardResultData(), CardResultData())
    var roomOwnerCardNumbers: List<Int> = arrayListOf(0, 0, 0)
    var inviteeCardResults: List<CardResultData> =
        arrayListOf(CardResultData(), CardResultData(), CardResultData())
    var inviteeCardNumbers: List<Int> = arrayListOf(0, 0, 0)

    fun clear() { isRoomOwnerTab.value = true }

    fun setSharedTarotResult(sharedTarotResult: TarotOutputDto) {
        this._sharedTarotResult.value = sharedTarotResult
    }

    fun distinguishCardResult(tarotResult: TarotOutputDto) {
        roomOwnerCardResults = tarotResult.cardResults!!.slice(0..2)
        roomOwnerCardNumbers = tarotResult.cards.slice(0..2)
        inviteeCardResults = tarotResult.cardResults!!.slice(3..5)
        inviteeCardNumbers = tarotResult.cards.slice(3..5)
    }

    fun isRoomOwnerTab(): Boolean = this.isRoomOwnerTab.value
    fun roomOwnerTab() { this.isRoomOwnerTab.value = true }
    fun roomInviteeTab() { this.isRoomOwnerTab.value = false }

    /** Returns the loaded result on success so caller can decide navigation. */
    suspend fun loadSharedTarot(tarotId: String): TarotOutputDto? =
        repository.getTarotById(tarotId).fold(
            onSuccess = { tarot ->
                setSharedTarotResult(tarot)
                if (tarot.tarotType == 5) distinguishCardResult(tarot)
                tarot
            },
            onFailure = { e ->
                Log.e("ShareVM", "loadSharedTarot failed", e)
                null
            }
        )
}
