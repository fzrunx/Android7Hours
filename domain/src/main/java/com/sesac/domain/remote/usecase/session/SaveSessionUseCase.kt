package com.sesac.domain.remote.usecase.session

import com.sesac.domain.remote.model.LoginResponse
import com.sesac.domain.remote.model.User
import com.sesac.domain.remote.repository.SessionRepository
import javax.inject.Inject

class SaveSessionUseCase @Inject constructor(private val sessionRepository: SessionRepository) {
    suspend operator fun invoke(loginResponse: LoginResponse) = sessionRepository.saveSession(loginResponse)
}
