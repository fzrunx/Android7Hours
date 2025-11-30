package com.sesac.domain.repository

import com.sesac.domain.model.Coord
import com.sesac.domain.model.PetLocation
import com.sesac.domain.result.AuthResult
import com.sesac.domain.result.LocationFlowResult
import kotlinx.coroutines.flow.Flow

interface LocationRepository {
    fun getCurrentCoord(): Flow<LocationFlowResult<Coord>>
    fun getRealtimePathLocation(): Flow<LocationFlowResult<Coord>>
    suspend fun postPetLocation(token: String, location: PetLocation): Flow<AuthResult<PetLocation>>
}