package com.sesac.home.nav_graph

import androidx.compose.ui.graphics.vector.ImageVector

interface TopBarData {
    var title: String
    var titleIcon: Any?
    val actions: List<TopBarAction>
}

interface BottomBarItem{
    val tabName: String
    val icon: ImageVector
    val destination: Any?

    fun fetch(): List<BottomBarItem>
}
