package com.sesac.common.model.webrtc

import org.webrtc.IceCandidate
import org.webrtc.SessionDescription

/**
 * WebRTC 시그널링 과정에서 발생하는 이벤트를 정의하는 Sealed Class
 */
sealed class SignalingEvent {
    /**
     * 상대방으로부터 통화 제안(Offer)을 받았을 때 발생하는 이벤트
     * @param sdp 통화 제안에 대한 상세 정보를 담은 SessionDescription
     * @param fromUserId Offer를 보낸 사용자의 ID
     */
    data class OfferReceived(val sdp: SessionDescription, val fromUserId: String) : SignalingEvent()

    /**
     * 상대방으로부터 통화 수락(Answer)을 받았을 때 발생하는 이벤트
     * @param sdp 통화 수락에 대한 상세 정보를 담은 SessionDescription
     * @param fromUserId Answer를 보낸 사용자의 ID
     */
    data class AnswerReceived(val sdp: SessionDescription, val fromUserId: String) : SignalingEvent()

    /**
     * 상대방으로부터 ICE Candidate(네트워크 연결 후보)를 받았을 때 발생하는 이벤트
     * @param iceCandidate 상대방의 네트워크 연결 후보 정보
     * @param fromUserId ICE Candidate를 보낸 사용자의 ID
     */
    data class IceCandidateReceived(val iceCandidate: IceCandidate, val fromUserId: String) : SignalingEvent()
}
