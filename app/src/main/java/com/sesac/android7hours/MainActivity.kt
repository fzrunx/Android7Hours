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
import androidx.compose.runtime.compositionLocalOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateMapOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.naver.maps.map.MapView
import com.sesac.android7hours.common.AppTopBarData
import com.sesac.android7hours.common.topBarAsRouteName
import com.sesac.android7hours.nav_graph.AppBottomBarItem
import com.sesac.android7hours.nav_graph.AppNavHost
import com.sesac.home.nav_graph.EntryPointScreen
import com.sesac.common.ui.theme.Android7HoursTheme
import com.sesac.common.utils.MapViewLifecycleHelper
import com.sesac.home.nav_graph.HomeNavigationRoute
import dagger.hilt.android.AndroidEntryPoint
import com.sesac.common.R as cR

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            val context = LocalContext.current
            val mapView = MapView(context)
            val navController = rememberNavController()
            val startDestination = HomeNavigationRoute.HomeTab
            val navBackStackEntry by navController.currentBackStackEntryAsState()
            val appTopBarData = navBackStackEntry?.topBarAsRouteName ?: AppTopBarData()
            val appBottomBarItem = remember { AppBottomBarItem().fetch() }
            val lifecycleHelper = remember { MapViewLifecycleHelper(mapView) }
            val isSearchOpen = remember { mutableStateOf(false) }
            val LocalIsSearchOpen = compositionLocalOf { mutableStateOf(false) }
            val permissionStates = remember { mutableStateMapOf<String, Boolean>() }

            Android7HoursTheme {
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
                    ),
                    appTopBarData = appTopBarData,
                    appBottomBarItem = appBottomBarItem,
                    isSearchOpen = isSearchOpen,
                    LocalIsSearchOpen = LocalIsSearchOpen,
                    navHost = { paddingValues ->
                        AppNavHost(
                            paddingValues = paddingValues,
                            navController = navController,
                            startDestination = startDestination,
                            isSearchOpen = isSearchOpen,
                            lifecycleHelper = lifecycleHelper,
                            permissionState = permissionStates
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