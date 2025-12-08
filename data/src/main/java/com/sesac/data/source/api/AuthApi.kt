package com.sesac.data.source.api

import com.sesac.data.dto.AuthDTO
import com.sesac.data.dto.KakaoLoginRequestDTO
import com.sesac.data.dto.LoginResponseDTO
import com.sesac.data.dto.TokenRefreshRequestDTO
import com.sesac.data.dto.TokenRefreshResponseDTO
import com.sesac.domain.model.LoginRequest
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Multipart
import retrofit2.http.PATCH
import retrofit2.http.POST
import retrofit2.http.Part
import retrofit2.http.Path

interface AuthApi {
    @GET("users/")
    suspend fun getUsers(): List<AuthDTO>

    @POST("users/")
    suspend fun postUser(@Body auth: AuthDTO): AuthDTO

    @POST("users/")
    suspend fun loginWithKakao(
        @Body request: KakaoLoginRequestDTO
    ): Response<LoginResponseDTO>

    @DELETE("users/{id}/")
    suspend fun deleteUser(@Path("id") id: Int): Unit

    @POST("users/token/")
    suspend fun login(@Body loginRequest: LoginRequest): Response<LoginResponseDTO>

    @POST("users/token/refresh/")
    suspend fun refreshToken(@Body body: TokenRefreshRequestDTO): Response<TokenRefreshResponseDTO>

    @Multipart
    @PATCH("users/me/profile/")
    suspend fun updateProfile(
        @Header("Authorization") token: String,      // "Bearer 토큰값"
        @Part image: MultipartBody.Part?,            // 이미지 파일 (선택)
        @Part("nickname") nickname: RequestBody?     // 닉네임 (선택)
    ): Response<AuthDTO>
}