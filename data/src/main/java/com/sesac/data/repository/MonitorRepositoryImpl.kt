package com.sesac.data.repository

import com.sesac.data.source.local.datasource.MockMonitor
import com.sesac.domain.local.model.LatLngPoint
import com.sesac.domain.local.repository.MonitorRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class MonitorRepositoryImpl @Inject constructor(): MonitorRepository {
    override suspend fun getAllDummyLatLng(): Flow<List<LatLngPoint?>> = flow {
        emit(MockMonitor.latLngPointList)
    }

    override suspend fun getRandomDummyLatLng(): Flow<LatLngPoint?> = flow {
        emit(MockMonitor.latLngPointList.random())
    }
}