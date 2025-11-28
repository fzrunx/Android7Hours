package com.sesac.data.mapper

import com.sesac.data.dto.PetLocationRequestDTO
import com.sesac.data.dto.PetLocationResponseDTO
import com.sesac.domain.model.Coord
import com.sesac.domain.model.PetLocation

// Domain to DTO (for sending location updates)
fun PetLocation.toRequestDTO(): PetLocationRequestDTO {
    return PetLocationRequestDTO(
        latitude = this.latitude,
        longitude = this.longitude,
        accuracy = this.accuracy,
        batteryLevel = this.batteryLevel
    )
}

// DTO to Domain (for receiving location updates)
fun PetLocationResponseDTO.toDomain(): PetLocation {
    return PetLocation(
        latitude = this.latitude,
        longitude = this.longitude,
        accuracy = this.accuracy,
        batteryLevel = this.batteryLevel,
        createdAt = this.createdAt
    )
}

// NEW: Coord to PetLocation mapper
fun Coord.toPetLocation(
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
