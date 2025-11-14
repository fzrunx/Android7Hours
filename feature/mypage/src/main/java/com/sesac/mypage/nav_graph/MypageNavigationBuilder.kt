package com.sesac.mypage.nav_graph

import androidx.compose.runtime.snapshots.SnapshotStateMap
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.sesac.domain.model.CommonAuthUiState
import com.sesac.mypage.presentation.ui.MypageDetailScreen
import com.sesac.mypage.presentation.ui.MypageFavoriteScreen
import com.sesac.mypage.presentation.ui.MypageMainScreen
import com.sesac.mypage.presentation.ui.MypageManageScreen
import com.sesac.mypage.presentation.ui.MypageSettingScreen

fun NavGraphBuilder.mypageRoute(
    navController: NavController,
    nav2LoginScreen: () -> Unit,
    uiState: CommonAuthUiState,
    permissionState: SnapshotStateMap<String, Boolean>,
    ) {
    composable<MypageNavigationRoute.MainTab> {
        MypageMainScreen(
            navController = navController,
            nav2LoginScreen = nav2LoginScreen,
            uiState = uiState,
            )
    }
    composable<MypageNavigationRoute.ManageTab> {
        MypageManageScreen()
    }
    composable<MypageNavigationRoute.FavoriteTab> {
        MypageFavoriteScreen()
    }
    composable<MypageNavigationRoute.SettingTab> {
        MypageSettingScreen(permissionStates = permissionState)
    }
    composable<MypageNavigationRoute.DetailScreen> {
        MypageDetailScreen(
            navController = navController,
            uiState = uiState,
        )
    }
}