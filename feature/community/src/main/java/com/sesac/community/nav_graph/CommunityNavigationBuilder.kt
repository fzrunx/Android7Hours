package com.sesac.community.nav_graph

import androidx.compose.runtime.MutableState
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.sesac.community.presentation.CommunityViewModel
import com.sesac.community.presentation.ui.CommunityMainScreen

fun NavGraphBuilder.communityRoute(
    // isSearchOpen:  MutableState<Boolean>,
    viewModel: CommunityViewModel,
    ) {
    composable<CommunityNavigationRoute.MainTab>() {
        CommunityMainScreen(viewModel)
    }
}