package com.sesac.android7hours.nav_graph

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.input.TextFieldState
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.sesac.android7hours.common.TopBarData
import com.sesac.android7hours.common.topBarAsRouteName
import com.sesac.community.nav_graph.CommunityNavigationRoute
import com.sesac.community.presentation.CommunityCreateScreen
import com.sesac.community.presentation.CommunityMainScreen
import com.sesac.community.presentation.CommunityPostScreen
import com.sesac.home.nav_graph.HomeNavigationRoute
import com.sesac.home.presentation.ui.HomeScreen
import com.sesac.monitor.presentation.nav_graph.MonitorNavigationRoute
import com.sesac.monitor.presentation.ui.MonitorCamScreen
import com.sesac.monitor.presentation.ui.MonitorGPSScreen
import com.sesac.mypage.nav_graph.MypageNavigationRoute
import com.sesac.mypage.presentation.MypageMainScreen
import com.sesac.trail.nav_graph.TrailNavigationRoute
import com.sesac.trail.presentation.ui.TrailRecommendScreen
import com.sesac.common.R as cR

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EntryPointScreen() {
    // Static 변수
    val recommend = stringResource(cR.string.trail_button_recommend)

    // State 변수
    val navController = rememberNavController()
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val topBarData = navBackStackEntry?.topBarAsRouteName ?: TopBarData()
    val bottomBarItems = remember { BottomBarItem.fetchBottomBarItems() }
    val trailSelectedMenu = remember { mutableStateOf(recommend) }
    val modifier = Modifier
    val textFieldState = TextFieldState()

    // Event(임시)
    val onSearch: (String) -> Unit = { }
    val searchResult = listOf("a", "b")

    // nav 경로
    val nav2CommunityMain = { navController.navigate(CommunityNavigationRoute.CommunityMainTab) }
    val nav2TrailRecommend = { navController.navigate(TrailNavigationRoute.TrailRecommendTab )}


    Scaffold(
        modifier = Modifier.fillMaxSize(),
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = topBarData.title,
                        fontWeight = FontWeight.Bold
                    )
                },
                navigationIcon = {
                    IconButton(
                        onClick = if (topBarData.title != "Home") {
                            {
                                navController.navigateUp()
                            }
                        } else {
                            {

                            }

                        }) {
                        Icon(topBarData.titleIcon, contentDescription = "Navigation Icon")
                    }
                }

            )
        },

        bottomBar = {
            NavigationBar {
                bottomBarItems.forEach { bottomBarItem ->
                    NavigationBarItem(
                        selected = bottomBarItem.tabName == topBarData.title,
                        label = {
                            Text(text = bottomBarItem.tabName, color = Color.Unspecified)
                        },
                        icon = {
                            Icon(
                                imageVector = bottomBarItem.icon,
                                contentDescription = bottomBarItem.tabName,
                                tint = Color.Unspecified,
                            )
                        },
                        onClick = {
                            navController.navigate(route = bottomBarItem.destination) {
                                popUpTo(route = bottomBarItem.destination) {
//                                    inclusive = true
                                    saveState = true
                                }
                                launchSingleTop = true
                            }
                        }
                    )
                }
            }
        },
    ) { paddingValues ->
        NavHost(
            navController = navController,
            startDestination = HomeNavigationRoute.HomeTab,
            modifier = Modifier.padding(paddingValues = paddingValues),
        ) {

            // Home
            composable<HomeNavigationRoute.HomeTab> {
                HomeScreen(
                    textFieldState = textFieldState,
                    onSearch = onSearch,
                    searchResults = searchResult,
                    modifier = modifier,
                    nav2CommunityMain = nav2CommunityMain,
                    nav2TrailRecommend = nav2TrailRecommend,
                )
            }

            // Trail
            composable<TrailNavigationRoute.TrailRecommendTab> {
                TrailRecommendScreen(trailSelectedMenu = trailSelectedMenu)
            }

            // Community
            composable<CommunityNavigationRoute.CommunityMainTab> {
                CommunityMainScreen(
                    navController = navController,
                )
            }
            composable<CommunityNavigationRoute.CommunityPostTab> {
                CommunityPostScreen(
                    navController = navController,
                )
            }
            composable<CommunityNavigationRoute.CommunityCreateTab> {
                CommunityCreateScreen(
                    navController = navController,
                )
            }

            // Monitor
            composable<MonitorNavigationRoute.CamTab> {
                MonitorCamScreen(
                    navController = navController,
                )
            }
            composable<MonitorNavigationRoute.GPSTab> {
                MonitorGPSScreen(
                    navController = navController,
                )
            }

            //Mypage
            composable<MypageNavigationRoute.MypageMainTab> {
                MypageMainScreen(
//                    navController = navController,
                )
            }

        }
    }
}