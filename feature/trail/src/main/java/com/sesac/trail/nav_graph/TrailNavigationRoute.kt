package com.sesac.trail.nav_graph

import kotlinx.serialization.Serializable

@Serializable
sealed interface TrailNavigationRoute {
    @Serializable
    data object TrailRecommendTab: TrailNavigationRoute
    @Serializable
    data object TrailFollowTab: TrailNavigationRoute
    @Serializable
    data object TrailRecordTab: TrailNavigationRoute
}