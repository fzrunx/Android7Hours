package com.sesac.community.nav_graph

import kotlinx.serialization.Serializable

@Serializable
sealed interface CommunityNavigationRoute {
    @Serializable
    data object MainTab: CommunityNavigationRoute

    @Serializable
    data object CreateTab: CommunityNavigationRoute

    @Serializable
    data object PostTab: CommunityNavigationRoute
}