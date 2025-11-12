package com.sesac.auth.nav_graph

import kotlinx.serialization.Serializable

@Serializable
sealed interface AuthNavigationRoute {
    @Serializable
    data object JoinTab: AuthNavigationRoute
    @Serializable
    data object LoginTab: AuthNavigationRoute
}