package com.sesac.android7hours

import android.Manifest
import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Rect
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.Settings
import android.util.Log
import android.view.MotionEvent
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ExitToApp
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateMapOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.core.content.ContextCompat
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.sesac.android7hours.common.AppTopBarData
import com.sesac.android7hours.common.topBarAsRouteName
import com.sesac.android7hours.nav_graph.AppBottomBarItem
import com.sesac.android7hours.nav_graph.AppNavHost
import com.sesac.auth.nav_graph.AuthNavigationRoute
import com.sesac.common.CommonViewModel
import com.sesac.common.component.CommonMapLifecycle
import com.sesac.common.component.CommonMapView
import com.sesac.common.service.CurrentLocationService
import com.sesac.common.ui.theme.Android7HoursTheme
import com.sesac.community.presentation.CommunityViewModel
import com.sesac.home.nav_graph.EntryPointScreen
import com.sesac.home.nav_graph.HomeNavigationRoute
import com.sesac.home.nav_graph.TopBarAction
import com.sesac.mypage.presentation.MypageViewModel
import com.sesac.trail.nav_graph.NestedNavigationRoute
import com.sesac.trail.presentation.TrailViewModel
import dagger.hilt.android.AndroidEntryPoint
import com.sesac.common.R as cR


@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    private val commonViewModel: CommonViewModel by viewModels()
    // 권한 요청 런처
    private val permissionLauncher = registerForActivityResult(
        ActivityResultContracts.RequestMultiplePermissions()
    ) { permissions ->
        val allGranted = permissions.all { it.value }
        if (allGranted) {
            startLocationServiceIfNeeded()
        } else {
            // 권한 거부 처리
            showPermissionDeniedDialog()
        }
    }

    // 권한 체크 및 요청
    private fun checkAndRequestPermissions() {
        val requiredPermissions = mutableListOf(
            Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.ACCESS_COARSE_LOCATION
        )

        // Android 13 이상: 알림 권한 추가
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            requiredPermissions.add(Manifest.permission.POST_NOTIFICATIONS)
        }

        val deniedPermissions = requiredPermissions.filter {
            ContextCompat.checkSelfPermission(this, it) != PackageManager.PERMISSION_GRANTED
        }

        if (deniedPermissions.isEmpty()) {
            // 모든 권한 있음 -> Service 시작
            startLocationServiceIfNeeded()
        } else {
            // 권한 요청
            permissionLauncher.launch(deniedPermissions.toTypedArray())
        }
    }

    // Service 시작
    private fun startLocationServiceIfNeeded() {
        try {
            val intent = Intent(this, CurrentLocationService::class.java)
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                startForegroundService(intent)
            } else {
                startService(intent)
            }
            Log.d("TAG-MainActivity", "Location service started")
        } catch (e: SecurityException) {
            Log.e("TAG-MainActivity", "SecurityException: ${e.message}")
            showPermissionDeniedDialog()
        } catch (e: Exception) {
            Log.e("TAG-MainActivity", "Failed to start service: ${e.message}")
        }
    }

    // 권한 거부 시 안내 다이얼로그
    private fun showPermissionDeniedDialog() {
        AlertDialog.Builder(this)
            .setTitle("권한 필요")
            .setMessage("위치 추적 서비스를 사용하려면 위치 권한과 알림 권한이 필요합니다.")
            .setPositiveButton("설정으로 이동") { _, _ ->
                openAppSettings()
            }
            .setNegativeButton("취소", null)
            .show()
    }

    // 앱 설정 화면으로 이동
    private fun openAppSettings() {
        val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS).apply {
            data = Uri.fromParts("package", packageName, null)
        }
        startActivity(intent)
    }

    @OptIn(ExperimentalPermissionsApi::class)
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
            val trailViewModel = hiltViewModel<TrailViewModel>()
            val communityViewModel = hiltViewModel<CommunityViewModel>()
            val mypageViewModel = hiltViewModel<MypageViewModel>()
            val navController = rememberNavController()
            val startDestination = HomeNavigationRoute.HomeTab
            val navBackStackEntry by navController.currentBackStackEntryAsState()

            val topBarActions = if (uiState.isLoggedIn) {
                listOf(
                    TopBarAction.TextAction(text = uiState.user?.nickname ?: "User"),
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
            val isRecording by trailViewModel.isRecording.collectAsStateWithLifecycle()

            // 로그인 상태 변경 시 권한 체크
            LaunchedEffect(uiState.isLoggedIn) {
                if (uiState.isLoggedIn) {
                    checkAndRequestPermissions()
                } else {
                    stopService(Intent(context, CurrentLocationService::class.java))
                }
            }


            Android7HoursTheme {
                LaunchedEffect(uiState) {
                    if (!uiState.isLoggedIn && loginRequiredScreen.contains(finalTopBarData.title)){
                        navController.navigate(AuthNavigationRoute.LoginTab)
                    }
                }
                EntryPointScreen(
                    isRecording = isRecording,
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
                        stringResource(cR.string.trail_info_detail_page),
                    ),
                    appTopBarData = finalTopBarData,
                    appBottomBarItem = appBottomBarItem,
                    isSearchOpen = isSearchOpen,
                    screensWithCustomTopBar = listOf(stringResource(cR.string.community)), // New parameter
                    navHost = { paddingValues ->
                        AppNavHost(
                            trailViewModel = trailViewModel,
                            communityViewModel = communityViewModel,
                            mypageViewModel = mypageViewModel,
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
                            onStartFollowing = { path ->
                                trailViewModel.startFollowing(path) // ✅ ViewModel 함수 호출
                                trailViewModel.updateIsSheetOpen(false) // 시트 닫기
                                trailViewModel.updateIsFollowingPath(true) // 상태 업데이트
                                Log.d("Tag-MainActivity", "Following path: ${path.pathName}")
                            },
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