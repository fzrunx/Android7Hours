package com.sesac.trail.nav_graph

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.sesac.common.component.CommonMapLifecycle
import com.sesac.domain.result.AuthUiState
import com.sesac.trail.presentation.TrailViewModel
import com.sesac.trail.presentation.ui.TrailCreateScreen
import com.sesac.trail.presentation.ui.TrailDetailScreen
import com.sesac.trail.presentation.ui.TrailMainScreen


fun NavGraphBuilder.trailRoute(
    trailViewModel: TrailViewModel,
    navController: NavController,
    uiState: AuthUiState,
    onStartFollowing: (Any) -> Unit,
    commonMapLifecycle : CommonMapLifecycle,
) {
    composable<TrailNavigationRoute.TrailMainTab> {
        TrailMainScreen(
            viewModel = trailViewModel,
            navController = navController,
            uiState = uiState,
            commonMapLifecycle = commonMapLifecycle,
        )
    }
    composable<TrailNavigationRoute.TrailCreateTab> {
        TrailCreateScreen(
            viewModel = trailViewModel,
            navController = navController,
            uiState = uiState
        )
    }
    composable<TrailNavigationRoute.TrailDetailTab> {
        TrailDetailScreen(
            viewModel = trailViewModel,
            uiState = uiState,
            navController = navController,
            onStartFollowing = onStartFollowing,
        )
    }
}