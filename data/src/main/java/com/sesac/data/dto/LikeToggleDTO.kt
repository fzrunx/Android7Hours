package com.sesac.data.dto

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

/**
 * 좋아요 토글 API (/posts/{pk}/like-toggle/)의 응답 DTO.
 * is_liked와 like_count를 수신합니다.
 */
@JsonClass(generateAdapter = true)
data class LikeToggleDTO(
    @Json(name = "is_liked")
    val isLiked: Boolean,
    @Json(name = "like_count")
    val likeCount: Int
)