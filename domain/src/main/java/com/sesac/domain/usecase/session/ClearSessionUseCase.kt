package com.sesac.domain.usecase.session

import com.sesac.domain.repository.SessionRepository
import javax.inject.Inject

class ClearSessionUseCase @Inject constructor(private val sessionRepository: SessionRepository) {
    suspend operator fun invoke() = sessionRepository.clearSession()
}
