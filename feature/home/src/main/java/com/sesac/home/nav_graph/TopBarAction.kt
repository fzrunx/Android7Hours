package com.sesac.home.nav_graph

import androidx.compose.ui.graphics.vector.ImageVector

sealed interface TopBarAction {
    data class IconAction(
        val icon: ImageVector,
        val contentDescription: String,
        val onClick: () -> Unit
    ) : TopBarAction

    data class TextAction(
        val text: String,
        val isButton: Boolean = false,
        val onClick: () -> Unit = {}
    ) : TopBarAction
}
