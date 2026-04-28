package com.fourleafclover.tarot.data.repository

import android.util.Log
import com.fourleafclover.tarot.BuildConfig
import com.fourleafclover.tarot.data.MatchTarotInputDto
import com.fourleafclover.tarot.data.TarotIdsInputDto
import com.fourleafclover.tarot.data.TarotInputDto
import com.fourleafclover.tarot.data.TarotOutputDto
import com.fourleafclover.tarot.network.TarotService
import io.socket.client.IO
import io.socket.client.Socket
import io.socket.emitter.Emitter
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.suspendCancellableCoroutine
import org.json.JSONArray
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import javax.inject.Singleton
import kotlin.coroutines.resume

@Singleton
class RemoteTarotRepository(
    private val tarotService: TarotService
) : TarotRepository {

    private val _events = MutableSharedFlow<RoomEvent>(extraBufferCapacity = 16)

    private var socket: Socket? = null
    private val listenersAttached = mutableListOf<Pair<String, Emitter.Listener>>()

    override suspend fun postTarotResult(
        input: TarotInputDto,
        topicNumber: Int
    ): Result<TarotOutputDto> = awaitCall {
        tarotService.postTarotResult(input, pathFor(topicNumber))
    }

    override suspend fun getTarotById(tarotId: String): Result<TarotOutputDto> {
        val listResult = getTarotList(listOf(tarotId))
        return listResult.mapCatching { it.firstOrNull() ?: error("empty response") }
    }

    override suspend fun getTarotList(tarotIds: List<String>): Result<List<TarotOutputDto>> =
        awaitCall { tarotService.getMyTarotResult(TarotIdsInputDto(ArrayList(tarotIds))) }

    override fun connect() {
        if (socket?.connected() == true) return
        try {
            val options = IO.Options().apply {
                reconnection = false
                reconnectionAttempts = 5
                reconnectionDelay = 2000
                forceNew = false
                transports = arrayOf("polling", "websocket")
            }
            socket = IO.socket(BuildConfig.BASE_URL, options).also { sock ->
                sock.on(Socket.EVENT_CONNECT) { Log.d(TAG, "connect") }
                sock.on(Socket.EVENT_DISCONNECT) { Log.d(TAG, "disconnect") }
                sock.on(Socket.EVENT_CONNECT_ERROR) { args ->
                    Log.d(TAG, "connect error ${args.contentDeepToString()}")
                }
                attachRoomListeners(sock)
                sock.connect()
            }
        } catch (e: Exception) {
            Log.e(TAG, "connect failed", e)
            emit(RoomEvent.Error(e.message ?: "connect failed"))
        }
    }

    override fun disconnect() {
        socket?.let { sock ->
            listenersAttached.forEach { (event, listener) -> sock.off(event, listener) }
            listenersAttached.clear()
            sock.close()
        }
        socket = null
    }

    override fun observeRoomEvents(): Flow<RoomEvent> = _events.asSharedFlow()

    override suspend fun createRoom() {
        socket?.emit("create")
    }

    override suspend fun joinRoom(roomId: String, nickname: String) {
        socket?.emit("join", JSONObject().apply {
            put("nickname", nickname)
            put("roomId", roomId)
        })
    }

    override suspend fun signalStart(roomId: String, nickname: String) {
        socket?.emit("start", JSONObject().apply {
            put("nickname", nickname)
            put("roomId", roomId)
        })
    }

    override suspend fun selectCard(roomId: String, nickname: String, cardNumber: Int) {
        socket?.emit("cardSelect", JSONObject().apply {
            put("nickname", nickname)
            put("roomId", roomId)
            put("cardNum", cardNumber)
        })
    }

    override suspend fun requestMatchResult(input: MatchTarotInputDto): Result<TarotOutputDto> =
        awaitCall { tarotService.getMatchResult(input) }

    override suspend fun confirmResultReceived(roomId: String, tarotId: String) {
        socket?.emit("resultReceived", JSONObject().apply {
            put("roomId", roomId)
            put("tarotId", tarotId)
        })
    }

    override suspend fun exitRoom(roomId: String) {
        socket?.emit("exit", JSONObject().apply { put("roomId", roomId) })
    }

    // ---------------- internals ----------------

    private fun attachRoomListeners(sock: Socket) {
        on(sock, "createComplete") { args ->
            val roomId = JSONObject(args[0].toString()).getString("roomId")
            emit(RoomEvent.Created(roomId))
        }
        on(sock, "joinComplete") { args ->
            val response = JSONObject(args[0].toString())
            val arr: JSONArray = response.getJSONArray("nickname")
            val nicknames = (0 until arr.length()).map { arr.getString(it) }
            emit(RoomEvent.Joined(nicknames))
        }
        on(sock, "next") { args ->
            val cardNum = if (args.isNotEmpty()) {
                runCatching { JSONObject(args[0].toString()).getString("cardNum").toInt() }.getOrNull()
            } else null
            emit(RoomEvent.PartnerNext(cardNum))
        }
        on(sock, "resultPrepared") { args ->
            val tarotId = runCatching { JSONObject(args[0].toString()).getString("tarotId") }.getOrNull().orEmpty()
            emit(RoomEvent.ResultPrepared(tarotId))
        }
        on(sock, "exit") {
            emit(RoomEvent.PartnerExited)
        }
    }

    private fun on(sock: Socket, event: String, body: (Array<Any>) -> Unit) {
        val listener = Emitter.Listener { args -> body(args) }
        sock.on(event, listener)
        listenersAttached += event to listener
    }

    private fun emit(event: RoomEvent) {
        // SharedFlow.emit suspends, but we need to emit from socket thread.
        // tryEmit relies on extraBufferCapacity which we set to 16.
        val ok = _events.tryEmit(event)
        if (!ok) {
            // Fall back to blocking emit if buffer exhausted; should not happen during normal usage.
            runBlocking { _events.emit(event) }
        }
    }

    private suspend fun <T> awaitCall(factory: () -> Call<T>): Result<T> =
        suspendCancellableCoroutine { cont ->
            val call = factory()
            cont.invokeOnCancellation { call.cancel() }
            call.enqueue(object : Callback<T> {
                override fun onResponse(call: Call<T>, response: Response<T>) {
                    val body = response.body()
                    if (body == null) cont.resume(Result.failure(IllegalStateException("response body null")))
                    else cont.resume(Result.success(body))
                }
                override fun onFailure(call: Call<T>, t: Throwable) {
                    cont.resume(Result.failure(t))
                }
            })
        }

    private fun pathFor(topicNumber: Int): String = when (topicNumber) {
        0 -> "love"
        1 -> "study"
        2 -> "dream"
        3 -> "job"
        4 -> "today"
        else -> ""
    }

    private companion object {
        const val TAG = "RemoteTarotRepo"
    }
}
