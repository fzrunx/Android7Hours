package com.sesac.monitor.presentation.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Navigation
import androidx.compose.material.icons.twotone.Videocam
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import com.naver.maps.map.NaverMap
import com.sesac.common.component.CommonFilterTabs
import com.sesac.common.ui.theme.paddingMedium
import com.sesac.monitor.presentation.MonitorMapViewLifecycleHelper
import com.sesac.monitor.presentation.MonitorViewModel
import com.sesac.common.R as cR

@Composable
fun MonitorMainScreen (
    modifier: Modifier = Modifier,
    navController: NavController,
    viewModel: MonitorViewModel = hiltViewModel(),
    monitorLifecycleHelper: MonitorMapViewLifecycleHelper, // 라이프 사이클 따로 관리하려고 만듬
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

    Column(
        modifier = Modifier
            .fillMaxSize()
    ) {
        CommonFilterTabs(
            filterOptions = listOf(stringResource(cR.string.monitor_button_webcam), stringResource(cR.string.monitor_button_GPS)),
            modifier = Modifier.padding(horizontal = paddingMedium),
            selectedFilter = activeTab,
            fiterIcons = listOf(Icons.TwoTone.Videocam, Icons.Default.Navigation),
            horizontalArrangement = Arrangement.Center,
            onFilterSelected = { viewModel.selecteTab(it) },
        )

        when(activeTab) {
            webCam -> MonitorCamScreen()
            GPS -> MonitorGpsScreen(
                monitorLifecycleHelper = monitorLifecycleHelper,
                onMapReady = null,
            )
        }
    }
}