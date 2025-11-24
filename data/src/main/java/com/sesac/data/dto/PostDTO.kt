package com.sesac.data.dto

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass
import com.sesac.data.dto.BookmarkedObject

/**
 * Represents a Post object received from the API.
 * Based on the Django PostSerializer.
 */
@JsonClass(generateAdapter = true)
data class PostDTO(
    override val id: Int,
    @Json(name = "auth_user")
    val authUser: String,
    @Json(name = "post_type")
    val title: String,
    val content: String,
    val image: String?,
    @Json(name = "created_at")
    val createdAt: String,
    @Json(name = "updated_at")
    val updatedAt: String,
    @Json(name = "comments_count")
    val commentsCount: Int,
    @Json(name = "likes_count")
    val likesCount: Int,
    @Json(name = "bookmarks_count")
    val bookmarksCount: Int,
    @Json(name = "is_liked")
    val isLiked: Boolean,
    @Json(name = "is_bookmarked")
    val isBookmarked: Boolean,
) : BookmarkedObject

/**
 * DTO for the response of the like-toggle endpoint.
 */
@JsonClass(generateAdapter = true)
data class LikeToggleResponseDTO(
    val liked: Boolean,
    @Json(name = "likes_count")
    val likesCount: Int
)

/**
 * DTO for the response of the bookmark-toggle endpoint.
 */
@JsonClass(generateAdapter = true)
data class BookmarkToggleResponseDTO(
    val bookmarked: Boolean,
    @Json(name = "bookmarks_count")
    val bookmarksCount: Int
)