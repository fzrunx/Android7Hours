package com.sesac.data.type

import com.squareup.moshi.Json

enum class PostTypeDTO {
    @Json(name = "review") REVIEW,
    @Json(name = "info") INFO,
    @Json(name = "unknown") UNKNOWN
}