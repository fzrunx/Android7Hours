package com.sesac.domain.remote.repository

import com.sesac.domain.remote.model.LoginResponse
import com.sesac.domain.remote.model.User
import kotlinx.coroutines.flow.Flow

interface SessionRepository {
    fun getAccessToken(): Flow<String?>
    fun getRefreshToken(): Flow<String?>
    fun getUserInfo(): Flow<User?>
    suspend fun saveSession(loginResponse: LoginResponse)
    suspend fun clearSession()
}