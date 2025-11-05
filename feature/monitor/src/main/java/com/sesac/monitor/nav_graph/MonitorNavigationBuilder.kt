package com.sesac.monitor.nav_graph

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.sesac.monitor.presentation.ui.MonitorGpsScreen
import com.sesac.monitor.presentation.ui.MonitorMainScreen
import com.sesac.common.utils.MapViewLifecycleHelper

fun NavGraphBuilder.monitorRoute(
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