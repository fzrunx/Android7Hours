package com.sesac.domain.local.repository

import com.sesac.domain.local.model.LatLngPoint
import kotlinx.coroutines.flow.Flow

interface MonitorRepository {
    suspend fun getAllDummyLatLng(): Flow<List<LatLngPoint?>>
    suspend fun getRandomDummyLatLng(): Flow<LatLngPoint?>
}