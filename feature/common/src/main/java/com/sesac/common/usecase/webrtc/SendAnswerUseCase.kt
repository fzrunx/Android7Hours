package com.sesac.common.usecase.webrtc

import com.sesac.common.repository.WebRTCRepository
import org.webrtc.SessionDescription
import javax.inject.Inject

class SendAnswerUseCase @Inject constructor(
    private val webRTCRepository: WebRTCRepository
) {
    suspend operator fun invoke(sessionDescription: SessionDescription) = webRTCRepository.sendAnswer(sessionDescription)
}
