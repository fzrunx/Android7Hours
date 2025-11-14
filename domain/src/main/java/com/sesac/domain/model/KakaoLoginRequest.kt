package com.sesac.domain.model

data class KakaoLoginRequest(
    val accessToken: String,
    val email: String?,
    val nickname: String?
)
