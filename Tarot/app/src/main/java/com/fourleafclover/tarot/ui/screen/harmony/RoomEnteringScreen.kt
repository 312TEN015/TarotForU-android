package com.fourleafclover.tarot.ui.screen.harmony

import android.os.Handler
import android.os.Looper
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.fourleafclover.tarot.R
import com.fourleafclover.tarot.demo.ui.theme.backgroundColorScheme
import com.fourleafclover.tarot.demo.ui.theme.textColorScheme
import com.fourleafclover.tarot.ui.component.getBackgroundModifier
import com.fourleafclover.tarot.ui.navigation.PreventBackPressed
import com.fourleafclover.tarot.ui.navigation.ScreenEnum
import com.fourleafclover.tarot.ui.navigation.navigateInclusive
import com.fourleafclover.tarot.ui.theme.TextH02M22

@Composable
@Preview
fun RoomEnteringScreen(navController: NavHostController = rememberNavController()) {

    var initialize by remember { mutableStateOf(false) }

    PreventBackPressed()

    /* 한번만 실행 */
    if (!initialize) {
        initialize = true
        initialComposition = true

        Handler(Looper.getMainLooper())
            .postDelayed({
                navigateInclusive(navController, ScreenEnum.RoomChatScreen.name)
            }, 3000)
    }

    Column(
        modifier = getBackgroundModifier(color = MaterialTheme.backgroundColorScheme.mainBackgroundColor).padding(horizontal = 38.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {

        Image(
            modifier = Modifier.padding(bottom = 40.dp),
            painter = painterResource(id = R.drawable.check),
            contentDescription = null
        )

        TextH02M22(
            text = "상대방이 초대방에 입장했어요!\n이제 궁합 운세 보기가 시작됩니다.",
            color = MaterialTheme.textColorScheme.titleTextColor,
            textAlign = TextAlign.Center
        )

    }


}