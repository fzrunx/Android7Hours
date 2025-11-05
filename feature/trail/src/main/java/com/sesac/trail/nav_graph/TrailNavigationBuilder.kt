package com.sesac.trail.nav_graph

import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.sesac.trail.presentation.ui.TrailRecommendScreen

fun NavGraphBuilder.TrailSection() {
    composable<TrailNavigationRoute.TrailRecommendTab> {
        TrailRecommendScreen(
        )
    }
}