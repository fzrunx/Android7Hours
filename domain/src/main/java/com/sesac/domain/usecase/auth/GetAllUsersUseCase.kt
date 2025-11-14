package com.sesac.domain.usecase.auth

import com.sesac.domain.repository.AuthRepository
import javax.inject.Inject

class GetAllUsersUseCase @Inject constructor(
    private val authRepository: AuthRepository
) {
    suspend operator fun invoke() = authRepository.getUsers()
}