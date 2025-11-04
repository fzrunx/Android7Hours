package com.sesac.monitor.nav_graph

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.naver.maps.map.MapView
import com.sesac.monitor.presentation.ui.MonitorGpsScreen
import com.sesac.monitor.presentation.ui.MonitorMainScreen
import com.sesac.monitor.utils.MapViewLifecycleHelper

fun NavGraphBuilder.MonitorSection(
    navController: NavController,
    lifecycleHelper: MapViewLifecycleHelper,
) {
    composable<MonitorNavigationRoute.MainTab> {
        MonitorMainScreen(
            navController = navController,
            lifecycleHelper = lifecycleHelper,
        )
    }
    composable<MonitorNavigationRoute.GpsTab> {
        MonitorGpsScreen(
            lifecycleHelper = lifecycleHelper
        )
    }
}