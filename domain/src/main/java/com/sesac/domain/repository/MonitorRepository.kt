package com.sesac.domain.repository

import com.sesac.domain.model.Coord
import kotlinx.coroutines.flow.Flow

interface MonitorRepository {
    suspend fun getAllDummyLatLng(): Flow<List<Coord?>>
    suspend fun getRandomDummyLatLng(): Flow<Coord?>
}