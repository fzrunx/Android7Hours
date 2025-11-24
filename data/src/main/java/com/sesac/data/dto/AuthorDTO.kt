package com.sesac.data.dto

import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class AuthorDTO(
    val id: Int,
    val nickname: String,
    val image: String? // 서버에서 추가될 필드로, 현재는 nullable로 처리
)
