package com.fourleafclover.tarot.ui.screen.harmony.viewmodel

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class GenderViewModel @Inject constructor(): ViewModel() {

    private var pickedGender = mutableStateOf(-1)

    fun clear() {
        pickedGender.value = -1
    }

    fun updatePickedGender(newPick: Int){
        pickedGender.value = newPick
    }

    fun getPickedGender(): Int = pickedGender.value

    fun isSelected(thisGender: Int): Boolean {
        return pickedGender.value == thisGender
    }

    fun isCompleted(): Boolean {
        return pickedGender.value != -1
    }
}