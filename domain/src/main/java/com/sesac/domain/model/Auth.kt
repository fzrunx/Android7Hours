package com.sesac.domain.model

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass


data class Auth(
    val username: String?,
    val email: String,
    val fullName: String,
    val nickname: String,
    val password: String,
    val passwordVerification: String,
)

@JsonClass(generateAdapter = true)
data class User(
    val id: Int = -1,
    val username: String?,
    val nickname: String?,
    val fullName: String,
    val email: String,
    @Json(name = "profile_image_url") val profileImageUrl: String? = null,
)

data class JoinFormState(
    val email: String = "",
    val isEmailValid: Boolean = true,
    val password: String = "",
    val isPasswordValid: Boolean = true,
    val passwordConfirm: String = "",
    val doPasswordsMatch: Boolean = true,
    val name: String = "",
    val nickname: String = "",
    val phone: String = "",
    val agreeAll: Boolean = false,
    val agreeAge: Boolean = false,
    val agreeTerms: Boolean = false,
    val agreePrivacy: Boolean = false,
    val showValidationErrors: Boolean = false
) {
    fun toAuth() = Auth(
        username = this.email,
        email = this.email,
        fullName = this.name,
        nickname = this.nickname,
        password = this.password,
        passwordVerification = this.passwordConfirm,
    )
}

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