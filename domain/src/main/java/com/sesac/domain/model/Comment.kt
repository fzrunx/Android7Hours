package com.sesac.domain.model

data class Comment(
    val id: Long,
    val postId: Int,
    val authorId: Int,
    val author: String,
    val content: String,
    val timeAgo: String,
    val authorImage: String = "https://img.icons8.com/?size=100&id=Fx70T4fgtNmt&format=png&color=000000",
)
