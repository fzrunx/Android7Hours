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
import org.webrtc.SessionDescription
import org.webrtc.SurfaceTextureHelper
import org.webrtc.VideoTrack
import javax.inject.Inject

class WebRTCClient @Inject constructor(
    private val context: Context,
    private val eglBase: EglBase,
    private val peerConnectionFactory: PeerConnectionFactory // Hilt로 주입받도록 변경
) {
    private val iceServer = listOf(
        PeerConnection.IceServer.builder("stun:stun.l.google.com:19302").createIceServer()
    )

    private var peerConnection: PeerConnection? = null
    private val mediaConstraints = MediaConstraints()

    private var localAudioTrack: AudioTrack? = null
    private var localVideoTrack: VideoTrack? = null

    fun initializePeerConnection(
        onIceCandidate: (IceCandidate) -> Unit,
        onRemoteTrack: (VideoTrack) -> Unit,
        onLocalTrack: (VideoTrack) -> Unit // 로컬 트랙 콜백 추가
    ) {
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
                    onRemoteTrack(track)
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
        startLocalStream(onLocalTrack) // 콜백 전달
    }

    private fun startLocalStream(onLocalTrack: (VideoTrack) -> Unit) {
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
        localVideoTrack?.let {
            peerConnection?.addTrack(it)
            onLocalTrack(it) // 로컬 비디오 트랙 생성 후 콜백 호출
        }
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
