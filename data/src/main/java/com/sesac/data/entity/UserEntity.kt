package com.sesac.data.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "user")
data class UserEntity(
    @PrimaryKey(autoGenerate = true)
    val user_id: Int = 0,
    val password: String,
    val username: String,
    val email: String,
    val phone: String?,
    val address: String?
)