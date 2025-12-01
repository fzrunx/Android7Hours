package com.sesac.data.repository

import android.util.Log
import com.sesac.data.source.remote.SignalingClient
import com.sesac.data.source.remote.WebRTCClient
import com.sesac.common.model.webrtc.SignalingEvent
import com.sesac.common.model.webrtc.WebRTCSessionState
import com.sesac.common.repository.WebRTCRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import org.webrtc.IceCandidate
import org.webrtc.SessionDescription
import org.webrtc.VideoTrack
import javax.inject.Inject

class WebRTCRepositoryImpl @Inject constructor(
    private val signalingClient: SignalingClient,
    private val webRTCClient: WebRTCClient,
) : WebRTCRepository {

    private val repositoryScope = CoroutineScope(SupervisorJob() + Dispatchers.Default)

    private val _sessionState = MutableStateFlow<WebRTCSessionState>(WebRTCSessionState.Idle)
    private val _remoteVideoTrack = MutableSharedFlow<VideoTrack?>()

    private var myId: String? = null
    private var targetId: String? = null

    companion object {
        private const val TAG = "WebRTCRepositoryImpl"
    }

    override fun initialize(myId: String, targetId: String) {
        this.myId = myId
        this.targetId = targetId

        webRTCClient.initializePeerConnection(
            onIceCandidate = { iceCandidate ->
                signalingClient.sendIceCandidate(iceCandidate, targetId)
            },
            onRemoteTrack = { videoTrack ->
                repositoryScope.launch {
                    _remoteVideoTrack.emit(videoTrack)
                }
            }
        )
    }

    override fun observeSignalingEvents(): Flow<SignalingEvent> = signalingClient.observeEvents()

    override fun observeSessionState(): Flow<WebRTCSessionState> = _sessionState.asStateFlow()

    override fun observeRemoteVideoTrack(): Flow<VideoTrack?> = _remoteVideoTrack

    override suspend fun sendOffer() {
        Log.d(TAG, "sendOffer called")
        val offer = webRTCClient.createOffer()
        webRTCClient.setLocalDescription(offer)
        signalingClient.sendOffer(offer, targetId!!)
        _sessionState.value = WebRTCSessionState.SendingOffer
    }

    override suspend fun sendAnswer(sessionDescription: SessionDescription) {
        Log.d(TAG, "sendAnswer called")
        webRTCClient.setRemoteDescription(sessionDescription)
        val answer = webRTCClient.createAnswer()
        webRTCClient.setLocalDescription(answer)
        signalingClient.sendAnswer(answer, targetId!!)
        _sessionState.value = WebRTCSessionState.Connected
    }

    override fun sendIceCandidate(iceCandidate: IceCandidate) {
        webRTCClient.addIceCandidate(iceCandidate)
    }

    override fun closeSession() {
        Log.d(TAG, "closeSession called")
        webRTCClient.close()
        _sessionState.value = WebRTCSessionState.Closed
    }
}
