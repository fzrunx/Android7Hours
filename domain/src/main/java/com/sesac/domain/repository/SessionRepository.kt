package com.sesac.domain.repository

import com.sesac.domain.model.LoginResponse
import com.sesac.domain.model.User
import kotlinx.coroutines.flow.Flow

interface SessionRepository {
    fun getAccessToken(): Flow<String?>
    fun getRefreshToken(): Flow<String?>
    fun getUserInfo(): Flow<User?>
    suspend fun saveSession(loginResponse: LoginResponse)
    suspend fun clearSession()
}