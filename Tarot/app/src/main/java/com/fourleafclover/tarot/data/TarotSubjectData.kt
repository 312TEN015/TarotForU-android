package com.fourleafclover.tarot.data

import androidx.compose.ui.graphics.Color
import com.fourleafclover.tarot.demo.ui.theme.color.gray_9

/* 선택한 대주제 정보 */
data class TarotSubjectData(
        val majorTopic: String = "",
        val majorQuestion: String = "",
        val subQuestions : ArrayList<String> = arrayListOf(),
        val placeHolders : ArrayList<String> = arrayListOf(),
        val primaryColor: Color = gray_9
)
