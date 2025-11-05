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
import com.sesac.common.utils.MapViewLifecycleHelper
import com.sesac.community.nav_graph.communityRoute
import com.sesac.home.nav_graph.homeRoute
import com.sesac.monitor.nav_graph.monitorRoute
import com.sesac.mypage.nav_graph.mypageRoute
import com.sesac.trail.nav_graph.trailRoute

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AppNavHost(
    paddingValues: PaddingValues,
    navController: NavHostController,
    startDestination: Any,
    isSearchOpen: MutableState<Boolean>,
    lifecycleHelper: MapViewLifecycleHelper,
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
        trailRoute()
        communityRoute(isSearchOpen = isSearchOpen,)
        monitorRoute(
            navController = navController,
            lifecycleHelper = lifecycleHelper,
        )
        mypageRoute(
            navController = navController,
            permissionState = permissionState,
        )
    }
}