package com.sesac.trail.nav_graph

import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.sesac.trail.presentation.ui.TrailRecommendScreen

fun NavGraphBuilder.trailRoute() {
    composable<TrailNavigationRoute.TrailRecommendTab> {
        TrailRecommendScreen(
        )
    }
}