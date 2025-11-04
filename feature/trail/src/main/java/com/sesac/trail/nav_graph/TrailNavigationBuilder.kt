package com.sesac.trail.nav_graph

import androidx.compose.runtime.MutableState
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.sesac.trail.presentation.ui.TrailRecommendScreen

fun NavGraphBuilder.TrailSection(trailSelectedMenu: MutableState<String>) {
    composable<TrailNavigationRoute.TrailRecommendTab> {
        TrailRecommendScreen(
            trailSelectedMenu = trailSelectedMenu,
        )
    }
}