package com.sesac.common.usecase.webrtc

data class WebRTCUseCase(
    val initializeWebRTC: InitializeWebRTCUseCase,
    val sendOffer: SendOfferUseCase,
    val sendAnswer: SendAnswerUseCase,
    val sendIceCandidate: SendIceCandidateUseCase,
    val observeSignalingEvents: ObserveSignalingEventsUseCase,
    val observeRemoteVideoTrack: ObserveRemoteVideoTrackUseCase,
    val observeSessionState: ObserveSessionStateUseCase,
    val closeSession: CloseSessionUseCase
)
