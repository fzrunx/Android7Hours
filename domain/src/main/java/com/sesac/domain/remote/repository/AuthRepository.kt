package com.sesac.domain.remote.repository

import com.sesac.domain.remote.model.LoginRequest
import com.sesac.domain.remote.model.LoginResponse
import com.sesac.domain.remote.model.UserAPI
import com.sesac.domain.remote.model.UserInfo
import com.sesac.domain.remote.result.AuthResult
import kotlinx.coroutines.flow.Flow

interface AuthRepository {
    suspend fun getUsers(): Flow<AuthResult<List<UserInfo>>>
    suspend fun postUser(user: UserAPI): Flow<AuthResult<Unit>>
    suspend fun login(loginRequest: LoginRequest): Flow<AuthResult<LoginResponse>>
}