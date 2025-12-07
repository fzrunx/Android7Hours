package com.sesac.monitor.presentation.ui

import android.Manifest
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.viewinterop.AndroidView
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.rememberMultiplePermissionsState
import com.sesac.common.ui.theme.paddingLarge
import com.sesac.common.ui.theme.paddingMedium
import com.sesac.common.ui_state.MonitorUiState
import com.sesac.monitor.presentation.MonitorViewModel
import org.webrtc.EglBase
import org.webrtc.RendererCommon
import org.webrtc.SurfaceViewRenderer
import org.webrtc.VideoTrack

@OptIn(ExperimentalPermissionsApi::class)
@Composable
fun MonitorCamScreen(viewModel: MonitorViewModel = hiltViewModel()) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val remoteVideoTrack by viewModel.remoteVideoTrack.collectAsStateWithLifecycle()
    val localVideoTrack by viewModel.localVideoTrack.collectAsStateWithLifecycle()
    val eglBase = viewModel.getEglBase()

    // WebRTC에 필요한 카메라 및 오디오 권한 요청
    val permissionsState = rememberMultiplePermissionsState(
        permissions = listOf(Manifest.permission.CAMERA, Manifest.permission.RECORD_AUDIO)
    )

    LaunchedEffect(Unit) {
        if (!permissionsState.allPermissionsGranted) {
            permissionsState.launchMultiplePermissionRequest()
        }
    }

    if (!permissionsState.allPermissionsGranted) {
        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            Text("카메라와 마이크 권한이 필요합니다.", textAlign = TextAlign.Center)
        }
        return
    }

    Surface(modifier = Modifier.fillMaxSize()) {
        when (val state = uiState) {
            is MonitorUiState.Loading -> {
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    CircularProgressIndicator()
                }
            }
            is MonitorUiState.PetScreen -> {
                PetStreamingReadyContent(onStartClick = { viewModel.prepareStreaming() })
            }
            is MonitorUiState.Calling -> {
                CallingContent(petName = state.pet.name, onCancel = { viewModel.endCall() })
            }
            is MonitorUiState.Viewing -> {
                Box(modifier = Modifier.fillMaxSize()) {
                    VideoView(
                        videoTrack = remoteVideoTrack,
                        eglBase = eglBase,
                        modifier = Modifier.fillMaxSize()
                    )
                    Button(
                        onClick = { viewModel.endCall() },
                        modifier = Modifier
                            .align(Alignment.BottomCenter)
                            .padding(paddingLarge)
                    ) {
                        Text("종료")
                    }
                }
            }
            is MonitorUiState.Streaming -> {
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    // 로컬 카메라 미리보기 화면
                    VideoView(
                        videoTrack = localVideoTrack,
                        eglBase = eglBase,
                        modifier = Modifier.fillMaxSize(),
                        isMirror = true // 로컬 카메라는 좌우 반전
                    )
                }
            }
            is MonitorUiState.Error -> {
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    Text(text = state.message)
                }
            }
            // OwnerScreen is handled by MonitorMainScreen now
            is MonitorUiState.OwnerScreen -> {
                // This case is now handled before navigating to the dashboard.
                // You can show a loading indicator or a placeholder.
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    CircularProgressIndicator()
                }
            }
        }
    }
}

@Composable
fun VideoView(
    videoTrack: VideoTrack?,
    eglBase: EglBase?,
    modifier: Modifier = Modifier,
    isMirror: Boolean = false // 좌우 반전 여부 파라미터 추가
) {
    val eglContext = eglBase?.eglBaseContext
    val context = LocalContext.current
    // remember를 사용하여 SurfaceViewRenderer 인스턴스를 리컴포지션 간에도 유지
    val surfaceViewRenderer = remember { SurfaceViewRenderer(context) }

    // videoTrack sink의 라이프사이클을 DisposableEffect로 관리
    DisposableEffect(surfaceViewRenderer, videoTrack, eglContext, isMirror) {
        surfaceViewRenderer.init(eglContext, null)
        surfaceViewRenderer.setEnableHardwareScaler(true)
        surfaceViewRenderer.setMirror(isMirror)
        // 영상의 종횡비를 유지하면서 화면에 맞도록 스케일링 설정
        surfaceViewRenderer.setScalingType(RendererCommon.ScalingType.SCALE_ASPECT_FIT)
        // 뷰를 다른 UI 요소 위에 올리기 위해 Z-order 설정
        surfaceViewRenderer.setZOrderMediaOverlay(true)


        videoTrack?.addSink(surfaceViewRenderer)

        onDispose {
            videoTrack?.removeSink(surfaceViewRenderer)
            surfaceViewRenderer.release() // Renderer 리소스 해제
        }
    }

    AndroidView(
        factory = {
            // factory는 기억된 인스턴스를 반환하기만 하면 됩니다.
            surfaceViewRenderer
        },
        // update 람다는 DisposableEffect가 sink 관리를 처리하므로 필요 없음
        modifier = modifier
    )
}

@Composable
fun PetStreamingReadyContent(onStartClick: () -> Unit) {
    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text("모니터링 대기", style = MaterialTheme.typography.headlineMedium)
        Spacer(modifier = Modifier.height(paddingMedium))
        Text("보호자가 모니터링을 시작할 수 있도록 준비합니다.", textAlign = TextAlign.Center)
        Spacer(modifier = Modifier.height(paddingLarge))
        Button(onClick = onStartClick) {
            Text("준비 완료")
        }
    }
}

@Composable
fun CallingContent(petName: String, onCancel: () -> Unit) {
    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text("${petName}에게 연결 중...", style = MaterialTheme.typography.headlineSmall)
        Spacer(modifier = Modifier.height(paddingLarge))
        CircularProgressIndicator()
        Spacer(modifier = Modifier.height(paddingLarge))
        Button(onClick = onCancel, colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.error)) {
            Text("취소")
        }
    }
}
