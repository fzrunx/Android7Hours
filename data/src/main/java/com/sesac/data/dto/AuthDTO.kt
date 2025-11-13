package com.sesac.data.dto

import com.sesac.domain.remote.model.User
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class AuthDTO(
    val id: Int = -1,
    val username: String?,
    val email: String,
    @Json(name = "full_name")
    val fullName: String,
    val nickname: String?,
    val password: String? = null,
    @Json(name = "password_verification")
    val passwordVerification: String? = null,
)

@JsonClass(generateAdapter = true)
data class TokenDTO(
    val refresh: String,
    val access: String,
)

@JsonClass(generateAdapter = true)
data class LoginResponseDTO(
    val refresh: String,
    val access: String,
    val user: AuthDTO,
)

@JsonClass(generateAdapter = true)
data class LoginRequestDTO(
    val email: String,
    val password: String,
)