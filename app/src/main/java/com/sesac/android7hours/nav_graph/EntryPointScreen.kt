package com.sesac.android7hours.nav_graph

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.input.TextFieldState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import coil3.compose.AsyncImage
import com.sesac.android7hours.common.TopBarData
import com.sesac.android7hours.common.topBarAsRouteName
import com.sesac.common.component.CommonSearchBarContent
import com.sesac.common.ui.theme.Android7HoursTheme
import com.sesac.common.ui.theme.cardImageHeight
import com.sesac.community.nav_graph.CommunityNavigationRoute
import com.sesac.community.presentation.ui.CommunityMainScreen
import com.sesac.home.nav_graph.HomeNavigationRoute
import com.sesac.home.presentation.ui.HomeScreen
import com.sesac.monitor.presentation.nav_graph.MonitorNavigationRoute
import com.sesac.monitor.presentation.ui.MonitorCamScreen
import com.sesac.mypage.nav_graph.MypageNavigationRoute
import com.sesac.mypage.presentation.ui.MypageMainScreen
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

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    Text(
                        text = topBarData.title,
                        fontWeight = FontWeight.Bold
                    )
                },
                colors = TopAppBarDefaults.topAppBarColors(MaterialTheme.colorScheme.primaryContainer),
                navigationIcon = {
                    IconButton(
                        onClick = { navController.navigate(HomeNavigationRoute.HomeTab) }
                        ) {
                        Icon(
                            painterResource(cR.drawable.image7hours),
                            contentDescription = "Home Icon",
                            tint = Color.Unspecified,
                        )
                    }
                },
                actions = {
                    if (topBarData.title.contains("Community")){
                        IconButton(
                            onClick = {
//                                TODO()
                            },
                        ) {
                            Icon(
                                imageVector = Icons.Filled.Search,
                                contentDescription = "search",
                                tint = Color.Unspecified,
                            )
                        }
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
                    onNavigateToWalkPath = {},
                    onNavigateToCommunity = {},
                )
            }

            // Trail
            composable<TrailNavigationRoute.TrailRecommendTab> {
                TrailRecommendScreen(trailSelectedMenu = trailSelectedMenu)
            }

            // Community
            composable<CommunityNavigationRoute.CommunityMainTab> {
                CommunityMainScreen(
                )
            }

            // Monitor
            composable<MonitorNavigationRoute.CamTab> {
                MonitorCamScreen(
                    navController = navController,
                )
            }

            //Mypage
            composable<MypageNavigationRoute.MypageMainTab> {
                MypageMainScreen(
                    navController = navController,
                )
            }

        }
    }
}

@Preview
@Composable
fun EntryPointScreenPreview() {
    Android7HoursTheme {
        EntryPointScreen()
    }
}