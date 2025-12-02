package com.sesac.domain.model

import java.util.Date

enum class LikeType {
    PATH,
    POST
}

/**
 * A sealed interface to represent the different types of items that can be liked.
 * This allows for exhaustive 'when' checks in the UI layer.
 */
sealed interface LikedItem {
    val id: Int
}

/**
 * Represents a single liked item in the domain layer.
 */
data class Like(
    val id: Int,
    val createdAt: Date,
    val likedItem: LikedItem // Polymorphic item
)

data class LikeResponse(
    val isLiked: Boolean,
    val likeCount: Int,
    val status: String,
) {
    companion object{
        val EMPTY = LikeResponse(
            isLiked = false,
            likeCount = 0,
            status = "empty-state"
        )
    }
}