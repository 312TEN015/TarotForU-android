package com.fourleafclover.tarot.demo.viewmodel

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class DemoViewModel @Inject constructor(): ViewModel() {
    private var _isDemo = mutableStateOf(false)
    val isDemo get() = _isDemo.value

    private var _demoDialogData = mutableStateOf(DemoDialogData())
    val demoDialogData get() = _demoDialogData.value

    data class DemoDialogData(
        var visibility: Boolean = false,
        var title: String = "",
        var content: String = ""
    )

    fun setIsDemo(value: Boolean) {
        _isDemo.value = value
    }

    fun setDemoDialog(visibility: Boolean, title: String, content: String) {
        _demoDialogData.value = DemoDialogData(visibility, title, content)
    }

    fun setDemoDialogVisibility(value: Boolean) {
        _demoDialogData.value = DemoDialogData(visibility = value)
    }
}