package com.sesac.domain.model

import java.util.Date

data class Post(
    val id: Long,
    val author: String,
    val authorImage: String,
    val timeAgo: String,
    val content: String,
    val image: String?,
    val likes: Int,
    val comments: Int,
    val isLiked: Boolean,
    val category: String,
    val createdAt: Date = Date(System.currentTimeMillis())
)
