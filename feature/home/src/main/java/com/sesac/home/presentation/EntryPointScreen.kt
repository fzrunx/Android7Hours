package com.sesac.home.presentation

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.input.TextFieldState
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.sesac.home.nav_graph.HomeNavigationRoute
import com.sesac.home.presentation.ui.HomeScreen

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EntryPointScreen() {
    val navController = rememberNavController()
//    val BottomBarItems = remember { TODO() }
    val navBackStackEntry by navController.currentBackStackEntryAsState()

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
            val textFieldState = TextFieldState()
            val onSearch: (String) -> Unit = { }
            val searchResult = listOf("a", "b")
            val modifier = Modifier
            composable<HomeNavigationRoute.HomeTab> {
                HomeScreen(
                    textFieldState,
                    onSearch,
                    searchResult,
                    modifier
                )
            }
        }
    }
}