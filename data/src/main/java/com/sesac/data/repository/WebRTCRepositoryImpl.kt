package com.sesac.data.repository

import android.util.Log
import com.sesac.common.model.webrtc.SignalingEvent
import com.sesac.common.model.webrtc.WebRTCSessionState
import com.sesac.common.repository.WebRTCRepository
import com.sesac.data.source.webrtc.SignalingClient
import com.sesac.data.source.webrtc.WebRTCClient
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

    private var myUserId: String? = null
    private var targetUserId: String? = null

    companion object {
        private const val TAG = "WebRTCRepositoryImpl"
    }

    override fun initialize(myUserId: String, targetUserId: String) {
        this.myUserId = myUserId
        this.targetUserId = targetUserId

        // PeerConnection은 한 번만 초기화하도록 합니다.
        if (webRTCClient.getPeerConnection() == null) {
            Log.d(TAG, "Initializing PeerConnection for the first time.")
            webRTCClient.initializePeerConnection(
                onIceCandidate = { iceCandidate ->
                    // 람다가 캡처한 값이 아닌, 항상 현재의 targetUserId 값을 참조하도록 수정
                    this.targetUserId?.let { currentTargetId ->
                        if (currentTargetId.isNotEmpty()) {
                            signalingClient.sendIceCandidate(iceCandidate, currentTargetId)
                        }
                    }
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
        } else {
            Log.d(TAG, "PeerConnection already initialized. Updating user IDs only.")
            // 이미 초기화되어 있다면, 내부 user ID만 업데이트합니다.
            // (WebRTCClient 내부에서는 myUserId와 targetUserId를 직접 사용하지 않고,
            // signalingClient에 전달할 때만 사용되므로 이 부분만 업데이트하면 충분합니다.)
        }
    }

    override fun observeSignalingEvents(): Flow<SignalingEvent> = signalingClient.observeEvents()

    override fun observeSessionState(): Flow<WebRTCSessionState> = _sessionState.asStateFlow()

    override fun observeRemoteVideoTrack(): Flow<VideoTrack?> = _remoteVideoTrack

    override fun observeLocalVideoTrack(): Flow<VideoTrack?> = _localVideoTrack // 함수 구현

    override suspend fun sendOffer() {
        Log.d(TAG, "sendOffer called")
        Log.d(TAG, "Attempting to create offer...")
        val offer = webRTCClient.createOffer()
        Log.d(TAG, "Offer created: type=${offer.type}, sdp starts with: ${offer.description.take(50)}...")
        Log.d(TAG, "Attempting to set local description...")
        webRTCClient.setLocalDescription(offer)
        Log.d(TAG, "Local description set successfully.")
        Log.d(TAG, "Attempting to send offer via signaling client...")
        signalingClient.sendOffer(offer, targetUserId!!)
        Log.d(TAG, "Offer sent via signaling client. Updating session state.")
        _sessionState.value = WebRTCSessionState.SendingOffer
    }

    override suspend fun sendAnswer(sessionDescription: SessionDescription) {
        Log.d(TAG, "sendAnswer called")

        // Remote Description 설정
        webRTCClient.setRemoteDescription(sessionDescription)

        // Answer 생성
        val answer = webRTCClient.createAnswer()

        // CRITICAL: Answer SDP 수정 - inactive를 sendrecv로 변경
        val modifiedSdp = answer.description.replace("a=inactive", "a=sendrecv")
        val modifiedAnswer = SessionDescription(SessionDescription.Type.ANSWER, modifiedSdp)

        Log.d(TAG, "Modified answer SDP: ${modifiedSdp.take(500)}...")

        // Local Description 설정
        webRTCClient.setLocalDescription(modifiedAnswer)

        // Answer 전송
        signalingClient.sendAnswer(modifiedAnswer, targetUserId!!)
        _sessionState.value = WebRTCSessionState.Connected
    }

    override suspend fun setRemoteDescription(sessionDescription: SessionDescription) {
        webRTCClient.setRemoteDescription(sessionDescription)
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
