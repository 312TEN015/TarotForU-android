package com.fourleafclover.tarot

import com.fourleafclover.tarot.constant.dreamFortune
import com.fourleafclover.tarot.constant.dreamMajorQuestion
import com.fourleafclover.tarot.constant.dreamPlaceHolder1
import com.fourleafclover.tarot.constant.dreamPlaceHolder2
import com.fourleafclover.tarot.constant.dreamPlaceHolder3
import com.fourleafclover.tarot.constant.dreamSubQuestion1
import com.fourleafclover.tarot.constant.dreamSubQuestion2
import com.fourleafclover.tarot.constant.dreamSubQuestion3
import com.fourleafclover.tarot.constant.harmonyFortune
import com.fourleafclover.tarot.constant.harmonyMajorQuestion
import com.fourleafclover.tarot.constant.jobFortune
import com.fourleafclover.tarot.constant.jobMajorQuestion
import com.fourleafclover.tarot.constant.jobPlaceHolder1
import com.fourleafclover.tarot.constant.jobPlaceHolder2
import com.fourleafclover.tarot.constant.jobPlaceHolder3
import com.fourleafclover.tarot.constant.jobSubQuestion1
import com.fourleafclover.tarot.constant.jobSubQuestion2
import com.fourleafclover.tarot.constant.jobSubQuestion3
import com.fourleafclover.tarot.constant.loveFortune
import com.fourleafclover.tarot.constant.loveMajorQuestion
import com.fourleafclover.tarot.constant.lovePlaceHolder1
import com.fourleafclover.tarot.constant.lovePlaceHolder2
import com.fourleafclover.tarot.constant.lovePlaceHolder3
import com.fourleafclover.tarot.constant.loveSubQuestion1
import com.fourleafclover.tarot.constant.loveSubQuestion2
import com.fourleafclover.tarot.constant.loveSubQuestion3
import com.fourleafclover.tarot.constant.studyFortune
import com.fourleafclover.tarot.constant.studyMajorQuestion
import com.fourleafclover.tarot.constant.studyPlaceHolder1
import com.fourleafclover.tarot.constant.studyPlaceHolder2
import com.fourleafclover.tarot.constant.studyPlaceHolder3
import com.fourleafclover.tarot.constant.studySubQuestion1
import com.fourleafclover.tarot.constant.studySubQuestion2
import com.fourleafclover.tarot.constant.studySubQuestion3
import com.fourleafclover.tarot.constant.todayFortune
import com.fourleafclover.tarot.constant.todayMajorQuestion
import com.fourleafclover.tarot.data.TarotSubjectData
import com.fourleafclover.tarot.ui.theme.gray_9
import com.fourleafclover.tarot.ui.theme.primaryDream
import com.fourleafclover.tarot.ui.theme.primaryJob
import com.fourleafclover.tarot.ui.theme.primaryLove
import com.fourleafclover.tarot.ui.theme.primaryStudy

val SubjectLove = TarotSubjectData(
        loveFortune,
        loveMajorQuestion,
        arrayListOf(loveSubQuestion1, loveSubQuestion2, loveSubQuestion3),
        arrayListOf(lovePlaceHolder1, lovePlaceHolder2, lovePlaceHolder3),
        primaryLove
)
val SubjectStudy = TarotSubjectData(
        studyFortune,
        studyMajorQuestion,
        arrayListOf(studySubQuestion1, studySubQuestion2, studySubQuestion3),
        arrayListOf(studyPlaceHolder1, studyPlaceHolder2, studyPlaceHolder3),
        primaryStudy
)
val SubjectDream = TarotSubjectData(
        dreamFortune,
        dreamMajorQuestion,
        arrayListOf(dreamSubQuestion1, dreamSubQuestion2, dreamSubQuestion3),
        arrayListOf(dreamPlaceHolder1, dreamPlaceHolder2, dreamPlaceHolder3),
        primaryDream
)
val SubjectJob = TarotSubjectData(
        jobFortune,
        jobMajorQuestion,
        arrayListOf(jobSubQuestion1, jobSubQuestion2, jobSubQuestion3),
        arrayListOf(jobPlaceHolder1, jobPlaceHolder2, jobPlaceHolder3),
        primaryJob
)
val SubjectToday = TarotSubjectData(
        todayFortune,
        todayMajorQuestion,
        arrayListOf(),
        arrayListOf(),
        gray_9
)
val SubjectHarmony = TarotSubjectData(
        majorTopic = harmonyFortune,
        majorQuestion = harmonyMajorQuestion,
        primaryColor = gray_9
)


val entireCards = arrayListOf<Int>(0,1,2,3,4,5,6,7,8,9,10,11,12,13,14,15,16,17,18,19,20,21)
fun getRandomCards(): List<Int> { return entireCards.toMutableList().shuffled() }
