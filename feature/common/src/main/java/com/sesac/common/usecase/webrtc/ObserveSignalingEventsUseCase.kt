package com.sesac.common.usecase.webrtc

import com.sesac.common.repository.WebRTCRepository
import javax.inject.Inject

class ObserveSignalingEventsUseCase @Inject constructor(
    private val webRTCRepository: WebRTCRepository
) {
    operator fun invoke() = webRTCRepository.observeSignalingEvents()
}
