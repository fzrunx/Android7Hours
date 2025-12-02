package com.sesac.data.dto.post.type

import com.squareup.moshi.Json

enum class PostTypeDTO {
    @Json(name = "review") REVIEW,
    @Json(name = "info") INFO,
    @Json(name = "unknown") UNKNOWN
}