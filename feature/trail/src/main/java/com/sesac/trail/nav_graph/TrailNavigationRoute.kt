package com.sesac.trail.nav_graph

import kotlinx.serialization.Serializable

@Serializable
sealed interface TrailNavigationRoute {
    @Serializable
    data object TrailMainTab: TrailNavigationRoute
    @Serializable
    data object TrailDetailTab: TrailNavigationRoute
    @Serializable
    data object TrailCreateTab: TrailNavigationRoute
}