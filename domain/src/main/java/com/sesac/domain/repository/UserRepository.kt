package com.sesac.domain.repository

import com.sesac.domain.model.Auth
import com.sesac.domain.model.User
import com.sesac.domain.result.AuthResult
import kotlinx.coroutines.flow.Flow

interface UserRepository {
    suspend fun getUsers(): Flow<AuthResult<List<User>>>
    suspend fun postUser(user: Auth): Flow<AuthResult<Unit>>
    suspend fun deleteUser(id: Int): Flow<AuthResult<Unit>>
}