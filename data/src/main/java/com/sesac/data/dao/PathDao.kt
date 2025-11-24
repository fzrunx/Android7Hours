package com.sesac.data.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.sesac.data.entity.PathEntity
import com.sesac.data.type.DraftStatus
import kotlinx.coroutines.flow.Flow

@Dao
interface PathDao {
    // Draft 저장 (덮어쓰기)
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertDraft(draft: PathEntity): Long

    // Draft 업데이트
    @Update
    suspend fun updateDraft(draft: PathEntity)

    // Draft 삭제
    @Delete
    suspend fun deleteDraft(draft: PathEntity)

    // 상태별 Draft 목록 조회 (Flow 지원)
    @Query("SELECT * FROM Path WHERE status = :status")
    fun getDraftsByStatusFlow(status: DraftStatus = DraftStatus.DRAFT): Flow<List<PathEntity>>

    // 상태별 Draft 목록 단발 조회
    @Query("SELECT * FROM Path WHERE status = :status")
    suspend fun getDraftsByStatus(status: DraftStatus = DraftStatus.DRAFT): List<PathEntity>

    // ID로 단일 Draft 조회
    @Query("SELECT * FROM Path WHERE id = :id LIMIT 1")
    suspend fun getDraftById(id: Int): PathEntity?

    // 상태별 Draft 삭제
    @Query("DELETE FROM Path WHERE status = :status")
    suspend fun deleteDraftsByStatus(status: DraftStatus = DraftStatus.DRAFT)

    // 전체 Draft 삭제
    @Query("DELETE FROM Path")
    suspend fun clearAllDrafts()
}