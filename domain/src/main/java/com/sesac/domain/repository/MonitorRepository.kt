package com.sesac.domain.repository

import com.sesac.domain.model.LatLngPoint
import kotlinx.coroutines.flow.Flow

interface MonitorRepository {
    suspend fun getAllDummyLatLng(): Flow<List<LatLngPoint?>>
    suspend fun getRandomDummyLatLng(): Flow<LatLngPoint?>
}