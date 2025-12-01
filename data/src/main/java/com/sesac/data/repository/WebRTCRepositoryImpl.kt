package com.sesac.data.repository

import android.util.Log
import com.sesac.data.source.webrtc.SignalingClient
import com.sesac.data.source.webrtc.WebRTCClient
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
    private val _localVideoTrack = MutableSharedFlow<VideoTrack?>() // 로컬 비디오 트랙 Flow 추가

    private var myUserName: String? = null
    private var targetUser: String? = null

    companion object {
        private const val TAG = "WebRTCRepositoryImpl"
    }

    override fun initialize(myUserName: String, targetUser: String) {
        this.myUserName = myUserName
        this.targetUser = targetUser

        webRTCClient.initializePeerConnection(
            onIceCandidate = { iceCandidate ->
                signalingClient.sendIceCandidate(iceCandidate, targetUser)
            },
            onRemoteTrack = { videoTrack ->
                repositoryScope.launch {
                    _remoteVideoTrack.emit(videoTrack)
                }
            },
            onLocalTrack = { videoTrack -> // onLocalTrack 콜백 처리
                repositoryScope.launch {
                    _localVideoTrack.emit(videoTrack)
                }
            }
        )
    }

    override fun observeSignalingEvents(): Flow<SignalingEvent> = signalingClient.observeEvents()

    override fun observeSessionState(): Flow<WebRTCSessionState> = _sessionState.asStateFlow()

    override fun observeRemoteVideoTrack(): Flow<VideoTrack?> = _remoteVideoTrack

    override fun observeLocalVideoTrack(): Flow<VideoTrack?> = _localVideoTrack // 함수 구현

    override suspend fun sendOffer() {
        Log.d(TAG, "sendOffer called")
        val offer = webRTCClient.createOffer()
        webRTCClient.setLocalDescription(offer)
        signalingClient.sendOffer(offer, targetUser!!)
        _sessionState.value = WebRTCSessionState.SendingOffer
    }

    override suspend fun sendAnswer(sessionDescription: SessionDescription) {
        Log.d(TAG, "sendAnswer called")
        webRTCClient.setRemoteDescription(sessionDescription)
        val answer = webRTCClient.createAnswer()
        webRTCClient.setLocalDescription(answer)
        signalingClient.sendAnswer(answer, targetUser!!)
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
