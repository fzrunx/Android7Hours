package com.sesac.domain.usecase.session

import com.sesac.domain.model.LoginResponse
import com.sesac.domain.repository.SessionRepository
import javax.inject.Inject

class SaveSessionUseCase @Inject constructor(private val sessionRepository: SessionRepository) {
    suspend operator fun invoke(loginResponse: LoginResponse) = sessionRepository.saveSession(loginResponse)
}
