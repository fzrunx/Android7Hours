package com.sesac.community.presentation

import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

object AppTheme {
    // Colors
    val background = Color(0xFFF9FAFB) // bg-gray-50
    val surface = Color.White
    val header = Color(0xFFDBE8CC)
    val primary = Color(0xFF8B5CF6) // purple-600
    val primaryContainer = Color(0xFFEDE9FE) // bg-purple-100
    val onPrimaryContainer = Color(0xFF6D28D9) // text-purple-700
    val textPrimary = Color(0xFF1F2937) // text-gray-900
    val textSecondary = Color(0xFF6B7280) // text-gray-500
    val textDisabled = Color(0xFF9CA3AF) // text-gray-400
    val border = Color(0xFFF3F4F6) // border-gray-100
    val buttonSecondary = Color(0xFFF3F4F6) // bg-gray-100
    val onButtonSecondary = Color(0xFF374151) // text-gray-700
    val error = Color(0xFFEF4444) // text-red-600

    // Dimensions
    val paddingLarge = 16.dp
    val paddingMedium = 12.dp
    val paddingSmall = 8.dp
    val paddingMicro = 4.dp

    val headerHeight = 64.dp
    val avatarSize = 40.dp
    val iconSize = 20.dp
    val iconSizeSmall = 16.dp
    val cardImageHeight = 256.dp // h-64

    // Shapes
    val cardShape = RoundedCornerShape(12.dp)
    val buttonShape = RoundedCornerShape(8.dp)
}