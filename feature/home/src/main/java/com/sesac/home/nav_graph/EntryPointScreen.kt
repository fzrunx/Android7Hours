package com.sesac.home.nav_graph

import android.util.Log
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
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
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import coil3.compose.AsyncImage
import coil3.request.CachePolicy
import coil3.request.ImageRequest
import com.sesac.common.component.LocalIsSearchOpen
import com.sesac.common.ui.theme.White
import com.sesac.common.ui.theme.paddingMicro
import com.sesac.common.ui.theme.paddingNone
import com.sesac.common.R as cR


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EntryPointScreen(
    isRecording: Boolean,
    startDestination: Any,
    navController: NavHostController,
    scaffoldActionCases: List<String>,
    navBackOptions: List<String>,
    appTopBarData: TopBarData,
    appBottomBarItem: List<BottomBarItem>,
    isSearchOpen: MutableState<Boolean>,
    screensWithCustomTopBar: List<String>, // New parameter
    navHost: @Composable (PaddingValues) -> Unit,
) {
    val context = LocalContext.current
    // 2. 현재 화면의 Route(경로) 감지 -> Community 탭일 때 TopBar 숨기기
    val isHideTopBar by remember(appTopBarData.title, screensWithCustomTopBar) {
        derivedStateOf {
            screensWithCustomTopBar.contains(appTopBarData.title)
        }
    }
    val isHideBottomBar by remember(isRecording) {
        derivedStateOf {
            isRecording
        }
    }

    val isScaffoldAction = scaffoldActionCases.contains(appTopBarData.title)

    // 화면 이동 시 검색창 닫기 로직
    LaunchedEffect(isScaffoldAction) {
        if (!isScaffoldAction) {
            isSearchOpen.value = false
        }
    }

    // 4. 전역 LocalProvider에 MainActivity의 상태(isSearchOpen)를 주입
    CompositionLocalProvider(LocalIsSearchOpen provides isSearchOpen) {
        Scaffold(
            modifier = Modifier.fillMaxSize(),

            // 5. TopBar 조건부 렌더링

            topBar = {
                if (!isHideTopBar && !isRecording) {
                    CenterAlignedTopAppBar(
                        title = {
                            Text(
                                text = appTopBarData.title,
                                fontWeight = FontWeight.Bold
                            )
                        },
                        colors = TopAppBarDefaults.topAppBarColors(
                            containerColor = MaterialTheme.colorScheme.primaryContainer
                        ),
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
                            appTopBarData.actions.forEach { action ->
                                when {
                                    action is TopBarAction.IconAction && action.icon is ImageVector -> {
                                        IconButton(onClick = action.onClick) {
                                            Icon(
                                                imageVector = action.icon,
                                                contentDescription = action.contentDescription
                                            )
                                        }
                                    }

                                    action is TopBarAction.IconAction && action.icon is String -> {
                                        IconButton(onClick = action.onClick) {
                                            AsyncImage(
                                                model = ImageRequest.Builder(context)
                                                    .data(action.icon ?: cR.drawable.placeholder)
                                                    .build(),
                                                contentDescription = "Profile Picture",
                                                modifier = Modifier
                                                    .fillMaxSize()
                                                    .padding(paddingMicro)
                                                    .clip(CircleShape),
                                                contentScale = ContentScale.Crop,
                                                placeholder = painterResource(id = cR.drawable.placeholder),
                                                error = painterResource(id = cR.drawable.placeholder),
                                                // 에러 발생 시 로그 출력 (디버깅용)
                                                onError = { Log.e("IMAGE_LOAD", "실패 원인: ${it.result.throwable.message}") }
                                            )
                                        }
                                    }

                                    action is TopBarAction.TextAction -> {
                                        if (action.isButton) {
                                            TextButton(onClick = action.onClick) {
                                                Text(text = action.text)
                                            }
                                        } else {
                                            Text(text = action.text)
                                        }
                                    }
                                }
                            }

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
                }
            },
            bottomBar = {
                if (!isHideBottomBar) {
                    NavigationBar {
                        appBottomBarItem.forEach { bottomBarItem ->
                            val isSelected = bottomBarItem.tabName == appTopBarData.title

                            NavigationBarItem(
                                selected = isSelected,
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
                                // 🌟🌟🌟 [핵심 수정 부분] 🌟🌟🌟
                                onClick = {
                                    val targetRoute = bottomBarItem.destination ?: startDestination

                                    navController.navigate(targetRoute) {
                                        // 중요: popUpTo의 대상을 "이동하려는 곳"이 아니라 "그래프의 시작점(Home)"으로 설정해야 합니다.
                                        // 이렇게 해야 탭 이동 시 백스택이 계속 쌓이지 않고 깔끔하게 교체됩니다.
                                        popUpTo(navController.graph.findStartDestination().id) {
//                                        saveState = true
                                        }
                                        launchSingleTop = true
                                        restoreState = true
                                    }
                                }
                            )
                        }
                    }

                }
            },
        ) { paddingValues ->
            navHost(
                PaddingValues(
                    top = if (isHideTopBar) paddingNone else paddingValues.calculateTopPadding(),
                    bottom = paddingValues.calculateBottomPadding()
                )
            )
        }
    }
}