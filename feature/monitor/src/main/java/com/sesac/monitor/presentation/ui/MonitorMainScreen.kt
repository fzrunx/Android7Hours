package com.sesac.monitor.presentation.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Navigation
import androidx.compose.material.icons.twotone.Videocam
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.navigation.NavController
import com.naver.maps.map.NaverMap
import com.sesac.common.component.CommonFilterTabs
import com.sesac.common.ui.theme.paddingMedium
import com.sesac.monitor.utils.MapViewLifecycleHelper
import com.sesac.common.R as cR

@Composable
fun MonitorMainScreen (
    modifier: Modifier = Modifier,
    navController: NavController,
    lifecycleHelper: MapViewLifecycleHelper, // 라이프 사이클 따로 관리하려고 만듬
    onMapReady: ((NaverMap) -> Unit)? = null,
    content: @Composable () -> Unit = {},
    ) {
    var activeTab by remember { mutableStateOf("영상") }
    val webCam = stringResource(cR.string.monitor_button_webcam)
    val GPS = stringResource(cR.string.monitor_button_GPS)

    Column(
        modifier = Modifier
            .fillMaxSize()
//            .background(Color.White)
    ) {
        CommonFilterTabs(
            filterOptions = listOf(stringResource(cR.string.monitor_button_webcam), stringResource(cR.string.monitor_button_GPS)),
            modifier = Modifier.padding(horizontal = paddingMedium),
            selectedFilter = activeTab,
            fiterIcons = listOf(Icons.TwoTone.Videocam, Icons.Default.Navigation),
            horizontalArrangement = Arrangement.Center,
            onFilterSelected = { activeTab = it },
        )

        when(activeTab) {
            webCam -> MonitorCamScreen()
            GPS -> MonitorGpsScreen(
                lifecycleHelper = lifecycleHelper,
                onMapReady = null,
            )
        }
    }
}