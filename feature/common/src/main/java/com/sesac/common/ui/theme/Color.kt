package com.sesac.common.ui.theme

import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color

// --- 기본 색상 ---
val Purple80 = Color(0xFFD0BCFF)
val PurpleGrey80 = Color(0xFFCCC2DC)
val Pink80 = Color(0xFFEFB8C8)

val Purple40 = Color(0xFF6650a4)
val PurpleGrey40 = Color(0xFF625b71)
val Pink40 = Color(0xFF7D5260)

val Gray400 = Color(0xFF9CA3AF) // text-gray-400

// --- 앱 커스텀 색상 ---
val Background = Color(0xFFF9FAFB)
val Surface = Color.White
val Header = Color(0xFFDBE8CC)
val Primary = Color(0xFF8B5CF6)  // purple-600
val TextPrimary = Color(0xFF1F2937) // text-gray-900
val TextSecondary = Color(0xFF6B7280)   // text-gray-500
val TextDisabled = Color(0xFF9CA3AF) // text-gray-400
val TextOnPrimary = Color.White
val Icon = Color(0xFF4B5563)
val Border = Color(0xFFE5E7EB)
val Indicator = Color(0xFFD1D5DB)
val ButtonSecondary = Color(0xFFF3F4F6) // bg-gray-100
val OnButtonSecondary = Color(0xFF374151) // text-gray-700
val Error = Color(0xFFEF4444)
val OnError = Color(0xFFFEE2E2)
val AccentGreen = Color(0xFF22C55E)
val ColorGreen = Color(0xFF22C55E)
val ColorBlue = Color(0xFF3B82F6)
val ColorPurple = Color(0xFF8B5CF6)
val ColorPink = Color(0xFFEC4899)
val ColorOrange = Color(0xFFF97316)
val SheetHandle = Color(0xFFD1D5DB)
val NoteBox = Color(0xFFF3F4F6) // bg-gray-100
val star = Color(0xFFF59E0B) // text-yellow-500
val PrimaryPurple = Color(0xFF7C3AED) // purple-600
val PrimaryPurpleLight = Color(0xFFF5F3FF) // purple-100
val Red500 = Color(0xFFEF4444)

// --- 브러시 / 그라데이션 ---
val brushPurple = Brush.verticalGradient(listOf(Color(0xFFA855F7), Color(0xFF8B5CF6)))
val brushBlue = Brush.verticalGradient(listOf(Color(0xFF60A5FA), Color(0xFF3B82F6)))
val brushGreen = Brush.verticalGradient(listOf(Color(0xFF4ADE80), Color(0xFF22C55E)))

val StatPurple = brushPurple
val StatBlue = brushBlue
val StatGreen = brushGreen

// --- 권한 관련 색상 ---
val permEnabledBorder = Color(0xFFD8B4FE) // border-purple-200
val permEnabledBg = Color(0xFFF5F3FF) // bg-purple-50/50
val badgeEnabledBg = Color(0xFFDCFCE7) // bg-green-100
val badgeEnabledText = Color(0xFF166534) // text-green-700

// --- 권한 안내 / 정보 박스 ---
val infoBoxBg = Color(0xFFEFF6FF) // bg-blue-50
val infoBoxBorder = Color(0xFFBFDBFE) // border-blue-200
val infoBoxTitle = Color(0xFF1E3A8A) // text-blue-900
val infoBoxText = Color(0xFF1D4ED8) // text-blue-700

val OnPrimaryContainer = Color(0xFF6D28D9) // text-purple-700
val primaryContainer = Color(0xFFEDE9FE) // bg-purple-100
val Purple100 = Color(0xFFEDE9FE) // bg-purple-100