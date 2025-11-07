package com.sesac.monitor.nav_graph

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.sesac.monitor.presentation.ui.MonitorGpsScreen
import com.sesac.monitor.presentation.ui.MonitorMainScreen
import com.sesac.monitor.presentation.MonitorMapViewLifecycleHelper

fun NavGraphBuilder.monitorRoute(
    navController: NavController,
    monitorLifecycleHelper: MonitorMapViewLifecycleHelper,
) {
    composable<MonitorNavigationRoute.MainTab> {
        MonitorMainScreen(
            navController = navController,
            monitorLifecycleHelper = monitorLifecycleHelper,
        )
    }
    composable<MonitorNavigationRoute.GpsTab> {
        MonitorGpsScreen(
            monitorLifecycleHelper = monitorLifecycleHelper
        )
    }
}