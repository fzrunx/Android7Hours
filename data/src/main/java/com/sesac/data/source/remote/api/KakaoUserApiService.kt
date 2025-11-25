package com.sesac.data.source.remote.api

import com.sesac.data.dto.KakaoUserResponseDTO
import retrofit2.http.GET
import retrofit2.http.Header

interface KakaoUserApiService {
    @GET("v2/user/me")
    suspend fun getUserInfo(
        @Header("Authorization") token: String
    ): KakaoUserResponseDTO
}
