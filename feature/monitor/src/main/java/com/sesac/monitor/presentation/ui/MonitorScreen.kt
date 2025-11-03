package com.sesac.monitor.presentation.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import com.naver.maps.map.NaverMap
import com.sesac.common.navigation.CommonHeader
import com.sesac.monitor.presentation.MapViewLifecycleHelper
import com.sesac.monitor.presentation.component.MonitorCommonTabBar

@Composable
fun MonitorScreen (
    modifier: Modifier = Modifier,
    lifecycleHelper: MapViewLifecycleHelper, // 라이프 사이클 따로 관리하려고 만듬
    onNavigateToHome: () -> Unit,
    onMapReady: ((NaverMap) -> Unit)? = null
    ) {
    var activeTab by remember { mutableStateOf("영상") }
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
    ) {
        /** :흰색_확인_표시: Header */
        CommonHeader(
            title = "모니터링",
            onNavigateToHome = { /* 홈 이동 */ }
        )
        /** TabBar */
        MonitorCommonTabBar(
            activeTab = activeTab,
            onTabSelected = { activeTab = it }
        )
        when (activeTab) {
            "영상" -> MonitorCamScreen()
            "GPS" -> MonitorGpsScreen(
                lifecycleHelper = lifecycleHelper,
                onMapReady = onMapReady
            )
        }
    }
}