package com.sesac.domain.model.post

import java.util.Date

data class PostModel(
    val postId: Int,
    var title: String = "",
    val userName: String,
    var content: String = "",
    val imageResList: List<Int>? = null,
    var likes: Int = 0,
    var comments: List<String>? = null,
    var status: Boolean = false,
    var create_at: Date = Date(System.currentTimeMillis()),
    val category: String = "정보공유",
)