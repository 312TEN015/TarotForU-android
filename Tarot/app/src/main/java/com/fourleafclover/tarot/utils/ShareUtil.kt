package com.fourleafclover.tarot.utils

import android.app.Activity
import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.util.Log
import androidx.core.net.toUri
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavHostController
import com.fourleafclover.tarot.BuildConfig
import com.fourleafclover.tarot.MyApplication
import com.fourleafclover.tarot.R
import com.fourleafclover.tarot.ui.navigation.ScreenEnum
import com.fourleafclover.tarot.ui.navigation.navigateInclusive
import com.fourleafclover.tarot.ui.screen.harmony.viewmodel.HarmonyViewModel
import com.fourleafclover.tarot.ui.screen.harmony.viewmodel.LoadingViewModel
import com.fourleafclover.tarot.ui.screen.my.viewmodel.ShareViewModel
import kotlinx.coroutines.launch


enum class ShareLinkType {
    MY,
    HARMONY
}

enum class ShareActionType {
    OPEN_SHEET,
    COPY_LINK
}

// send ============================================================================================

fun setDynamicLink(
    context: Context,
    value: String,
    linkType: ShareLinkType,
    actionType: ShareActionType,
    harmonyViewModel: HarmonyViewModel
) {
    val url = initLink(value, linkType).toUri()

    if (linkType == ShareLinkType.HARMONY)
        harmonyViewModel.shortLink = url.toString()

    doShare(context, actionType, url, linkType)
}

fun initLink(value: String, linkType: ShareLinkType): String {
    var url = "${BuildConfig.SHARE_URL}?"
    url += when (linkType) {
        ShareLinkType.MY -> "tarotId=${value}"
        ShareLinkType.HARMONY -> "roomId=${value}"
    }
    return url
}

// actions -----------------------------------------------------------------------------------------

fun doShare(
    context: Context,
    actionType: ShareActionType,
    link: Uri?,
    linkType: ShareLinkType
) {
    when (actionType) {
        ShareActionType.OPEN_SHEET -> showShareSheet(context, link, linkType)
        ShareActionType.COPY_LINK -> copyLink(context, link.toString())
    }
}

fun showShareSheet(context: Context, link: Uri?, linkType: ShareLinkType) {
    val intent = Intent(Intent.ACTION_SEND)
    intent.type = "text/plain"

    val shareText = when (linkType) {
        ShareLinkType.HARMONY -> "${context.resources.getString(R.string.share_harmony_content)}\n\n$link"
        ShareLinkType.MY -> "${context.resources.getString(R.string.share_content)}\n\n$link"
    }

    intent.putExtra(Intent.EXTRA_TEXT, shareText)
    context.startActivity(Intent.createChooser(intent, null))
}

fun copyLink(context: Context, linkToCopy: String) {
    val clipboardManager = context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
    val clip = ClipData.newPlainText(
        "타로결과 공유하기",
        "${context.resources.getString(R.string.share_harmony_content)}\n\n$linkToCopy"
    )
    clipboardManager.setPrimaryClip(clip)
    MyApplication.toastUtil.makeShortToast("초대 링크가 복사되었어요!")
}


// receive =========================================================================================

/**
 * Handles incoming App Links (Android App Links replacing Firebase Dynamic Links).
 * Reads the deep link URL directly from the intent without any third-party SDK.
 */
fun receiveShareRequest(
    activity: Activity,
    navController: NavHostController,
    shareViewModel: ShareViewModel,
    loadingViewModel: LoadingViewModel,
    harmonyViewModel: HarmonyViewModel
) {
    val deepLinkUri = activity.intent?.data ?: return

    Log.d("AppLink", "received: $deepLinkUri")

    if (deepLinkUri.getBooleanQueryParameter("tarotId", false)) {
        val sharedTarotId = deepLinkUri.getQueryParameter("tarotId")!!
        loadingViewModel.startLoading(navController, ScreenEnum.LoadingScreen, ScreenEnum.ShareDetailScreen)
        // 로딩 화면으로 넘어가면 홈 화면의 컴포지션 스코프는 취소되므로,
        // 화면 전환과 무관하게 살아 있는 ViewModel 스코프에서 조회한다.
        shareViewModel.viewModelScope.launch {
            val tarot = shareViewModel.loadSharedTarot(sharedTarotId)
            if (tarot?.tarotType == 5) {
                loadingViewModel.changeDestination(ScreenEnum.ShareHarmonyDetailScreen)
            }
            loadingViewModel.endLoading(navController)
        }
    }

    if (deepLinkUri.getBooleanQueryParameter("roomId", false)) {
        harmonyViewModel.setIsRoomOwner(false)
        harmonyViewModel.enterInvitedRoom(deepLinkUri.getQueryParameter("roomId")!!)
        navigateInclusive(navController, ScreenEnum.RoomGenderScreen.name)
    }

    activity.intent = null
}
