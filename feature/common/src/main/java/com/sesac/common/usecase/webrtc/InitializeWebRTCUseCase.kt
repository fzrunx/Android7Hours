package com.sesac.common.usecase.webrtc

import com.sesac.common.repository.WebRTCRepository
import javax.inject.Inject

class InitializeWebRTCUseCase @Inject constructor(
    private val webRTCRepository: WebRTCRepository
) {
    operator fun invoke(myUserId: String, targetUserId: String) = webRTCRepository.initialize(myUserId, targetUserId)
}
