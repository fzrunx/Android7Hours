package com.sesac.monitor.presentation.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Navigation
import androidx.compose.material.icons.twotone.Videocam
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
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

    LaunchedEffect(activeTab) {
        if (activeTab.isBlank()) {
            viewModel.selecteTab(webCam)
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
            onFilterSelected = viewModel::selecteTab,
            fiterIcons = filterIcons,
        )
    }

    when (activeTab) {
        GPS -> {
            Box(modifier = Modifier.fillMaxSize()) {
                MonitorGpsScreen(
                    commonMapLifecycle = commonMapLifecycle,
                    onMapReady = null,
                )

                // ⭐ GPS는 화면 위에 오버레이 → Modifier만 다르게 적용
                tabBar(
                    Modifier
                        .align(Alignment.TopCenter)
                )
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