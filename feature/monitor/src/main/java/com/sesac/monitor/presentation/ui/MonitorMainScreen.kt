package com.sesac.monitor.presentation.ui

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Navigation
import androidx.compose.material.icons.twotone.Videocam
import androidx.compose.material3.CircularProgressIndicator // NEW
import androidx.compose.material3.DropdownMenuItem // NEW
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox // NEW
import androidx.compose.material3.ExposedDropdownMenuDefaults // NEW
import androidx.compose.material3.OutlinedTextField // NEW
import androidx.compose.material3.Text // NEW
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf // NEW
import androidx.compose.runtime.remember // NEW
import androidx.compose.runtime.setValue // NEW
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import com.naver.maps.map.NaverMap
import com.sesac.common.component.CommonFilterTabs
import com.sesac.common.component.CommonMapLifecycle
import com.sesac.common.ui.theme.paddingMedium
import com.sesac.monitor.presentation.MonitorViewModel
import com.sesac.common.R as cR
import com.sesac.domain.model.Pet // NEW
import com.sesac.domain.result.ResponseUiState // NEW

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MonitorMainScreen (
    modifier: Modifier = Modifier,
    navController: NavController,
    viewModel: MonitorViewModel = hiltViewModel(),
    commonMapLifecycle: CommonMapLifecycle,
    onMapReady: ((NaverMap) -> Unit)? = null,
    content: @Composable () -> Unit = {},
    ) {
    val webCam = stringResource(cR.string.monitor_button_webcam)
    val GPS = stringResource(cR.string.monitor_button_GPS)
    val activeTab by viewModel.activeTab.collectAsStateWithLifecycle()
    val monitorablePetsState by viewModel.monitorablePets.collectAsStateWithLifecycle() // NEW

    var selectedPet by remember { mutableStateOf<Pet?>(null) } // NEW
    var isPetDropdownExpanded by remember { mutableStateOf(false) } // NEW

    LaunchedEffect(Unit) { // Initial data fetch
        viewModel.getMonitorablePets()
    }

    LaunchedEffect(activeTab) {
        if (activeTab.isBlank()) {
            viewModel.selectTab(webCam)
        }
    }

    // ⭐ 여기서 공통 정의 (중복 제거 핵심)
    val filterOptions = listOf(webCam, GPS)
    val filterIcons = listOf(Icons.TwoTone.Videocam, Icons.Default.Navigation)
    val tabBar: @Composable (Modifier) -> Unit = { modifierForPosition ->
        CommonFilterTabs(
            modifier = modifierForPosition.padding(horizontal = paddingMedium),
            filterOptions = filterOptions,
            selectedFilter = activeTab,
            onFilterSelected = viewModel::selectTab,
            fiterIcons = filterIcons,
        )
    }

    when (activeTab) {
        GPS -> {
            Box(modifier = Modifier.fillMaxSize()) {
                // NEW: Pet selection dropdown
                Column(
                    modifier = Modifier
                        .align(Alignment.TopCenter)
                        .fillMaxWidth()
                        .padding(horizontal = paddingMedium, vertical = paddingMedium)
                ) {
                    when (val state = monitorablePetsState) {
                        is ResponseUiState.Loading -> CircularProgressIndicator()
                        is ResponseUiState.Success -> {
                            if (state.result.isNotEmpty()) {
                                ExposedDropdownMenuBox(
                                    expanded = isPetDropdownExpanded,
                                    onExpandedChange = { isPetDropdownExpanded = !isPetDropdownExpanded },
                                    modifier = Modifier.fillMaxWidth()
                                ) {
                                    OutlinedTextField(
                                        value = selectedPet?.name ?: "모니터링할 펫 선택",
                                        onValueChange = { },
                                        readOnly = true,
                                        trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = isPetDropdownExpanded) },
                                        modifier = Modifier.menuAnchor().fillMaxWidth()
                                    )
                                    ExposedDropdownMenu(
                                        expanded = isPetDropdownExpanded,
                                        onDismissRequest = { isPetDropdownExpanded = false }
                                    ) {
                                        state.result.forEach { pet ->
                                            DropdownMenuItem(
                                                text = { Text(pet.name) },
                                                onClick = {
                                                    selectedPet = pet
                                                    isPetDropdownExpanded = false
                                                }
                                            )
                                        }
                                    }
                                }
                            } else {
                                Text("모니터링 가능한 펫이 없습니다.", modifier = Modifier.padding(vertical = paddingMedium))
                            }
                        }
                        is ResponseUiState.Error -> Text("펫 목록 로드 실패: ${state.message}")
                        else -> Text("펫 목록을 불러오는 중...")
                    }

                    // Original Tab Bar
                    tabBar(Modifier)
                }


                // Only show MonitorGpsScreen if a pet is selected
                selectedPet?.let { pet ->
                    MonitorGpsScreen(
                        petId = pet.id, // 전달받은 petId를 넘겨줌
                        commonMapLifecycle = commonMapLifecycle
                    )
                }
            }
        }

        else -> {
            Column(modifier = Modifier.fillMaxSize()) {

                // ⭐ CAM은 일반 Column 상단에 배치
                tabBar(Modifier)

                MonitorCamScreen()
            }
        }
    }
}