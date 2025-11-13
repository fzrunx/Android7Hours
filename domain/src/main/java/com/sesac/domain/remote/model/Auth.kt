package com.sesac.domain.remote.model

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class UserAPI(
    val id: Int,
    val username: String,
    val email: String,
    @Json(name = "full_name")
    val fullName: String,
    val nickname: String?,
    val password: String? = null,
    @Json(name = "password_verification")
    val passwordVerification: String? = null,
)

@JsonClass(generateAdapter = true)
data class UserInfo(
    val username: String,
    val nickname: String?,
    @Json(name = "full_name")
    val fullName: String,
    val email: String,
)

@JsonClass(generateAdapter = true)
data class Token(
    val refresh: String,
    val access: String,
)

@JsonClass(generateAdapter = true)
data class LoginResponse(
    val refresh: String,
    val access: String,
    val user: UserInfo,
//    val user: UserAPI,
)

@JsonClass(generateAdapter = true)
data class LoginRequest(
    val email: String,
    val password: String,
)