package com.sesac.android7hours.nav_graph

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
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
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.compositionLocalOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.naver.maps.map.MapView
import com.sesac.android7hours.common.TopBarData
import com.sesac.android7hours.common.topBarAsRouteName
import com.sesac.community.nav_graph.CommunitySection
import com.sesac.home.nav_graph.HomeSection
import com.sesac.monitor.nav_graph.MonitorSection
import com.sesac.monitor.utils.MapViewLifecycleHelper
import com.sesac.mypage.nav_graph.MypageSection
import com.sesac.trail.nav_graph.TrailSection
import com.sesac.common.R as cR

val LocalIsSearchOpen = compositionLocalOf { mutableStateOf(false) }

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EntryPointScreen(
    startDestination: Any,
    scaffoldActionCases: List<String>,
    navBackOptions: List<String>,
) {
    // Static 변수
    val recommend = stringResource(cR.string.trail_button_recommend)    // 수정 예정
    val context = LocalContext.current
    val mapView = MapView(context)

    // State 변수
    val navController = rememberNavController()
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val topBarData = navBackStackEntry?.topBarAsRouteName ?: TopBarData()
    val bottomBarItems = remember { BottomBarItem.fetchBottomBarItems() }
    val trailSelectedMenu = remember { mutableStateOf(recommend) }  // 수정 예정
    val lifecycleHelper = remember { MapViewLifecycleHelper(mapView) }
    val isSearchOpen = remember { mutableStateOf(false) }
    val topBarTitle = topBarData.title
    val isScaffoldAction = scaffoldActionCases.contains(topBarTitle)

    LaunchedEffect(isScaffoldAction) {
        if (!isScaffoldAction) {
            isSearchOpen.value = false
        }
    }

    CompositionLocalProvider(LocalIsSearchOpen provides isSearchOpen) {
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
                        if (topBarData.title in navBackOptions) {
                            IconButton(onClick = { navController.popBackStack() }) {
                                Icon(
                                    imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                                    contentDescription = "Back"
                                )
                            }
                        } else {
                            IconButton(
                                onClick = { navController.navigate(startDestination) }
                            ) {
                                Icon(
                                    painterResource(cR.drawable.image7hours),
                                    contentDescription = "Home Icon",
                                    tint = Color.Unspecified,
                                )
                            }
                        }
                    },
                    actions = {
                        if (isScaffoldAction) {
                            IconButton(
                                onClick = { isSearchOpen.value = !isSearchOpen.value },
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
                startDestination = startDestination,
                modifier = Modifier.padding(paddingValues = paddingValues),
            ) {
                HomeSection()
                TrailSection(trailSelectedMenu = trailSelectedMenu)
                CommunitySection(isSearchOpen = isSearchOpen,)
                MonitorSection(
                    navController = navController,
                    lifecycleHelper = lifecycleHelper,
                )
                MypageSection(navController = navController,)
            }
        }
    }
}

//@Preview
//@Composable
//fun EntryPointScreenPreview() {
//    Android7HoursTheme {
//        EntryPointScreen(HomeNavigationRoute.HomeTab)
//    }
//}