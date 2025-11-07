package com.sesac.trail.presentation.ui

import android.util.Log
import android.view.ViewGroup
import androidx.activity.compose.LocalActivity
import androidx.compose.runtime.Composable
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.BiasAlignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.navigation.NavController
import com.naver.maps.map.LocationTrackingMode
import com.naver.maps.map.MapView
import com.naver.maps.map.NaverMap
import com.naver.maps.map.util.FusedLocationSource
import kotlinx.coroutines.delay
import com.sesac.common.ui.theme.*
import com.sesac.trail.presentation.TrailMapViewLifecycleHelper
import com.sesac.trail.presentation.component.BottomSheetContent
import com.sesac.trail.presentation.component.RecordingControls
import com.sesac.trail.presentation.component.ReopenSheetButton
import com.sesac.trail.presentation.model.MyRecord
import com.sesac.trail.presentation.model.UserPath


data class MapPosition(val top: Float, val left: Float) // 0.0f to 1.0f


enum class WalkPathTab {
    RECOMMENDED, MY_RECORDS
}

// --- Mock Data ---

val mockRecommendedPaths = listOf(
    UserPath(1, "강남역 주변 산책로", "산책왕123", "1.5km", "초급 15분", 45, "0.3km",  MapPosition(0.2f, 0.3f)),
    UserPath(2, "압구정 한강 야경 코스", "멍멍이사랑", "2.8km", "중급 35분", 89, "0.8km",  MapPosition(0.45f, 0.6f)),
    UserPath(3, "청담동 카페거리 산책", "댕댕이집사", "2.0km", "초급 25분", 67, "1.2km", MapPosition(0.3f, 0.7f)),
    UserPath(4, "선릉역 공원 코스", "산책매니아", "1.2km", "초급 15분", 34, "1.5km", MapPosition(0.55f, 0.4f)),
    UserPath(5, "서울숲 산책로", "자연러버", "2.5km", "중급 30분", 152, "2.1km",  MapPosition(0.25f, 0.5f)),
)

val mockMyRecords = listOf(
    MyRecord(101, "아침 산책", "2025.11.02", "2.3km", "32분", 3420, 145, Purple500),
    MyRecord(102, "한강공원 조깅", "2025.11.01", "4.5km", "58분", 6780, 298, ColorBlue),
    MyRecord(103, "저녁 산책", "2025.10.31", "1.8km", "25분", 2560, 112, AccentGreen),
)

// --- Main Page Composable ---
@Composable
fun TrailMainScreen(navController: NavController,
                    trailLifecycleHelper: TrailMapViewLifecycleHelper, // 라이프 사이클 따로 관리하려고 만듬
                    onMapReady: ((NaverMap) -> Unit)? = null) {
    var isSheetOpen by remember { mutableStateOf(true) }
    var isRecording by remember { mutableStateOf(false) }
    var isPaused by remember { mutableStateOf(false) }
    var recordingTime by remember { mutableStateOf(0L) } // 초 단위
    var selectedPath by remember { mutableStateOf<UserPath?>(null) }
    var isFollowingPath by remember { mutableStateOf(false) }
    var showCreatePage by remember { mutableStateOf(false) }
    var activeTab by remember { mutableStateOf(WalkPathTab.RECOMMENDED) }
    val activity = LocalActivity.current
    val locationSource = remember {
        activity?.let { FusedLocationSource(it, 1000) }
            ?: throw IllegalStateException("Activity not found for FusedLocationSource")
    }
    val context = LocalContext.current
    val trailMapView = remember { MapView(context) }
    val trailLifecycleHelper = remember { TrailMapViewLifecycleHelper(trailMapView) }

    // --- 타이머 로직 (녹화 중일 때 시간 증가) ---
    LaunchedEffect(key1 = isRecording, key2 = isPaused) {
        if (isRecording && !isPaused) {
            while (true) {
                delay(1000)
                recordingTime++
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
            isSheetOpen = true
        }
    }

    // --- Navigation Logic (React의 조건부 렌더링) 화면 전환 로직 ---
    when {
        showCreatePage -> {
            TrailCreateScreen(
                onBack = { showCreatePage = false },
                onSave = { pathData ->
                    println("New path created: $pathData")
                    showCreatePage = false
                }
            )
        }

        selectedPath != null -> {
            TrailDetailScreen(
                path = selectedPath!!,
                onBack = { selectedPath = null },
                onStartFollowing = { path ->
                    isFollowingPath = true
                    isRecording = true
                    selectedPath = null
                    isSheetOpen = false // 기록 시작 시 시트 닫기
                    println("Following path: ${path.name}")
                }
            )
        }
        else -> {
        // --- Main Screen Content ---
            // --- 메인화면 (Trail 지도 및 컨트롤 UI) ---
            Scaffold { _ ->
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
                        update = { trailMapView ->
                            trailLifecycleHelper.onResume() }
                    )
                    // ✅ 마커 표시
                    if (!isRecording) {
                        mockRecommendedPaths.forEach { path ->
                            val hBias = (path.mapPosition.left * 2) - 1f
                            val vBias = (path.mapPosition.top * 2) - 1f

                            PathMarker(
                                path = path,
                                modifier = Modifier.align(BiasAlignment(hBias, vBias)),
                                onClick = { selectedPath = path }
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
                            activeTab = activeTab,
                            onSheetOpenToggle = { isSheetOpen = !isSheetOpen },
                            onStartRecording = {
                                isRecording = true
                                isSheetOpen = false
                                isFollowingPath = false
                                recordingTime = 0
                            },
                            onTabChange = { activeTab = it },
                            onPathClick = { selectedPath = it },
                            onFollowClick = { path ->
                                isFollowingPath = true
                                isRecording = true
                                isSheetOpen = false
                                println("Following path: ${path.name}")
                            },
                            onRegisterClick = {
                                showCreatePage = true
                                isSheetOpen = false
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
                        ReopenSheetButton(onClick = { isSheetOpen = true })
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
                            onPauseToggle = { isPaused = !isPaused },
                            onStopRecording = {
                                isRecording = false
                                isPaused = false
                                recordingTime = 0
                                isFollowingPath = false
                                showCreatePage = true
                            }
                        )
                    }
                }
            }
        }
    }
}


@Composable
fun PathMarker(
    path: UserPath,
    modifier: Modifier = Modifier,
    onClick: () -> Unit
) {
    // React의 animate-pulse, animate-ping 효과
    val infiniteTransition = rememberInfiniteTransition(label = "marker_pulse")
    val pulseAlpha by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 1f,
        animationSpec = infiniteRepeatable(
            animation = tween(1500),
        ), label = "pulse_alpha"
    )
    val pulseScale by infiniteTransition.animateFloat(
        initialValue = 1f,
        targetValue = 2.5f,
        animationSpec = infiniteRepeatable(
            animation = tween(1500),
        ), label = "pulse_scale"
    )

    Box(
        modifier = modifier.clickable(onClick = onClick),
        contentAlignment = Alignment.Center
    ) {
        // Ripple (animate-ping)
        Box(
            modifier = Modifier
                .size(32.dp)
                .graphicsLayer {
                    scaleX = pulseScale
                    scaleY = pulseScale
                    alpha = 1f - pulseAlpha
                }
                .background(PrimaryGreenLight.copy(alpha = 0.5f), CircleShape)
        )

        // Marker Dot (animate-pulse)
        Box(
            modifier = Modifier
                .size(32.dp)
                .graphicsLayer {
                    alpha = if (pulseAlpha > 0.5f) 1f else 0.7f
                }
                .background(PrimaryGreenLight, CircleShape)
                .padding(8.dp) // 흰색 내부 원을 위한 패딩
                .background(Color.White, CircleShape),
            contentAlignment = Alignment.Center
        ) {
            // React 코드의 Tooltip은 모바일에서는 Click/LongClick으로 변경해야 함
            // 여기서는 마커 자체만 구현
        }
    }
}

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