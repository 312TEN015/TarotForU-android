
package com.fourleafclover.tarot.ui.screen.my

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.MaterialTheme
import androidx.compose.material3.BottomSheetDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.platform.LocalView
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.fourleafclover.tarot.MyApplication
import com.fourleafclover.tarot.R
import com.fourleafclover.tarot.demo.ui.theme.backgroundColorScheme
import com.fourleafclover.tarot.demo.ui.theme.color.ColorSet
import com.fourleafclover.tarot.demo.ui.theme.textColorScheme
import com.fourleafclover.tarot.ui.component.AppBarPlain
import com.fourleafclover.tarot.ui.component.DeleteTarotResultDialog
import com.fourleafclover.tarot.ui.component.setStatusbarColor
import com.fourleafclover.tarot.ui.navigation.ScreenEnum
import com.fourleafclover.tarot.ui.navigation.navigateSaveState
import com.fourleafclover.tarot.ui.screen.fortune.viewModel.FortuneViewModel
import com.fourleafclover.tarot.ui.screen.my.viewmodel.MyTarotViewModel
import com.fourleafclover.tarot.ui.theme.TextB03M14
import com.fourleafclover.tarot.ui.theme.TextB04M12
import com.fourleafclover.tarot.ui.theme.TextH03SB18
import com.fourleafclover.tarot.ui.theme.getTextStyle

var showSheet = mutableStateOf(false)

@Preview
@Composable
fun MyTarotScreen(
    navController: NavHostController = rememberNavController(),
    myTarotViewModel: MyTarotViewModel = hiltViewModel(),
    fortuneViewModel: FortuneViewModel = hiltViewModel()
) {

    setStatusbarColor(LocalView.current, MaterialTheme.backgroundColorScheme.mainBackgroundColor)

    Box {

        if (showSheet.value) {
            BottomSheet(myTarotViewModel)
        }

        Column(
            modifier = Modifier
                .background(color = MaterialTheme.backgroundColorScheme.mainBackgroundColor)
                .padding(horizontal = 20.dp)
                .fillMaxSize()
        ) {

            // MY 타로 앱바
            AppBarPlain(title = "MY 타로", backgroundColor = MaterialTheme.backgroundColorScheme.mainBackgroundColor, backButtonVisible = false)

            // 갯수 표시
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.End) {
                Text(
                    text = "${myTarotViewModel.myTarotResults.size}", style = getTextStyle(
                        fontSize = 14,
                        fontWeight = FontWeight.Medium,
                        color = MaterialTheme.textColorScheme.highlightTextColor,
                    )
                )

                Text(
                    text = "/10", style = getTextStyle(
                        fontSize = 14,
                        fontWeight = FontWeight.Medium,
                        color = MaterialTheme.textColorScheme.onDialogContentColor,
                    )
                )

            }

            // 메인
            Box {

                // 목록이 비어있는 경우
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center,
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(bottom = 60.dp)
                        .alpha(if (myTarotViewModel.myTarotResults.size == 0) 1f else 0f)
                ) {
                    Image(
                        painter = painterResource(id = R.drawable.illust_crystalball),
                        contentDescription = "아직 저장된 타로 기록이 없어요!",
                        modifier = Modifier.padding(bottom = 24.dp)
                    )
                    TextB03M14(
                        text = "아직 저장된\n타로 기록이 없어요!",
                        color = MaterialTheme.textColorScheme.captionTextColor,
                        textAlign = TextAlign.Center
                    )
                }

                // 목록 있는 경우
                LazyColumn(
                    Modifier.padding(bottom = 50.dp),
                    contentPadding = PaddingValues(vertical = 10.dp),
                    content = {
                        items(myTarotViewModel.myTarotResults.size) {
                            MyTarotItemComponent(navController, it, fortuneViewModel, myTarotViewModel)
                        }

                    })
            }
        }
    }
}

@Composable
fun MyTarotItemComponent(
    navController: NavHostController = rememberNavController(),
    idx: Int = 0,
    fortuneViewModel: FortuneViewModel,
    myTarotViewModel: MyTarotViewModel
){

    Box(modifier = Modifier
        .padding(bottom = 16.dp)
        .clickable {
            myTarotViewModel.selectItem(idx)
            if (myTarotViewModel.selectedTarotResult.tarotType == 5) {
                myTarotViewModel.distinguishCardResult(myTarotViewModel.selectedTarotResult)
                navigateSaveState(navController, ScreenEnum.MyTarotHarmonyDetailScreen.name)
            } else {
                navigateSaveState(navController, ScreenEnum.MyTarotDetailScreen.name)
            }
        })
    {

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .background(color = MaterialTheme.backgroundColorScheme.secondaryBackgroundColor, shape = RoundedCornerShape(10.dp))
                .padding(start = 16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {

            Image(painter = painterResource(id = when (myTarotViewModel.myTarotResults[idx].tarotType) {
                0 -> R.drawable.icon_love
                1 -> R.drawable.icon_study
                2 -> R.drawable.icon_dream
                3 -> R.drawable.icon_job
                4 -> R.drawable.icon_today
                5 -> R.drawable.icon_match
                else -> R.drawable.icon_love
            }),
                contentDescription = null,
                modifier = Modifier.padding(top = 16.dp, end = 16.dp, bottom = 16.dp))

            Column(
                modifier = Modifier
                    .weight(1f)
                    .padding(top = 16.dp, bottom = 16.dp)
            ) {

                Row(verticalAlignment = Alignment.CenterVertically) {

                    TextH03SB18(
                        text = fortuneViewModel.getPickedTopic(myTarotViewModel.myTarotResults[idx].tarotType).majorTopic,
                        color = MaterialTheme.textColorScheme.titleTextColor,
                        modifier = Modifier.padding(end = 8.dp)
                    )

                    TextB04M12(
                        text = fortuneViewModel.getPickedTopic(myTarotViewModel.myTarotResults[idx].tarotType).majorQuestion,
                        color = MaterialTheme.textColorScheme.resultScreenSubTitleColor,
                        modifier = Modifier
                    )


                }

                TextB04M12(
                    text = myTarotViewModel.myTarotResults[idx].createdAt,
                    color = MaterialTheme.textColorScheme.resultScreenCreatedAtColor,
                    modifier = Modifier,
                    textAlign = TextAlign.End
                )
            }

            Image(
                painter = painterResource(id = R.drawable.dots_2),
                modifier = Modifier
                    .size(24.dp)
                    .padding(top = 6.dp, end = 6.dp)
                    .fillMaxHeight()
                    .align(Alignment.Top)
                    .clickable {
                        myTarotViewModel.selectItem(idx)
                        showSheet.value = true
                    },
                contentDescription = ""
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BottomSheet(myTarotViewModel: MyTarotViewModel) {
    val modalBottomSheetState = rememberModalBottomSheetState()
    var openDeleteDialog by remember { mutableStateOf(false) }

    if (openDeleteDialog){

        Dialog(onDismissRequest = {
            openDeleteDialog = false
            showSheet.value = false
        }) {
            DeleteTarotResultDialog(onClickNo = {
                openDeleteDialog = false
                showSheet.value = false
                                                },
                onClickOk = {
                    MyApplication.prefs.deleteTarotResult(myTarotViewModel)
                    showSheet.value = false
                })
        }
    }

    ModalBottomSheet(
        onDismissRequest = { showSheet.value = false },
        sheetState = modalBottomSheetState,
        dragHandle = { BottomSheetDefaults.DragHandle() },
        contentColor = BottomSheetDefaults.ContainerColor
    ) {
        Text(
            text = "삭제하기",
            style = getTextStyle(
                fontSize = 18,
                fontWeight = FontWeight.Normal,
                color = MaterialTheme.backgroundColorScheme.mainBackgroundColor
            ),
            modifier = Modifier
                .padding(horizontal = 20.dp)
                .padding(bottom = 20.dp)
                .clickable {
                    openDeleteDialog = true
                })
    }

}
