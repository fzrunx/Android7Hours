package com.sesac.domain.remote.repository

import com.sesac.domain.remote.model.Auth
import com.sesac.domain.remote.model.LoginRequest
import com.sesac.domain.remote.model.LoginResponse
import com.sesac.domain.remote.model.User
import com.sesac.domain.remote.result.AuthResult
import kotlinx.coroutines.flow.Flow

interface AuthRepository {
    suspend fun getUsers(): Flow<AuthResult<List<User>>>
    suspend fun postUser(user: Auth): Flow<AuthResult<Unit>>
    suspend fun deleteUser(id: Int): Flow<AuthResult<Unit>>
    suspend fun login(loginRequest: LoginRequest): Flow<AuthResult<LoginResponse>>
}