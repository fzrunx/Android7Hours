package com.sesac.domain.usecase.auth

import com.sesac.domain.model.LoginResponse
import com.sesac.domain.repository.AuthRepository
import com.sesac.domain.result.AuthResult
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class LoginWithKakaoUseCase @Inject constructor(
    private val authRepository: AuthRepository
) {
    suspend operator fun invoke(accessToken: String): Flow<AuthResult<LoginResponse>> {
        return authRepository.loginWithKakao(accessToken)
    }
}