package com.sesac.domain.model

import java.util.Date

/**
 * Represents a Post in the domain layer, used by the UI.
 */
data class Post(
    override val id: Int,
    val userId: Int,
    val postType: String, // "review" or "info"
    val title: String,
    val content: String,
    val imageUrl: String?,
    val createdAt: Date,
    val updatedAt: Date,
    val commentsCount: Int,
    var likesCount: Int,
    var bookmarksCount: Int,
    var isLiked: Boolean,
    var isBookmarked: Boolean,
) : BookmarkedItem {
    companion object {
        val EMPTY = Post(
            id = -1,
            userId = -1,
            postType = "",
            title = "",
            content = "",
            imageUrl = "",
            createdAt = Date(),
            updatedAt = Date(),
            commentsCount = 0,
            likesCount = 0,
            bookmarksCount = 0,
            isLiked = false,
            isBookmarked = false,
        )
    }
}
