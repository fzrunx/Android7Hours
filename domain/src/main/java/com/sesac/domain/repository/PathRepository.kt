package com.sesac.domain.repository

import com.sesac.domain.model.BookmarkResponse
import com.sesac.domain.model.Coord
import com.sesac.domain.model.MyRecord
import com.sesac.domain.model.Path
import com.sesac.domain.result.AuthResult
import kotlinx.coroutines.flow.Flow

interface PathRepository {
    suspend fun getAllRecommendedPaths(coord: Coord?, radius: Float?): Flow<AuthResult<List<Path>>>
    suspend fun getMyPaths(token: String): Flow<AuthResult<List<Path>>>
    suspend fun createPath(token: String, path: Path): Flow<AuthResult<Path>>
    suspend fun updatePath(token: String, id: Int, updatedPath: Path): Flow<AuthResult<Path>>
    suspend fun deletePath(token: String, id: Int): Flow<AuthResult<Unit>>
    suspend fun toggleBookmark(token: String, id: Int): Flow<AuthResult<BookmarkResponse>>

    suspend fun getAllMyRecord(): Flow<List<MyRecord?>>
    suspend fun addMyRecord(newRecord: MyRecord): Flow<Boolean>
}