package com.sesac.community.nav_graph

import kotlinx.serialization.Serializable

@Serializable
sealed interface CommunityNavigationRoute {
    @Serializable
    data object CommunityMain: CommunityNavigationRoute
}