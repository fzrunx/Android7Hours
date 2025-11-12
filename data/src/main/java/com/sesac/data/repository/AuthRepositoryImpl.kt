package com.sesac.data.repository

import android.util.Log
import com.sesac.data.dto.toUserInfoList
import com.sesac.data.source.api.AuthApi
import com.sesac.domain.remote.model.LoginRequest
import com.sesac.domain.remote.model.UserAPI
import com.sesac.domain.remote.repository.AuthRepository
import com.sesac.domain.remote.result.AuthResult
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class AuthRepositoryImpl @Inject constructor(
    private val authApi: AuthApi,
): AuthRepository {
    override suspend fun getUsers() = flow {
        emit(AuthResult.Loading)
        val result = authApi.getUsers().toUserInfoList()
        emit(AuthResult.Success(result))
    }.catch {
        Log.d("TAG-AuthRepositoryImpl", "getUsers() : error : $it")
        emit(AuthResult.NetworkError(it))
    }

    override suspend fun postUser(user: UserAPI) = flow {
        emit(AuthResult.Loading)
        authApi.postUser(user)
        emit(AuthResult.Success(Unit))
    }.catch {
        Log.d("TAG-AuthRepositoryImpl", "postUser() : error : $it")
        emit(AuthResult.NetworkError(it))
    }

    override suspend fun login(loginRequest: LoginRequest) = flow {
        emit(AuthResult.Loading)
        val result = authApi.login(loginRequest)
        emit(AuthResult.Success(result))
    }.catch {
        Log.d("TAG-AuthRepositoryImpl", "login() : error : $it")
        emit(AuthResult.NetworkError(it))
    }
}