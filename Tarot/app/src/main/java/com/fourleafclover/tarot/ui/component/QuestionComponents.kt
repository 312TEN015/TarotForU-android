package com.fourleafclover.tarot.ui.component

import android.content.Context
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.MaterialTheme
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.fourleafclover.tarot.MyApplication
import com.fourleafclover.tarot.constant.questionCount
import com.fourleafclover.tarot.data.TarotSubjectData
import com.fourleafclover.tarot.demo.ui.component.SecondaryButtonColors
import com.fourleafclover.tarot.demo.ui.component.TextFieldColors
import com.fourleafclover.tarot.demo.ui.theme.color.ColorSet
import com.fourleafclover.tarot.demo.ui.theme.color.gray_9
import com.fourleafclover.tarot.demo.ui.theme.textColorScheme
import com.fourleafclover.tarot.ui.navigation.ScreenEnum
import com.fourleafclover.tarot.ui.navigation.navigateSaveState
import com.fourleafclover.tarot.ui.screen.fortune.viewModel.FortuneViewModel
import com.fourleafclover.tarot.ui.screen.fortune.viewModel.QuestionInputViewModel
import com.fourleafclover.tarot.ui.theme.TextB03M14
import com.fourleafclover.tarot.ui.theme.TextB04M12
import com.fourleafclover.tarot.ui.theme.TextButtonM16
import com.fourleafclover.tarot.ui.theme.TextCaptionM12
import com.fourleafclover.tarot.ui.theme.TextH02M22
import com.fourleafclover.tarot.ui.theme.TextH03SB18
import com.fourleafclover.tarot.ui.theme.getTextStyle

val questionNumberModifier = Modifier
    .padding(end = 10.dp)
    .background(color = ColorSet.Default.tarotLightColors.textColor.highlightTextColor, shape = RoundedCornerShape(2.dp))
    .padding(start = 4.dp, end = 4.dp)

@Composable
fun QuestionsComponent(
    pickedTopicTemplate: TarotSubjectData,
    idx: Int = 0,
    navController: NavHostController = rememberNavController(),
    context: Context,
    fortuneViewModel: FortuneViewModel,
    questionInputViewModel: QuestionInputViewModel
) {
    // header --------------------------------------------------------------------------------------

    if (idx == 0){
        QuestionViewHeader(pickedTopicTemplate, context, fortuneViewModel, questionInputViewModel)
        return
    }

    // footer --------------------------------------------------------------------------------------

    if (idx == questionCount +1){
        QuestionViewFooter(navController, questionInputViewModel)
        return
    }

    // body ----------------------------------------------------------------------------------------

    QuestionViewBody(idx, pickedTopicTemplate, questionInputViewModel)
}

@Composable
fun QuestionViewHeader(
    pickedTopicTemplate: TarotSubjectData,
    context: Context,
    fortuneViewModel: FortuneViewModel,
    questionInputViewModel:QuestionInputViewModel
){

    Column(modifier = Modifier.padding(bottom = 40.dp)) {

        val imoji = remember { fortuneViewModel.getSubjectImoji(context) }

        Text(
            text = "$imoji ${pickedTopicTemplate.majorQuestion}",
            style = getTextStyle(22, FontWeight.Bold, gray_9),
            modifier = Modifier
                .fillMaxWidth()
                .background(color = pickedTopicTemplate.primaryColor)
                .padding(top = 15.dp, bottom = 43.dp),
            textAlign = TextAlign.Center
        )

        Row(modifier = Modifier
            .fillMaxWidth()
            .padding(top = 24.dp, bottom = 12.dp)
            .padding(horizontal = 20.dp)) {

            TextH02M22(
                text = "마음속에 있는 고민거리를\n입력해보세요!",
                color = MaterialTheme.textColorScheme.titleTextColor,
                modifier = Modifier
                    .weight(1f)
                    .padding(top = 16.dp)
            )

            Image(painter = painterResource(id = questionInputViewModel.getTarotIllustId(fortuneViewModel.pickedTopicState.value.topicNumber)), contentDescription = null,
                modifier = Modifier
                    .width(83.dp)
                    .fillMaxHeight(),
                contentScale = ContentScale.FillWidth,
                alignment = Alignment.CenterEnd)
        }

        Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.padding(horizontal = 20.dp)) {
            TextCaptionM12(
                text = "TIP!",
                color = MaterialTheme.textColorScheme.highlightTextColor,
                modifier = Modifier
                    .padding(horizontal = 4.dp, vertical = 2.dp)
            )

            TextCaptionM12(
                text = "구체적으로 입력할수록 더욱 상세한 결과를 받아볼 수 있어요.",
                color = MaterialTheme.textColorScheme.captionTextColor,
                modifier = Modifier.padding(start = 8.dp)
            )
        }
    }
}

@Composable
fun QuestionViewBody(idx: Int, pickedTopicTemplate: TarotSubjectData, questionInputViewModel: QuestionInputViewModel){
    val maxChar = remember { questionInputViewModel.maxChar }

    Column(modifier = Modifier
        .padding(bottom = 32.dp)
        .padding(horizontal = 20.dp)) {


        Column(modifier = Modifier
            .padding(top = 8.dp, bottom = 8.dp)) {
            TextB04M12(
                text = "0${idx}",
                color = gray_9,
                modifier = questionNumberModifier
            )
            TextH03SB18(
                text = pickedTopicTemplate.subQuestions[idx-1],
                color = MaterialTheme.textColorScheme.subTitleTextColor,
                modifier = Modifier.padding(top = 8.dp, bottom = 16.dp)
            )
        }


        TextField(
            modifier = Modifier
                .fillMaxWidth()
                .height(92.dp)
                .background(color = Color.Transparent),
            textStyle = getTextStyle(fontSize = 14, fontWeight = FontWeight.Medium, color = MaterialTheme.textColorScheme.subTitleTextColor),
            shape = RoundedCornerShape(size = 10.dp),
            onValueChange = { newText ->
                if (newText.text.length > maxChar){
                    MyApplication.toastUtil.makeShortToast("${maxChar}자 이상 입력할 수 없습니다.")
                }
                else {
                    questionInputViewModel.setTextField(idx, newText)
                }
            },
            value = questionInputViewModel.getNowTextField(idx).value,
            placeholder = {
                TextB03M14(
                    text = pickedTopicTemplate.placeHolders[idx-1],
                    color = MaterialTheme.textColorScheme.textFieldPlaceHolderColor,
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(2.dp)) },
            singleLine = false,
            colors = TextFieldColors()
        )
    }
}

@Composable
fun QuestionViewFooter(navController: NavHostController, questionInputViewModel: QuestionInputViewModel){

    Button(
        onClick = {
            navigateSaveState(navController, ScreenEnum.PickTarotScreen.name)
        },
        shape = RoundedCornerShape(10.dp),
        modifier = Modifier
            .wrapContentHeight()
            .fillMaxWidth()
            .padding(top = 72.dp, bottom = 44.dp)
            .padding(horizontal = 20.dp),
        colors = SecondaryButtonColors(),
        enabled = questionInputViewModel.allFilled()
    ) {
        TextButtonM16(text = "다음",
            modifier = Modifier.padding(vertical = 8.dp),
            color = if (questionInputViewModel.allFilled()) MaterialTheme.textColorScheme.onActiveSecondaryButtonColor
            else MaterialTheme.textColorScheme.onDisabledButtonColor
        )
    }
}