package com.sesac.domain.remote.usecase.session

import com.sesac.domain.remote.model.UserInfo
import com.sesac.domain.remote.repository.SessionRepository
import javax.inject.Inject

class SaveSessionUseCase @Inject constructor(private val sessionRepository: SessionRepository) {
    suspend operator fun invoke(accessToken: String, refreshToken: String, userInfo: UserInfo) = sessionRepository.saveSession(accessToken, refreshToken, userInfo)
}
