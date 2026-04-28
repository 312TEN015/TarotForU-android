package com.fourleafclover.tarot.data.repository

import com.fourleafclover.tarot.data.MatchTarotInputDto
import com.fourleafclover.tarot.data.TarotInputDto
import com.fourleafclover.tarot.data.TarotOutputDto
import kotlinx.coroutines.flow.Flow

/**
 * Single source of truth for tarot data and room session.
 * Implementations decide whether to talk to the real backend or to use local fakes.
 */
interface TarotRepository {

    // ---------- Single tarot (Retrofit) ----------

    /** Submit answers + picked cards and get a generated tarot reading. */
    suspend fun postTarotResult(
        input: TarotInputDto,
        topicNumber: Int
    ): Result<TarotOutputDto>

    /** Fetch a single saved tarot reading by id. */
    suspend fun getTarotById(tarotId: String): Result<TarotOutputDto>

    /** Fetch a list of tarot readings by ids. */
    suspend fun getTarotList(tarotIds: List<String>): Result<List<TarotOutputDto>>

    // ---------- Harmony room (Socket.IO) ----------

    /** Open the realtime session. Safe to call repeatedly. */
    fun connect()

    /** Close the realtime session. Safe to call when never opened. */
    fun disconnect()

    /** Stream of room events for the currently active session. */
    fun observeRoomEvents(): Flow<RoomEvent>

    /** Ask the server to create a new room. Result arrives via [RoomEvent.Created]. */
    suspend fun createRoom()

    /** Join an existing room. Result arrives via [RoomEvent.Joined]. */
    suspend fun joinRoom(roomId: String, nickname: String)

    /** Inviter signals "start" to partner. */
    suspend fun signalStart(roomId: String, nickname: String)

    /** Submit a card pick. Partner notification arrives via [RoomEvent.PartnerNext]. */
    suspend fun selectCard(roomId: String, nickname: String, cardNumber: Int)

    /** Submit final card array and request the match reading. */
    suspend fun requestMatchResult(input: MatchTarotInputDto): Result<TarotOutputDto>

    /** Acknowledge that the result has been received by this client. */
    suspend fun confirmResultReceived(roomId: String, tarotId: String)

    /** Leave the room. */
    suspend fun exitRoom(roomId: String)
}
