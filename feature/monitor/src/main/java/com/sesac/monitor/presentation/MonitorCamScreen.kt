package com.sesac.monitor.presentation

//import androidx.compose.foundation.BorderStroke
//import androidx.compose.foundation.background
//import androidx.compose.foundation.border
//import androidx.compose.foundation.Image
//import androidx.compose.foundation.layout.*
//import androidx.compose.foundation.shape.RoundedCornerShape
//import androidx.compose.material3.*
//import androidx.compose.runtime.Composable
//import androidx.compose.ui.Alignment
//import androidx.compose.ui.Modifier
//import androidx.compose.ui.draw.clip
//import androidx.compose.ui.draw.shadow
//import androidx.compose.ui.geometry.Offset
//import androidx.compose.ui.graphics.Brush
//import androidx.compose.ui.graphics.Color
//import androidx.compose.ui.graphics.ColorFilter
//import androidx.compose.ui.res.painterResource
//import androidx.compose.ui.text.TextStyle
//import androidx.compose.ui.text.font.FontWeight
//import androidx.compose.ui.tooling.preview.Preview
//import androidx.compose.ui.unit.dp
//import androidx.compose.ui.unit.em
//import androidx.compose.ui.unit.sp
//import com.sesac.common.R
//
//@OptIn(ExperimentalMaterial3Api::class)
//@Composable
//fun MonitorCamScreen(modifier: Modifier = Modifier) {
//    Scaffold(
//        topBar = {
//            TopAppBar(
//                title = {
//                    Text(
//                        text = "모니터링",
//                        color = Color(0xff101828),
//                        fontSize = 20.sp,
//                        fontWeight = FontWeight.Bold,
//                        lineHeight = 1.4.em
//                    )
//                },
//                navigationIcon = {
//                    Image(
//                        painter = painterResource(id = R.drawable.image7hours),
//                        contentDescription = "Image (7Hours Home)",
//                        modifier = Modifier
//                            .padding(8.dp)
//                            .size(36.dp)
//                            .clip(RoundedCornerShape(8.dp))
//                    )
//                },
//                actions = {
//                    Spacer(modifier = Modifier.width(40.dp))
//                }
//            )
//        }
//    ) { innerPadding ->
//        Column(
//            modifier = modifier
//                .fillMaxSize()
//                .padding(innerPadding)
//                .background(Color.White),
//            verticalArrangement = Arrangement.SpaceBetween
//        ) {
//
//            // 상단 버튼 영역
//            Row(
//                horizontalArrangement = Arrangement.spacedBy(12.dp),
//                modifier = Modifier
//                    .fillMaxWidth()
//                    .padding(horizontal = 16.dp, vertical = 16.dp)
//            ) {
//                // 영상 버튼
//                Row(
//                    horizontalArrangement = Arrangement.spacedBy(8.dp),
//                    verticalAlignment = Alignment.CenterVertically,
//                    modifier = Modifier
//                        .weight(1f)
//                        .clip(RoundedCornerShape(16.dp))
//                        .background(
//                            Brush.linearGradient(
//                                0f to Color(0xff9810fa),
//                                1f to Color(0xffad46ff),
//                                start = Offset(0f, 0f),
//                                end = Offset(193f, 0f)
//                            )
//                        )
//                        .shadow(6.dp, RoundedCornerShape(16.dp))
//                        .padding(horizontal = 12.dp, vertical = 10.dp)
//                ) {
//                    Image(
//                        painter = painterResource(id = R.drawable.),
//                        contentDescription = "Icon",
//                        modifier = Modifier.size(20.dp)
//                    )
//                    Text(
//                        text = "영상",
//                        color = Color.White,
//                        fontSize = 16.sp
//                    )
//                }
//
//                // GPS 버튼
//                Row(
//                    horizontalArrangement = Arrangement.spacedBy(8.dp),
//                    verticalAlignment = Alignment.CenterVertically,
//                    modifier = Modifier
//                        .weight(1f)
//                        .clip(RoundedCornerShape(16.dp))
//                        .background(Color.White)
//                        .border(BorderStroke(2.dp, Color(0xffe5e7eb)), RoundedCornerShape(16.dp))
//                        .padding(horizontal = 12.dp, vertical = 10.dp)
//                ) {
//                    Image(
//                        painter = painterResource(id = R.drawable.gps_icon),
//                        contentDescription = "Icon",
//                        modifier = Modifier.size(20.dp)
//                    )
//                    Text(
//                        text = "GPS",
//                        color = Color(0xff4a5565),
//                        fontSize = 16.sp
//                    )
//                }
//            }
//
//            // 영상 화면 영역
//            Box(
//                modifier = Modifier
//                    .fillMaxWidth()
//                    .weight(1f)
//                    .padding(horizontal = 16.dp)
//                    .clip(RoundedCornerShape(24.dp))
//                    .background(Color(0xffe5e7eb)),
//                contentAlignment = Alignment.Center
//            ) {
//                Text(
//                    text = "영상 화면",
//                    color = Color(0xff4a5565),
//                    fontSize = 20.sp
//                )
//            }
//
//            // 하단 버튼 영역
//            Row(
//                horizontalArrangement = Arrangement.spacedBy(48.dp, Alignment.CenterHorizontally),
//                verticalAlignment = Alignment.CenterVertically,
//                modifier = Modifier
//                    .fillMaxWidth()
//                    .padding(vertical = 24.dp)
//            ) {
//                // 초록 버튼
//                Box(
//                    modifier = Modifier
//                        .size(80.dp)
//                        .clip(RoundedCornerShape(50))
//                        .background(Color(0xff7bf1a8))
//                        .shadow(6.dp, RoundedCornerShape(50)),
//                    contentAlignment = Alignment.Center
//                ) {
//                    Image(
//                        painter = painterResource(id = R.drawable.gps_icon),
//                        contentDescription = "Icon",
//                        colorFilter = ColorFilter.tint(Color(0xff1e2939)),
//                        modifier = Modifier.size(40.dp)
//                    )
//                }
//
//                // 빨강 버튼
//                Box(
//                    modifier = Modifier
//                        .size(80.dp)
//                        .clip(RoundedCornerShape(50))
//                        .background(Color(0xffffa2a2))
//                        .shadow(6.dp, RoundedCornerShape(50)),
//                    contentAlignment = Alignment.Center
//                ) {
//                    Image(
//                        painter = painterResource(id = R.drawable.gps_icon),
//                        contentDescription = "Icon",
//                        modifier = Modifier.size(40.dp)
//                    )
//                }
//            }
//        }
//    }
//}

import androidx.compose.animation.animateColorAsState
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
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
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.sesac.common.R
@Composable
fun MonitoringPage(onNavigateToHome: () -> Unit) {
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
        /** :흰색_확인_표시: Tab Buttons */
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .background(Color.White)
                .padding(horizontal = 16.dp, vertical = 12.dp),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            MonitoringTabButton(
                label = "영상",
                icon = Icons.Default.Videocam,
                selected = activeTab == "영상",
                onClick = { activeTab = "영상" }
            )
            MonitoringTabButton(
                label = "GPS",
                icon = Icons.Default.Navigation,
                selected = activeTab == "GPS",
                onClick = { activeTab = "GPS" }
            )
        }
        /** :흰색_확인_표시: Video / GPS 화면 */
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f)
                .padding(horizontal = 16.dp, vertical = 8.dp)
                .clip(RoundedCornerShape(24.dp))
                .background(Color(0xFFE5E7EB)),
            contentAlignment = Alignment.Center
        ) {
            if (activeTab == "영상") {
                Text("영상 화면", color = Color.Gray, fontSize = 20.sp)
            } else {
                Box(modifier = Modifier.fillMaxSize()) {
                    Image(
                        painter = painterResource(id = R.drawable.icons8_dog_50), // gpsMapImage 추가 필요
                        contentDescription = "GPS Map",
                        contentScale = ContentScale.Crop,
                        modifier = Modifier.fillMaxSize()
                    )
                    Box(
                        modifier = Modifier
                            .align(Alignment.TopEnd)
                            .offset(x = (-80).dp, y = 120.dp)
                            .clip(CircleShape)
                            .background(Color.White)
                            .padding(8.dp)
                            .shadow(4.dp, CircleShape)
                    ) {
                        Icon(
                            imageVector = Icons.Default.Place,
                            contentDescription = "User Location",
                            tint = Color(0xFF8B5CF6),
                            modifier = Modifier.size(36.dp)
                        )
                    }
                }
            }
        }
        /** :흰색_확인_표시: Control Buttons (only when 영상 tab) */
        if (activeTab == "영상") {
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
                    if (isRecording) Color(0xFFEF4444) else Color(0xFFFCA5A5),
                    label = "recordColorAnim"
                )
                IconButton(
                    onClick = { isRecording = !isRecording },
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
    }
}
/** :흰색_확인_표시: Custom Composable for Tabs */
@Composable
fun MonitoringTabButton(
    label: String,
    icon: ImageVector,
    selected: Boolean,
    onClick: () -> Unit
) {
    val backgroundColor by animateColorAsState(
        if (selected) Color(0xFF8B5CF6) else Color.White,
        label = "tabBgColor"
    )
    val textColor by animateColorAsState(
        if (selected) Color.White else Color(0xFF4B5563),
        label = "tabTextColor"
    )
    Box(
        modifier = Modifier

            .clip(RoundedCornerShape(16.dp))
            .background(backgroundColor)
            .clickable(onClick = onClick)
            .padding(vertical = 12.dp),
        contentAlignment = Alignment.Center
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center
        ) {
            Icon(
                imageVector = icon,
                contentDescription = label,
                tint = textColor,
                modifier = Modifier.size(20.dp)
            )
            Spacer(modifier = Modifier.width(6.dp))
            Text(text = label, color = textColor, fontSize = 15.sp, fontWeight = FontWeight.Medium)
        }
    }
}
@Preview(showBackground = true, showSystemUi = true)
@Composable
fun MonitorCamScreenPreview() {
    MonitoringPage(onNavigateToHome = {})
}
