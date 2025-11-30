package com.sesac.trail.nav_graph

import com.sesac.common.model.PathParceler
import com.sesac.common.model.PlaceParceler
import com.sesac.domain.model.Place
import kotlinx.serialization.Serializable
import kotlinx.serialization.Contextual

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
sealed interface NestedNavigationRoute  {
    val route: String
    @Serializable
    data class TrailDetail(val pathParceler: PathParceler) : NestedNavigationRoute {
    override val route: String = "trail_detail"
    }
    @Serializable
    data class PlaceDetail(val placeParceler: PlaceParceler) : NestedNavigationRoute {
        override val route: String = ROUTE
        companion object {
            const val ROUTE = "place_detail"
        }
    }
}