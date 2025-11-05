package com.sesac.home.nav_graph

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
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
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.ProvidableCompositionLocal
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.navigation.NavHostController
import com.sesac.common.R as cR

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EntryPointScreen(
    startDestination: Any,
    navController: NavHostController,
    scaffoldActionCases: List<String>,
    navBackOptions: List<String>,
    appTopBarData: TopBarData,
    appBottomBarItem: List<BottomBarItem>,
    isSearchOpen:  MutableState<Boolean>,
    LocalIsSearchOpen: ProvidableCompositionLocal<MutableState<Boolean>>,
    navHost: @Composable (PaddingValues) -> Unit,
) {
    val topBarTitle = appTopBarData.title
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
                            text = appTopBarData.title,
                            fontWeight = FontWeight.Bold
                        )
                    },
                    colors = TopAppBarDefaults.topAppBarColors(MaterialTheme.colorScheme.primaryContainer),
                    navigationIcon = {
                        if (appTopBarData.title in navBackOptions) {
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
                    appBottomBarItem.forEach { bottomBarItem ->
                        NavigationBarItem(
                            selected = bottomBarItem.tabName == appTopBarData.title,
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
                                navController.navigate(route = bottomBarItem.destination ?: startDestination) {
                                    popUpTo(route = bottomBarItem.destination ?: startDestination) {
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
            navHost(paddingValues)
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