package com.sesac.domain.model

data class Comment(
    val id: Long,
    val postId: Int,
    val author: String,
    val content: String,
    val timeAgo: String,
    val authorImage: String
)
