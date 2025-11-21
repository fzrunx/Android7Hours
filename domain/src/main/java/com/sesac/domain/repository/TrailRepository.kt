package com.sesac.domain.repository

import com.sesac.domain.model.Coord
import com.sesac.domain.model.MyRecord
import com.sesac.domain.model.UserPath
import com.sesac.domain.result.AuthResult
import kotlinx.coroutines.flow.Flow

interface TrailRepository {
    suspend fun getAllRecommendedPaths(coord: Coord?, radius: Float?): Flow<AuthResult<List<UserPath>>>
    suspend fun getMyPaths(token: String): Flow<AuthResult<List<UserPath>>>
    suspend fun createPath(token: String, path: UserPath): Flow<AuthResult<UserPath>>
    suspend fun updatePath(token: String, id: Int, updatedPath: UserPath): Flow<AuthResult<UserPath>>
    suspend fun deletePath(token: String, id: Int): Flow<AuthResult<Unit>>
    suspend fun getAllMyRecord(): Flow<List<MyRecord?>>
    suspend fun addMyRecord(newRecord: MyRecord): Flow<Boolean>
}