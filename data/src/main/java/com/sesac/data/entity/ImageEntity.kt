package com.sesac.data.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.Date

@Entity(tableName = "image")
data class ImageEntity(
    @PrimaryKey(autoGenerate = true)
    val image_id: Int = 0,
    val url: String,
    val description: String?,
    val create_at: Date
)