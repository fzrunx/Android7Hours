package com.sesac.data.repository

import android.util.Log
import com.sesac.data.mapper.toAuthDTO
import com.sesac.data.mapper.toUserList
import com.sesac.data.source.remote.api.AuthApi
import com.sesac.domain.model.Auth
import com.sesac.domain.repository.UserRepository
import com.sesac.domain.result.AuthResult
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class UserRepositoryImpl @Inject constructor(
    private val authApi: AuthApi,
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

}