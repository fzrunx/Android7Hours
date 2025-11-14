package com.sesac.domain.usecase.auth

import com.sesac.domain.model.LoginRequest
import com.sesac.domain.repository.AuthRepository
import javax.inject.Inject

class LoginUseCase @Inject constructor(
    private val authRepository: AuthRepository
) {
    suspend operator fun invoke(loginRequest: LoginRequest) = authRepository.login(loginRequest)
}
