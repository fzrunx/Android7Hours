package com.sesac.community.nav_graph

import androidx.compose.runtime.MutableState
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.sesac.community.presentation.CommunityViewModel
import com.sesac.community.presentation.ui.CommunityMainScreen
import com.sesac.domain.result.AuthUiState

fun NavGraphBuilder.communityRoute(
    nav2LoginScreen: () -> Unit,
    uiState: AuthUiState,
    viewModel: CommunityViewModel,
    ) {
    composable<CommunityNavigationRoute.MainTab>() {
        CommunityMainScreen(
            nav2LoginScreen = nav2LoginScreen,
            uiState = uiState,
            viewModel = viewModel
        )
    }
}