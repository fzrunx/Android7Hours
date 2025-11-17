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
import com.sesac.common.component.CommonMapLifecycle
import com.sesac.community.nav_graph.communityRoute
import com.sesac.domain.local.model.UserPath
import com.sesac.home.nav_graph.homeRoute
import com.sesac.monitor.nav_graph.monitorRoute
import com.sesac.mypage.nav_graph.mypageRoute
import com.sesac.trail.nav_graph.trailRoute
import com.sesac.trail.presentation.TrailViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AppNavHost(
    paddingValues: PaddingValues,
    trailViewModel: TrailViewModel,
    navController: NavHostController,
    nav2Home: () -> Unit,
    startDestination: Any,
    isSearchOpen: MutableState<Boolean>,
    onStartFollowing: (UserPath) -> Unit,
    commonMapLifecycle: CommonMapLifecycle,
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
            commonMapLifecycle = commonMapLifecycle,
//            path = ,
//            onSave = onSave,
            )
        communityRoute(isSearchOpen = isSearchOpen,)
        monitorRoute(
            navController = navController,
            commonMapLifecycle = commonMapLifecycle,
        )
        mypageRoute(
            navController = navController,
            permissionState = permissionState,
        )
        authRoute(
            navController = navController,
            nav2Home = nav2Home,
        )
    }
}