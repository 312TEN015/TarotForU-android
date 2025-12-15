package com.fourleafclover.tarot.ui.screen.fortune.viewModel

import android.content.Context
import android.content.res.Resources
import android.util.Log
import androidx.compose.runtime.Composable
import androidx.compose.runtime.ProvidableCompositionLocal
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.fourleafclover.tarot.R
import com.fourleafclover.tarot.SubjectDream
import com.fourleafclover.tarot.SubjectHarmony
import com.fourleafclover.tarot.SubjectJob
import com.fourleafclover.tarot.SubjectLove
import com.fourleafclover.tarot.SubjectStudy
import com.fourleafclover.tarot.SubjectToday
import com.fourleafclover.tarot.data.CardResultData
import com.fourleafclover.tarot.data.OverallResultData
import com.fourleafclover.tarot.data.PickedTopicState
import com.fourleafclover.tarot.data.TarotOutputDto
import com.fourleafclover.tarot.data.TarotSubjectData
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

val dummyFullResult = "당신에게는 어려운 상황에서도 포기하지 않고 끝까지 이겨내는 힘이 있습니다. 포기하지 않고 매달 100만원씩 적금을 들고 있는 것이 그 예시겠네요. 하지만, 때로는 지나친 의욕 때문에 무리한 목표를 세우다가 실패로 이어지는 경우도 있지요. \n" +
        " 따라서, 지금까지의 생활 패턴을 유지하면서 안정감을 추구하려는 마음가짐이 중요합니다. 그리고, 다른 사람들을 위해 헌신하거나 배려하는 자세 역시 꼭 필요해요. 자신의 목표만 바라보기보다, 다른 사람에게 양보하는 마음가짐을 가지고 인생의 균형을 지켜가는 것이 중요합니다. \n" +
        " 이러한 부분들이 잘 지켜진다면, 조만간 경제적으로 여유로워질 기회가 찾아올 것입니다. 30살이 되기 전에 1억을 모으는 것도 가능할지도 몰라요!"

val dummySummary = "당신에게는 어려운 상황에서도 포기하지 않고 끝까지 이겨내는 힘이 있습니다."

var dummyTarotOutputDto = TarotOutputDto(
    "0",
    0,
    arrayListOf(0, 1, 2),
    "2024-01-14T12:38:23.000Z",
    arrayListOf(
        CardResultData(arrayListOf("keyword1", "keyword2", "keyword3", "keyword3"), "어려움이나 역경 등 힘든 상황에서도 포기하지 않고 끝까지 이겨내는 힘이 필요합니다."),
        CardResultData(arrayListOf("keyword4", "keyword5", "keyword6", "keyword3"), "하면서 안정감을 추구하려는 마음가짐이 중요합니다. 그리고, 다른 "),
        CardResultData(arrayListOf("keyword7", "keyword8", "keyword9", "keyword3"), " 않고 매달 100만원씩 적금을 들고 있는 것이 그 예시겠네요. 하지만, 때로는 지")
    ),
    OverallResultData(dummySummary, dummyFullResult)
)


/** 유저가 선택한 주제 관리 */
@HiltViewModel
class FortuneViewModel @Inject constructor() : ViewModel() {
    private var _pickedTopicState = mutableStateOf(PickedTopicState())
    val pickedTopicState get() = _pickedTopicState

    /* 선택한 대주제 인덱스 */
    // 0 -> 연애운
    // 1 -> 학업운
    // 2 -> 소망운
    // 3 -> 직업운
    // 4 -> 오늘의 운세
    // 5 -> 궁합 운세

    // ---------------------------------------------------------------------------------------------

    fun getPickedTopic(topicNumber: Int) : TarotSubjectData {
        return when (topicNumber) {
            0 -> SubjectLove
            1 -> SubjectStudy
            2 -> SubjectDream
            3 -> SubjectJob
            4 -> SubjectToday
            5 -> SubjectHarmony
            else -> { SubjectLove }
        }
    }

    fun setPickedTopic(topicNumber: Int) {
        _pickedTopicState.value.topicNumber = topicNumber
        _pickedTopicState.value.topicSubjectData = getPickedTopic(topicNumber)
    }

    fun getSubjectImoji(localContext: Context, topicNumber: Int = _pickedTopicState.value.topicNumber): String {
        val resources: Resources = localContext.resources
        return when(topicNumber){
            0 -> resources.getString(R.string.imoji_love)
            1 -> resources.getString(R.string.imoji_study)
            2 -> resources.getString(R.string.imoji_dream)
            3 -> resources.getString(R.string.imoji_job)
            else -> {
                Log.e("tarotError", "error getPath(). pickedTopicNumber: ${_pickedTopicState.value.topicNumber}")
                ""
            }
        }
    }

    fun getCardImageId(localContext: Context, cardNumber: String): Int {
        val resources: Resources = localContext.resources
        return resources.getIdentifier("tarot_$cardNumber", "drawable", localContext.packageName)
    }
}