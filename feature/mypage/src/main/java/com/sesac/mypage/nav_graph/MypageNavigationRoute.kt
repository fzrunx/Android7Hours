package com.sesac.mypage.nav_graph

import kotlinx.serialization.Serializable

@Serializable
sealed interface MypageNavigationRoute {
    @Serializable
    data object MainTab: MypageNavigationRoute
    @Serializable
    data object ManageTab: MypageNavigationRoute
    @Serializable
    data object FavoriteTab: MypageNavigationRoute
    @Serializable
    data object SettingTab: MypageNavigationRoute
    @Serializable
    data object DetailScreen: MypageNavigationRoute
    @Serializable
    data class AddPetScreen(val petId: Int = -1) : MypageNavigationRoute

}