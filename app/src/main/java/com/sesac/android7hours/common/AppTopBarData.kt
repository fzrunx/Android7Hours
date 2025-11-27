package com.sesac.android7hours.common

import android.util.Log
import androidx.compose.material.icons.Icons.AutoMirrored
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.navigation.NavBackStackEntry
import com.sesac.home.nav_graph.TopBarAction
import com.sesac.home.nav_graph.TopBarData
import com.sesac.common.R as cR

data class AppTopBarData(
    override var title: String = "",
    override var titleIcon: Any? = cR.drawable.image7hours,
    override val actions: List<TopBarAction> = emptyList(),
): TopBarData

val NavBackStackEntry.topBarAsRouteName: TopBarData
    get() {
        val routeName = destination.route ?: return AppTopBarData()
        Log.d("Tag TopBarData", routeName)
        return when {
            routeName.contains("HomeTab") -> {
                AppTopBarData(title = "7Hours")
            }

            routeName.contains("Login") -> {
                AppTopBarData(title = "로그인")
            }

            routeName.contains("FindAccount") -> {
                AppTopBarData(title = "아이디/비밀번호 찾기", titleIcon = AutoMirrored.Filled.ArrowBack)
            }

            routeName.contains("Join") -> {
                AppTopBarData(title = "회원가입")
            }

            routeName.contains("TrailMain") -> {
                AppTopBarData(title = "산책로")
            }

            routeName.contains("TrailDetail") -> {
                AppTopBarData(title = "산책로 상세", titleIcon = AutoMirrored.Filled.ArrowBack)
            }

            routeName.contains("TrailCreate") -> {
                AppTopBarData(title = "산책로 생성", titleIcon = AutoMirrored.Filled.ArrowBack)
            }

            routeName.contains("Community") -> {
                AppTopBarData(title = "커뮤니티")
            }

            routeName.contains("Monitor") -> {
                AppTopBarData(title = "모니터링")
            }

            routeName.contains("MypageNavigationRoute.MainTab") -> {
                AppTopBarData(title = "마이페이지")
            }

            routeName.contains("MypageNavigationRoute.DetailScreen") -> {
                AppTopBarData(title = "내 프로필")
            }

            routeName.contains("AddPetScreen") -> {
                AppTopBarData(title = "반려견 등록")
            }

            routeName.contains("Manage") -> {
                AppTopBarData(title = "일정관리", titleIcon = AutoMirrored.Filled.ArrowBack)
            }

            routeName.contains("Favorite") -> {
                AppTopBarData(title = "즐겨찾기", titleIcon = AutoMirrored.Filled.ArrowBack)
            }

            routeName.contains("Setting") -> {
                AppTopBarData(title = "설정", titleIcon = AutoMirrored.Filled.ArrowBack)
            }

            else -> throw IllegalArgumentException(routeName)
        }
    }

