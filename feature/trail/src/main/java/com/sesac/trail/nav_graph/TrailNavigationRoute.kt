package com.sesac.trail.nav_graph

import com.sesac.common.model.PathParceler
import kotlinx.serialization.Serializable

@Serializable
sealed interface TrailNavigationRoute {
    @Serializable
    data object TrailMainTab: TrailNavigationRoute
//    @Serializable
//    data object TrailDetailTab: TrailNavigationRoute
    @Serializable
    data object TrailCreateTab: TrailNavigationRoute
}

@Serializable
sealed interface NestedNavigationRoute {
    @Serializable
    data class TrailDetail(val pathParceler: PathParceler) : NestedNavigationRoute
}
