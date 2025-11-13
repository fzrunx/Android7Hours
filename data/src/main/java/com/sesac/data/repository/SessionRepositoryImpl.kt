package com.sesac.data.repository

import com.sesac.data.datastore.AuthDataStore
import com.sesac.domain.remote.model.LoginResponse
import com.sesac.domain.remote.model.User
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
    override fun getUserInfo(): Flow<User?> = authDataStore.user

    override suspend fun saveSession(loginResponse: LoginResponse) {
        authDataStore.saveSession(loginResponse.access, loginResponse.refresh, loginResponse.user)
    }

    override suspend fun clearSession() {
        authDataStore.clearSession()
    }
}
