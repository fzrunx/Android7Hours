package com.sesac.data.repository

import com.sesac.data.source.local.datasource.MockMonitor
import com.sesac.domain.model.Coord
import com.sesac.domain.repository.MonitorRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class MonitorRepositoryImpl @Inject constructor(): MonitorRepository {
    override suspend fun getAllDummyLatLng(): Flow<List<Coord?>> = flow {
        emit(MockMonitor.coordList)
    }

    override suspend fun getRandomDummyLatLng(): Flow<Coord?> = flow {
        emit(MockMonitor.coordList.random())
    }
}