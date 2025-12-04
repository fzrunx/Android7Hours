package com.sesac.common.ui.theme

import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color

// =================================================================
// 1. 내부 팔레트 (중복되는 색상 원본 정의)
// =================================================================
private object Palette {
    val PurpleMain = Color(0xFF8B5CF6) // Primary, ColorPurple
    val PurpleDark = Color(0xFF7C3AED) // PrimaryPurple
    val GreenMain = Color(0xFF22C55E)  // AccentGreen, ColorGreen
    val BlueMain = Color(0xFF3B82F6)   // ColorBlue
    val RedMain = Color(0xFFEF4444)    // Error, Red500

    val Gray50 = Color(0xFFF9FAFB)
    val Gray100 = Color(0xFFF3F4F6)
    val Gray200 = Color(0xFFE5E7EB)
    val Gray300 = Color(0xFFD1D5DB)
    val Gray400 = Color(0xFF9CA3AF)
    val Gray500 = Color(0xFF6B7280)
    val Gray700 = Color(0xFF374151)
    val Gray900 = Color(0xFF1F2937)
}

// =================================================================
// 2. Material 3 기본 색상 (템플릿 호환용)
// =================================================================
val Purple80 = Color(0xFFD0BCFF)
val PurpleGrey80 = Color(0xFFCCC2DC)
val Pink80 = Color(0xFFEFB8C8)

val Purple40 = Color(0xFF6650a4)
val PurpleGrey40 = Color(0xFF625b71)
val Pink40 = Color(0xFF7D5260)

// =================================================================
// 3. 앱 메인 테마 색상 (App Custom Colors)
// =================================================================
val White = Color.White
val Background = Palette.Gray50
val Surface = Color.White

// Brand Colors
val Primary = Palette.PurpleMain
val PrimaryPurple = Palette.PurpleDark
val PrimaryPurpleLight = Color(0xFFF5F3FF)

val PrimaryGreenLight = Color(0xFFDBE8CC) // Header와 동일
val PrimaryGreenMedium = Color(0xFFB8D4A8)
val PrimaryGreenDark = Color(0xFF2C4A6E)
val Header = PrimaryGreenLight

// Text Colors
val TextPrimary = Palette.Gray900
val TextSecondary = Palette.Gray500
val TextDisabled = Palette.Gray400
val TextOnPrimary = Color.White
val GrayTabText = Color(0xFF4B5563)

// UI Element Colors
val Icon = Color(0xFF4B5563)
val Border = Palette.Gray200
val Indicator = Palette.Gray300
val ButtonSecondary = Palette.Gray100
val OnButtonSecondary = Palette.Gray700
val SheetHandle = Palette.Gray300
val NoteBox = Palette.Gray100

// Status & Point Colors
val Error = Palette.RedMain
val OnError = Color(0xFFFEE2E2)
val AccentGreen = Palette.GreenMain
val star = Color(0xFFF59E0B)

// Color Group (Legacy support)
val ColorGreen = Palette.GreenMain
val ColorBlue = Palette.BlueMain
val ColorPurple = Palette.PurpleMain
val ColorPink = Color(0xFFEC4899)
val ColorOrange = Color(0xFFF97316)

// =================================================================
// 4. 추가 색상 팔레트 (Extra Palette)
// =================================================================
val Red500 = Palette.RedMain
val Yellow400 = Color(0xFFFACC15)

// Purple Variations
val Purple100 = Color(0xFFEDE9FE)
val Purple500 = Color(0xFF6200EE) // 참고: 기존 코드에 6200EE와 8B5CF6가 혼재되어 있었음.
val Purple600 = Color(0xFF5D3E8C)
val Purple700 = Color(0xFF6B21A8)

// Light Variations
val LightPurple = Color(0xFFEBE5FF)
val LightBlue = Color(0xFFE0F3FF)
val LightGreen = Color(0xFFE0FFE8)
val LightGray = Color(0xFFEEF0F2)
val LightPink = Color(0xFFFDF0F3)
val LightBlue2 = Color(0xFFEFF5FF)

// Gray Variations (직접 호출용)
val Gray200 = Palette.Gray200
val Gray300 = Palette.Gray300
val Gray400 = Palette.Gray400
val Gray500 = Palette.Gray500

// =================================================================
// 5. 기능별 색상 (Feature Specific)
// =================================================================

// Permission (권한)
val permEnabledBorder = Color(0xFFD8B4FE)
val permEnabledBg = Color(0xFFF5F3FF)
val badgeEnabledBg = Color(0xFFDCFCE7)
val badgeEnabledText = Color(0xFF166534)

// Info Box (안내 박스)
val infoBoxBg = Color(0xFFEFF6FF)
val infoBoxBorder = Color(0xFFBFDBFE)
val infoBoxTitle = Color(0xFF1E3A8A)
val infoBoxText = Color(0xFF1D4ED8)

// Container (컨테이너)
val primaryContainer = Color(0xFFEDE9FE)
val OnPrimaryContainer = Color(0xFF6D28D9)

// =================================================================
// 6. 브러시 & 그라데이션 (Brush)
// =================================================================
val brushPurple = Brush.verticalGradient(listOf(Color(0xFFA855F7), Palette.PurpleMain))
val brushBlue = Brush.verticalGradient(listOf(Color(0xFF60A5FA), Palette.BlueMain))
val brushGreen = Brush.verticalGradient(listOf(Color(0xFF4ADE80), Palette.GreenMain))

// Stat Graphs
val StatPurple = brushPurple
val StatBlue = brushBlue
val StatGreen = brushGreen