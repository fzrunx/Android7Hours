package com.sesac.data.repository

import android.util.Log
import com.sesac.data.mapper.toAuthDTO
import com.sesac.data.mapper.toLoginResponse
import com.sesac.data.mapper.toUserList
import com.sesac.data.source.api.AuthApi
import com.sesac.domain.remote.model.Auth
import com.sesac.domain.remote.model.LoginRequest
import com.sesac.domain.remote.repository.AuthRepository
import com.sesac.domain.remote.result.AuthResult
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class AuthRepositoryImpl @Inject constructor(
    private val authApi: AuthApi,
): AuthRepository {
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

    override suspend fun login(loginRequest: LoginRequest) = flow {
        emit(AuthResult.Loading)
        val resultDTO = authApi.login(loginRequest)
        val result = resultDTO.toLoginResponse()
        Log.d("TAG-AuthRepositoryImpl", "loginRequestDTD : $resultDTO")
        Log.d("TAG-AuthRepositoryImpl", "loginRequest : $result")
        emit(AuthResult.Success(result))
    }.catch {
        Log.d("TAG-AuthRepositoryImpl", "login() : error : $it")
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