package com.sesac.data.mapper

import com.sesac.data.dto.PetLocationRequestDTO
import com.sesac.data.dto.PetLocationResponseDTO
import com.sesac.data.util.truncateWithBigDecimal
import com.sesac.domain.model.PetLocation

// Domain to DTO (for sending location updates)
fun PetLocation.toRequestDTO(): PetLocationRequestDTO {
    return PetLocationRequestDTO(
        latitude = truncateWithBigDecimal(this.latitude, 6),
        longitude = truncateWithBigDecimal(this.longitude, 6),
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

