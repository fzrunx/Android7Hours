package com.sesac.trail.nav_graph

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.sesac.common.component.CommonMapLifecycle
import com.sesac.domain.local.model.UserPath
import com.sesac.trail.presentation.TrailViewModel
import com.sesac.trail.presentation.ui.TrailCreateScreen
import com.sesac.trail.presentation.ui.TrailDetailScreen
import com.sesac.trail.presentation.ui.TrailMainScreen


fun NavGraphBuilder.trailRoute(
    trailViewModel: TrailViewModel,
    navController: NavController,
//    onSave: () -> Unit,
//    path: UserPath,
    onStartFollowing: (UserPath) -> Unit,
    commonMapLifecycle : CommonMapLifecycle,
) {
    composable<TrailNavigationRoute.TrailMainTab> {
        TrailMainScreen(
            viewModel = trailViewModel,
            navController = navController,
            commonMapLifecycle = commonMapLifecycle,
        )
    }
    composable<TrailNavigationRoute.TrailCreateTab> {
        TrailCreateScreen(
            viewModel = trailViewModel,
            navController = navController,
//            onSave = onSave,
        )
    }
    composable<TrailNavigationRoute.TrailDetailTab> {
        TrailDetailScreen(
//            path = path,
            viewModel = trailViewModel,
            navController = navController,
            onStartFollowing = onStartFollowing,
        )
    }
}