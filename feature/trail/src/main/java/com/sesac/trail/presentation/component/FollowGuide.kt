package com.sesac.trail.presentation.component

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material3.Icon
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.sesac.trail.presentation.TrailViewModel

@Composable
fun FollowGuide(viewModel: TrailViewModel,
                onStopFollowing: () -> Unit,
                modifier: Modifier = Modifier
) {
    val offRoute by viewModel.offRoute.collectAsStateWithLifecycle()
    val progress by viewModel.followProgress.collectAsStateWithLifecycle()
    val nextDirection = viewModel.getNextDirection()
    val remainingDistance = viewModel.getRemainingDistance()

    Surface(
        modifier = modifier
            .fillMaxWidth()
            .padding(16.dp),
        shape = RoundedCornerShape(16.dp),
        shadowElevation = 4.dp,
        color = if (offRoute) Color(0xFFFFCDD2) else Color(0xFFE8F5E9)
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // 🔹 이탈 경고
            if (offRoute) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = Icons.Filled.Warning,
                        contentDescription = null,
                        tint = Color(0xFFD32F2F)
                    )
                    Spacer(Modifier.width(8.dp))
                    Text(
                        "경로에서 벗어났습니다",
                        color = Color(0xFFD32F2F),
                        fontWeight = FontWeight.Bold
                    )
                }
            } else {
                Text("경로 안내 중", fontWeight = FontWeight.Bold)
            }

            Spacer(Modifier.height(8.dp))

            // 🔹 진행률
            LinearProgressIndicator(
                progress = progress / 100f,
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(Modifier.height(8.dp))

            // 🔹 남은 거리
            Text(
                "남은 거리: ${String.format("%.0f", remainingDistance)}m",
                style = MaterialTheme.typography.bodyMedium
            )

            // 🔹 다음 방향 표시
            nextDirection?.let {
                Text(
                    "다음 지점까지 직선거리: ${String.format("%.0f",
                        viewModel.userLocationMarker.value?.distanceTo(it) ?: 0f)}m",
                    style = MaterialTheme.typography.bodySmall
                )
            }
            // ✅ 종료 버튼
            Spacer(Modifier.height(8.dp))
            Button(
                onClick = onStopFollowing,
                colors = ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.error
                )
            ) {
                Text("따라가기 종료")
            }
        }
    }
}