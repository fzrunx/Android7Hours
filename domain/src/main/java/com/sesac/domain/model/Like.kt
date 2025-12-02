package com.sesac.domain.model

/**
 * A sealed interface to represent the different types of items that can be liked.
 * This allows for exhaustive 'when' checks in the UI layer.
 */
sealed interface LikedItem {
    val id: Int
}

data class Like(
    val isLiked: Boolean,
    val likeCount: Int,
    val status: String,
) {
    companion object{
        val EMPTY = Like(
            isLiked = false,
            likeCount = 0,
            status = "empty-state"
        )
    }
}