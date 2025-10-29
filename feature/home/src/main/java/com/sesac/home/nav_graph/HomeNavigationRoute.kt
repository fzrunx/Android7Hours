package com.sesac.home.nav_graph

import kotlinx.serialization.Serializable

@Serializable
sealed interface HomeNavigationRoute {
    @Serializable
    data object HomeTab: HomeNavigationRoute
}