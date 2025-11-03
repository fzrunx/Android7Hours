package com.sesac.common.data.model.relation

import androidx.room.Entity

@Entity(
    tableName = "post_image",
    primaryKeys = ["post_id", "image_id"]
)
data class PostImageCrossRef(
    val post_id: Int,
    val image_id: Int
)