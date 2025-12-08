package com.sesac.monitor.nav_graph

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.sesac.common.component.CommonMapLifecycle
import com.sesac.monitor.presentation.ui.MonitorGpsScreen
import com.sesac.monitor.presentation.ui.MonitorMainScreen


fun NavGraphBuilder.monitorRoute(
    navController: NavController,
    commonMapLifecycle: CommonMapLifecycle,
) {
    composable<MonitorNavigationRoute.MainTab> {
        MonitorMainScreen(
            navController = navController,
            commonMapLifecycle = commonMapLifecycle,
        )
    }
//    composable<MonitorNavigationRoute.GpsTab> {
//        MonitorGpsScreen(
//            commonMapLifecycle = commonMapLifecycle,
//        )
//    }
}