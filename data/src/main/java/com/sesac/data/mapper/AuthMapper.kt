package com.sesac.data.mapper

import com.sesac.data.dto.AuthDTO
import com.sesac.data.dto.LoginRequestDTO
import com.sesac.data.dto.LoginResponseDTO
import com.sesac.data.dto.TokenDTO
import com.sesac.domain.model.Auth
import com.sesac.domain.model.LoginRequest
import com.sesac.domain.model.LoginResponse
import com.sesac.domain.model.Token
import com.sesac.domain.model.User

// User
fun User.toAuthDTO() = AuthDTO(
    username = this.username,
    nickname = this.nickname,
    fullName = this.fullName,
    email = this.email,
)

fun Auth.toAuthDTO() = AuthDTO(
    username = this.username,
    email = this.email,
    fullName = this.fullName,
    nickname = this.nickname,
    password = this.password,
    passwordVerification = this.passwordVerification,
)

fun AuthDTO.toUser() = User(
    id = this.id,
    username = this.username,
    nickname = this.nickname,
    fullName = this.fullName,
    email = this.email,
)

fun List<AuthDTO>.toUserList() = this.map {
    it.toUser()
}.toList()

// Token
fun Token.toTokenDTO() = TokenDTO(
    access = this.access,
    refresh = this.refresh
)

fun TokenDTO.toToken() = Token(
    refresh = this.refresh,
    access = this.access,
)

// Login Request/Response
fun LoginRequest.LoginRequestDTO() = LoginRequestDTO(
    email = this.email,
    password = this.password,
)

fun LoginRequestDTO.toLoginRequest() = LoginRequest(
    email = this.email,
    password = this.password,
)

fun LoginResponse.toLoginResponseDTO() = LoginResponseDTO(
    access = this.access,
    refresh = this.refresh,
    user = this.user.toAuthDTO(),
)

fun LoginResponseDTO.toLoginResponse() = LoginResponse(
    refresh = refresh,
    access = access,
    user = user.toUser(),
)
