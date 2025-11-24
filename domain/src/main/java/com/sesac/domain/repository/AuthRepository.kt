package com.sesac.domain.repository

import com.sesac.domain.model.Auth
import com.sesac.domain.model.LoginRequest
import com.sesac.domain.model.LoginResponse
import com.sesac.domain.model.User
import com.sesac.domain.result.AuthResult
import kotlinx.coroutines.flow.Flow

interface AuthRepository {
    suspend fun login(loginRequest: LoginRequest): Flow<AuthResult<LoginResponse>>
    suspend fun loginWithKakao(accessToken: String): Flow<AuthResult<LoginResponse>>
}