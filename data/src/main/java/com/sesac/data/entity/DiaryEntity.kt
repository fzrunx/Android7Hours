package com.sesac.data.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "diaries")
data class DiaryEntity(
    @PrimaryKey
    val scheduleId: Long,  // Schedule ID를 PK로 사용
    val pathId: Int,
    val diary: String,
    val createdAt: Long = System.currentTimeMillis(),
    val isSynced: Boolean = false  // 서버 동기화 여부
)
