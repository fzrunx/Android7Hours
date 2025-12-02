package com.sesac.common.repository

import com.sesac.common.model.webrtc.SignalingEvent
import com.sesac.common.model.webrtc.WebRTCSessionState
import kotlinx.coroutines.flow.Flow
import org.webrtc.IceCandidate
import org.webrtc.SessionDescription
import org.webrtc.VideoTrack

interface WebRTCRepository {
    fun initialize(myUserId: String, targetUserId: String): Unit
    fun observeSignalingEvents(): Flow<SignalingEvent>
    fun observeSessionState(): Flow<WebRTCSessionState>
    fun observeRemoteVideoTrack(): Flow<VideoTrack?>
    fun observeLocalVideoTrack(): Flow<VideoTrack?>
    suspend fun sendOffer(): Unit
    suspend fun sendAnswer(sessionDescription: SessionDescription): Unit
    fun sendIceCandidate(iceCandidate: IceCandidate)
    fun closeSession()
}