package com.sesac.monitor.presentation

import androidx.compose.animation.animateColorAsState
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.naver.maps.map.NaverMap
import com.sesac.common.R

@Composable
fun MonitorCamScreen(
    onNavigateToHome: () -> Unit,
                     modifier: Modifier = Modifier,
                     lifecycleHelper: MapViewLifecycleHelper, // MapView 생명주기 관리
                     onMapReady: ((NaverMap) -> Unit)? = null,
                     ) {
    var activeTab by remember { mutableStateOf("영상") }
    var isRecording by remember { mutableStateOf(false) }
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
    ) {
        /** :흰색_확인_표시: Header */
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(56.dp)
                .background(Color(0xFFDBE8CC))
                .padding(horizontal = 16.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            IconButton(onClick = onNavigateToHome) {
                Image(
                    painter = painterResource(id = R.drawable.image7hours), // logo.png 추가 필요
                    contentDescription = "7Hours Home",
                    modifier = Modifier.size(40.dp)
                )
            }
            Text(
                text = "모니터링",
                color = Color(0xFF1F2937),
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold
            )
            Spacer(modifier = Modifier.width(40.dp))
        }
        /** TabBar */
        MonitorCommonTabBar(
            activeTab = activeTab,
            onTabSelected = { activeTab = it }
        )
        /** :영상 부분*/
        if (activeTab == "영상") {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f)
                    .padding(horizontal = 16.dp, vertical = 8.dp)
                    .clip(RoundedCornerShape(24.dp))
                    .background(Color(0xFFE5E7EB)),
                contentAlignment = Alignment.Center
            ) {
                Text("영상 화면", color = Color.Gray, fontSize = 20.sp)
            }
            // Control Buttons (영상 탭일 때만)
            CamControlButtons(isRecording) { isRecording = it }
        } else if (activeTab == "GPS") {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f)
                    .padding(start = 16.dp, top = 8.dp, end = 16.dp, bottom = 88.dp)
                    .clip(RoundedCornerShape(24.dp)),
                contentAlignment = Alignment.Center
            ) {
                MonitorGpsScreen(
                    modifier = Modifier.fillMaxSize(), // Box 안에서 전체 차지
                    lifecycleHelper = lifecycleHelper,
                    onMapReady = onMapReady
                )
            }
        }
    }
}


@Composable
fun CamControlButtons(isRecording: Boolean, onRecordToggle: (Boolean) -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(bottom = 48.dp)
            .background(Color.White),
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically
    ) {
        IconButton(
            onClick = { /* TODO: Play video */ },
            modifier = Modifier
                .size(80.dp)
                .clip(CircleShape)
                .background(Color(0xFF86EFAC))
                .padding(10.dp)
        ) {
            Icon(
                imageVector = Icons.Default.PlayArrow,
                contentDescription = "Play",
                tint = Color(0xFF1F2937),
                modifier = Modifier.size(48.dp)
            )
        }
        Spacer(modifier = Modifier.width(48.dp))
        val recordColor by animateColorAsState(
            if (isRecording) Color(0xFFEF4444) else Color(0xFFFCA5A5)
        )
        IconButton(
            onClick = { onRecordToggle(!isRecording) },
            modifier = Modifier
                .size(80.dp)
                .clip(CircleShape)
                .background(recordColor)
                .padding(10.dp)
        ) {
            Icon(
                imageVector = Icons.Default.Circle,
                contentDescription = "Record",
                tint = Color(0xFF1F2937),
                modifier = Modifier.size(48.dp)
            )
        }
    }
}


//@Preview(showBackground = true, showSystemUi = true)
//@Composable
//fun MonitorCamScreenPreview() {
//    MonitorCamScreen (onNavigateToHome = {})
//}
