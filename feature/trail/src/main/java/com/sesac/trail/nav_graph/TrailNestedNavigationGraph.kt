package com.sesac.trail.nav_graph

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.navigation.toRoute
import com.sesac.common.model.PathParceler
import com.sesac.common.model.parcelableType
import com.sesac.domain.model.Path
import com.sesac.domain.result.AuthUiState
import com.sesac.trail.presentation.TrailViewModel
import com.sesac.trail.presentation.ui.TrailDetailScreen
import kotlin.reflect.typeOf

fun NavGraphBuilder.trailNestedNavGraph(
    uiState: AuthUiState,
    trailViewModel: TrailViewModel,
    navController: NavController,
    onStartFollowing: (Path) -> Unit,
) {
    composable<NestedNavigationRoute.TrailDetail>(
        typeMap = mapOf(typeOf<PathParceler>() to parcelableType<PathParceler>())
    ) { navBackStackEntry ->
        val selectedDetailPath = navBackStackEntry.toRoute<NestedNavigationRoute.TrailDetail>().pathParceler.toPath()
        TrailDetailScreen(
            uiState = uiState,
            viewModel = trailViewModel,
            navController = navController,
            selectedDetailPath = selectedDetailPath,
            onStartFollowing = onStartFollowing,
            onEditClick = { path ->
                navController.navigate(TrailNavigationRoute.TrailCreateTab)
            },
            onDeleteClick = { path ->
                trailViewModel.deletePath(uiState.token, path.id)
                navController.popBackStack()
            }
        )
    }
}