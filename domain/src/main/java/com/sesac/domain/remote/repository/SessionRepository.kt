package com.sesac.domain.remote.repository

import com.sesac.domain.remote.model.UserInfo
import kotlinx.coroutines.flow.Flow

interface SessionRepository {
    fun getAccessToken(): Flow<String?>
    fun getRefreshToken(): Flow<String?>
    fun getUserInfo(): Flow<UserInfo?>
    suspend fun saveSession(accessToken: String, refreshToken: String, userInfo: UserInfo)
    suspend fun clearSession()
}