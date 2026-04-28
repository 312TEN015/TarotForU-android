package com.fourleafclover.tarot.data.repository

import com.fourleafclover.tarot.data.MatchTarotInputDto
import com.fourleafclover.tarot.data.TarotInputDto
import com.fourleafclover.tarot.data.TarotOutputDto
import com.fourleafclover.tarot.demo.data.demoHarmonyTarotResult
import com.fourleafclover.tarot.demo.data.demoTarotResult
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch
import javax.inject.Singleton

/**
 * Local-only impl: dummy data + scripted partner.
 * Mirrors the timing/feel of the real flow so screens see the same events.
 */
@Singleton
class FakeTarotRepository : TarotRepository {

    private val scope = CoroutineScope(SupervisorJob() + Dispatchers.Main)
    private val _events = MutableSharedFlow<RoomEvent>(extraBufferCapacity = 16)

    private var partnerScript: Job? = null
    private val partnerCards = listOf(4, 5, 6) // matches demoHarmonyTarotResult.cards[3..5]

    override suspend fun postTarotResult(
        input: TarotInputDto,
        topicNumber: Int
    ): Result<TarotOutputDto> {
        delay(800)
        return Result.success(demoTarotResult)
    }

    override suspend fun getTarotById(tarotId: String): Result<TarotOutputDto> {
        val match = if (tarotId == demoHarmonyTarotResult.tarotId) demoHarmonyTarotResult
        else demoTarotResult
        return Result.success(match)
    }

    override suspend fun getTarotList(tarotIds: List<String>): Result<List<TarotOutputDto>> {
        val list = tarotIds.mapNotNull { id ->
            when (id) {
                demoTarotResult.tarotId -> demoTarotResult
                demoHarmonyTarotResult.tarotId -> demoHarmonyTarotResult
                else -> null
            }
        }
        return Result.success(list)
    }

    override fun connect() { /* no-op */ }

    override fun disconnect() {
        partnerScript?.cancel()
        partnerScript = null
    }

    override fun observeRoomEvents(): Flow<RoomEvent> = _events.asSharedFlow()

    override suspend fun createRoom() {
        delay(1500)
        _events.emit(RoomEvent.Created(roomId = demoHarmonyTarotResult.tarotId))
    }

    override suspend fun joinRoom(roomId: String, nickname: String) {
        // Simulate the partner ("아지") joining a moment after the user.
        delay(1500)
        _events.emit(RoomEvent.Joined(nicknames = listOf(nickname, DEMO_PARTNER_NICKNAME)))
    }

    override suspend fun signalStart(roomId: String, nickname: String) {
        // Partner "starts" shortly after the user does.
        scheduleAfter(700) { _events.emit(RoomEvent.PartnerNext()) }
    }

    override suspend fun selectCard(roomId: String, nickname: String, cardNumber: Int) {
        // Partner picks a scripted card after the user picks.
        val pickIndex = nextPartnerPickIndex
        nextPartnerPickIndex = (pickIndex + 1).coerceAtMost(partnerCards.lastIndex)
        scheduleAfter(900) {
            _events.emit(RoomEvent.PartnerNext(cardNumber = partnerCards[pickIndex]))
        }
    }

    override suspend fun requestMatchResult(input: MatchTarotInputDto): Result<TarotOutputDto> {
        delay(1200)
        // Mimic the real backend: the result drops on the event flow as well.
        _events.emit(RoomEvent.ResultPrepared(tarotId = demoHarmonyTarotResult.tarotId))
        return Result.success(demoHarmonyTarotResult)
    }

    override suspend fun confirmResultReceived(roomId: String, tarotId: String) { /* no-op */ }

    override suspend fun exitRoom(roomId: String) {
        partnerScript?.cancel()
        partnerScript = null
        nextPartnerPickIndex = 0
    }

    // ---------------- internals ----------------

    private var nextPartnerPickIndex = 0

    private fun scheduleAfter(delayMs: Long, block: suspend () -> Unit) {
        partnerScript = scope.launch {
            delay(delayMs)
            block()
        }
    }

    private companion object {
        const val DEMO_PARTNER_NICKNAME = "아지"
    }
}
