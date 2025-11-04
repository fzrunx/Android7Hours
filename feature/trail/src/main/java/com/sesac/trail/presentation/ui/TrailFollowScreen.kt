package com.sesac.trail.presentation.ui

import androidx.compose.ui.graphics.Color
import androidx.compose.runtime.Composable
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.launch
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.ui.tooling.preview.Preview
import com.sesac.common.component.CommonFilterTabs
import com.sesac.common.ui.theme.Android7HoursTheme
import com.sesac.common.ui.theme.SheetHandle
import com.sesac.common.ui.theme.paddingLarge
import com.sesac.trail.presentation.TrailViewModel
import com.sesac.trail.presentation.component.BottomSheetContent
import com.sesac.trail.presentation.component.RecordingControls
import com.sesac.trail.presentation.component.ReopenSheetButton
import com.sesac.trail.presentation.model.RecommendedPath
import com.sesac.trail.presentation.model.UserPath

// --- 임시 데이터 소스 ---
object DataSource {
    val recommendedPaths = listOf(
        RecommendedPath(1, "서울숲 산책로 1", "2.5km", "중급 30분", 4.9, 130, Color.Green),
        RecommendedPath(2, "서울숲 산책로 2", "1.8km", "초급 20분", 4.7, 89, Color.Green),
        RecommendedPath(3, "한강공원 산책로", "3.2km", "중급 40분", 4.8, 215, Color.Blue)
    )

    val userPaths = listOf(
        UserPath(1, "강남역 주변 산책로", "산책왕123", "1.5km", "초급 15분", 45, "0.3km", Color.Cyan),
        UserPath(2, "압구정 한강 야경 코스", "멍멍이사랑", "2.8km", "중급 35분", 89, "0.8km", Color.Blue),
        UserPath(3, "청담동 카페거리 산책", "댕댕이집사", "2.0km", "초급 25분", 67, "1.2km", Color.Magenta),
        UserPath(4, "선릉역 공원 코스", "산책매니아", "1.2km", "초급 15분", 34, "1.5km", Color.Yellow)
    )
}

// 1. 상태 정의
enum class WalkMode { RECOMMENDED, FOLLOW, RECORD, NONE }

data class WalkPathUiState(
    val activeMode: WalkMode = WalkMode.NONE,
    val isRecording: Boolean = false,
    val recordingTime: Long = 0,
    val recommendedPaths: List<RecommendedPath> = DataSource.recommendedPaths,
    val userPaths: List<UserPath> = DataSource.userPaths,
    val isFollowingPath: Boolean = false
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TrailFollowScreen(
    viewModel: TrailViewModel,
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


    Box(
        modifier = Modifier
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

        val filterOptions = listOf("추천", "따라가기", "기록")
        // 2. 모드 필터 버튼
        CommonFilterTabs(
            filterOptions = filterOptions,
            selectedFilter = when(uiState.activeMode) {
                WalkMode.RECOMMENDED -> filterOptions[0]
                WalkMode.FOLLOW -> filterOptions[1]
                WalkMode.RECORD -> filterOptions[2]
                WalkMode.NONE -> null
            },
            onFilterSelected = { when(it) {
                filterOptions[0] -> viewModel.onModeChange(WalkMode.RECOMMENDED)
                filterOptions[1] -> viewModel.onModeChange(WalkMode.FOLLOW)
                filterOptions[2] -> viewModel.onModeChange(WalkMode.RECORD)
            } },
//            surfaceColor = null
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
                containerColor = MaterialTheme.colorScheme.primaryContainer,
                dragHandle = {
                    BottomSheetDefaults.DragHandle(color = SheetHandle)
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
                .padding(paddingLarge),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Text("경로 상세 페이지", style = MaterialTheme.typography.titleLarge)
            Spacer(modifier = Modifier.height(paddingLarge))
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
fun TrailFollowScreenPreview() {
    // 더미 ViewModel 생성
    val dummyViewModel = remember { TrailViewModel() }

    Android7HoursTheme {
        TrailFollowScreen(
            viewModel = dummyViewModel,
            onNavigateToHome = {},
            onNavigateToDetail = { /* Preview에서는 아무 동작 안함 */ }
        )
    }
}