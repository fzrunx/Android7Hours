package com.sesac.data.dto

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

/**
 * 북마크 토글 API (/posts/{pk}/bookmark-toggle/)의 응답 DTO.
 * is_bookmarked와 bookmark_count를 수신합니다.
 */
@JsonClass(generateAdapter = true)
data class BookmarkToggleDTO(
    @Json(name = "is_bookmarked")
    val isBookmarked: Boolean,
    @Json(name = "bookmark_count")
    val bookmarkCount: Int
)