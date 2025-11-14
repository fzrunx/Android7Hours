package com.sesac.domain.remote.model


data class Auth(
    val username: String?,
    val email: String,
    val fullName: String,
    val nickname: String,
    val password: String,
    val passwordVerification: String,
)

data class User(
    val id: Int = -1,
    val username: String?,
    val nickname: String?,
    val fullName: String,
    val email: String,
)

data class Token(
    val refresh: String,
    val access: String,
)

data class LoginRequest(
    val email: String,
    val password: String,
)

data class LoginResponse(
    val access: String,
    val refresh: String,
    val user: User,
)