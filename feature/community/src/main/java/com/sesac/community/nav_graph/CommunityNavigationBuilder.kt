package com.sesac.community.nav_graph

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.ProvidableCompositionLocal
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.sesac.community.presentation.ui.CommunityMainScreen

fun NavGraphBuilder.CommunitySection(
    isSearchOpen:  MutableState<Boolean>,
) {
    composable<CommunityNavigationRoute.MainTab>() {
        CommunityMainScreen(isSearchOpen = isSearchOpen,)
    }
}