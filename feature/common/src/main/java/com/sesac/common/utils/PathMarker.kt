package com.sesac.common.utils

import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.unit.dp
import com.sesac.common.ui.theme.PrimaryGreenLight

@Composable
fun PathMarker(
    modifier: Modifier = Modifier,
    onClick: () -> Unit
) {
    // React의 animate-pulse, animate-ping 효과
    val infiniteTransition = rememberInfiniteTransition(label = "marker_pulse")
    val pulseAlpha by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 1f,
        animationSpec = infiniteRepeatable(
            animation = tween(1500),
        ), label = "pulse_alpha"
    )
    val pulseScale by infiniteTransition.animateFloat(
        initialValue = 1f,
        targetValue = 2.5f,
        animationSpec = infiniteRepeatable(
            animation = tween(1500),
        ), label = "pulse_scale"
    )

    Box(
        modifier = modifier.clickable(onClick = onClick),
        contentAlignment = Alignment.Center
    ) {
        // Ripple (animate-ping)
        Box(
            modifier = Modifier
                .size(32.dp)
                .graphicsLayer {
                    scaleX = pulseScale
                    scaleY = pulseScale
                    alpha = 1f - pulseAlpha
                }
                .background(PrimaryGreenLight.copy(alpha = 0.5f), CircleShape)
        )

        // Marker Dot (animate-pulse)
        Box(
            modifier = Modifier
                .size(32.dp)
                .graphicsLayer {
                    alpha = if (pulseAlpha > 0.5f) 1f else 0.7f
                }
                .background(PrimaryGreenLight, CircleShape)
                .padding(8.dp) // 흰색 내부 원을 위한 패딩
                .background(Color.White, CircleShape),
            contentAlignment = Alignment.Center
        ) {
            // React 코드의 Tooltip은 모바일에서는 Click/LongClick으로 변경해야 함
            // 여기서는 마커 자체만 구현
        }
    }
}