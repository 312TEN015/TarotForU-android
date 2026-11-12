package com.fourleafclover.tarot.data.repository

import com.fourleafclover.tarot.data.MatchTarotInputDto
import com.fourleafclover.tarot.data.TarotInputDto
import com.fourleafclover.tarot.data.TarotOutputDto
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
class FakeTarotRepository(private val demoMode: DemoMode) : TarotRepository {

    private val scope = CoroutineScope(SupervisorJob() + Dispatchers.Main)
    private val _events = MutableSharedFlow<RoomEvent>(extraBufferCapacity = 16)

    private var partnerScript: Job? = null
    private val partnerCards get() = demoMode.demoHarmonyResult.cards.drop(3)

    override suspend fun postTarotResult(
        input: TarotInputDto,
        topicNumber: Int
    ): Result<TarotOutputDto> {
        delay(800)
        return Result.success(resultForTopic(topicNumber))
    }

    override suspend fun getTarotById(tarotId: String): Result<TarotOutputDto> {
        return Result.success(findById(tarotId) ?: demoMode.demoSingleResult)
    }

    override suspend fun getTarotList(tarotIds: List<String>): Result<List<TarotOutputDto>> {
        return Result.success(tarotIds.mapNotNull { findById(it) })
    }

    override fun connect() { /* no-op */ }

    override fun disconnect() {
        partnerScript?.cancel()
        partnerScript = null
    }

    override fun observeRoomEvents(): Flow<RoomEvent> = _events.asSharedFlow()

    override suspend fun createRoom() {
        scope.launch {
            delay(1500)
            _events.emit(RoomEvent.Created(roomId = demoMode.demoHarmonyResult.tarotId))
        }
    }

    override suspend fun joinRoom(roomId: String, nickname: String) {
        scope.launch {
            delay(1500)
            _events.emit(RoomEvent.Joined(nicknames = listOf(nickname, DEMO_PARTNER_NICKNAME)))
        }
    }

    override suspend fun signalStart(roomId: String, nickname: String) {
        scheduleAfter(700) { _events.emit(RoomEvent.PartnerNext()) }
    }

    override suspend fun selectCard(roomId: String, nickname: String, cardNumber: Int) {
        val pickIndex = nextPartnerPickIndex
        nextPartnerPickIndex = (pickIndex + 1).coerceAtMost(partnerCards.lastIndex)
        scheduleAfter(900) {
            _events.emit(RoomEvent.PartnerNext(cardNumber = partnerCards[pickIndex]))
        }
    }

    override suspend fun requestMatchResult(input: MatchTarotInputDto): Result<TarotOutputDto> {
        delay(1200)
        _events.emit(RoomEvent.ResultPrepared(tarotId = demoMode.demoHarmonyResult.tarotId))
        return Result.success(demoMode.demoHarmonyResult)
    }

    override suspend fun confirmResultReceived(roomId: String, tarotId: String) { /* no-op */ }

    override suspend fun exitRoom(roomId: String) {
        partnerScript?.cancel()
        partnerScript = null
        nextPartnerPickIndex = 0
    }

    // ---------------- internals ----------------

    private var nextPartnerPickIndex = 0

    private fun resultForTopic(topicNumber: Int): TarotOutputDto {
        if (topicNumber == 5) return demoMode.demoHarmonyResult
        val id = TOPIC_TAROT_IDS[topicNumber] ?: demoMode.demoSingleResult.tarotId
        return demoMode.demoSingleResult.copy(tarotId = id, tarotType = topicNumber)
    }

    private fun findById(tarotId: String): TarotOutputDto? {
        if (tarotId == demoMode.demoHarmonyResult.tarotId) return demoMode.demoHarmonyResult
        val topicNumber = TOPIC_TAROT_IDS.entries.firstOrNull { it.value == tarotId }?.key ?: return null
        return demoMode.demoSingleResult.copy(tarotId = tarotId, tarotType = topicNumber)
    }

    private fun scheduleAfter(delayMs: Long, block: suspend () -> Unit) {
        partnerScript = scope.launch {
            delay(delayMs)
            block()
        }
    }

    private companion object {
        const val DEMO_PARTNER_NICKNAME = "아지"
        val TOPIC_TAROT_IDS = mapOf(
            0 to "0",
            1 to "demo-study",
            2 to "demo-dream",
            3 to "demo-job",
            4 to "demo-today",
        )
    }
}
