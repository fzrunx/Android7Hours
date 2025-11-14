package com.sesac.domain.remote.usecase.session

import com.sesac.domain.remote.model.User
import com.sesac.domain.remote.repository.SessionRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetUserInfoUseCase @Inject constructor(private val sessionRepository: SessionRepository) {
    operator fun invoke(): Flow<User?> = sessionRepository.getUserInfo()
}
