package com.sesac.domain.remote.usecase.auth

import com.sesac.domain.remote.repository.AuthRepository
import javax.inject.Inject

class GetAllUsersUseCase @Inject constructor(
    private val authRepository: AuthRepository
) {
    suspend operator fun invoke() = authRepository.getUsers()
}