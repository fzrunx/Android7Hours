package com.sesac.domain.model

data class PathInfo(
    val id: Int,
    val title: String,
    val imageUrl: String,
    val distance: Float,
    val timeTaken: Int,
    val difficulty: String
)