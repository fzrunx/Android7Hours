package com.sesac.home.nav_graph

import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.sesac.home.presentation.ui.HomeScreen

fun NavGraphBuilder.HomeSection() {
    composable<HomeNavigationRoute.HomeTab> {
        HomeScreen()
    }
}