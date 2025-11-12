package com.sesac.data.source.api

import com.sesac.domain.remote.model.LoginRequest
import com.sesac.domain.remote.model.LoginResponse
import com.sesac.domain.remote.model.UserAPI
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST

interface AuthApi {
    @GET("users/")
    suspend fun getUsers(): List<UserAPI>

    @POST("users/")
    suspend fun postUser(@Body user: UserAPI): UserAPI

    @POST("users/token/")
    suspend fun login(@Body loginRequest: LoginRequest): LoginResponse
}