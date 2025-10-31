package com.sesac.mypage.nav_graph

import kotlinx.serialization.Serializable

@Serializable
sealed interface MypageNavigationRoute {
    @Serializable
    data object MypageMainTab: MypageNavigationRoute
}