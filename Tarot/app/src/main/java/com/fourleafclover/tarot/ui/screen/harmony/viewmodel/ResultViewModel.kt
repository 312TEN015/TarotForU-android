package com.fourleafclover.tarot.ui.screen.harmony.viewmodel

import android.util.Log
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import com.fourleafclover.tarot.MyApplication
import com.fourleafclover.tarot.data.CardResultData
import com.fourleafclover.tarot.data.TarotInputDto
import com.fourleafclover.tarot.data.TarotOutputDto
import com.fourleafclover.tarot.data.repository.TarotRepository
import com.fourleafclover.tarot.ui.screen.fortune.viewModel.PickTarotViewModel
import com.fourleafclover.tarot.ui.screen.fortune.viewModel.QuestionInputViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

/**
 * Holds tarot reading results for both single and harmony flows,
 * and exposes suspend operations that delegate to the repository.
 */
@HiltViewModel
class ResultViewModel @Inject constructor(
    private val repository: TarotRepository
) : ViewModel() {

    // ----- single tarot -----

    private var _tarotResult = mutableStateOf(TarotOutputDto())
    val tarotResult get() = _tarotResult

    // ----- harmony -----

    var myCardResults: List<CardResultData> =
        arrayListOf(CardResultData(), CardResultData(), CardResultData())
    var myCardNumbers: List<Int> = arrayListOf(0, 0, 0)
    var partnerCardResults: List<CardResultData> =
        arrayListOf(CardResultData(), CardResultData(), CardResultData())
    var partnerCardNumbers: List<Int> = arrayListOf(0, 0, 0)

    private var isMyTab = mutableStateOf(true)
    var isMatchResultPrepared = mutableStateOf(false)

    // ----- shared dialog state -----

    private var _openCloseDialog = mutableStateOf(false)
    val openCloseDialog get() = _openCloseDialog

    private var _openCompleteDialog = mutableStateOf(false)
    val openCompleteDialog get() = _openCompleteDialog

    private var _saveState = mutableStateOf(false)
    val saveState get() = _saveState

    fun clear() {
        isMyTab.value = true
        isMatchResultPrepared.value = false
        _openCloseDialog.value = false
        _openCompleteDialog.value = false
        _saveState.value = false
    }

    fun setIsMatchResultPrepared(isPrepared: Boolean) {
        isMatchResultPrepared.value = isPrepared
    }

    private fun initResult() {
        _tarotResult.value = TarotOutputDto()

        myCardResults = arrayListOf(CardResultData(), CardResultData(), CardResultData())
        myCardNumbers = arrayListOf(0, 0, 0)
        partnerCardResults = arrayListOf(CardResultData(), CardResultData(), CardResultData())
        partnerCardNumbers = arrayListOf(0, 0, 0)

        isMyTab.value = true
        _openCloseDialog.value = false
        _openCompleteDialog.value = false
        _saveState.value = false
    }

    fun distinguishCardResult(tarotResult: TarotOutputDto, isRoomOwner: Boolean) {
        initResult()
        setTarotResult(tarotResult)
        if (isRoomOwner) {
            myCardResults = tarotResult.cardResults!!.slice(0..2)
            myCardNumbers = tarotResult.cards.slice(0..2)
            partnerCardResults = tarotResult.cardResults!!.slice(3..5)
            partnerCardNumbers = tarotResult.cards.slice(3..5)
        } else {
            partnerCardResults = tarotResult.cardResults!!.slice(0..2)
            partnerCardNumbers = tarotResult.cards.slice(0..2)
            myCardResults = tarotResult.cardResults!!.slice(3..5)
            myCardNumbers = tarotResult.cards.slice(3..5)
        }
    }

    fun openCloseDialog() { _openCloseDialog.value = true }
    fun closeCloseDialog() { _openCloseDialog.value = false }
    fun openCompleteDialog() { _openCompleteDialog.value = true }
    fun closeCompleteDialog() { _openCompleteDialog.value = false }
    fun saveResult() { _saveState.value = true }

    fun isMyTab(): Boolean = this.isMyTab.value
    fun myTab() { this.isMyTab.value = true }
    fun partnerTab() { this.isMyTab.value = false }

    fun setTarotResult(result: TarotOutputDto) { _tarotResult.value = result }

    /** Save the current tarot result id locally. */
    fun saveCurrentTarotToMyList() {
        MyApplication.prefs.addTarotResult(_tarotResult.value.tarotId)
        saveResult()
        openCompleteDialog()
    }

    // ----- suspend operations exposed to the loading screen -----

    /**
     * Calls the repository to generate a single tarot reading and stores it.
     * Returns true on success, false on failure (UI may navigate home and toast).
     */
    suspend fun fetchTarotResult(
        pickTarotViewModel: PickTarotViewModel,
        questionInputViewModel: QuestionInputViewModel,
        topicNumber: Int
    ): Boolean {
        val input = TarotInputDto(
            firstAnswer = questionInputViewModel.answer1.value.text,
            secondAnswer = questionInputViewModel.answer2.value.text,
            thirdAnswer = questionInputViewModel.answer3.value.text,
            cards = arrayListOf(
                pickTarotViewModel.pickedCardNumberState.value.firstCardNumber,
                pickTarotViewModel.pickedCardNumberState.value.secondCardNumber,
                pickTarotViewModel.pickedCardNumberState.value.thirdCardNumber,
            )
        )
        val result = repository.postTarotResult(input, topicNumber)
        return result.fold(
            onSuccess = { tarot ->
                setTarotResult(tarot)
                pickTarotViewModel.initCardDeck()
                questionInputViewModel.initAnswers()
                true
            },
            onFailure = { e ->
                Log.e("ResultVM", "fetchTarotResult failed", e)
                false
            }
        )
    }
}
