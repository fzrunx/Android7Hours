package com.sesac.android7hours.common

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.Icons.AutoMirrored
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Menu
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.navigation.NavBackStackEntry
data class TopBarData(
    var title: String = "",
//    var titleIcon: ImageVector = AutoMirrored.Filled.ArrowBack
    var titleIcon: ImageVector = AutoMirrored.Filled.ArrowBack
)

val NavBackStackEntry.topBarAsRouteName: TopBarData
    get() {
        val routeName = destination.route ?: return TopBarData()
        return when {
            routeName.contains("HomeTab") -> {
                TopBarData(title = "Home", titleIcon = Icons.Default.Menu)
            }

            routeName.contains("Trail") -> {
                TopBarData(title = "Trail")
            }

            routeName.contains("Community") -> {
                TopBarData(title = "Community")
            }

            routeName.contains("Monitor") -> {
                TopBarData(title = "Monitor")
            }

            routeName.contains("Mypage") -> {
                TopBarData(title = "Mypage")
            }

            else -> throw IllegalArgumentException("내가 선언하지 않은 Route Screen")
        }
    }

