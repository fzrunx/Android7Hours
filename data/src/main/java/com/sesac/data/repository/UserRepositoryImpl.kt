package com.sesac.data.repository

import android.util.Log
import com.sesac.data.mapper.toAuthDTO
import com.sesac.data.mapper.toDomain
import com.sesac.data.mapper.toUser
import com.sesac.data.mapper.toUserList
import com.sesac.data.source.api.AuthApi
import com.sesac.data.source.api.PetsApi
import com.sesac.domain.model.Auth
import com.sesac.domain.model.InvitationCode
import com.sesac.domain.model.User
import com.sesac.domain.repository.UserRepository
import com.sesac.domain.result.AuthResult
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import okhttp3.MultipartBody
import okhttp3.RequestBody
import javax.inject.Inject

class UserRepositoryImpl @Inject constructor(
    private val authApi: AuthApi,
    private val petsApi: PetsApi,
): UserRepository {
    override suspend fun getUsers() = flow {
        emit(AuthResult.Loading)
        val result = authApi.getUsers().toUserList()
        emit(AuthResult.Success(result))
    }.catch {
        Log.d("TAG-AuthRepositoryImpl", "getUsers() : error : $it")
        emit(AuthResult.NetworkError(it))
    }

    override suspend fun postUser(user: Auth) = flow {
        emit(AuthResult.Loading)
        authApi.postUser(user.toAuthDTO())
        emit(AuthResult.Success(Unit))
    }.catch {
        Log.d("TAG-AuthRepositoryImpl", "postUser() : error : $it")
        emit(AuthResult.NetworkError(it))
    }

    override suspend fun deleteUser(id: Int): Flow<AuthResult<Unit>> =flow {
        emit(AuthResult.Loading)
        authApi.deleteUser(id = id)
        emit(AuthResult.Success(Unit))
    }.catch {
        emit(AuthResult.NetworkError(it))
    }

    override fun updateProfile(
        token: String,
        image: MultipartBody.Part?,
        nickname: RequestBody?
    ): Flow<AuthResult<User>> = flow {
        emit(AuthResult.Loading) // 로딩 상태 추가하면 좋습니다
        try {
            val response = authApi.updateProfile(token, image, nickname)

            if (response.isSuccessful && response.body() != null) {
                val dto = response.body()!!
                emit(AuthResult.Success(dto.toUser()))
            } else {
                // AuthResult.Error가 없다면 NetworkError 사용
                val errorBody = response.errorBody()?.string() ?: "No error body"
                emit(AuthResult.NetworkError(Exception("Error: ${response.code()} ${response.message()} - $errorBody")))
            }
        } catch (e: Exception) {
            emit(AuthResult.NetworkError(e))
        }
    }

    override suspend fun postInvitationCode(token: String): Flow<AuthResult<InvitationCode>> = flow {
        emit(AuthResult.Loading)
        val result = petsApi.postInvitationCode("Bearer $token").toDomain()
        emit(AuthResult.Success(result))
    }.catch {
        Log.e("UserRepositoryImpl", "postInvitationCode failed", it)
        emit(AuthResult.NetworkError(it))
    }
}