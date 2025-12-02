package com.sesac.domain.model

import java.util.Date

enum class BookmarkType {
    PATH,
    POST
}

/**
 * A sealed interface to represent the different types of items that can be bookmarked.
 * This allows for exhaustive 'when' checks in the UI layer.
 */
sealed interface BookmarkedItem {
    val id: Int
}

/**
 * Represents a single bookmarked item in the domain layer.
 */
data class Bookmark(
    val id: Int,
    val createdAt: Date,
    val bookmarkedItem: BookmarkedItem // Polymorphic item
)

data class BookmarkResponse(
    val isBookmarked: Boolean,
    val bookmarkCount: Int,
    val status: String,
) {
    companion object{
        val EMPTY = BookmarkResponse(
            isBookmarked = false,
            bookmarkCount = 0,
            status = "empty-state"
        )
    }
}