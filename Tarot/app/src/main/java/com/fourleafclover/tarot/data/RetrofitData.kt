package com.fourleafclover.tarot.data

import com.google.gson.annotations.SerializedName
import java.text.SimpleDateFormat
import java.util.Locale
import java.util.TimeZone


// 타로 고민 입력 후 request
data class TarotInputDto(
    var firstAnswer: String,
    var secondAnswer: String,
    var thirdAnswer: String,
    var cards: ArrayList<Int> = arrayListOf()
)

// 타로 고민 결과 response
data class TarotOutputDto(
    var tarotId: String,
    var tarotType: Int, // 분류
    var cards: ArrayList<Int>,
    @SerializedName("createdAt") var createdAt_: String, // 날짜
    var cardResults: ArrayList<CardResultData>?,    // 카드설명
    var overallResult: OverallResultData?   // 총평
) {
    val createdAt: String
        get() {
            // ex) createdAt_ = 2024-01-14T12:38:23.000Z
            val usFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.US)
            usFormat.timeZone = TimeZone.getTimeZone("UTC")
            val usDateTime = usFormat.parse(createdAt_) ?: return "wrong date format"
            val koreaFormat = SimpleDateFormat("yyyy년 MM월 dd일", Locale.KOREA)
            return koreaFormat.format(usDateTime)
        }
}

// 뽑은 카드 세장에 대한 키워드, 설명
data class CardResultData(
    val keywords: ArrayList<String>,
    val description: String,
)

// 뽑은 타로 결과에 대한 요약문, 전문
data class OverallResultData(
    val summary: String,
    val full: String
)

data class TarotIdsInputDto(
    val tarotIds : ArrayList<String> = arrayListOf<String>()
)

data class TarotIdsOutputDto(
    val tarotIds : ArrayList<TarotOutputDto> = arrayListOf<TarotOutputDto>()
)