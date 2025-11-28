package com.sesac.domain.repository

import com.sesac.domain.model.Coord
import com.sesac.domain.result.LocationFlowResult
import kotlinx.coroutines.flow.Flow

interface LocationRepository {
    fun getCurrentCoord(): Flow<LocationFlowResult<Coord>>
//    fun updateLatestLocation(coord: Coord): Flow<AuthResult<Coord?>>
//    fun fetchPetLocation():List<PetLocation>
}