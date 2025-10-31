package com.sesac.trail.presentation.ui

import androidx.compose.ui.graphics.Color
import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import androidx.compose.runtime.Composable
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.sesac.common.R // ⚠️ 본인의 R 패키지 경로로 수정하세요.
import com.sesac.trail.presentation.component.AppTheme
import kotlinx.coroutines.launch
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.ui.tooling.preview.Preview
import com.sesac.trail.presentation.component.BottomSheetContent
import com.sesac.trail.presentation.component.ModeFilterButtons
import com.sesac.trail.presentation.component.RecordingControls
import com.sesac.trail.presentation.component.ReopenSheetButton
import com.sesac.trail.presentation.component.TrailControlBarViewModel
import com.sesac.trail.presentation.component.WalkPathAppBar


// --- 데이터 모델 ---
data class RecommendedPath(
    val id: Int,
    val name: String,
    val distance: String,
    val time: String,
    val rating: Double,
    val reviews: Int,
    val color: Color
)

data class UserPath(
    val id: Int,
    val name: String,
    val uploader: String,
    val distance: String,
    val time: String,
    val likes: Int,
    val distanceFromMe: String,
    val color: Color
)

// --- 임시 데이터 소스 ---
object DataSource {
    val recommendedPaths = listOf(
        RecommendedPath(1, "서울숲 산책로 1", "2.5km", "중급 30분", 4.9, 130, AppTheme.colorGreen),
        RecommendedPath(2, "서울숲 산책로 2", "1.8km", "초급 20분", 4.7, 89, AppTheme.colorGreen),
        RecommendedPath(3, "한강공원 산책로", "3.2km", "중급 40분", 4.8, 215, AppTheme.colorBlue)
    )

    val userPaths = listOf(
        UserPath(1, "강남역 주변 산책로", "산책왕123", "1.5km", "초급 15분", 45, "0.3km", AppTheme.colorPurple),
        UserPath(2, "압구정 한강 야경 코스", "멍멍이사랑", "2.8km", "중급 35분", 89, "0.8km", AppTheme.colorBlue),
        UserPath(3, "청담동 카페거리 산책", "댕댕이집사", "2.0km", "초급 25분", 67, "1.2km", AppTheme.colorPink),
        UserPath(4, "선릉역 공원 코스", "산책매니아", "1.2km", "초급 15분", 34, "1.5km", AppTheme.colorOrange)
    )
}

// 1. 상태 정의
enum class WalkMode { RECOMMENDED, FOLLOW, RECORD, NONE }

data class WalkPathUiState(
    val activeMode: WalkMode = WalkMode.RECOMMENDED,
    val isRecording: Boolean = false,
    val recordingTime: Long = 0,
    val recommendedPaths: List<RecommendedPath> = DataSource.recommendedPaths,
    val userPaths: List<UserPath> = DataSource.userPaths,
    val isFollowingPath: Boolean = false
)

// 2. ViewModel
class WalkPathViewModel : ViewModel() {

    private val _uiState = MutableStateFlow(WalkPathUiState())
    val uiState = _uiState.asStateFlow()

    // React의 모드 변경 로직
    fun onModeChange(newMode: WalkMode) {
        _uiState.update { currentState ->
            val nextMode = if (currentState.activeMode == newMode) {
                WalkMode.NONE // 같은 버튼을 다시 누르면 모드 해제
            } else {
                newMode
            }

            // 모드 변경 시 녹화 중이면 중지
            if (nextMode != WalkMode.RECORD && currentState.isRecording) {
                currentState.copy(
                    activeMode = nextMode,
                    isRecording = false,
                    recordingTime = 0
                )
            } else {
                currentState.copy(activeMode = nextMode)
            }
        }
    }

    // React의 녹화 토글 로직
    fun onToggleRecording() {
        _uiState.update {
            if (it.isRecording) {
                // TODO: 타이머 중지 로직
                it.copy(isRecording = false, recordingTime = 0)
            } else {
                // TODO: 타이머 시작 로직
                it.copy(isRecording = true)
            }
        }
    }

    // '따라가기' 시작
    fun startFollowingPath() {
        _uiState.update {
            it.copy(
                isFollowingPath = true,
                activeMode = WalkMode.FOLLOW
            )
        }
    }
}
@Composable
fun WalkPathScreen(
    onNavigateToHome: () -> Unit,
//    viewModel: WalkPathViewModel = viewModel()
) {
//    val navController = rememberNavController()

//    NavHost(navController = navController, startDestination = "main_map") {
//
//        // 메인 맵 화면
//        composable("main_map") {
//            WalkPathMainScreen(
//                viewModel = viewModel,
//                onNavigateToHome = onNavigateToHome,
//                onNavigateToDetail = { pathId ->
//                    navController.navigate("path_detail/$pathId")
//                }
//            )
//        }
//
//        // 경로 상세 화면
//        composable(
//            route = "path_detail/{pathId}",
//            arguments = listOf(navArgument("pathId") { type = NavType.IntType })
//        ) { backStackEntry ->
//            val pathId = backStackEntry.arguments?.getInt("pathId")
//            val path = DataSource.userPaths.find { it.id == pathId } // 임시로 DataSource에서 찾기
//
//            if (path != null) {
//                WalkPathDetailPage(
//                    path = path,
//                    onBack = { navController.popBackStack() },
//                    onStartFollowing = {
//                        viewModel.startFollowingPath()
//                        navController.popBackStack() // 메인 맵으로 복귀
//                    }
//                )
//            } else {
//                // 경로를 찾을 수 없는 경우
//                navController.popBackStack()
//            }
//        }
//    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun WalkPathMainScreen(
    viewModel: WalkPathViewModel,
    onNavigateToHome: () -> Unit,
    onNavigateToDetail: (Int) -> Unit
) {
    val uiState by viewModel.uiState.collectAsState()
    val scope = rememberCoroutineScope()

    // 바텀시트 상태 관리
    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)
    val showBottomSheet = uiState.activeMode == WalkMode.RECOMMENDED ||
            uiState.activeMode == WalkMode.FOLLOW

    // 모드가 변경되어 시트를 보여줘야 할 때, 시트를 확장
    LaunchedEffect(showBottomSheet) {
        if (showBottomSheet) {
            scope.launch { sheetState.expand() }
        } else {
            scope.launch { sheetState.hide() }
        }
    }

    Scaffold(
        topBar = {
            WalkPathAppBar(
                title = "산책로",
                onNavigateToHome = onNavigateToHome
            )
        }
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .padding(paddingValues)
                .fillMaxSize()
        ) {
            // 1. 지도 이미지 (배경)
            // ⚠️ 'map_image'를 res/drawable에 추가한 리소스 이름으로 변경하세요.
//            Image(
//                painter = painterResource(id = R.drawable.map_image),
//                contentDescription = "Map",
//                modifier = Modifier.fillMaxSize(),
//                contentScale = ContentScale.Crop
//            )

            // 2. 모드 필터 버튼
            ModeFilterButtons(
                activeMode = uiState.activeMode,
                onModeChange = viewModel::onModeChange,
                modifier = Modifier
                    .align(Alignment.TopCenter)
                    .padding(top = AppTheme.paddingLarge)
            )

            // 3. 기록 모드 UI
            if (uiState.activeMode == WalkMode.RECORD) {
                RecordingControls(
                    isRecording = uiState.isRecording,
                    recordingTime = uiState.recordingTime,
                    onToggleRecording = viewModel::onToggleRecording,
                    modifier = Modifier.align(Alignment.BottomCenter)
                )
            }

            // 4. 시트 닫혔을 때 '다시 열기' 버튼
            if (showBottomSheet && !sheetState.isVisible) {
                ReopenSheetButton(
                    mode = uiState.activeMode,
                    onClick = { scope.launch { sheetState.expand() } },
                    modifier = Modifier
                        .align(Alignment.BottomCenter)
                        .padding(bottom = 32.dp)
                )
            }

            // 5. 바텀 시트
            if (showBottomSheet) {
                ModalBottomSheet(
                    onDismissRequest = { viewModel.onModeChange(WalkMode.NONE) },
                    sheetState = sheetState,
                    containerColor = AppTheme.surface,
                    dragHandle = {
                        BottomSheetDefaults.DragHandle(color = AppTheme.sheetHandle)
                    }
                ) {
                    BottomSheetContent(
                        mode = uiState.activeMode,
                        recommendedPaths = uiState.recommendedPaths,
                        userPaths = uiState.userPaths,
                        onPathClick = { userPath ->
                            scope.launch { sheetState.hide() }.invokeOnCompletion {
                                onNavigateToDetail(userPath.id)
                            }
                        }
                    )
                }
            }
        }
    }
}




// React의 WalkPathDetailPage 자리 표시자
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun WalkPathDetailPage(
    path: UserPath,
    onBack: () -> Unit,
    onStartFollowing: (UserPath) -> Unit
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(path.name) },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, "뒤로가기")
                    }
                }
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .padding(padding)
                .fillMaxSize()
                .padding(AppTheme.paddingLarge),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Text("경로 상세 페이지", style = MaterialTheme.typography.titleLarge)
            Spacer(modifier = Modifier.height(AppTheme.paddingLarge))
            Text("경로: ${path.name}")
            Text("업로더: ${path.uploader}")
            Text("거리: ${path.distance}")
            Spacer(modifier = Modifier.height(32.dp))
            Button(onClick = { onStartFollowing(path) }) {
                Text("이 경로 따라가기")
            }
        }
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun WalkPathMainScreenPreview() {
    // 더미 ViewModel 생성
    val dummyViewModel = remember { WalkPathViewModel() }

    WalkPathMainScreen(
        viewModel = dummyViewModel,
        onNavigateToHome = {},
        onNavigateToDetail = { /* Preview에서는 아무 동작 안함 */ }
    )
}