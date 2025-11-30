package com.sesac.domain.usecase.location

import com.sesac.domain.model.Coord
import com.sesac.domain.repository.LocationRepository
import com.sesac.domain.result.LocationFlowResult
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetRealtimeLocationUseCase @Inject constructor(
    private val locationRepository: LocationRepository
) {
    operator fun invoke(): Flow<LocationFlowResult<Coord>> =
        locationRepository.getRealtimePathLocation()
}
