package com.sesac.android7hours.common

import android.util.Log
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.Icons.AutoMirrored
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Menu
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.painterResource
import androidx.navigation.NavBackStackEntry
import com.sesac.common.R as cR

data class TopBarData(
    var title: String = "",
    var titleIcon: Any = cR.drawable.image7hours
)

val NavBackStackEntry.topBarAsRouteName: TopBarData
    get() {
        val routeName = destination.route ?: return TopBarData()
        Log.d("Tag TopBarData", routeName)
        return when {
            routeName.contains("HomeTab") -> {
                TopBarData(title = "7Hours")
            }

            routeName.contains("Trail") -> {
                TopBarData(title = "산책로")
            }

            routeName.contains("Community") -> {
                TopBarData(title = "커뮤니티")
            }

            routeName.contains("Monitor") -> {
                TopBarData(title = "모니터링")
            }

            routeName.contains("MypageNavigationRoute.MainTab") -> {
                TopBarData(title = "마이페이지")
            }

            routeName.contains("Manage") -> {
                TopBarData(title = "일정관리", titleIcon = AutoMirrored.Filled.ArrowBack)
            }

            routeName.contains("Favorite") -> {
                TopBarData(title = "즐겨찾기", titleIcon = AutoMirrored.Filled.ArrowBack)
            }

            routeName.contains("Setting") -> {
                TopBarData(title = "설정", titleIcon = AutoMirrored.Filled.ArrowBack)
            }

            else -> throw IllegalArgumentException(routeName)
        }
    }

