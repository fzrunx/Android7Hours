package com.sesac.trail.presentation.model

import androidx.compose.ui.graphics.Color
import com.sesac.trail.presentation.ui.MapPosition

data class UserPath(
    val id: Int,
    val name: String,
    val uploader: String,
    val distance: String,
    val time: String,
    val likes: Int,
    val distanceFromMe: String, // React의 distance_from_me
    val mapPosition: MapPosition,
    val tags: List<String> = emptyList()
)

