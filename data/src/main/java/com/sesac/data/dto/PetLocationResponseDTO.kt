package com.sesac.data.dto

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class PetLocationResponseDTO(
    @Json(name = "latitude")
    val latitude: Double,
    @Json(name = "longitude")
    val longitude: Double,
    @Json(name = "accuracy")
    val accuracy: Float? = null,
    @Json(name = "battery_level")
    val batteryLevel: Int? = null,
    @Json(name = "created_at")
    val createdAt: String // Assuming ISO 8601 string from backend
)