package com.sesac.domain.model.post

data class PostModel(
    var title: String = "",
    val userName: String,
    var content: String = "",
    val imageResList: List<Int>? = null,
    var likes: Int = 0,
    var comments: List<String>? = null,
//    var create_at: Date,
    var status: Boolean = false,
)