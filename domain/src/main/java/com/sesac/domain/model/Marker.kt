package com.sesac.domain.model

import com.squareup.moshi.JsonClass
import kotlinx.serialization.Serializable

@Serializable
@JsonClass(generateAdapter = true)
data class MemoMarker(
    val latitude: Double,
    val longitude: Double,
    val memo: String = ""
)

data class InfoMarker(
    val latitude: Double,
    val longitude: Double,
    val name: String,
)