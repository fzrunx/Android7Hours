package com.sesac.monitor.presentation.ui

import android.Manifest
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.rememberMultiplePermissionsState
import com.sesac.common.ui.theme.paddingLarge
import com.sesac.common.ui.theme.paddingMedium
import com.sesac.domain.model.Pet
import com.sesac.common.ui_state.MonitorUiState
import com.sesac.monitor.presentation.MonitorViewModel

@OptIn(ExperimentalPermissionsApi::class)
@Composable
fun MonitorCamScreen(viewModel: MonitorViewModel = hiltViewModel()) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

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
            is MonitorUiState.OwnerScreen -> {
                PetSelectionContent(
                    pets = state.pets,
                    onPetSelect = { pet -> viewModel.startCall(pet) }
                )
            }
            is MonitorUiState.PetScreen -> {
                PetStreamingReadyContent(onStartClick = { viewModel.prepareStreaming() })
            }
            is MonitorUiState.Calling -> {
                CallingContent(petName = state.pet.name, onCancel = { viewModel.endCall() })
            }
            is MonitorUiState.Viewing -> {
                // TODO: WebRTC 영상 뷰 구현
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    Text(text = "${state.pet.name} 모니터링 중...")
                    Button(onClick = { viewModel.endCall() }, modifier = Modifier.align(Alignment.BottomCenter)) {
                        Text("종료")
                    }
                }
            }
            is MonitorUiState.Streaming -> {
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    Text(text = "스트리밍 중입니다...")
                }
            }
            is MonitorUiState.Error -> {
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    Text(text = state.message)
                }
            }
        }
    }
}

@Composable
fun PetSelectionContent(pets: List<Pet>, onPetSelect: (Pet) -> Unit) {
    Column(
        modifier = Modifier.fillMaxSize().padding(paddingLarge),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text("모니터링할 펫을 선택하세요", style = MaterialTheme.typography.titleLarge)
        Spacer(modifier = Modifier.height(paddingLarge))
        if (pets.isEmpty()) {
            Text("모니터링 가능한 펫이 없습니다.")
        } else {
            LazyColumn(verticalArrangement = Arrangement.spacedBy(paddingMedium)) {
                items(pets) { pet ->
                    Card(
                        modifier = Modifier.fillMaxWidth().clickable { onPetSelect(pet) },
                        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
                    ) {
                        Row(
                            modifier = Modifier.padding(paddingLarge),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(Icons.Default.Pets, contentDescription = null, modifier = Modifier.size(40.dp))
                            Spacer(modifier = Modifier.width(paddingMedium))
                            Text(pet.name, style = MaterialTheme.typography.bodyLarge)
                        }
                    }
                }
            }
        }
    }
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

