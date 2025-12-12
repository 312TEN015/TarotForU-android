package com.fourleafclover.tarot.ui.screen.fortune

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalView
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.fourleafclover.tarot.constant.questionCount
import com.fourleafclover.tarot.demo.ui.theme.backgroundColorScheme
import com.fourleafclover.tarot.ui.component.AppBarCloseWithDialog
import com.fourleafclover.tarot.ui.component.QuestionsComponent
import com.fourleafclover.tarot.ui.component.getBackgroundModifier
import com.fourleafclover.tarot.ui.component.setStatusbarColor
import com.fourleafclover.tarot.ui.screen.fortune.viewModel.FortuneViewModel
import com.fourleafclover.tarot.ui.screen.fortune.viewModel.QuestionInputViewModel
import com.fourleafclover.tarot.ui.screen.main.DialogViewModel

@Composable
fun InputScreen(
    navController: NavHostController = rememberNavController(),
    fortuneViewModel: FortuneViewModel,
    questionInputViewModel: QuestionInputViewModel,
    dialogViewModel: DialogViewModel
) {
    val localContext = LocalContext.current
    val pickedTopicTemplate by fortuneViewModel.pickedTopicState

//    setStatusbarColor(LocalView.current, pickedTopicTemplate.topicSubjectData.primaryColor)

    Column(modifier = getBackgroundModifier(MaterialTheme.backgroundColorScheme.mainBackgroundColor ))
    {

        AppBarCloseWithDialog(
            navController = navController,
            pickedTopicTemplate = pickedTopicTemplate.topicSubjectData,
            backgroundColor = pickedTopicTemplate.topicSubjectData.primaryColor,
            dialogViewModel = dialogViewModel
        )

        Column(modifier = Modifier) {

            LazyColumn(content = {
                // numberOfQuestion + header + footer
                items(questionCount + 2) {
                    QuestionsComponent(
                        pickedTopicTemplate.topicSubjectData,
                        it,
                        navController,
                        localContext,
                        fortuneViewModel,
                        questionInputViewModel
                    )
                }

            })

        }
    }

}

