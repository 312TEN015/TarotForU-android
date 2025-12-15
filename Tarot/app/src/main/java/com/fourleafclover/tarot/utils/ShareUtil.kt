package com.fourleafclover.tarot.utils

import android.app.Activity
import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.util.Log
import androidx.core.net.toUri
import androidx.navigation.NavHostController
import com.fourleafclover.tarot.MyApplication
import com.fourleafclover.tarot.R
import com.fourleafclover.tarot.demo.viewmodel.DemoViewModel
import com.fourleafclover.tarot.ui.navigation.ScreenEnum
import com.fourleafclover.tarot.ui.navigation.navigateInclusive
import com.fourleafclover.tarot.ui.screen.harmony.viewmodel.HarmonyViewModel
import com.fourleafclover.tarot.ui.screen.harmony.viewmodel.LoadingViewModel
import com.fourleafclover.tarot.ui.screen.my.viewmodel.ShareViewModel
import com.google.firebase.Firebase
import com.google.firebase.dynamiclinks.PendingDynamicLinkData
import com.google.firebase.dynamiclinks.ShortDynamicLink
import com.google.firebase.dynamiclinks.androidParameters
import com.google.firebase.dynamiclinks.dynamicLink
import com.google.firebase.dynamiclinks.dynamicLinks
import com.google.firebase.dynamiclinks.iosParameters
import com.google.firebase.dynamiclinks.shortLinkAsync


enum class ShareLinkType {
    MY,     // 타로 결과 공유
    HARMONY // 방 코드 공유
}

enum class ShareActionType {
    OPEN_SHEET, // 공유 바텀 시트 열기
    COPY_LINK   // 링크 복사
}

private const val DYNAMIC_LINK_PREFIX = "https://fourleafclover.page.link"

// send ============================================================================================

// 1) 링크 타입에 따른 분기
fun setDynamicLink(
    context: Context,
    value: String,
    linkType: ShareLinkType,
    actionType: ShareActionType,
    harmonyViewModel: HarmonyViewModel
){
    if (linkType == ShareLinkType.HARMONY) {
        if (harmonyViewModel.roomId.value.isNotEmpty()){
            if (harmonyViewModel.shortLink.isNotEmpty())
                doShare(context, actionType, harmonyViewModel.shortLink.toUri(), linkType)
            else if (harmonyViewModel.dynamicLink.isNotEmpty())
                doShare(context, actionType, harmonyViewModel.dynamicLink.toUri(), linkType)
            else if (harmonyViewModel.dynamicLink.isEmpty() && harmonyViewModel.shortLink.isEmpty())
                getDynamicLink(context, value, linkType, actionType, harmonyViewModel)
            return
        }
    }

    getDynamicLink(context, value, linkType, actionType, harmonyViewModel)
}

// 2) 다이나믹 링크 초기화
fun getDynamicLink(
    context: Context,
    value: String,
    linkType: ShareLinkType,
    actionType: ShareActionType,
    harmonyViewModel: HarmonyViewModel
){
    val url = initLink(value, linkType)

    val dynamicLink = Firebase.dynamicLinks.dynamicLink {
        link = Uri.parse(url)
        domainUriPrefix = DYNAMIC_LINK_PREFIX
        androidParameters { }
        iosParameters("com.example.ios") { }
    }

    if (linkType == ShareLinkType.HARMONY)
        harmonyViewModel.shortLink = dynamicLink.uri.toString()

   val shortLinkTask = Firebase.dynamicLinks.shortLinkAsync(ShortDynamicLink.Suffix.SHORT) {
        longLink = dynamicLink.uri
        link = Uri.parse(url)
        domainUriPrefix = DYNAMIC_LINK_PREFIX
        androidParameters { }
        iosParameters("com.example.ios") { }

   }
       .addOnSuccessListener { shortLink ->
            doShare(context, actionType, shortLink.shortLink, linkType)

            if (linkType == ShareLinkType.HARMONY)
                harmonyViewModel.shortLink = shortLink.shortLink.toString()

       }
       .addOnFailureListener {
            doShare(context, actionType, dynamicLink.uri, linkType)
       }
}

// 3) 공유하기 실행
fun doShare(
    context: Context,
    actionType: ShareActionType,
    link: Uri?,
    linkType: ShareLinkType
){
    when(actionType) {
        ShareActionType.OPEN_SHEET -> showShareSheet(context, link, linkType)

        ShareActionType.COPY_LINK ->  copyLink(context, link.toString())
    }
}

fun initLink(value: String, linkType: ShareLinkType): String {
    var url = "http://tarot-for-u.shop/share?"

    url += when (linkType) {
        ShareLinkType.MY -> {
            "tarotId=${value}"
        }
        ShareLinkType.HARMONY -> {
            "roomId=${value}"
        }
    }

    return url
}

// actions -----------------------------------------------------------------------------------------
fun showShareSheet(context: Context, link: Uri?, linkType: ShareLinkType){
    val intent = Intent(Intent.ACTION_SEND)
    intent.type = "text/plain"

    val shareText = when (linkType) {
        ShareLinkType.HARMONY -> "${context.resources.getString(R.string.share_harmony_content)}\n\n$link"

        ShareLinkType.MY -> "${context.resources.getString(R.string.share_content)}\n\n$link"
    }

    intent.putExtra(Intent.EXTRA_TEXT, shareText)
    context.startActivity(Intent.createChooser(intent, null))
}

fun copyLink(context: Context, linkToCopy: String){
    val clipboardManager = context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
    val clip = ClipData.newPlainText("타로결과 공유하기", "${context.resources.getString(R.string.share_harmony_content)}\n\n$linkToCopy")
    clipboardManager.setPrimaryClip(clip)

    MyApplication.toastUtil.makeShortToast("초대 링크가 복사되었어요!")
}



// receive =========================================================================================
fun receiveShareRequest(
    activity: Activity,
    navController: NavHostController,
    shareViewModel: ShareViewModel,
    loadingViewModel: LoadingViewModel,
    harmonyViewModel: HarmonyViewModel,
    demoViewModel: DemoViewModel
){
    Firebase.dynamicLinks
        .getDynamicLink(activity.intent)
        .addOnSuccessListener(activity) { pendingDynamicLinkData: PendingDynamicLinkData? ->
            // Get deep link from result (may be null if no link is found)
            if (pendingDynamicLinkData == null) return@addOnSuccessListener

            val deepLink = pendingDynamicLinkData.link ?: return@addOnSuccessListener

            val deepLinkUri = Uri.parse(deepLink.toString())

            // 타로 결과 공유
            if (deepLinkUri.getBooleanQueryParameter("tarotId", false)){
                val sharedTarotId = deepLinkUri.getQueryParameter("tarotId")!!
                getSharedTarotDetail(navController, sharedTarotId, shareViewModel, loadingViewModel, demoViewModel.isDemo)
                loadingViewModel.startLoading(navController, ScreenEnum.LoadingScreen, ScreenEnum.ShareDetailScreen)
            }

            // 궁합 초대
            if (deepLinkUri.getBooleanQueryParameter("roomId", false)){

                MyApplication.connectSocket()
                harmonyViewModel.setIsRoomOwner(false)
                harmonyViewModel.enterInvitedRoom(deepLinkUri.getQueryParameter("roomId")!!)
                navigateInclusive(navController, ScreenEnum.RoomGenderScreen.name)

            }

            activity.intent = null

        }
        .addOnFailureListener(activity) { e ->
            Log.w(
                "DynamicLink",
                "getDynamicLink:onFailure",
                e
            )
        }
}
