package com.sesac.domain.usecase.session

import com.sesac.domain.repository.SessionRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetRefreshTokenUseCase @Inject constructor(private val sessionRepository: SessionRepository) {
    operator fun invoke(): Flow<String?> = sessionRepository.getRefreshToken()
}
