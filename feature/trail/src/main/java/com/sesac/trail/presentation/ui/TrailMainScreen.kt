package com.sesac.trail.presentation.ui
import android.Manifest
import android.content.pm.PackageManager
import android.util.Log
import android.view.ViewGroup
import android.widget.Toast
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import com.naver.maps.map.CameraUpdate
import com.naver.maps.map.overlay.OverlayImage
import com.sesac.common.model.toPathParceler
import com.sesac.domain.model.Place
import com.sesac.trail.nav_graph.NestedNavigationRoute
import com.sesac.trail.presentation.component.FollowGuide
import com.sesac.trail.utils.toLatLng
import com.sesac.common.model.toParceler

enum class WalkPathTab { RECOMMENDED, MY_RECORDS }

// --- Main Page Composable ---
@Composable
fun TrailMainScreen(
    viewModel: TrailViewModel = hiltViewModel(),
    navController: NavController,
    uiState: AuthUiState,
    commonMapLifecycle : CommonMapLifecycle,
    onStartFollowing: (Path) -> Unit,
    onMapReady: ((NaverMap) -> Unit)? = null,
) {

    val context = LocalContext.current
    val activity = LocalActivity.current
    val lifecycle = LocalLifecycleOwner.current.lifecycle

    val lifecycleState by lifecycle.currentStateAsState()
    // ViewModel State 수집
    val recommendedPaths by viewModel.recommendedPaths.collectAsStateWithLifecycle()
    val myPaths by viewModel.myPaths.collectAsStateWithLifecycle()
    val userInfo by viewModel.userInfo.collectAsStateWithLifecycle()
    val isSheetOpen by viewModel.isSheetOpen.collectAsStateWithLifecycle()
    val isFollowingPath by viewModel.isFollowingPath.collectAsStateWithLifecycle()
    val isRecording by viewModel.isRecording.collectAsStateWithLifecycle()
    val recordingTime by viewModel.recordingTime.collectAsStateWithLifecycle()
    val activeTab by viewModel.activeTab.collectAsStateWithLifecycle()
    val selectedPath by viewModel.selectedPath.collectAsStateWithLifecycle()
    val tempPathCoords by viewModel.tempPathCoords.collectAsStateWithLifecycle()
    val polylineFromVM by viewModel.polylineOverlay.collectAsStateWithLifecycle()
    val placesState by viewModel.placesState.collectAsStateWithLifecycle()


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

    // Place 마커 관리 (ViewModel 외부)
    val placeMarkers = remember { mutableListOf<Marker>() }

    var initialCameraMoved by remember(currentNaverMap) { mutableStateOf(false) }

    // 폴리라인 좌표 업데이트
    LaunchedEffect(tempPathCoords.size, isRecording) {
        val currentPolyline = polylineFromVM

        if (isRecording && tempPathCoords.size >= 2) {
            currentPolyline?.coords = tempPathCoords.toList()
            currentPolyline?.map = currentNaverMap
            Log.d("TrailMainScreen", "📊 폴리라인 업데이트:  ${tempPathCoords.size}개 좌표")
        } else {
            currentPolyline?.map = null
            Log.d("TrailMainScreen", "❌ 폴리라인 지도에서 제거")
        }
    }
    // Draft, 경로 목록, 사용자 정보 초기화
    LaunchedEffect(Unit, hasLocationPermission, uiState) {
        if (hasLocationPermission) {
            viewModel.startLocationUpdates()
        } else {
            locationPermissionLauncher.launch(
                arrayOf(
                    Manifest.permission.ACCESS_FINE_LOCATION,
                    Manifest.permission.ACCESS_COARSE_LOCATION
                )
            )
        }

        viewModel.loadDrafts()
        viewModel.getMyPaths()
        viewModel.getCurrentUserInfo() // 현재 사용자 정보 요청
    }

    // 녹화 종료 시 초기화
    LaunchedEffect(isRecording) {
        if (!isRecording) {
            Log.d("TrailMainScreen", "🧹 녹화 중지 시 폴리라인, 마커, 좌표 초기화 완료")
        }
    }


    // --- 타이머 로직 (녹화 중일 때 시간 증가) ---
    LaunchedEffect(lifecycleState, isRecording) {
        while (isRecording && lifecycleState == Lifecycle.State.RESUMED) {
            delay(1000)
            viewModel.updateRecordingTime(1)
        }
        Log.d("effectPauseStop", "타이머 자동 정지됨 (lifecycle or paused)")
    }
    // effectPauseStop 적용  // 화면 Pause/Stop 시 MapView도 같이 pause/stop 호출
    lifecycle.EffectPauseStop {
        commonMapLifecycle.mapView?.onPause()
        commonMapLifecycle.mapView?.onStop()
        Log.d("TrailMainScreen", "📌 Trail Pause/Stop → MapView pause/stop 호출됨")
    }

    DisposableEffect(Unit) {
        onDispose {
            currentNaverMap?.locationSource = null // NaverMap에서 locationSource 해제
            locationSource.deactivate() // FusedLocationSource 비활성화
            Log.d("TrailMainScreen", "📍 화면 사라짐, 위치 업데이트 중지 및 NaverMap locationSource 해제")
        }
    }


    Box(
        modifier = Modifier
            .fillMaxSize()
    ) {
        // 지도 영역 (AsyncImage → AndroidView 로 대체) // 🔹 AndroidView 안에서 attach 처리
        key(lifecycleState) {
            if (lifecycleState.isAtLeast(Lifecycle.State.CREATED)) {
                AndroidView(
                    modifier = Modifier.fillMaxSize(),
                    factory = { context ->
                        // 1. MapView 가져오기
                        val mapView = commonMapLifecycle.mapView ?: CommonMapView.getMapView(context).also {
                            commonMapLifecycle.setMapView(it)
                        }

                        // 2. 이미 부모가 있으면 제거 (IllegalStateException 방지)
                        (mapView.parent as? ViewGroup)?.removeView(mapView)

                        // 3. MapView start/resume
                        mapView.onStart()
                        mapView.onResume()
                        mapView.getMapAsync{ naverMap ->
                            currentNaverMap = naverMap   // ready 된 지도 저장!!
                            naverMap.locationSource = locationSource
                            naverMap.locationTrackingMode = LocationTrackingMode.Follow
                            // Trail 용 지도 세팅 (기본 위치 / UI 세팅 등)
                            naverMap.uiSettings.isLocationButtonEnabled = true
                            naverMap.uiSettings.isZoomControlEnabled = false
                            onMapReady?.invoke(naverMap) // 화면마다 콜백 재등록
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
        // Place 마커 표시 (지도 준비 후)
        LaunchedEffect(placesState, currentNaverMap, isRecording, isFollowingPath) {
            Log.d("TAG-TrailMainScreen", "Place Marker Effect Triggered: isRecording=$isRecording, isFollowingPath=$isFollowingPath, placesState=${placesState.javaClass.simpleName}")
            val map = currentNaverMap ?: return@LaunchedEffect

            // 녹화나 따라가기 중일 때는 Place 마커 숨기기
            if (isRecording || isFollowingPath) {
                Log.d("TrailMainScreen", "Place Markers Hidden: isRecording or isFollowingPath is true.")
                placeMarkers.forEach { it.map = null }
                placeMarkers.clear()
                return@LaunchedEffect
            }

            // 기존 마커 제거
            placeMarkers.forEach { it.map = null }
            placeMarkers.clear()

            // Place 마커 추가
            when (placesState) {
                is ResponseUiState.Success -> {
                    val places = (placesState as ResponseUiState.Success<List<Place>>).result
                    Log.d("TrailMainScreen", "Place Markers Success: ${places.size} places loaded.")

                    places.forEach { place ->
                        val marker = Marker().apply {
                            position = place.toLatLng()
                            icon = Marker.DEFAULT_ICON

                            // 즉시 상세 페이지로 이동
                            setOnClickListener { clickedMarker ->
                                navController.navigate(
                                    NestedNavigationRoute.PlaceDetail(place.toParceler())
                                )
                                true // 이벤트 소비 완료를 나타냄
                            }

                            this.map = map
                        }
                        placeMarkers.add(marker)
                    }

                    Log.d("TrailMainScreen", "✅ 병원 마커 ${places.size}개 표시됨")
                }
                is ResponseUiState.Loading -> {
                    Log.d("TrailMainScreen", "⏳ 병원 데이터 로딩 중...")
                }
                is ResponseUiState.Error -> {
                    Log.e("TrailMainScreen", "❌ 병원 로드 실패: ${(placesState as ResponseUiState.Error).message}")
                }
                else -> {}
            }
        }

        // Place 마커 정리
        DisposableEffect(Unit) {
            onDispose {
                placeMarkers.forEach { it.map = null }
                placeMarkers.clear()
            }
        }
        // 선택된 경로의 폴리라인 표시
        DisposableEffect(isFollowingPath, selectedPath, currentNaverMap) {
            Log.d("TrailMainScreen", "🔹 DisposableEffect 진입: isFollowing=$isFollowingPath, path=${selectedPath?.pathName}, map=$currentNaverMap")
            val map = currentNaverMap
            val path = selectedPath

            val followPolyline: PolylineOverlay?
            val startMarker: Marker?
            val endMarker: Marker?

            if (map != null && isFollowingPath && path != null) {
                val coords = path.coord?.map { it.toLatLng() } ?: emptyList()

                if (coords.size < 2) {
                    Log.e("TrailMainScreen", "❌ 좌표 부족")
                    followPolyline = null
                    startMarker = null
                    endMarker = null
                } else {
                    // 폴리라인
                    followPolyline = PolylineOverlay().apply {
                        this.coords = coords
                        color = 0xFF6200EE.toInt()
                        width = 12
                        capType = PolylineOverlay.LineCap.Round
                        joinType = PolylineOverlay.LineJoin.Round
                        this.map = map
                    }

                    // 시작 마커 (초록색)
                    startMarker = Marker().apply {
                        position = coords.first()
                        icon = OverlayImage.fromResource(android.R.drawable.ic_input_add) // 또는 커스텀 아이콘
                        captionText = "출발"
                        captionColor = Color.Green.toArgb()
                        this.map = map
                    }

                    // 종료 마커 (빨간색)
                    endMarker = Marker().apply {
                        position = coords.last()
                        icon = OverlayImage.fromResource(android.R.drawable.ic_menu_close_clear_cancel)
                        captionText = "도착"
                        captionColor = Color.Red.toArgb()
                        this.map = map
                    }

                    // 카메라 이동
                    val cameraUpdate = CameraUpdate.scrollTo(coords.first())
                    map.moveCamera(cameraUpdate)

                    Log.d("TrailMainScreen", "✅ 폴리라인 + 시작/종료 마커 생성 완료")
                }
            } else {
                followPolyline = null
                startMarker = null
                endMarker = null
            }

            onDispose {
                followPolyline?.map = null
                startMarker?.map = null
                endMarker?.map = null
                Log.d("TrailMainScreen", "🧹 폴리라인 + 마커 제거")
            }
        }
                // 사용자 현재 위치 마커
                val userLocation by viewModel.userLocationMarker.collectAsStateWithLifecycle()
                var userMarker by remember { mutableStateOf<Marker?>(null) }
        
                LaunchedEffect(userLocation, currentNaverMap, isFollowingPath) {
                    Log.d(
                        "TrailMainScreen",
                        "🔹 마커 LaunchedEffect: location=$userLocation, map=$currentNaverMap, isFollowing=$isFollowingPath"
                    )
                    val map = currentNaverMap ?: return@LaunchedEffect
                    val location = userLocation
        
                    if (isFollowingPath && location != null) {
                        try {
                            if (userMarker == null) {
                                Log.d("TrailMainScreen", "🎯 마커 생성 시작...")
                                userMarker = Marker().apply {
                                    this.position = location // Set position FIRST
                                    this.icon = OverlayImage.fromResource(android.R.drawable.ic_menu_mylocation)
                                    this.width = 60
                                    this.height = 60
                                    this.map = map // Set map LAST
                                }
                                Log.d("TrailMainScreen", "✅ 마커 생성 완료")
                            } else {
                                // If marker already exists, just update its position
                                userMarker?.position = location
                            }
                            Log.d(
                                "TrailMainScreen",
                                "📍 마커 위치 업데이트: (${location.latitude}, ${location.longitude})"
                            )
                        } catch (e: Exception) {
                            Log.e("TrailMainScreen", "❌ 마커 생성/업데이트 실패: ${e.message}", e)
                        }
                    } else {
                        if (userMarker != null) {
                            Log.d("TrailMainScreen", "🗑️ 마커 제거")
                        }
                        userMarker?.map = null
                        userMarker = null
                    }
                }

        // 따라가기 안내 UI
        AnimatedVisibility(
            visible = isFollowingPath,
            modifier = Modifier
                .align(Alignment.TopCenter)
                .padding(top = 80.dp)
        ) {
            FollowGuide(viewModel = viewModel,
                onStopFollowing = {
                    viewModel.stopFollowing()
                    viewModel.updateIsFollowingPath(false)
                    viewModel.clearUserLocationMarker()
                }
            )
        }

        // 메모 마커 표시 (ViewModel 상태 기반)
        val memoMarkers by viewModel.memoMarkers.collectAsStateWithLifecycle()
        LaunchedEffect(memoMarkers, currentNaverMap, isRecording, isFollowingPath) {
            val map = currentNaverMap ?: return@LaunchedEffect

            // 녹화 또는 따라가기 중일 때만 마커 표시
            if (!isRecording && !isFollowingPath) {
                // 기존 마커 정리
                currentMarkers.forEach { it.map = null }
                currentMarkers.clear()
                return@LaunchedEffect
            }

            // 기존 마커 정리
            currentMarkers.forEach { it.map = null }
            currentMarkers.clear()

            // 새 마커 추가
            memoMarkers.forEach { memoMarker ->
                addMemoMarker(
                    context = context,
                    naverMap = map,
                    coord = LatLng(memoMarker.latitude, memoMarker.longitude),
                    memo = memoMarker.memo ?: "",
                    markers = currentMarkers,
                    infoWindowStates = infoWindowStates
                )
            }
        }

        // 하단 Bottom Sheet
        AnimatedVisibility(
            visible = isSheetOpen && !isRecording && !isFollowingPath,
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
                        uiState = uiState,
                        activeTab = activeTab,
                        recommendedPaths = (recommendedPaths as? ResponseUiState.Success)?.result ?: emptyList(),
                        myPaths = (myPaths as? ResponseUiState.Success)?.result ?: emptyList(),
                        currentUser = userInfo,
                        onSheetOpenToggle = { viewModel.updateIsSheetOpen(null) },
                        onStartRecording = {
                            viewModel.startRecording()
                            viewModel.updateIsSheetOpen(false)
                        },
                        onTabChange = { viewModel.updateActiveTab(it) },
                        onPathClick = {
                            viewModel.updateSelectedPath(it)
                            navController.navigate(NestedNavigationRoute.TrailDetail(it.toPathParceler()))
                        },
                        onFollowClick = onStartFollowing,
                        onModifyClick = { path ->
                            viewModel.updateSelectedPath(path)
                            navController.navigate(TrailNavigationRoute.TrailCreateTab)
                        },
                        onDeleteClick = { pathId -> viewModel.deletePath(pathId) }
                    )
                }
            }
        }

        // 시트 다시 열기 버튼
        AnimatedVisibility(
            visible = !isSheetOpen && !isRecording && !isFollowingPath,
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .padding(bottom = paddingLarge * 2)
        ) {
            ReopenSheetButton(onClick = { viewModel.updateIsSheetOpen(true) })
        }

        // 녹화 중 UI
        AnimatedVisibility(
            visible = isRecording,
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .padding(bottom = paddingLarge * 2)
        ) {
            RecordingControls(
                recordingTime = recordingTime,
                onStopRecording = {
                    viewModel.resetCreateState()
                    viewModel.resetUpdateState()
                    // 현재 기록된 좌표(LatLng)를 도메인 모델의 Coord로 변환
                    val recordedCoords = tempPathCoords.map { latLng -> Coord(latLng.latitude, latLng.longitude) } // ← MODIFIED

                    // 🔥 마커 데이터 포함
                    val currentMemoMarkers = viewModel.memoMarkers.value
                    val newPath = Path.EMPTY.copy(
                        coord = recordedCoords,
                        markers = currentMemoMarkers
                    )

                    // ViewModel에 새로 생성된 경로를 업데이트
                    viewModel.updateSelectedPath(newPath)

                    // 녹화 관련 상태 초기화
                    viewModel.updateIsRecording(false)
                    viewModel.stopRecording()
                    viewModel.updateIsFollowingPath(false)

                    viewModel.clearAllMapObjects(currentNaverMap)

                    currentNaverMap?.locationTrackingMode = LocationTrackingMode.Follow

                    // 화면 이동
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
                selectedCoord?.let {
                    viewModel.addMemoMarker(it.latitude, it.longitude, memoText)
                }

                // Append the memo to the description in the ViewModel
                viewModel.selectedPath.value?.let { currentPath ->
                    val currentDescription = currentPath.pathComment ?: ""
                    val newDescription = if (currentDescription.isEmpty()) {
                        memoText
                    } else {
                        "$currentDescription\n\n$memoText"
                    }
                    viewModel.updateSelectedPath(currentPath.copy(pathComment = newDescription))
                }

                showMemoDialog = false
            }
        )
    }
}

