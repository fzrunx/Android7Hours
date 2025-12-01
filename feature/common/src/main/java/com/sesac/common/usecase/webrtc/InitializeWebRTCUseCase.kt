package com.sesac.common.usecase.webrtc

import com.sesac.common.repository.WebRTCRepository
import javax.inject.Inject

class InitializeWebRTCUseCase @Inject constructor(
    private val webRTCRepository: WebRTCRepository
) {
    operator fun invoke(myId: String, targetId: String) = webRTCRepository.initialize(myId, targetId)
}
