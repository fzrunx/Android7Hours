package com.sesac.data.dto

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class CommentDTO(
    val id: Int,
    val author: AuthorDTO,
    val content: String,
    @Json(name = "created_at")
    val createdAt: String
)
