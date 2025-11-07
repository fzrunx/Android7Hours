package com.sesac.trail.presentation.component

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Pause
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Stop
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.LargeFloatingActionButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.sesac.common.ui.theme.ColorOrange
import com.sesac.common.ui.theme.Purple600
import com.sesac.common.ui.theme.Red500
import com.sesac.common.ui.theme.paddingLarge
import com.sesac.common.ui.theme.paddingMicro
import com.sesac.common.ui.theme.paddingSmall
import java.util.concurrent.TimeUnit

@Composable
fun RecordingControls(
    isPaused: Boolean,
    recordingTime: Long,
    onPauseToggle: () -> Unit,
    onStopRecording: () -> Unit
) {
    // 시간 포맷팅
    val formattedTime = remember(recordingTime) {
        val minutes = TimeUnit.SECONDS.toMinutes(recordingTime)
        val seconds = recordingTime % 60
        "%02d:%02d".format(minutes, seconds)
    }

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(paddingSmall)
    ) {
        // Recording Info
        Surface(
            shape = RoundedCornerShape(16.dp),
            shadowElevation = 4.dp,
            color = Color.White
        ) {
            Column(
                modifier = Modifier.padding(horizontal = 24.dp, vertical = 12.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Box(
                        modifier = Modifier
                            .size(12.dp)
                            .background(
                                if (isPaused) ColorOrange else Red500,
                                CircleShape
                            )
                    )
                    Spacer(Modifier.width(paddingMicro))
                    Text(
                        text = if (isPaused) "일시정지" else "기록 중",
                        fontWeight = FontWeight.Bold
                    )
                }
                Text(formattedTime, fontSize = 24.sp, fontWeight = FontWeight.Bold)
                Row(horizontalArrangement = Arrangement.spacedBy(paddingSmall)) {
                    Text("0.0 km", fontSize = 14.sp, color = Color.Gray)
                    Text("•", fontSize = 14.sp, color = Color.Gray)
                    Text("0 걸음", fontSize = 14.sp, color = Color.Gray)
                }
            }
        }

        // Control Buttons
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(paddingLarge)
        ) {
            // Stop Button
            FloatingActionButton(
                onClick = onStopRecording,
                containerColor = Red500,
                contentColor = Color.White,
                shape = CircleShape,
                modifier = Modifier.size(56.dp)
            ) {
                Icon(Icons.Filled.Stop, contentDescription = "기록 중지")
            }

            // Play/Pause Button
            LargeFloatingActionButton(
                onClick = onPauseToggle,
                containerColor = if (isPaused) Purple600 else ColorOrange,
                contentColor = Color.White,
                shape = CircleShape,
                modifier = Modifier.size(80.dp)
            ) {
                if (isPaused) {
                    Icon(Icons.Filled.PlayArrow, contentDescription = "재개", modifier = Modifier.size(40.dp))
                } else {
                    Icon(Icons.Filled.Pause, contentDescription = "일시정지", modifier = Modifier.size(40.dp))
                }
            }

            // React 코드에는 Stop/Play/Pause만 있지만, 좌우 균형을 위해 Spacer 추가
            Spacer(Modifier.size(56.dp))
        }
    }
}