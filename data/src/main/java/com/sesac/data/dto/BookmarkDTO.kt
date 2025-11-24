package com.sesac.data.dto

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

/**
 * Represents a Bookmark object received from the API, wrapping a polymorphic bookmarked item.
 * Based on Django's BookmarkSerializer.
 */
@JsonClass(generateAdapter = true)
data class BookmarkDTO(
    val id: Int,
    @Json(name = "created_at")
    val createdAt: String,
    @Json(name = "bookmarked_object")
    val bookmarkedObject: BookmarkedObject // This will be either BookmarkedPathDTO or PostDTO
)

@JsonClass(generateAdapter = true)
data class BookmarkResponseDTO(
    val bookmarked: Boolean,
    @Json(name = "bookmarks_count")
    val bookmarksCount: Int,
    val status: String,
)