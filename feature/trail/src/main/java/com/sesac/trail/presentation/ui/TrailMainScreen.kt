package com.sesac.trail.presentation.ui

import android.util.Log
import android.view.ViewGroup
import androidx.activity.compose.LocalActivity
import androidx.compose.runtime.Composable
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.layout.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.BiasAlignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.viewinterop.AndroidView
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import com.naver.maps.map.LocationTrackingMode
import com.naver.maps.map.MapView
import com.naver.maps.map.NaverMap
import com.naver.maps.map.util.FusedLocationSource
import com.sesac.common.ui.theme.paddingLarge
import kotlinx.coroutines.delay
import com.sesac.common.utils.PathMarker
import com.sesac.domain.local.model.UserPath
import com.sesac.trail.nav_graph.TrailNavigationRoute
import com.sesac.trail.presentation.TrailMapViewLifecycleHelper
import com.sesac.trail.presentation.TrailViewModel
import com.sesac.trail.presentation.component.BottomSheetContent
import com.sesac.trail.presentation.component.RecordingControls
import com.sesac.trail.presentation.component.ReopenSheetButton

enum class WalkPathTab { RECOMMENDED, MY_RECORDS }

// --- Main Page Composable ---
@Composable
fun TrailMainScreen(
    viewModel: TrailViewModel = hiltViewModel(),
    navController: NavController,
    trailLifecycleHelper: TrailMapViewLifecycleHelper, // 라이프 사이클 따로 관리하려고 만듬
    onMapReady: ((NaverMap) -> Unit)? = null
) {
    val activity = LocalActivity.current
    val context = LocalContext.current

    val recommendedPaths by viewModel.recommendedPaths.collectAsStateWithLifecycle()
    val myRecords by viewModel.myRecords.collectAsStateWithLifecycle()

    val isSheetOpen by viewModel.isSheetOpen.collectAsStateWithLifecycle()
    val isPaused  by viewModel.isPaused.collectAsStateWithLifecycle()
    val isFollowingPath by viewModel.isFollowingPath.collectAsStateWithLifecycle()
    val isRecording by viewModel.isRecoding.collectAsStateWithLifecycle()
    val recordingTime by viewModel.recordingTime.collectAsStateWithLifecycle()
    val activeTab by viewModel.activeTab.collectAsStateWithLifecycle()
//    var showCreatePage by remember { mutableStateOf(false) }

    val locationSource = remember {
        activity?.let { FusedLocationSource(it, 1000) }
            ?: throw IllegalStateException("Activity not found for FusedLocationSource")
    }
    val trailMapView = remember { MapView(context) }
    val trailLifecycleHelper = remember { TrailMapViewLifecycleHelper(trailMapView) }

    // --- 타이머 로직 (녹화 중일 때 시간 증가) ---
    LaunchedEffect(key1 = isRecording, key2 = isPaused) {
        if (isRecording && !isPaused) {
            while (true) {
                delay(1000)
                viewModel.updateRecordingTime(1)
            }
        }
    }
    // 기록 시작 시 시트 닫기 (React의 useEffect)
    // -> onClick 핸들러에서 직접 처리하는 것이 Compose 방식
    // 화면 종료 시 MapView 안전하게 해제
    DisposableEffect(Unit) {
        onDispose {
            val parent = trailMapView.parent as? ViewGroup
            parent?.removeView(trailMapView) // 부모에서 제거
            trailLifecycleHelper.onPause()
            trailLifecycleHelper.onStop()
            trailLifecycleHelper.onDestroy()

            // 화면 종료 시 Bottom Sheet 상태 초기화
            viewModel.updateIsSheetOpen(true)
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
    ) {
        // ✅ 지도 영역 (AsyncImage → AndroidView 로 대체)
        AndroidView(
            modifier = Modifier.fillMaxSize(),
            factory = { context ->
                trailLifecycleHelper.trailMapView.apply {
                    trailLifecycleHelper.onCreate(null)
                    getMapAsync { naverMap ->
                        naverMap.locationSource = locationSource
                        naverMap.locationTrackingMode = LocationTrackingMode.Follow
                        // ✅ Trail 용 지도 세팅 (기본 위치 / UI 세팅 등)
                        naverMap.uiSettings.isLocationButtonEnabled = true
                        naverMap.uiSettings.isZoomControlEnabled = false

                        // ✅ onMapReady 시점에 콜백 실행 가능
                        Log.d("TrailMainScreen", "지도 준비 완료")
                    }
                }
            },
            update = {
                trailMapView ->
                trailLifecycleHelper.onResume()
            }
        )
        // ✅ 마커 표시
        if (!isRecording) {
            recommendedPaths.forEach { path ->
                val hBias = (path.latLngPoint!!.longitude * 2) - 1f
                val vBias = (path.latLngPoint!!.latitude * 2) - 1f

                PathMarker(
                    modifier = Modifier.align(BiasAlignment(hBias.toFloat(), vBias.toFloat())),
                    onClick = {
                        viewModel.updateSelectedPath(path)
                        navController.navigate(TrailNavigationRoute.TrailDetailTab)
                    }
                )
            }
        }
        // ✅ 하단 Bottom Sheet
        AnimatedVisibility(
            visible = isSheetOpen && !isRecording,
            modifier = Modifier.align(Alignment.BottomCenter),
            enter = slideInVertically { it },
            exit = slideOutVertically(
                targetOffsetY = { it }, // 필요하면 0으로도 설정 가능
                animationSpec = tween(durationMillis = 0) // 0ms로 즉시 사라지도록
            )
        ) {
            BottomSheetContent(
                viewModel = viewModel,
                activeTab = activeTab,
                recommendedPaths = recommendedPaths,
                myRecords = myRecords,
                onSheetOpenToggle = { viewModel.updateIsSheetOpen(null) },
                onStartRecording = {
                    viewModel.updateIsFollowingPath(false)
                    viewModel.updateIsRecording(true)
                    viewModel.updateRecordingTime(0)
                    viewModel.updateIsSheetOpen(false)
                },
                onTabChange = { viewModel.updateActiveTab(it) },
                onPathClick = {
                    viewModel.updateSelectedPath(it)
                    navController.navigate(TrailNavigationRoute.TrailDetailTab)
                },
                onFollowClick = { path ->
                    viewModel.updateIsFollowingPath(true)
                    viewModel.updateIsRecording(true)
                    viewModel.updateIsSheetOpen(false)
                    Log.d("Tag-TrailMainScree", "Following path: ${path.name}")
                },
                onRegisterClick = {
                    viewModel.updateIsSheetOpen(false)
                    navController.navigate(TrailNavigationRoute.TrailCreateTab)
                }
            )
        }

        // ✅ 시트 다시 열기 버튼
        AnimatedVisibility(
            visible = !isSheetOpen && !isRecording,
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .padding(bottom = paddingLarge * 2)
        ) {
            ReopenSheetButton(onClick = { viewModel.updateIsSheetOpen(true) })
        }

        // ✅ 녹화 중 UI
        AnimatedVisibility(
            visible = isRecording,
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .padding(bottom = paddingLarge * 2)
        ) {
            RecordingControls(
                isPaused = isPaused,
                recordingTime = recordingTime,
                onPauseToggle = { viewModel.updateIsPaused(null) },
                onStopRecording = {
                    viewModel.updateSelectedPath(UserPath.EMPTY)
                    viewModel.updateIsRecording(false)
                    viewModel.updateRecordingTime(0)
                    viewModel.updateIsFollowingPath(false)
                    viewModel.updateIsPaused(false)
                    navController.navigate(TrailNavigationRoute.TrailCreateTab)
                }
            )
        }
    }
}
//} 




//@Preview(showBackground = true)
//@Composable
//fun TrailMainScreenPreview_Simple() {
//    val navController = rememberNavController()
//    val context = LocalContext.current
//    val lifecycleHelper = remember { MapViewLifecycleHelper(context) }
//
//    MaterialTheme {
//        TrailMainScreen(navController = navController, lifecycleHelper = lifecycleHelper)
//    }
//}