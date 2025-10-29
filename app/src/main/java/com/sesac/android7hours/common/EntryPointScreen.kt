package com.sesac.android7hours.common

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.input.TextFieldState
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.sesac.community.nav_graph.CommunityNavigationRoute
import com.sesac.community.presentation.CommunityMainScreen
import com.sesac.home.nav_graph.HomeNavigationRoute
import com.sesac.home.presentation.ui.HomeScreen
import com.sesac.trail.nav_graph.TrailNavigationRoute
import com.sesac.trail.presentation.ui.TrailRecommendScreen
import com.sesac.common.R as cR

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EntryPointScreen() {
    val navController = rememberNavController()
//    val BottomBarItems = remember { TODO() }
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val modifier = Modifier
    val textFieldState = TextFieldState()
    val onSearch: (String) -> Unit = { }    // 임시
    val searchResult = listOf("a", "b")     // 임시
    val nav2CommunityMain = { navController.navigate(CommunityNavigationRoute.CommunityMain) }
    val nav2TrailRecommend = { navController.navigate(TrailNavigationRoute.TrailRecommendTab )}
    val recommend = stringResource(cR.string.trail_button_recommend)
    val trailSelectedMenu = remember { mutableStateOf(recommend) }

    Scaffold(
        modifier = Modifier.fillMaxSize(),
//        topBar = { TODO() },
//        bottomBar = { TODO() },
    ) { paddingValues ->
        NavHost(
            navController = navController,
            startDestination = HomeNavigationRoute.HomeTab,
            modifier = Modifier.padding(paddingValues = paddingValues),
        ) {


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

            composable<CommunityNavigationRoute.CommunityMain> {
                CommunityMainScreen()
            }

            composable<TrailNavigationRoute.TrailRecommendTab> {
                TrailRecommendScreen(trailSelectedMenu = trailSelectedMenu)
            }


        }
    }
}