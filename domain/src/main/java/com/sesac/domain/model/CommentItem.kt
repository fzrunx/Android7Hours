package com.sesac.domain.model

import java.util.Date

data class CommentItem(
    val id: Int,
    val author: AuthorSummary,
    val content: String,
    val createdAt: Date
) {
    companion object {
        val EMPTY = CommentItem(
            id = -1,
            author = AuthorSummary.EMPTY,
            content = "",
            createdAt = Date(0L)
        )
    }
}