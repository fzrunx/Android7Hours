package com.sesac.monitor.presentation.nav_graph

import kotlinx.serialization.Serializable

@Serializable
sealed interface MonitorNavigationRoute {
    @Serializable
    data object MainTab: MonitorNavigationRoute

    @Serializable
    data object GpsTab: MonitorNavigationRoute

}