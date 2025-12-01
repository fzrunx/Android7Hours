package com.sesac.data.source.remote

import android.content.Context
import android.util.Log
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.launch
import org.webrtc.AudioTrack
import org.webrtc.Camera2Enumerator
import org.webrtc.DefaultVideoDecoderFactory
import org.webrtc.DefaultVideoEncoderFactory
import org.webrtc.EglBase
import org.webrtc.IceCandidate
import org.webrtc.MediaConstraints
import org.webrtc.PeerConnection
import org.webrtc.PeerConnectionFactory
import org.webrtc.SessionDescription
import org.webrtc.SurfaceTextureHelper
import org.webrtc.VideoTrack
import javax.inject.Inject

class WebRTCClient @Inject constructor(
    private val context: Context,
    private val eglBase: EglBase,
    private val peerConnectionFactory: PeerConnectionFactory // Hilt로 주입받도록 변경
) {
    private val clientScope = CoroutineScope(SupervisorJob() + Dispatchers.Default)

    // PeerConnectionFactory는 이제 주입받으므로, lazy 초기화는 필요 없음
    // private val peerConnectionFactory: PeerConnectionFactory by lazy { ... }

    private val iceServer = listOf(
        PeerConnection.IceServer.builder("stun:stun.l.google.com:19302").createIceServer()
    )

    private var peerConnection: PeerConnection? = null
    private val mediaConstraints = MediaConstraints()

    private var localAudioTrack: AudioTrack? = null
    private var localVideoTrack: VideoTrack? = null
    val remoteVideoTrackFlow = MutableSharedFlow<VideoTrack>() // private에서 public으로 변경

    fun initializePeerConnection(onIceCandidate: (IceCandidate) -> Unit, onRemoteTrack: (VideoTrack) -> Unit) {
        // PeerConnectionFactory는 이미 초기화된 상태이므로 여기서 다시 빌더 호출할 필요 없음
        peerConnection = peerConnectionFactory.createPeerConnection(iceServer, object : PeerConnection.Observer {
            override fun onIceCandidate(candidate: IceCandidate?) {
                candidate?.let {
                    Log.d("WebRTCClient", "onIceCandidate: $it")
                    onIceCandidate(it)
                }

            }
            override fun onTrack(transceiver: org.webrtc.RtpTransceiver?) {
                super.onTrack(transceiver)
                val track = transceiver?.receiver?.track()
                if (track is VideoTrack) {
                    Log.d("WebRTCClient", "Remote video track received")
                    clientScope.launch {
//                        _remoteVideoTrack.emit(track) // MutableSharedFlow를 통해 방출
                        remoteVideoTrackFlow.emit(track)
                    }
                }
            }
            override fun onSignalingChange(p0: PeerConnection.SignalingState?) {}
            override fun onIceConnectionChange(p0: PeerConnection.IceConnectionState?) {}
            override fun onIceConnectionReceivingChange(p0: Boolean) {}
            override fun onIceGatheringChange(p0: PeerConnection.IceGatheringState?) {}
            override fun onIceCandidatesRemoved(p0: Array<out IceCandidate>?) {}
            override fun onAddStream(p0: org.webrtc.MediaStream?) {}
            override fun onRemoveStream(p0: org.webrtc.MediaStream?) {}
            override fun onDataChannel(p0: org.webrtc.DataChannel?) {}
            override fun onRenegotiationNeeded() {}
            override fun onAddTrack(p0: org.webrtc.RtpReceiver?, p1: Array<out org.webrtc.MediaStream>?) {}
        })
        startLocalStream()
    }

    private fun startLocalStream() {
        val audioSource = peerConnectionFactory.createAudioSource(MediaConstraints())
        localAudioTrack = peerConnectionFactory.createAudioTrack("local_audio_track", audioSource)

        val cameraEnumerator = Camera2Enumerator(context)
        val deviceName = cameraEnumerator.deviceNames.find { cameraEnumerator.isFrontFacing(it) }
            ?: cameraEnumerator.deviceNames.first()
        val videoCapturer = cameraEnumerator.createCapturer(deviceName, null)

        val surfaceTextureHelper = SurfaceTextureHelper.create("CaptureThread", eglBase.eglBaseContext)
        val videoSource = peerConnectionFactory.createVideoSource(videoCapturer.isScreencast)
        videoCapturer.initialize(surfaceTextureHelper, context, videoSource.capturerObserver)
        videoCapturer.startCapture(480, 640, 30)

        localVideoTrack = peerConnectionFactory.createVideoTrack("local_video_track", videoSource)
        localAudioTrack?.let { peerConnection?.addTrack(it) }
        localVideoTrack?.let { peerConnection?.addTrack(it) }
    }

    suspend fun createOffer(): SessionDescription {
        val sdpObserver = SimpleSdpObserver()
        peerConnection?.createOffer(sdpObserver, mediaConstraints)
        return sdpObserver.await()
    }

    suspend fun createAnswer(): SessionDescription {
        val sdpObserver = SimpleSdpObserver()
        peerConnection?.createAnswer(sdpObserver, mediaConstraints)
        return sdpObserver.await()
    }

    suspend fun setRemoteDescription(sessionDescription: SessionDescription) {
        val sdpObserver = SimpleSdpObserver()
        peerConnection?.setRemoteDescription(sdpObserver, sessionDescription)
        sdpObserver.await()
    }

    suspend fun setLocalDescription(sessionDescription: SessionDescription) {
        val sdpObserver = SimpleSdpObserver()
        peerConnection?.setLocalDescription(sdpObserver, sessionDescription)
        sdpObserver.await()
    }

    fun addIceCandidate(iceCandidate: IceCandidate) {
        peerConnection?.addIceCandidate(iceCandidate)
    }

    fun close() {
        peerConnection?.close()
        peerConnection = null
    }
}
