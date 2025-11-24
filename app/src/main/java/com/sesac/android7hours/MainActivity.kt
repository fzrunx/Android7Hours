package com.sesac.android7hours

import android.content.Context
import android.graphics.Rect
import android.os.Bundle
import android.view.MotionEvent
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ExitToApp
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.compositionLocalOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateMapOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.sesac.android7hours.common.AppTopBarData
import com.sesac.android7hours.common.topBarAsRouteName
import com.sesac.android7hours.nav_graph.AppBottomBarItem
import com.sesac.android7hours.nav_graph.AppNavHost
import com.sesac.auth.nav_graph.AuthNavigationRoute
import com.sesac.common.CommonViewModel
import com.sesac.common.component.CommonMapLifecycle
import com.sesac.common.component.CommonMapView
import com.sesac.common.ui.theme.Android7HoursTheme
import com.sesac.community.presentation.CommunityViewModel
import com.sesac.home.nav_graph.EntryPointScreen
import com.sesac.home.nav_graph.HomeNavigationRoute
import com.sesac.home.nav_graph.TopBarAction
import com.sesac.trail.nav_graph.NestedNavigationRoute
import com.sesac.trail.presentation.TrailViewModel
import dagger.hilt.android.AndroidEntryPoint
import com.sesac.common.R as cR


@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    private val commonViewModel: CommonViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            val context = LocalContext.current
            val uiState by commonViewModel.uiState.collectAsStateWithLifecycle()
         // 🔹 공통 MapView + 공통 LifecycleHelper 생성 (앱 전체 공유)
            val commonMapView = remember { CommonMapView.getMapView(context) }
            val lifecycle = LocalLifecycleOwner.current.lifecycle
            val commonMapLifecycle = remember { CommonMapLifecycle(lifecycle) }
            val communityViewModel = hiltViewModel<CommunityViewModel>()
            val trailViewModel = hiltViewModel<TrailViewModel>()
            val navController = rememberNavController()
            val startDestination = HomeNavigationRoute.HomeTab
            val navBackStackEntry by navController.currentBackStackEntryAsState()

            val topBarActions = if (uiState.isLoggedIn) {
                listOf(
                    TopBarAction.TextAction(text = uiState.nickname ?: "User"),
                    TopBarAction.IconAction(
                        icon = Icons.AutoMirrored.Filled.ExitToApp,
                        contentDescription = "Logout",
                        onClick = { commonViewModel.onLogout() }
                    )
                )
            } else {
                listOf(
                    TopBarAction.IconAction(
                        icon = Icons.Default.AccountCircle,
                        contentDescription = "Login",
                        onClick = { navController.navigate(AuthNavigationRoute.LoginTab) }
                    )
                )
            }
            val loginRequiredScreen = listOf(
                stringResource(cR.string.mypage_main),
                stringResource(cR.string.mypage_myinfo),
                stringResource(cR.string.mypage_management),
                stringResource(cR.string.mypage_setting),
                stringResource(cR.string.mypage_favorite),
                stringResource(cR.string.mypage_register_pet),
            )

            val currentTopBarData = navBackStackEntry?.topBarAsRouteName ?: AppTopBarData()
            val finalTopBarData = if (currentTopBarData is AppTopBarData) {
                currentTopBarData.copy(actions = topBarActions)
            } else {
                currentTopBarData
            }

            val appBottomBarItem = remember { AppBottomBarItem().fetch() }
            val isSearchOpen = remember { mutableStateOf(false) }
            val permissionStates = remember { mutableStateMapOf<String, Boolean>() }
            val LocalIsSearchOpen = compositionLocalOf { mutableStateOf(false) }

            Android7HoursTheme {
                LaunchedEffect(uiState) {
                    if (!uiState.isLoggedIn && loginRequiredScreen.contains(finalTopBarData.title)){
                        navController.navigate(AuthNavigationRoute.LoginTab)
                    }
                }
                EntryPointScreen(
                    navController = navController,
                    startDestination = startDestination,
                    scaffoldActionCases = listOf(
                        stringResource(cR.string.community)
                    ),
                    navBackOptions = listOf(
                        stringResource(cR.string.mypage_management),
                        stringResource(cR.string.mypage_favorite),
                        stringResource(cR.string.mypage_setting),
                        stringResource(cR.string.trail_create_page),
                        stringResource(cR.string.trail_detail_page),
                        stringResource(cR.string.mypage_myinfo),
                        stringResource(cR.string.mypage_register_pet),
                    ),
                    appTopBarData = finalTopBarData,
                    appBottomBarItem = appBottomBarItem,
                    isSearchOpen = isSearchOpen,
                    navHost = { paddingValues ->
                        AppNavHost(
                            trailViewModel = trailViewModel,
                            communityViewModel = communityViewModel,
                            paddingValues = paddingValues,
                            navController = navController,
                            nav2Home = { navController.navigate(HomeNavigationRoute.HomeTab) },
                            nav2LoginScreen = { navController.navigate(AuthNavigationRoute.LoginTab) },
                            onNavigateToPathDetail = { path ->
                                path?.let {
                                    navController.navigate(NestedNavigationRoute.TrailDetail(it))
                                }
                            },
                            startDestination = startDestination,
                            uiState = uiState,
                            isSearchOpen = isSearchOpen,
                            onStartFollowing = { Any -> Unit },
                            commonMapLifecycle = commonMapLifecycle,
                            permissionState = permissionStates,
                        )
                    }
                )
            }
        }
    }

    override fun dispatchTouchEvent(ev: MotionEvent?): Boolean {
        if (ev?.action == MotionEvent.ACTION_DOWN) {
            val cF = currentFocus
            if (cF is EditText) {
                val outRect = Rect()
                cF.getGlobalVisibleRect(outRect)
                if (!outRect.contains(ev.rawX.toInt(), ev.rawY.toInt())) {
                    cF.clearFocus()
                    val inputMethodManager: InputMethodManager = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
                    inputMethodManager.hideSoftInputFromWindow(cF.windowToken, 0)
                }
            }
        }
        return super.dispatchTouchEvent(ev)
    }

}