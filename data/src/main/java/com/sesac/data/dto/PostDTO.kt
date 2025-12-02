package com.sesac.data.dto

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class PostDTO(
    val id: Int,
    @Json(name = "auth_user")
    val authUser: Int,
    @Json(name = "auth_profile_image")
    val authUserProfileImageUrl: String?,
    @Json(name = "post_type")
    val postType: String?,
    val title: String,
    val content: String,
    val image: String?,
    @Json(name = "created_at")
    val createdAt: String,
    @Json(name = "updated_at")
    val updatedAt: String,
    @Json(name = "comment_count")
    val commentCount: Int,
    @Json(name = "like_count")
    val likeCount: Int,
    @Json(name = "bookmark_count")
    val bookmarkCount: Int,
    @Json(name = "is_liked")
    val isLiked: Boolean,
    @Json(name = "is_bookmarked")
    val isBookmarked: Boolean,
    val comments: List<CommentDTO>?
)