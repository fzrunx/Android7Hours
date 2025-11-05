package com.sesac.home.nav_graph

import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.sesac.home.presentation.ui.HomeScreen

fun NavGraphBuilder.homeRoute() {
    composable<HomeNavigationRoute.HomeTab> {
        HomeScreen()
    }
}