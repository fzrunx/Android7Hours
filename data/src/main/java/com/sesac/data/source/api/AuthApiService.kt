package com.sesac.data.source.api

import com.sesac.data.dto.KakaoLoginRequestDTO
import com.sesac.data.dto.LoginRequestDTO
import com.sesac.data.dto.LoginResponseDTO
import com.sesac.domain.model.Auth
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST

interface AuthApiService {

    // (Django 서버 주소 예시)
    @POST("users/join/")
    suspend fun postUser(@Body user: Auth): Response<Auth>

    // (Django 서버 주소 예시)
    @POST("users/login/")
    suspend fun login(@Body request: LoginRequestDTO): Response<LoginResponseDTO>

    // --- [추가] 카카오 로그인 엔드포인트 ---
    // (주소 예시: /users/kakao/login/ -> Django 담당자와 확인 필요)
    @POST("users/")
    suspend fun loginWithKakao(
        @Body request: KakaoLoginRequestDTO
    ): Response<LoginResponseDTO>
    // ---
}