package com.sesac.trail.nav_graph

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.sesac.trail.presentation.TrailMapViewLifecycleHelper
import com.sesac.trail.presentation.ui.TrailMainScreen



fun NavGraphBuilder.trailRoute(
    navController: NavController,
    trailLifecycleHelper: TrailMapViewLifecycleHelper,) {
    composable<TrailNavigationRoute.TrailMainTab> {
        TrailMainScreen(
            navController = navController,
            trailLifecycleHelper = trailLifecycleHelper
        )
    }
}