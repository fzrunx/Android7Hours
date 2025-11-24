package com.sesac.data.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.sesac.data.type.DraftStatus


@Entity(tableName = "Path")
data class PathEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val pathName: String,
    val pathComment: String?,
    val markers: String? = null,
    val level: Int = 2,
    val distance: Float? = 0f,
    val duration: Int = 0,
    val isPrivate: Boolean = true,
    val thumbnail: String = "",
    val geom: String = "",
    val authUserId: Int,
    val createdAt: Long = System.currentTimeMillis(),
    val updatedAt: Long = System.currentTimeMillis(),
    val status: DraftStatus = DraftStatus.DRAFT,
)



