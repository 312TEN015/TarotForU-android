package com.fourleafclover.tarot.data.repository

sealed class RoomEvent {
    data class Created(val roomId: String) : RoomEvent()
    data class Joined(val nicknames: List<String>) : RoomEvent()
    data class PartnerNext(val cardNumber: Int? = null) : RoomEvent()
    data class ResultPrepared(val tarotId: String) : RoomEvent()
    object PartnerExited : RoomEvent()
    data class Error(val message: String) : RoomEvent()
}
