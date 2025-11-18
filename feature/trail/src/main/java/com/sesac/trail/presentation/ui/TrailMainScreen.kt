package com.sesac.trail.presentation.ui

import android.util.Log
import android.widget.Toast
import androidx.activity.compose.LocalActivity
import androidx.compose.runtime.Composable
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.layout.*
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.viewinterop.AndroidView
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.compose.currentStateAsState
import androidx.navigation.NavController
import com.naver.maps.map.LocationTrackingMode
import com.naver.maps.map.NaverMap
import com.naver.maps.map.util.FusedLocationSource
import com.sesac.common.component.CommonMapLifecycle
import com.sesac.common.component.CommonMapView
import com.sesac.common.ui.theme.paddingLarge
import kotlinx.coroutines.delay
import com.sesac.domain.model.Coord
import com.sesac.domain.model.UserPath
import com.sesac.domain.result.AuthResult
import com.sesac.domain.result.AuthUiState
import com.sesac.trail.nav_graph.TrailNavigationRoute
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
    uiState: AuthUiState,
    commonMapLifecycle : CommonMapLifecycle,
    onMapReady: ((NaverMap) -> Unit)? = null
) {
    val activity = LocalActivity.current
    val context = LocalContext.current
    val lifecycle = LocalLifecycleOwner.current.lifecycle
    val lifecycleState by lifecycle.currentStateAsState()
    val recommendedPaths by viewModel.recommendedPaths.collectAsStateWithLifecycle()
    val myPaths by viewModel.myPaths.collectAsStateWithLifecycle()

    val isSheetOpen by viewModel.isSheetOpen.collectAsStateWithLifecycle()
    val isPaused by viewModel.isPaused.collectAsStateWithLifecycle()
    val isFollowingPath by viewModel.isFollowingPath.collectAsStateWithLifecycle()
    val isRecording by viewModel.isRecoding.collectAsStateWithLifecycle()
    val recordingTime by viewModel.recordingTime.collectAsStateWithLifecycle()
    val isEditMode by viewModel.isEditMode.collectAsStateWithLifecycle()
    val activeTab by viewModel.activeTab.collectAsStateWithLifecycle()
//    var showCreatePage by remember { mutableStateOf(false) }

    val locationSource = remember {
        activity?.let { FusedLocationSource(it, 1000) }
            ?: throw IllegalStateException("Activity not found for FusedLocationSource")
    }

    LaunchedEffect(Unit) {
        viewModel.getRecommendedPaths(Coord.DEFAULT, 10000f)
    }

    LaunchedEffect(uiState) {
        uiState.token?.let {
            viewModel.getMyPaths(it)
        }
    }

    // --- 타이머 로직 (녹화 중일 때 시간 증가) ---
    LaunchedEffect(key1 = isRecording, key2 = isPaused) {
        if (isRecording && !isPaused) {
            while (true) {
                delay(1000)
                viewModel.updateRecordingTime(1)
            }
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
    ) {
        // ✅ 지도 영역 (AsyncImage → AndroidView 로 대체) // 🔹 AndroidView 안에서 attach 처리
        key(lifecycleState) {
            if (lifecycleState.isAtLeast(Lifecycle.State.CREATED)) {
                AndroidView(
                    modifier = Modifier.fillMaxSize(),
                    factory = { context ->
                        CommonMapView.getMapView(context).apply {
                            getMapAsync { naverMap ->
                                naverMap.locationSource = locationSource
                                naverMap.locationTrackingMode = LocationTrackingMode.Follow
                                // ✅ Trail 용 지도 세팅 (기본 위치 / UI 세팅 등)
                                naverMap.uiSettings.isLocationButtonEnabled = true
                                naverMap.uiSettings.isZoomControlEnabled = false
                                onMapReady?.invoke(naverMap) // 🔹 화면마다 콜백 재등록
                                // ✅ onMapReady 시점에 콜백 실행 가능
                                Log.d("TrailMainScreen", "지도 준비 완료")
                            }
                        }
                    },
                    update = {
                        it.requestLayout()
                    }
                )
            }
        }
        // ✅ 마커 표시
        if (!isRecording) {
            when (recommendedPaths) {
                is AuthResult.Loading -> CircularProgressIndicator()
                is AuthResult.Success -> {
                    (recommendedPaths as AuthResult.Success<List<UserPath>>).resultData.forEach { path ->
                        path.coord?.forEach {
                            val hBias = (it.longitude * 2) - 1f
                            val vBias = (it.latitude * 2) - 1f

//                            PathMarker(
//                                modifier = Modifier.align(BiasAlignment(hBias.toFloat(), vBias.toFloat())),
//                                onClick = {
//                                    viewModel.updateSelectedPath(path)
//                                    navController.navigate(TrailNavigationRoute.TrailDetailTab)
//                                }
//                            )
                        }

                    }
                }
                is AuthResult.NetworkError -> Toast.makeText(
                    context,
                    (recommendedPaths as AuthResult.NetworkError).exception.message,
                    Toast.LENGTH_SHORT
                ).show()
                else -> { }
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
            // ToDo : NetworkError, 경로 없음 -> 빈화면 혹은 오류 화면 출력
            BottomSheetContent(
                viewModel = viewModel,
                activeTab = activeTab,
                recommendedPaths = if (recommendedPaths is AuthResult.Success) (recommendedPaths as AuthResult.Success<List<UserPath>>).resultData else listOf(),
                myPaths = if (myPaths is AuthResult.Success) (myPaths as AuthResult.Success<List<UserPath>>).resultData else listOf(),
                isEditMode = isEditMode,
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
                },
                onEditModeToggle = { viewModel.updateIsEditMode() },
                onModifyClick = {
                    viewModel.updateSelectedPath(it)
                    navController.navigate(TrailNavigationRoute.TrailCreateTab)
                },
                onDeleteClick = { viewModel.deletePath(uiState.token, it) }
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