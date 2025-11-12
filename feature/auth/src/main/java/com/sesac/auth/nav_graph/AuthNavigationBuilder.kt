package com.sesac.auth.nav_graph


import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.sesac.auth.presentation.AuthViewModel
import com.sesac.auth.presentation.ui.AuthJoinScreen
import com.sesac.auth.presentation.ui.AuthLoginScreen

fun NavGraphBuilder.authRoute(
    navController: NavController,
    nav2Home: () -> Unit,
) {
    composable<AuthNavigationRoute.JoinTab> {
        val authViewModel: AuthViewModel = hiltViewModel()
        AuthJoinScreen(
            nav2Home = nav2Home,
            viewModel = authViewModel,
        )
    }
    composable<AuthNavigationRoute.LoginTab> {
        val authViewModel: AuthViewModel = hiltViewModel()
        AuthLoginScreen(
            viewModel = authViewModel,
            navController = navController,
            onLoginSuccess = { navController.popBackStack() }
        )
    }
}