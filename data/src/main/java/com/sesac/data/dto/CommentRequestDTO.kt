package com.sesac.data.dto

import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class CommentRequestDTO(
    val content: String
)
