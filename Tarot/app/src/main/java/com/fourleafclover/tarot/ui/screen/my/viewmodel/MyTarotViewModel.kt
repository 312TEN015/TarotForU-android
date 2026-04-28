package com.fourleafclover.tarot.ui.screen.my.viewmodel

import android.util.Log
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import com.fourleafclover.tarot.data.CardResultData
import com.fourleafclover.tarot.data.TarotOutputDto
import com.fourleafclover.tarot.data.repository.TarotRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class MyTarotViewModel @Inject constructor(
    private val repository: TarotRepository
) : ViewModel() {

    var _selectedTarotResult = mutableStateOf(
        TarotOutputDto("0", 0, arrayListOf(), "", arrayListOf(), null)
    )
    val selectedTarotResult get() = _selectedTarotResult.value

    var _myTarotResults = mutableStateListOf<TarotOutputDto>()
    val myTarotResults get() = _myTarotResults

    private var isRoomOwnerTab = mutableStateOf(true)

    var roomOwnerCardResults: List<CardResultData> =
        arrayListOf(CardResultData(), CardResultData(), CardResultData())
    var roomOwnerCardNumbers: List<Int> = arrayListOf(0, 0, 0)
    var inviteeCardResults: List<CardResultData> =
        arrayListOf(CardResultData(), CardResultData(), CardResultData())
    var inviteeCardNumbers: List<Int> = arrayListOf(0, 0, 0)

    fun clear() { isRoomOwnerTab.value = true }

    fun selectItem(idx: Int) { _selectedTarotResult.value = _myTarotResults[idx] }

    fun deleteSelectedItem() {
        val itemToDelete = _myTarotResults.find { it.tarotId == _selectedTarotResult.value.tarotId }
        _myTarotResults.remove(itemToDelete)
    }

    fun setMyTarotResults(results: List<TarotOutputDto>) {
        _myTarotResults.clear()
        _myTarotResults.addAll(results)
        Log.d("MyTarotVM", _myTarotResults.joinToString(" "))
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

    /** Loads saved tarot list via repository and applies to state. */
    suspend fun loadMyTarotList(tarotIds: List<String>): Boolean {
        if (tarotIds.isEmpty()) {
            setMyTarotResults(emptyList())
            return true
        }
        return repository.getTarotList(tarotIds).fold(
            onSuccess = { list ->
                setMyTarotResults(list)
                true
            },
            onFailure = { e ->
                Log.e("MyTarotVM", "loadMyTarotList failed", e)
                false
            }
        )
    }
}
