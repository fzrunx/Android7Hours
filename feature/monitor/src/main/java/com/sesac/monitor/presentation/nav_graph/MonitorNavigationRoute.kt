package com.sesac.monitor.presentation.nav_graph

import kotlinx.serialization.Serializable

@Serializable
sealed interface MonitorNavigationRoute {
    @Serializable
    data object CamTab: MonitorNavigationRoute

    @Serializable
    data object GPSTab: MonitorNavigationRoute
}