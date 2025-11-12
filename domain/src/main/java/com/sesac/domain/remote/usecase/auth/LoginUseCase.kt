package com.sesac.domain.remote.usecase.auth

import com.sesac.domain.remote.model.LoginRequest
import com.sesac.domain.remote.repository.AuthRepository
import javax.inject.Inject

class LoginUseCase @Inject constructor(
    private val authRepository: AuthRepository
) {
    suspend operator fun invoke(loginRequest: LoginRequest) = authRepository.login(loginRequest)
}
