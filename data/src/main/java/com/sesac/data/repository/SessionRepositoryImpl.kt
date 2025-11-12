package com.sesac.data.repository

import com.sesac.data.datastore.AuthDataStore
import com.sesac.domain.remote.model.UserInfo
import com.sesac.domain.remote.repository.SessionRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class SessionRepositoryImpl @Inject constructor(
    private val authDataStore: AuthDataStore
) : SessionRepository {
    override fun getAccessToken(): Flow<String?> = authDataStore.accessToken
    override fun getRefreshToken(): Flow<String?> = authDataStore.refreshToken
    override fun getUserInfo(): Flow<UserInfo?> = authDataStore.userInfo

    override suspend fun saveSession(accessToken: String, refreshToken: String, userInfo: UserInfo) {
        authDataStore.saveSession(accessToken, refreshToken, userInfo)
    }

    override suspend fun clearSession() {
        authDataStore.clearSession()
    }
}
