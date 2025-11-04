package com.sesac.mypage.nav_graph

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.sesac.mypage.presentation.ui.MypageFavoriteScreen
import com.sesac.mypage.presentation.ui.MypageMainScreen
import com.sesac.mypage.presentation.ui.MypageManageScreen
import com.sesac.mypage.presentation.ui.MypageSettingScreen

fun NavGraphBuilder.MypageSection(navController: NavController) {
    composable<MypageNavigationRoute.MainTab> {
        MypageMainScreen(navController = navController,)
    }
    composable<MypageNavigationRoute.ManageTab> {
        MypageManageScreen()
    }
    composable<MypageNavigationRoute.FavoriteTab> {
        MypageFavoriteScreen()
    }
    composable<MypageNavigationRoute.SettingTab> {
        MypageSettingScreen()
    }
}