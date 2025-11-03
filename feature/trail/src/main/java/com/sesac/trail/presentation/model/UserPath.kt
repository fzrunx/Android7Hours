package com.sesac.trail.presentation.model

import androidx.compose.ui.graphics.Color

data class UserPath(
    val id: Int,
    val name: String,
    val uploader: String,
    val distance: String,
    val time: String,
    val likes: Int,
    val distanceFromMe: String,
    val color: Color
)
