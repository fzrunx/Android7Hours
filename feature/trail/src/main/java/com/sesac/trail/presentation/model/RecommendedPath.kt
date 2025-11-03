package com.sesac.trail.presentation.model

import androidx.compose.ui.graphics.Color

data class RecommendedPath(
    val id: Int,
    val name: String,
    val distance: String,
    val time: String,
    val rating: Double,
    val reviews: Int,
    val color: Color
)
