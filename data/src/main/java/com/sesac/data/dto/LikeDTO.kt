package com.sesac.data.dto

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class LikeDTO(
    val id: Int,
    @Json(name = "created_at")
    val createdAt: String,
    @Json(name = "liked_object")
    val likedObject: LikedObject // This will be either BookmarkedPathDTO or PostDTO
)

@JsonClass(generateAdapter = true)
data class LikeResponseDTO(
    @Json(name = "is_liked")
    val isLiked: Boolean,
    @Json(name = "like_count")
    val likeCount: Int,
    val status: String,
)