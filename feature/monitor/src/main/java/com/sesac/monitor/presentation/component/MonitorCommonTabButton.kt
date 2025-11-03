package com.sesac.monitor.presentation.component


import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Navigation
import androidx.compose.material.icons.twotone.Videocam
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.sesac.common.component.CommonFilterTabs
import com.sesac.common.ui.theme.paddingSmall

@Composable
fun MonitorCommonTabBar(
    activeTab: String,
    onTabSelected: (String) -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(Color.White)
            .padding(horizontal = 16.dp, vertical = 12.dp),
        horizontalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        CommonFilterTabs(
            modifier = Modifier.padding(horizontal = paddingSmall),
            horizontalArrangement = Arrangement.Center,
            filterOptions = listOf("영상", "GPS"),
            selectedFilter = activeTab,
            onFilterSelected = { selected ->
                onTabSelected(selected)
            },
            fiterIcons = listOf(Icons.TwoTone.Videocam, Icons.Default.Navigation),
        )
    }
}