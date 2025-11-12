package com.sesac.data.repository

import com.sesac.data.source.local.datasource.MockTrail
import com.sesac.domain.local.model.MyRecord
import com.sesac.domain.local.model.UserPath
import com.sesac.domain.local.repository.TrailRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class TrailRepositoryImpl @Inject constructor(): TrailRepository {
    override suspend fun getAllRecommendedPaths(): Flow<List<UserPath?>> = flow {
        emit(MockTrail.recommendedPaths)
    }

    override suspend fun getAllMyRecord(): Flow<List<MyRecord?>> = flow {
        emit(MockTrail.myRecord)
    }

    override suspend fun addMyRecord(newRecord: MyRecord): Flow<Boolean> = flow {
        MockTrail.myRecord.add(newRecord)
        emit(true)
    }
}