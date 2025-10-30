package com.sesac.android7hours.nav_graph

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Explore
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.People
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Videocam
import androidx.compose.ui.graphics.vector.ImageVector
import com.sesac.community.nav_graph.CommunityNavigationRoute
import com.sesac.home.nav_graph.HomeNavigationRoute
import com.sesac.monitor.presentation.nav_graph.MonitorNavigationRoute
import com.sesac.mypage.nav_graph.MypageNavigationRoute
import com.sesac.trail.nav_graph.TrailNavigationRoute

data class BottomBarItem(
    val tabName: String = "",
    val icon: ImageVector = Icons.Filled.Home,
    val destination: Any = HomeNavigationRoute.HomeTab
) {
    companion object {
        fun fetchBottomBarItems() = listOf(
            BottomBarItem(
                tabName = "Home",
                icon = Icons.Filled.Home,
                destination = HomeNavigationRoute.HomeTab
            ),
            BottomBarItem(
                tabName = "Trail",
                icon = Icons.Filled.Explore,
                destination = TrailNavigationRoute.TrailRecommendTab
            ),
            BottomBarItem(
                tabName = "Monitor",
                icon = Icons.Filled.Videocam,
                destination = MonitorNavigationRoute.RecommendTab
            ),
            BottomBarItem(
                tabName = "Community",
                icon = Icons.Filled.People,
                destination = CommunityNavigationRoute.CommunityMainTab
            ),
            BottomBarItem(
                tabName = "Mypage",
                icon = Icons.Filled.Person,
                destination = MypageNavigationRoute.MypageMainTab
            ),
        )
    }
}