package com.sesac.domain.remote.usecase.session

import com.sesac.domain.remote.repository.SessionRepository
import javax.inject.Inject

class ClearSessionUseCase @Inject constructor(private val sessionRepository: SessionRepository) {
    suspend operator fun invoke() = sessionRepository.clearSession()
}
