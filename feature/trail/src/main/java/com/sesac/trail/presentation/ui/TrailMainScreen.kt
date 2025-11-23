package com.sesac.trail.presentation.ui

import android.Manifest
import android.annotation.SuppressLint
import android.content.pm.PackageManager
import android.location.Location
import android.os.Looper
import android.util.Log
import android.widget.Toast
import android.view.ViewGroup
import androidx.activity.compose.LocalActivity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.runtime.Composable
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.layout.*
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.content.ContextCompat
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.compose.currentStateAsState
import androidx.navigation.NavController
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationResult
import com.google.android.gms.location.Priority
import com.naver.maps.geometry.LatLng
import com.naver.maps.map.LocationTrackingMode
import com.naver.maps.map.NaverMap
import com.naver.maps.map.overlay.Marker
import com.naver.maps.map.overlay.PolylineOverlay
import com.naver.maps.map.util.FusedLocationSource
import com.sesac.common.component.CommonMapLifecycle
import com.sesac.common.component.CommonMapView
import com.sesac.common.ui.theme.paddingLarge
import kotlinx.coroutines.delay
import com.sesac.domain.model.Coord
import com.sesac.common.utils.EffectPauseStop
import com.sesac.domain.model.Path
import com.sesac.domain.result.AuthUiState
import com.sesac.domain.result.ResponseUiState
import com.sesac.trail.nav_graph.TrailNavigationRoute
import com.sesac.trail.presentation.TrailViewModel
import com.sesac.trail.presentation.component.BottomSheetContent
import com.sesac.trail.presentation.component.MemoDialog
import com.sesac.trail.presentation.component.RecordingControls
import com.sesac.trail.presentation.component.ReopenSheetButton
import com.sesac.trail.presentation.component.addMemoMarker
import androidx.compose.runtime.DisposableEffect
import com.sesac.common.model.toPathParceler
import com.sesac.trail.nav_graph.NestedNavigationRoute

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

    val context = LocalContext.current
    val activity = LocalActivity.current
    val lifecycle = LocalLifecycleOwner.current.lifecycle
    var lastRawLocation by remember { mutableStateOf<android.location.Location?>(null) }
    var lastSmoothedLocation by remember { mutableStateOf<android.location.Location?>(null) }
    // 현재 화면의 라이프사이클 상태 (RESUMED, PAUSED 등)
    val lifecycleState by lifecycle.currentStateAsState()
    // ViewModel State 들
    val recommendedPaths by viewModel.recommendedPaths.collectAsStateWithLifecycle()
    val myPaths by viewModel.myPaths.collectAsStateWithLifecycle()

    val isSheetOpen by viewModel.isSheetOpen.collectAsStateWithLifecycle()
    val isPaused by viewModel.isPaused.collectAsStateWithLifecycle()
    val isFollowingPath by viewModel.isFollowingPath.collectAsStateWithLifecycle()
    val isRecording by viewModel.isRecoding.collectAsStateWithLifecycle()
    val recordingTime by viewModel.recordingTime.collectAsStateWithLifecycle()
    val isEditMode by viewModel.isEditMode.collectAsStateWithLifecycle()
    val activeTab by viewModel.activeTab.collectAsStateWithLifecycle()
    // GPS 기록 좌표
    val pathCoords = remember { mutableStateListOf<LatLng>() }
    val polylineFromVM by viewModel.polylineOverlay.collectAsStateWithLifecycle()
    var isTracking by remember { mutableStateOf(false) }
    // 네이버 지도 위치 소스
    val locationSource = remember {
        activity?.let { FusedLocationSource(it, 1000) }
            ?: throw IllegalStateException("Activity not found for FusedLocationSource")
    }
    // 위치 권한 상태 추적
    var hasLocationPermission by remember {
        mutableStateOf(
            ContextCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED &&
            ContextCompat.checkSelfPermission(context, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED
        )
    }

    // 위치 권한 요청
    val locationPermissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestMultiplePermissions()
    ) { permissions ->
        hasLocationPermission = permissions[Manifest.permission.ACCESS_FINE_LOCATION] == true &&
                                permissions[Manifest.permission.ACCESS_COARSE_LOCATION] == true
        if (hasLocationPermission) isTracking = true
    }

    val fusedLocationClient = remember {
        com.google.android.gms.location.LocationServices.getFusedLocationProviderClient(context)
    }
    // 메모 입력용 상태
    var showMemoDialog by remember { mutableStateOf(false) }
    var selectedCoord by remember { mutableStateOf<LatLng?>(null) }
    var memoText by remember { mutableStateOf("") }

    // NaverMap 저장 위한 변수
    var currentNaverMap by remember { mutableStateOf<NaverMap?>(null) }

    // 마커 관리 리스트/맵
    val currentMarkers = viewModel.currentMarkers
    val infoWindowStates = remember { mutableStateMapOf<Marker, Boolean>() }

    // 위치 콜백
    val locationCallback = remember {
        object : LocationCallback() {
            override fun onLocationResult(result: LocationResult) {
                result.locations.forEach { loc ->

                    // 🔥 1) accuracy 필터링
                    if (loc.accuracy > 25f) {
                        Log.d("GPS", "무시됨: accuracy=${loc.accuracy}")
                        return@forEach
                    }

                    // 🔥 2) smoothing 적용
                    val smoothLoc = smooth(lastSmoothedLocation, loc)

                    lastRawLocation = loc
                    lastSmoothedLocation = smoothLoc

                    val newPoint = LatLng(smoothLoc.latitude, smoothLoc.longitude)

                    // 🔥 3) 최소 이동거리 필터 (정지시 지그재그 방지)
                    val lastPoint = pathCoords.lastOrNull()
                    if (lastPoint != null) {
                        val diff = lastPoint.distanceTo(newPoint)
                        if (diff < 5) {
                            Log.d("GPS", "5m 미만이라 무시됨: 이동거리=$diff")
                            return@forEach
                        }
                    }

                    // 🔥 4) 최종 추가
                    pathCoords.add(newPoint)
                    Log.d("GPS", "추가됨: ${newPoint.latitude}, ${newPoint.longitude}")
                }
            }
        }
    }


    // ⭐⭐ 폴리라인 좌표 업데이트
    LaunchedEffect(pathCoords.size, isRecording) {
        val currentPolyline = polylineFromVM

        if (isRecording && pathCoords.size >= 2) {
            currentPolyline?.coords = pathCoords.toList()
            currentPolyline?.map = currentNaverMap
            Log.d("TrailMainScreen", "📊 폴리라인 업데이트: ${pathCoords.size}개 좌표")
        } else {
            currentPolyline?.map = null
            Log.d("TrailMainScreen", "❌ 폴리라인 지도에서 제거")
        }
    }

// ⭐ 녹화 종료 시 초기화
    LaunchedEffect(isRecording) {
        if (!isRecording) {
            //  기록 좌표 초기화
            pathCoords.clear()

            Log.d("TrailMainScreen", "🧹 녹화 중지 시 폴리라인, 마커, 좌표 초기화 완료")
        }
    }

    LaunchedEffect(Unit) {
        viewModel.getRecommendedPaths(Coord.DEFAULT, 10000f)
    }

    LaunchedEffect(Unit, uiState) {
        viewModel.getMyPaths(uiState.token)
        Log.d("TAG-TrailMainScreen", "myPaths : $myPaths")
    }

    // --- 타이머 로직 (녹화 중일 때 시간 증가) ---
    LaunchedEffect(lifecycleState, isRecording, isPaused) {
        while (isRecording && !isPaused && lifecycleState == Lifecycle.State.RESUMED) {
            delay(1000)
            viewModel.updateRecordingTime(1)
        }
        Log.d("effectPauseStop", "타이머 자동 정지됨 (lifecycle or paused)")
    }
    // 🔴 effectPauseStop 적용  // 화면 Pause/Stop 시 MapView도 같이 pause/stop 호출
    lifecycle.EffectPauseStop {
        commonMapLifecycle.mapView?.onPause()
        commonMapLifecycle.mapView?.onStop()
        Log.d("TrailMainScreen", "📌 Trail Pause/Stop → MapView pause/stop 호출됨")
    }

    // --- 위치 업데이트 시작/중지 ---
    LaunchedEffect(isRecording, isPaused, hasLocationPermission) {  // ⭐ hasLocationPermission 추가
        val locationRequest = LocationRequest.Builder(Priority.PRIORITY_HIGH_ACCURACY, 2000L)
            .setWaitForAccurateLocation(false)
            .setMinUpdateIntervalMillis(500L)
            .setMaxUpdateDelayMillis(1000L)
            .build()

        if (isRecording && !isPaused) {
            if (hasLocationPermission) {  // ⭐ state 사용
                @SuppressLint("MissingPermission")
                fusedLocationClient.requestLocationUpdates(
                    locationRequest,
                    locationCallback,
                    Looper.getMainLooper()
                )
                Log.d("TrailMainScreen", "📍 위치 업데이트 시작")
            } else {
                // 권한 요청
                locationPermissionLauncher.launch(
                    arrayOf(
                        Manifest.permission.ACCESS_FINE_LOCATION,
                        Manifest.permission.ACCESS_COARSE_LOCATION
                    )
                )
            }
        } else {
            fusedLocationClient.removeLocationUpdates(locationCallback)
            Log.d("TrailMainScreen", "📍 위치 업데이트 중지")
        }
    }
        DisposableEffect(Unit) {
            onDispose {
                fusedLocationClient.removeLocationUpdates(locationCallback)
                currentNaverMap?.locationSource = null // NaverMap에서 locationSource 해제
                locationSource.deactivate() // FusedLocationSource 비활성화
                Log.d("TrailMainScreen", "📍 화면 사라짐, 위치 업데이트 중지 및 NaverMap locationSource 해제")
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

                            // 지도에 연결하는 것은 LaunchedEffect(pathCoords.size, isRecording)에서 관리합니다.
                            val newPolyline = PolylineOverlay().apply {
                                color = 0xFF0000FF.toInt()
                                width = 10
                                capType = PolylineOverlay.LineCap.Round
                                joinType = PolylineOverlay.LineJoin.Round
                            }
                            viewModel.setPolylineInstance(newPolyline)  // ⭐ 항상 새로운 폴리라인 객체로 갱신

                            // 롱 클릭: 메모 입력
                            naverMap.setOnMapLongClickListener { _, coord ->
                                if (isRecording) {
                                    selectedCoord = coord
                                    memoText = ""
                                    showMemoDialog = true
                                }
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
            when (val state = recommendedPaths) {
                is ResponseUiState.Loading -> {
                    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                        CircularProgressIndicator()
                    }
                }
                is ResponseUiState.Success -> {
                    state.result.forEach { path ->
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
                is ResponseUiState.Error -> {
                    Toast.makeText(context, state.message, Toast.LENGTH_SHORT).show()
                }
                is ResponseUiState.Idle -> {}
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
            val activeState = if (activeTab == WalkPathTab.RECOMMENDED) recommendedPaths else myPaths

            when(activeState) {
                is ResponseUiState.Loading -> {
                    Box(modifier = Modifier.fillMaxWidth().height(300.dp), contentAlignment = Alignment.Center) {
                        CircularProgressIndicator()
                    }
                }
                is ResponseUiState.Error -> {
                    Box(modifier = Modifier.fillMaxWidth().height(300.dp), contentAlignment = Alignment.Center) {
                        Text(text = activeState.message)
                    }
                }
                else -> { // Success or Idle
                    BottomSheetContent(
                        viewModel = viewModel,
                        uiState = uiState,
                        activeTab = activeTab,
                        recommendedPaths = (recommendedPaths as? ResponseUiState.Success)?.result ?: emptyList(),
                        myPaths = (myPaths as? ResponseUiState.Success)?.result ?: emptyList(),
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
                            navController.navigate(NestedNavigationRoute.TrailDetail(it.toPathParceler()))
                        },
                        onFollowClick = { path ->
                            viewModel.updateIsFollowingPath(true)
                            viewModel.updateIsRecording(true)
                            viewModel.updateIsSheetOpen(false)
                            Log.d("Tag-TrailMainScree", "Following path: ${path.pathName}")
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
            }
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
                    viewModel.updateSelectedPath(Path.EMPTY)
                    viewModel.updateIsRecording(false)
                    viewModel.updateRecordingTime(0)
                    viewModel.updateIsFollowingPath(false)
                    viewModel.updateIsPaused(false)
                    viewModel.clearAllMapObjects(currentNaverMap)

                    pathCoords.clear()

                    currentNaverMap?.locationTrackingMode = LocationTrackingMode.Follow

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
                        markers = currentMarkers,
                        infoWindowStates = infoWindowStates
                    )
                }

                showMemoDialog = false
            }
        )
    }
}

fun smooth(old: Location?, new: Location): Location {
    if (old == null) return new

    val alpha = 0.2f // 0~1 (0에 가까울수록 더 부드러움)

    val smoothed = Location(new).apply {
        latitude = old.latitude + alpha * (new.latitude - old.latitude)
        longitude = old.longitude + alpha * (new.longitude - old.longitude)
        accuracy = new.accuracy
        bearing = new.bearing
        speed = new.speed
        time = new.time
    }

    return smoothed
}