package com.sesac.data.source.remote

import android.util.Log
import com.sesac.common.model.webrtc.SignalingEvent
import com.sesac.data.BuildConfig
import com.squareup.moshi.Moshi
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.cancel
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.launch
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.Response
import okhttp3.WebSocket
import okhttp3.WebSocketListener
import org.json.JSONObject
import org.webrtc.IceCandidate
import org.webrtc.SessionDescription
import javax.inject.Inject

class SignalingClient @Inject constructor(
    private val httpClient: OkHttpClient,
    private val moshi: Moshi
) {
    private val signalingScope = CoroutineScope(SupervisorJob() + Dispatchers.Default)
    private var webSocket: WebSocket? = null

    companion object {
        private const val TAG = "SignalingClient"
        private val SERVER_URL = "ws://${BuildConfig.SERVER_URL}/ws/call/"
    }

    fun observeEvents(): Flow<SignalingEvent> = callbackFlow {
        val webSocketListener = object : WebSocketListener() {
            override fun onOpen(webSocket: WebSocket, response: Response) {
                super.onOpen(webSocket, response)
                Log.d(TAG, "WebSocket connection opened.")
            }

            override fun onMessage(webSocket: WebSocket, text: String) {
                super.onMessage(webSocket, text)
                Log.d(TAG, "Received message: $text")
                try {
                    val json = JSONObject(text)
                    val messageJson = json.getJSONObject("message")
                    when (val type = messageJson.getString("type")) {
                        "offer" -> {
                            val sdp = messageJson.getString("sdp")
                            trySend(SignalingEvent.OfferReceived(SessionDescription(SessionDescription.Type.OFFER, sdp)))
                        }
                        "answer" -> {
                            val sdp = messageJson.getString("sdp")
                            trySend(SignalingEvent.AnswerReceived(SessionDescription(SessionDescription.Type.ANSWER, sdp)))
                        }
                        "ice_candidate" -> {
                            val candidateJson = messageJson.getJSONObject("candidate")
                            val sdpMid = candidateJson.getString("sdpMid")
                            val sdpMLineIndex = candidateJson.getInt("sdpMLineIndex")
                            val sdp = candidateJson.getString("candidate")
                            trySend(SignalingEvent.IceCandidateReceived(IceCandidate(sdpMid, sdpMLineIndex, sdp)))
                        }
                        else -> {
                            Log.w(TAG, "Unknown message type: $type")
                        }
                    }
                } catch (e: Exception) {
                    Log.e(TAG, "Error parsing message", e)
                }
            }

            override fun onClosing(webSocket: WebSocket, code: Int, reason: String) {
                super.onClosing(webSocket, code, reason)
                Log.d(TAG, "WebSocket closing: $code / $reason")
            }

            override fun onClosed(webSocket: WebSocket, code: Int, reason: String) {
                super.onClosed(webSocket, code, reason)
                Log.d(TAG, "WebSocket closed: $code / $reason")
            }

            override fun onFailure(webSocket: WebSocket, t: Throwable, response: Response?) {
                super.onFailure(webSocket, t, response)
                Log.e(TAG, "WebSocket failure", t)
                close(t) // Close the flow on failure
            }
        }

        Log.d(TAG, "Initializing WebSocket connection to $SERVER_URL")
        val request = Request.Builder().url(SERVER_URL).build()
        webSocket = httpClient.newWebSocket(request, webSocketListener)

        awaitClose {
            Log.d(TAG, "Closing WebSocket connection.")
            webSocket?.close(1000, "Client closing")
            signalingScope.cancel()
        }
    }

    fun sendOffer(sdp: SessionDescription, targetUserId: String) {
        val message = createBaseMessage(targetUserId).apply {
            put("type", "offer")
            put("sdp", sdp.description)
        }
        sendMessage(message)
    }

    fun sendAnswer(sdp: SessionDescription, targetUserId: String) {
        val message = createBaseMessage(targetUserId).apply {
            put("type", "answer")
            put("sdp", sdp.description)
        }
        sendMessage(message)
    }

    fun sendIceCandidate(candidate: IceCandidate, targetUserId: String) {
        val candidateJson = JSONObject().apply {
            put("candidate", candidate.sdp)
            put("sdpMid", candidate.sdpMid)
            put("sdpMLineIndex", candidate.sdpMLineIndex)
        }
        val message = createBaseMessage(targetUserId).apply {
            put("type", "ice_candidate")
            put("candidate", candidateJson)
        }
        sendMessage(message)
    }

    private fun createBaseMessage(targetUserId: String): JSONObject {
        return JSONObject().apply {
            put("target_user_id", targetUserId)
        }
    }

    private fun sendMessage(message: JSONObject) {
        signalingScope.launch {
            val messageString = message.toString()
            Log.d(TAG, "Sending message: $messageString")
            webSocket?.send(messageString)
        }
    }
}
