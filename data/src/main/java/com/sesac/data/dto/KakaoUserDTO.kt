package com.sesac.data.dto

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class KakaoUserResponseDTO(
    val id: Long,
    @Json(name = "kakao_account")
    val kakaoAccount: KakaoAccountDTO?
)

@JsonClass(generateAdapter = true)
data class KakaoAccountDTO(
    val email: String?,
    val profile: KakaoProfileDTO?
)

@JsonClass(generateAdapter = true)
data class KakaoProfileDTO(
    val nickname: String?,
    @Json(name = "profile_image_url")
    val profileImageUrl: String?
)
