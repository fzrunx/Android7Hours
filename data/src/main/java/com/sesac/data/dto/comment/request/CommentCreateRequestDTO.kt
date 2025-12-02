package com.sesac.data.dto.comment.request

import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class CommentCreateRequestDTO(
    val content: String
)