package com.sesac.data.dto.post.request

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class PostCreateRequestDTO(
    val id: Int?,
    @Json(name = "post_type")
    val postType: String?,
    val title: String,
    val content: String,
    val image: String?
)