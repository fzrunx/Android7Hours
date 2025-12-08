package com.sesac.data.entity

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey
import com.sesac.data.entity.UserEntity
import java.util.Date

@Entity(
    tableName = "post",
    foreignKeys = [
        ForeignKey(
            entity = UserEntity::class,
            parentColumns = ["user_id"],
            childColumns = ["user_id"],
            onDelete = ForeignKey.Companion.CASCADE
        )
    ]
)
data class PostEntity(
    @PrimaryKey(autoGenerate = true)
    val post_id: Int = 0,
    val user_id: Int,
    val title: String,
    val content: String,
    val create_at: Date,
    val status: String, // "private" or "public"
    val category: String,
)