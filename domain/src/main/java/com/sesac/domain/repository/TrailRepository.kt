package com.sesac.domain.repository

import com.sesac.domain.model.MyRecord
import com.sesac.domain.model.UserPath
import kotlinx.coroutines.flow.Flow

interface TrailRepository {
    suspend fun getAllRecommendedPaths(): Flow<List<UserPath?>>
    suspend fun getAllMyRecord(): Flow<List<MyRecord?>>
    suspend fun addMyRecord(newRecord: MyRecord): Flow<Boolean>
}