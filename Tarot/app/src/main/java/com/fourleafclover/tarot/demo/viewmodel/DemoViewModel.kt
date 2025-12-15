package com.fourleafclover.tarot.demo.viewmodel

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class DemoViewModel @Inject constructor(): ViewModel() {

    private var _demoDialogData = mutableStateOf(DemoDialogData())
    val demoDialogData get() = _demoDialogData.value

    data class DemoDialogData(
        var visibility: Boolean = false,
        var title: String = "",
        var content: String = ""
    )

    fun setDemoDialog(demoDialogData: DemoDialogData) {
        _demoDialogData.value = demoDialogData
    }

    fun setDemoDialogVisibility(value: Boolean) {
        _demoDialogData.value = DemoDialogData(visibility = value)
    }
}