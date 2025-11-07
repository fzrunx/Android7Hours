package com.sesac.trail.presentation.model

import androidx.compose.ui.graphics.Color

data class MyRecord(
    val id: Int,
    val name: String,
    val date: String,
    val distance: String,
    val time: String,
    val steps: Int,
    val calories: Int,
    val color: Color
)
