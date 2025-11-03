package com.sesac.trail.presentation.component

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
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
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Stop
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.sesac.common.ui.theme.TextPrimary
import com.sesac.common.ui.theme.TextSecondary
import com.sesac.common.ui.theme.circularButtonSize
import com.sesac.common.ui.theme.elevationLarge
import com.sesac.common.ui.theme.paddingLarge
import com.sesac.common.ui.theme.paddingMedium
import com.sesac.common.ui.theme.paddingSmall
import com.sesac.common.R as cR

@Composable
fun RecordingControls(
    isRecording: Boolean,
    recordingTime: Long,
    onToggleRecording: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier.padding(bottom = paddingLarge),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(paddingMedium)
    ) {
        AnimatedVisibility(
            visible = isRecording,
            enter = slideInVertically { it } + fadeIn(),
            exit = slideOutVertically { it } + fadeOut()
        ) {
            RecordingInfoChip(recordingTime = recordingTime)
        }

        Button(
            onClick = onToggleRecording,
            modifier = Modifier.size(circularButtonSize),
            shape = CircleShape,
            colors = ButtonDefaults.buttonColors(
                containerColor = if (isRecording) MaterialTheme.colorScheme.error else MaterialTheme.colorScheme.primary
            ),
            elevation = ButtonDefaults.buttonElevation(defaultElevation = elevationLarge)
        ) {
            Icon(
                imageVector = if (isRecording) Icons.Default.Stop else Icons.Default.PlayArrow,
                contentDescription = if (isRecording) "기록 중지" else "기록 시작",
                modifier = Modifier.size(circularButtonSize/2),
                tint = Color.White
            )
        }
    }
}

@Composable
fun RecordingInfoChip(recordingTime: Long) {
    Surface(
        shape = MaterialTheme.shapes.large,
        color = MaterialTheme.colorScheme.surface,
//        elevation = 8.dp
    ) {
        val distance = 0f

        Column(
            modifier = Modifier.padding(paddingMedium),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Box(
                    modifier = Modifier
                        .size(12.dp)
                        .background(MaterialTheme.colorScheme.error, CircleShape)
                ) // TODO: Animate pulse
                Spacer(modifier = Modifier.width(paddingSmall))
                Text(stringResource(cR.string.trail_button_now_record), fontWeight = FontWeight.Bold)
            }
            Text(
                // TODO: 실제 타이머 로직으로 대체
                text = "${recordingTime / 60}:${(recordingTime % 60).toString().padStart(2, '0')}",
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold,
                color = TextPrimary
            )
            Row(
                horizontalArrangement = Arrangement.spacedBy(paddingSmall),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(text = "0.0 km", style = MaterialTheme.typography.bodyMedium)
//                Text("•", fontSize = 14.sp, color = TextSecondary)
//                Text("0 걸음", fontSize = 14.sp, color = TextSecondary)
            }
        }
    }
}