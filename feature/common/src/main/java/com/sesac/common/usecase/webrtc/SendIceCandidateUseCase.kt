package com.sesac.common.usecase.webrtc

import com.sesac.common.repository.WebRTCRepository
import org.webrtc.IceCandidate
import javax.inject.Inject

class SendIceCandidateUseCase @Inject constructor(
    private val webRTCRepository: WebRTCRepository
) {
    fun invoke(iceCandidate: IceCandidate) = webRTCRepository.sendIceCandidate(iceCandidate)
}
