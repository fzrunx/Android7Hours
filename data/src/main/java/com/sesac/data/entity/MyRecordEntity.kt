package com.sesac.data.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.Date

@Entity(tableName = "my_record")
data class MyRecordEntity(
    @PrimaryKey
    val id: Int,
    val name: String,
    val date: Date,
    val distance: Float,
    val time: Int,
    val steps: Int,
    val calories: Int,
    val color: String?
)
