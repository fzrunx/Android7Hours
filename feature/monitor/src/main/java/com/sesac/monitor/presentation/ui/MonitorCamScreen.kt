package com.sesac.monitor.presentation.ui

import androidx.compose.animation.animateColorAsState
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp


@Composable
fun MonitorCamScreen() {
    var isRecording by remember { mutableStateOf(false) }
    Column(modifier = Modifier.fillMaxSize()) {
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
