package com.sesac.data.source.webrtc

import android.content.Context
import android.util.Log
import org.webrtc.AudioTrack
import org.webrtc.Camera2Enumerator
import org.webrtc.EglBase
import org.webrtc.IceCandidate
import org.webrtc.MediaConstraints
import org.webrtc.PeerConnection
import org.webrtc.PeerConnectionFactory
import org.webrtc.RtpTransceiver
import org.webrtc.SessionDescription
import org.webrtc.SurfaceTextureHelper
import org.webrtc.VideoTrack
import javax.inject.Inject
import kotlin.coroutines.Continuation
import kotlin.coroutines.suspendCoroutine

class WebRTCClient @Inject constructor(
    private val context: Context,
    private val eglBase: EglBase,
    private val peerConnectionFactory: PeerConnectionFactory
) {
    private val iceServer = listOf(
        PeerConnection.IceServer.builder("stun:stun.l.google.com:19302").createIceServer()
    )

    private var peerConnection: PeerConnection? = null

    // Offer/Answer constraints 설정 - mandatory로 변경
    private val offerAnswerConstraints = MediaConstraints().apply {
        mandatory.add(MediaConstraints.KeyValuePair("OfferToReceiveAudio", "true"))
        mandatory.add(MediaConstraints.KeyValuePair("OfferToReceiveVideo", "true"))
    }

    private var localAudioTrack: AudioTrack? = null
    private var localVideoTrack: VideoTrack? = null
    private var videoCapturer: org.webrtc.CameraVideoCapturer? = null

    fun getPeerConnection(): PeerConnection? {
        return peerConnection
    }

    fun initializePeerConnection(
        onIceCandidate: (IceCandidate) -> Unit,
        onRemoteTrack: (VideoTrack) -> Unit,
        onLocalTrack: (VideoTrack) -> Unit
    ) {
        peerConnection = peerConnectionFactory.createPeerConnection(
            iceServer,
            object : PeerConnection.Observer {
                override fun onIceCandidate(candidate: IceCandidate?) {
                    candidate?.let {
                        Log.d("WebRTCClient", "onIceCandidate: ${it.sdpMid} ${it.sdpMLineIndex}")
                        onIceCandidate(it)
                    }
                }

                override fun onTrack(transceiver: org.webrtc.RtpTransceiver?) {
                    super.onTrack(transceiver)
                    val track = transceiver?.receiver?.track()
                    Log.d("WebRTCClient", "onTrack called: track=${track?.kind()}")
                    if (track is VideoTrack) {
                        Log.d("WebRTCClient", "Remote video track received, enabled=${track.enabled()}")
                        onRemoteTrack(track)
                    }
                }

                override fun onSignalingChange(state: PeerConnection.SignalingState?) {
                    Log.d("WebRTCClient", "onSignalingChange: $state")
                }

                override fun onIceConnectionChange(state: PeerConnection.IceConnectionState?) {
                    Log.d("WebRTCClient", "onIceConnectionChange: $state")
                }

                override fun onIceGatheringChange(state: PeerConnection.IceGatheringState?) {
                    Log.d("WebRTCClient", "onIceGatheringChange: $state")
                }

                override fun onIceConnectionReceivingChange(receiving: Boolean) {
                    Log.d("WebRTCClient", "onIceConnectionReceivingChange: $receiving")
                }

                override fun onIceCandidatesRemoved(p0: Array<out IceCandidate>?) {}
                override fun onAddStream(p0: org.webrtc.MediaStream?) {}
                override fun onRemoveStream(p0: org.webrtc.MediaStream?) {}
                override fun onDataChannel(p0: org.webrtc.DataChannel?) {}
                override fun onRenegotiationNeeded() {
                    Log.d("WebRTCClient", "onRenegotiationNeeded")
                }
                override fun onAddTrack(p0: org.webrtc.RtpReceiver?, p1: Array<out org.webrtc.MediaStream>?) {}
            }
        )

        startLocalStream(onLocalTrack)
    }

    private fun startLocalStream(onLocalTrack: (VideoTrack) -> Unit) {
        // Audio Track 생성
        val audioSource = peerConnectionFactory.createAudioSource(MediaConstraints())
        localAudioTrack = peerConnectionFactory.createAudioTrack("local_audio_track", audioSource)

        // Video Track 생성
        val cameraEnumerator = Camera2Enumerator(context)
        val deviceName = cameraEnumerator.deviceNames.find { cameraEnumerator.isFrontFacing(it) }
            ?: cameraEnumerator.deviceNames.first()
        
        Log.d("WebRTCClient", "Selected camera deviceName: $deviceName") // 선택된 카메라 로그 추가

        videoCapturer = cameraEnumerator.createCapturer(deviceName, null)

        // videoCapturer가 null일 경우 에러 처리
        if (videoCapturer == null) {
            Log.e("WebRTCClient", "Failed to create video capturer for device: $deviceName")
            return
        }

        val surfaceTextureHelper = SurfaceTextureHelper.create("CaptureThread", eglBase.eglBaseContext)
        val videoSource = peerConnectionFactory.createVideoSource(videoCapturer!!.isScreencast)
        videoCapturer!!.initialize(surfaceTextureHelper, context, videoSource.capturerObserver)

        // 수정: width와 height 순서 수정 (640x480)
        videoCapturer!!.startCapture(640, 480, 30)

        localVideoTrack = peerConnectionFactory.createVideoTrack("local_video_track", videoSource)

        // CRITICAL: addTrack 사용 (addTransceiver 대신) - 이것이 핵심!
        // MediaStream을 생성하고 트랙을 추가
        val streamId = "local_stream"

        localAudioTrack?.let {
            peerConnection?.addTrack(it, listOf(streamId))
            Log.d("WebRTCClient", "Audio track added to stream $streamId")
        }

        localVideoTrack?.let {
            peerConnection?.addTrack(it, listOf(streamId))
            Log.d("WebRTCClient", "Video track added to stream $streamId")
            onLocalTrack(it)
        }
    }

    suspend fun createOffer(): SessionDescription {
        return suspendCoroutine<SessionDescription> { continuation ->
            peerConnection?.createOffer(
                CustomSdpObserver(continuation as Continuation<Any?>),
                offerAnswerConstraints // constraints 추가
            )
        } as SessionDescription
    }

    suspend fun createAnswer(): SessionDescription {
        return suspendCoroutine<SessionDescription> { continuation ->
            peerConnection?.createAnswer(
                CustomSdpObserver(continuation as Continuation<Any?>),
                offerAnswerConstraints // constraints 추가
            )
        } as SessionDescription
    }

    suspend fun setRemoteDescription(sessionDescription: SessionDescription) {
        suspendCoroutine<Unit> { continuation -> // Unit으로 명시하여 set 작업은 반환값이 없음을 나타냄
            peerConnection?.setRemoteDescription(
                CustomSdpObserver(continuation as Continuation<Any?>),
                sessionDescription
            )
        }
    }

    suspend fun setLocalDescription(sessionDescription: SessionDescription) {
        suspendCoroutine<Unit> { continuation -> // Unit으로 명시하여 set 작업은 반환값이 없음을 나타냄
            peerConnection?.setLocalDescription(
                CustomSdpObserver(continuation as Continuation<Any?>),
                sessionDescription
            )
        }
    }

    fun addIceCandidate(iceCandidate: IceCandidate) {
        Log.d("WebRTCClient", "Adding ICE candidate: ${iceCandidate.sdpMid}")
        peerConnection?.addIceCandidate(iceCandidate)
    }

    fun close() {
        videoCapturer?.stopCapture()
        videoCapturer?.dispose()
        localVideoTrack?.dispose()
        localAudioTrack?.dispose()
        peerConnection?.close()
        peerConnection = null
    }
}