package com.sesac.data.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.sesac.data.entity.DiaryEntity
@Dao
interface DiaryDao {
    @Query("SELECT * FROM diaries WHERE scheduleId = :scheduleId")
    suspend fun getDiaryByScheduleId(scheduleId: Long): DiaryEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertDiary(diary: DiaryEntity)

    @Query("DELETE FROM diaries WHERE scheduleId = :scheduleId")
    suspend fun deleteDiary(scheduleId: Long)

    @Query("SELECT * FROM diaries WHERE isSynced = 0")
    suspend fun getUnsyncedDiaries(): List<DiaryEntity>

    @Query("UPDATE diaries SET isSynced = 1 WHERE scheduleId = :scheduleId")
    suspend fun markAsSynced(scheduleId: Long)
}