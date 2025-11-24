package com.sesac.domain.usecase.auth

data class AuthUseCase(
    val login: LoginUseCase,
    val loginWithKakao: LoginWithKakaoUseCase,
)

