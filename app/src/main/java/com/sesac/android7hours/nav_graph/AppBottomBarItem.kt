package com.sesac.android7hours.nav_graph

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Explore
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.People
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Videocam
import androidx.compose.ui.graphics.vector.ImageVector
import com.sesac.community.nav_graph.CommunityNavigationRoute
import com.sesac.home.nav_graph.BottomBarItem
import com.sesac.home.nav_graph.HomeNavigationRoute
import com.sesac.monitor.nav_graph.MonitorNavigationRoute
import com.sesac.mypage.nav_graph.MypageNavigationRoute
import com.sesac.trail.nav_graph.TrailNavigationRoute

data class AppBottomBarItem(
    override val tabName: String = "",
    override val icon: ImageVector = Icons.Filled.Home,
    override val destination: Any = HomeNavigationRoute.HomeTab
): BottomBarItem {
    override fun fetch() = listOf(
        AppBottomBarItem(
            tabName = "Home",
            icon = Icons.Filled.Home,
            destination = HomeNavigationRoute.HomeTab
        ),
        AppBottomBarItem(
            tabName = "Trail",
            icon = Icons.Filled.Explore,
            destination = TrailNavigationRoute.TrailMainTab
        ),
        AppBottomBarItem(
            tabName = "Monitor",
            icon = Icons.Filled.Videocam,
            destination = MonitorNavigationRoute.MainTab
        ),
        AppBottomBarItem(
            tabName = "Community",
            icon = Icons.Filled.People,
            destination = CommunityNavigationRoute.MainTab
        ),
        AppBottomBarItem(
            tabName = "Mypage",
            icon = Icons.Filled.Person,
            destination = MypageNavigationRoute.MainTab
        ),
    )

//    companion object {
//    }
}