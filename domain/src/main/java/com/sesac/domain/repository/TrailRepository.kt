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
    // MyRecord 관련
    suspend fun getAllMyRecord(): Flow<List<MyRecord?>>
    suspend fun addMyRecord(newRecord: MyRecord): Flow<Boolean>
    // Room DB (Local) 관련 추가
    suspend fun saveDraft(draft: UserPath): Flow<Boolean>            // 작성중인 Path 저장
    suspend fun getAllDrafts(): Flow<List<UserPath>>                 // 저장된 Draft 불러오기
    suspend fun deleteDraft(draft: UserPath): Flow<Boolean>         // Draft 삭제
    suspend fun clearAllDrafts(): Flow<Boolean>
}