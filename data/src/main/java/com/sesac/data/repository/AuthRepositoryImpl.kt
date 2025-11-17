package com.sesac.data.repository

import android.util.Log
import com.sesac.data.dto.KakaoLoginRequestDTO
import com.sesac.data.dto.LoginRequestDTO
import com.sesac.data.mapper.toAuthDTO
import com.sesac.data.mapper.toLoginResponse
import com.sesac.data.mapper.toUserList
import com.sesac.data.source.api.AuthApi
import com.sesac.data.source.api.KakaoUserApiService
import com.sesac.domain.model.Auth
import com.sesac.domain.model.LoginRequest
import com.sesac.domain.model.LoginResponse
import com.sesac.domain.repository.AuthRepository
import com.sesac.domain.result.AuthResult
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class AuthRepositoryImpl @Inject constructor(
    private val authApi: AuthApi,
    private val kakaoUserApiService: KakaoUserApiService
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

    override fun loginWithKakao(accessToken: String): Flow<AuthResult<LoginResponse>> = flow {
        emit(AuthResult.Loading)

        // 1. 카카오 API 호출해서 사용자 정보 가져오기
        val kakaoUser = kakaoUserApiService.getUserInfo("Bearer $accessToken")
        Log.d("TAG-AuthRepositoryImpl", "kakaoApi: $kakaoUserApiService")
        Log.d("TAG-AuthRepositoryImpl", "kakaoUser: $kakaoUser")


        // 2. 우리 서버에 보낼 요청 데이터(DTO) 만들기
        val request = KakaoLoginRequestDTO(
            accessToken = accessToken,
            email = kakaoUser.kakaoAccount?.email,
            nickname = kakaoUser.kakaoAccount?.profile?.nickname
        )

        // 3. 우리 서버로 로그인/회원가입 요청 보내기
        val response = authApi.loginWithKakao(request)
        Log.d("TAG-AuthRepositoryImpl", "request: $request")
        Log.d("TAG-AuthRepositoryImpl", "response: $response")

        // 4. 서버 응답을 Domain 모델로 변환하여 전달
        if (response.isSuccessful) {
            val responseBody = response.body()
            Log.d("TAG-AuthRepositoryImpl", "responseBody: $responseBody")
            if (responseBody != null) {
                emit(AuthResult.Success(responseBody.toLoginResponse()))
            } else {
                emit(AuthResult.NetworkError(Exception("Kakao login response body is null")))
            }
        } else {
            emit(AuthResult.NetworkError(Exception("Kakao login failed with code: ${response.code()} - ${response.message()}")))
        }

    }.catch { e ->
        Log.e("AuthRepositoryImpl", "loginWithKakao Failed", e)
        emit(AuthResult.NetworkError(e))
    }
}