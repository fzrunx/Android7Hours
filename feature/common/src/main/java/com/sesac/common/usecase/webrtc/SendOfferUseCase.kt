package com.sesac.common.usecase.webrtc

import com.sesac.common.repository.WebRTCRepository
import javax.inject.Inject

class SendOfferUseCase @Inject constructor(
    private val webRTCRepository: WebRTCRepository
) {
    suspend operator fun invoke() = webRTCRepository.sendOffer()
}
