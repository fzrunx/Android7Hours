package com.sesac.domain.local.repository

import com.sesac.domain.local.model.MyRecord
import com.sesac.domain.local.model.UserPath
import kotlinx.coroutines.flow.Flow

interface TrailRepository {
    suspend fun getAllRecommendedPaths(): Flow<List<UserPath?>>
    suspend fun getAllMyRecord(): Flow<List<MyRecord?>>
    suspend fun addMyRecord(newRecord: MyRecord): Flow<Boolean>
}