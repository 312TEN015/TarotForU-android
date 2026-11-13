package com.fourleafclover.tarot.notice

import com.fourleafclover.tarot.data.NoticeConfigDto

/** 앱 사용을 막고 띄우는 공지. 확인 버튼의 동작이 종류마다 다르다. */
sealed class AppNotice {
    abstract val title: String
    abstract val content: String

    /** 점검 안내: 확인을 누르면 앱을 종료한다. */
    data class Maintenance(override val title: String, override val content: String) : AppNotice()

    /** 강제 업데이트: 확인을 누르면 스토어로 이동하고, 팝업은 닫히지 않는다. */
    data class ForceUpdate(override val title: String, override val content: String) : AppNotice()
}

/** 앱 시작 시 공지 설정(GET /config/notice)을 받아온 결과 */
sealed class NoticeFetchResult {
    /** 서버가 응답함. 공지 설정이 없는 서버라면 config는 null이다. */
    data class Reachable(val config: NoticeConfigDto?) : NoticeFetchResult()

    /** 연결 실패, 시간 초과, 5xx 응답 등 서버를 쓸 수 없는 상태 */
    object Unreachable : NoticeFetchResult()
}

private const val DEFAULT_MAINTENANCE_TITLE = "서비스 점검 중이에요"
private const val DEFAULT_UPDATE_TITLE = "업데이트가 필요해요"
private const val DEFAULT_UPDATE_CONTENT = "원활한 이용을 위해\n최신 버전으로 업데이트해 주세요."

// 서버에서 문구를 받아올 수 없을 때 쓰는 내장 문구.
// 사용자의 네트워크가 끊긴 경우에도 뜨기 때문에 "점검 중"이라고 단정하지 않는다.
private const val UNREACHABLE_TITLE = "서버에 연결할 수 없어요"
private const val UNREACHABLE_CONTENT = "네트워크 연결을 확인하거나\n잠시 후 다시 시도해 주세요."

/**
 * 띄워야 할 공지를 고른다. 우선순위는 서버 연결 불가 > 점검 > 강제 업데이트이고,
 * 해당하는 것이 없으면 null이다.
 */
fun resolveAppNotice(result: NoticeFetchResult, versionCode: Int): AppNotice? {
    val config = when (result) {
        NoticeFetchResult.Unreachable ->
            return AppNotice.Maintenance(UNREACHABLE_TITLE, UNREACHABLE_CONTENT)
        is NoticeFetchResult.Reachable -> result.config ?: return null
    }

    val maintenance = config.maintenance
    if (maintenance?.enabled == true) {
        return AppNotice.Maintenance(
            title = maintenance.title.orDefault(DEFAULT_MAINTENANCE_TITLE),
            content = maintenance.content.orEmpty()
        )
    }

    // 이미 업데이트한 사용자는 막지 않도록, 서버가 정한 최소 버전보다 낮을 때만 띄운다
    val forceUpdate = config.forceUpdate
    if (forceUpdate?.enabled == true && versionCode < forceUpdate.minVersionCode) {
        return AppNotice.ForceUpdate(
            title = forceUpdate.title.orDefault(DEFAULT_UPDATE_TITLE),
            content = forceUpdate.content.orDefault(DEFAULT_UPDATE_CONTENT)
        )
    }

    return null
}

private fun String?.orDefault(default: String): String = if (isNullOrBlank()) default else this
