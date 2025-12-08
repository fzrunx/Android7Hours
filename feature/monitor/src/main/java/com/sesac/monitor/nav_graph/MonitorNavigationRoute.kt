package com.sesac.monitor.nav_graph

import kotlinx.serialization.Serializable

@Serializable
sealed interface MonitorNavigationRoute {
    @Serializable
    data object MainTab: MonitorNavigationRoute

    @Serializable
    data object GpsTab: MonitorNavigationRoute

}