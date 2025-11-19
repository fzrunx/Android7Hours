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
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.BiasAlignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.viewinterop.AndroidView
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.compose.currentStateAsState
import androidx.navigation.NavController
import com.naver.maps.geometry.LatLng
import com.naver.maps.map.LocationTrackingMode
import com.naver.maps.map.NaverMap
import com.naver.maps.map.overlay.Marker
import com.naver.maps.map.util.FusedLocationSource
import com.sesac.common.component.CommonMapLifecycle
import com.sesac.common.component.CommonMapView
import com.sesac.common.ui.theme.paddingLarge
import kotlinx.coroutines.delay
import com.sesac.common.utils.PathMarker
import com.sesac.common.utils.effectPauseStop
import com.sesac.domain.model.UserPath
import com.sesac.trail.nav_graph.TrailNavigationRoute
import com.sesac.trail.presentation.TrailViewModel
import com.sesac.trail.presentation.component.BottomSheetContent
import com.sesac.trail.presentation.component.MemoDialog
import com.sesac.trail.presentation.component.RecordingControls
import com.sesac.trail.presentation.component.ReopenSheetButton
import com.sesac.trail.presentation.component.addMemoMarker

enum class WalkPathTab { RECOMMENDED, MY_RECORDS }

// --- Main Page Composable ---
@Composable
fun TrailMainScreen(
    viewModel: TrailViewModel = hiltViewModel(),
    navController: NavController,
    commonMapLifecycle : CommonMapLifecycle,
    onMapReady: ((NaverMap) -> Unit)? = null
) {
    val activity = LocalActivity.current
    val lifecycle = LocalLifecycleOwner.current.lifecycle
    // 현재 화면의 라이프사이클 상태 (RESUMED, PAUSED 등)
    val lifecycleState by lifecycle.currentStateAsState()
    // ViewModel State 들
    val recommendedPaths by viewModel.recommendedPaths.collectAsStateWithLifecycle()
    val myRecords by viewModel.myRecords.collectAsStateWithLifecycle()
    val isSheetOpen by viewModel.isSheetOpen.collectAsStateWithLifecycle()
    val isPaused by viewModel.isPaused.collectAsStateWithLifecycle()
    val isFollowingPath by viewModel.isFollowingPath.collectAsStateWithLifecycle()
    val isRecording by viewModel.isRecoding.collectAsStateWithLifecycle()
    val recordingTime by viewModel.recordingTime.collectAsStateWithLifecycle()
    val activeTab by viewModel.activeTab.collectAsStateWithLifecycle()
    // 네이버 지도 위치 소스
    val locationSource = remember {
        activity?.let { FusedLocationSource(it, 1000) }
            ?: throw IllegalStateException("Activity not found for FusedLocationSource")
    }
    // 메모 입력용 상태
    var showMemoDialog by remember { mutableStateOf(false) }
    var selectedCoord by remember { mutableStateOf<LatLng?>(null) }
    var memoText by remember { mutableStateOf("") }

    // NaverMap 저장 위한 변수
    var currentNaverMap by remember { mutableStateOf<NaverMap?>(null) }

    // 마커 관리 리스트/맵
    val markers = remember { mutableStateListOf<Marker>() }
    val infoWindowStates = remember { mutableStateMapOf<Marker, Boolean>() }
    val context = LocalContext.current

    // --- 타이머 로직 (녹화 중일 때 시간 증가) ---
    LaunchedEffect(lifecycleState, isRecording, isPaused) {
        if (isRecording && !isPaused && lifecycleState == Lifecycle.State.RESUMED) {
            while (isRecording && !isPaused && lifecycleState == Lifecycle.State.RESUMED) {
                delay(1000)
                viewModel.updateRecordingTime(1)
            }
            Log.d("effectPauseStop", "타이머 자동 정지됨 (lifecycle or paused)")
        }
    }
    // 🔴 effectPauseStop 적용  // 화면 Pause/Stop 시 MapView도 같이 pause/stop 호출
    lifecycle.effectPauseStop {
        commonMapLifecycle.mapView?.onPause()
        commonMapLifecycle.mapView?.onStop()
        Log.d("TrailMainScreen", "📌 Trail Pause/Stop → MapView pause/stop 호출됨")
    }
    Box(
        modifier = Modifier.fillMaxSize()
    ) {
        // ✅ 지도 영역 (AsyncImage → AndroidView 로 대체) // 🔹 AndroidView 안에서 attach 처리
        key(lifecycleState) {
            if (lifecycleState.isAtLeast(Lifecycle.State.CREATED)) {
                AndroidView(
                    modifier = Modifier.fillMaxSize(),
                    factory = { context ->
                        // 🔹 1. MapView 가져오기
                        val mapView = commonMapLifecycle.mapView ?: CommonMapView.getMapView(context).also {
                            commonMapLifecycle.setMapView(it)
                        }

                        // 🔹 2. 이미 부모가 있으면 제거 (IllegalStateException 방지)
                        (mapView.parent as? ViewGroup)?.removeView(mapView)

                        // 🔹 3. MapView start/resume
                        mapView.onStart()
                        mapView.onResume()
                        mapView.getMapAsync{ naverMap ->
                            currentNaverMap = naverMap   // ready 된 지도 저장!!
                            naverMap.locationSource = locationSource
                            naverMap.locationTrackingMode = LocationTrackingMode.Follow
                            // ✅ Trail 용 지도 세팅 (기본 위치 / UI 세팅 등)
                            naverMap.uiSettings.isLocationButtonEnabled = true
                            naverMap.uiSettings.isZoomControlEnabled = false
                            onMapReady?.invoke(naverMap) // 🔹 화면마다 콜백 재등록
                            // ✅ onMapReady 시점에 콜백 실행 가능
                            Log.d("TrailMainScreen", "지도 준비 완료")
                            // 롱 클릭: 메모 입력
                            naverMap.setOnMapLongClickListener { _, coord ->
                                selectedCoord = coord
                                memoText = ""
                                showMemoDialog = true
                            }
                        }
                        mapView
                    },
                    update = {
                        it.requestLayout()
                    }
                )
            }
        }
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
        MemoDialog(
            show = showMemoDialog,
            memoText = memoText,
            onTextChange = { memoText = it },
            onCancel = { showMemoDialog = false },
            onConfirm = {
                val coord = selectedCoord
                val map = currentNaverMap

                if (coord != null && map != null) {
                    addMemoMarker(
                        context = context,
                        naverMap = map,
                        coord = coord,
                        memo = memoText,
                        markers = markers,
                        infoWindowStates = infoWindowStates
                    )
                }

                showMemoDialog = false
            }
        )
    }
}