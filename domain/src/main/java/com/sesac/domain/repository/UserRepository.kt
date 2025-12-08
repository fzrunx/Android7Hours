package com.sesac.domain.repository

import com.sesac.domain.model.Auth
import com.sesac.domain.model.InvitationCode
import com.sesac.domain.model.User
import com.sesac.domain.result.AuthResult
import kotlinx.coroutines.flow.Flow
import okhttp3.MultipartBody
import okhttp3.RequestBody

interface UserRepository {
    suspend fun getUsers(): Flow<AuthResult<List<User>>>
    suspend fun postUser(user: Auth): Flow<AuthResult<Unit>>
    suspend fun deleteUser(id: Int): Flow<AuthResult<Unit>>
    fun updateProfile(token: String, image: MultipartBody.Part?, nickname: RequestBody?): Flow<AuthResult<User>>
    suspend fun postInvitationCode(token: String): Flow<AuthResult<InvitationCode>>
}