package com.sesac.data.source.api

import com.sesac.data.dto.AuthDTO
import com.sesac.data.dto.LoginResponseDTO
import com.sesac.domain.remote.model.LoginRequest
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path

interface AuthApi {
    @GET("users/")
    suspend fun getUsers(): List<AuthDTO>

    @POST("users/")
    suspend fun postUser(@Body auth: AuthDTO): AuthDTO

    @DELETE("users/{id}/")
    suspend fun deleteUser(@Path("id") id: Int): Unit

    @POST("users/token/")
    suspend fun login(@Body loginRequest: LoginRequest): LoginResponseDTO
}