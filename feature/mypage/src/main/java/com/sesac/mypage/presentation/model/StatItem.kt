package com.sesac.mypage.presentation.model

import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.vector.ImageVector

data class StatItem(
    val icon: ImageVector,
    val label: String,
    val value: String,
    val brush: Brush
)