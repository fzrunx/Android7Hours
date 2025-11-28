package com.sesac.domain.model

import java.time.Instant

data class PetLocation(
    val latitude: Double,
    val longitude: Double,
    val accuracy: Float?,
    val batteryLevel: Int?,
    val createdAt: String
) {
    companion object {
        val EMPTY = PetLocation(
            latitude = 0.0,
            longitude = 0.0,
            accuracy = null,
            batteryLevel = null,
            createdAt = Instant.now().toString(),
        )
    }
}
