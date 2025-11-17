package com.sesac.android7hours.nav_graph

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.snapshots.SnapshotStateMap
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import com.sesac.auth.nav_graph.authRoute
import com.sesac.community.nav_graph.communityRoute
import com.sesac.domain.result.AuthUiState
import com.sesac.home.nav_graph.homeRoute
import com.sesac.monitor.nav_graph.monitorRoute
import com.sesac.monitor.presentation.MonitorMapViewLifecycleHelper
import com.sesac.mypage.nav_graph.mypageRoute
import com.sesac.trail.nav_graph.trailRoute
import com.sesac.trail.presentation.TrailMapViewLifecycleHelper
import com.sesac.trail.presentation.TrailViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AppNavHost(
    paddingValues: PaddingValues,
    trailViewModel: TrailViewModel,
    navController: NavHostController,
    nav2Home: () -> Unit,
    nav2LoginScreen: () -> Unit,
    startDestination: Any,
    uiState: AuthUiState,
    isSearchOpen: MutableState<Boolean>,
    onStartFollowing: (Any) -> Unit,
    monitorLifecycleHelper: MonitorMapViewLifecycleHelper,
    trailLifecycleHelper: TrailMapViewLifecycleHelper,
//    onSave: () -> Unit,
    permissionState: SnapshotStateMap<String, Boolean>,
    ) {
    NavHost(
        modifier = Modifier.padding(paddingValues = paddingValues),
        navController = navController,
        startDestination = startDestination,
//        contentAlignment = ,
//        route = null,
//        typeMap = emptyMap(),
//        enterTransition = ,
//        exitTransition = ,
//        popEnterTransition = ,
//        popExitTransition = ,
//        sizeTransform = ,
    ) {
        homeRoute()
        trailRoute(
            trailViewModel = trailViewModel,
            navController = navController,
            onStartFollowing = onStartFollowing,
            trailLifecycleHelper = trailLifecycleHelper,
//            path = ,
//            onSave = onSave,
            )
        communityRoute(isSearchOpen = isSearchOpen,)
        monitorRoute(
            navController = navController,
            monitorLifecycleHelper = monitorLifecycleHelper,
        )
        mypageRoute(
            navController = navController,
            nav2LoginScreen = nav2LoginScreen,
            permissionState = permissionState,
            uiState = uiState,
        )
        authRoute(
            navController = navController,
            nav2Home = nav2Home,
        )
    }
}