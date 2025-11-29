package com.sesac.domain.model

import kotlinx.serialization.Serializable
import com.squareup.moshi.JsonClass

@Serializable
@JsonClass(generateAdapter = true)
data class Coord(
    val latitude: Double = 0.0,
    val longitude: Double = 0.0,
    val altitude: Double? = null,
) {
    companion object {
        val DEFAULT = Coord(
//            latitude = 37.52952,
//            longitude = 126.9645,
            latitude = 35.0996,
            longitude = 129.1237,
        )
    }

    fun toPetLocation(
        accuracy: Float? = null,
        batteryLevel: Int? = null,
        createdAt: String = "" // createdAt is server-generated, not sent by client
    ): PetLocation {
        return PetLocation(
            latitude = this.latitude,
            longitude = this.longitude,
            accuracy = accuracy,
            batteryLevel = batteryLevel,
            createdAt = createdAt // Will be an empty string for outbound PetLocation, server will populate on inbound
        )
    }

}

// NEW: Coord to PetLocation mapper
