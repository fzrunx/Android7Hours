package com.sesac.mypage.presentation.model

import androidx.compose.ui.graphics.vector.ImageVector

data class MenuItem(
    val icon: ImageVector,
    val label: String,
    val badgeCount: Int? = null,
    val route: String // 클릭 시 이동할 경로
)