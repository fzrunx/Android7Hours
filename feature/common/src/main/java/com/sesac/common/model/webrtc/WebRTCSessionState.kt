package com.sesac.common.model.webrtc

/**
 * WebRTC P2P 연결의 현재 상태를 나타내는 enum class
 */
enum class WebRTCSessionState {
    /**
     * 초기 상태 또는 연결이 종료된 상태
     */
    Idle,

    /**
     * 통화 제안(Offer)을 보내고 상대방의 응답(Answer)을 기다리는 중
     */
    SendingOffer,

    /**
     * 통화 제안(Offer)을 받고 응답(Answer)을 보내려고 준비 중
     */
    ReceivingOffer,

    /**
     * 통화가 연결되어 스트리밍이 진행 중인 상태
     */
    Connected,

    /**
     * 연결 실패 상태
     */
    Failed,

    /**
     * 연결이 닫힌 상태
     */
    Closed
}
