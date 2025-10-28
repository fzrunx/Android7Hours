package com.sesac.trail.presentation.component

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.FilledTonalButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

@Composable
fun TrailControlBar(
    state: TrailState,
    onStartClick: () -> Unit = {},
    onPauseClick: () -> Unit = {},
    onResumeClick: () -> Unit = {},
    onStopClick: () -> Unit = {}
) {
    Surface(
        color = MaterialTheme.colorScheme.surface.copy(alpha = 0.85f), // 반투명
        tonalElevation = 1.dp,
        shadowElevation = 0.dp,
        modifier = Modifier
            .fillMaxWidth()
            .navigationBarsPadding()
            .padding(horizontal = 12.dp, vertical = 6.dp) // 얇은 여백
    ) {
        Row(
            horizontalArrangement = Arrangement.SpaceEvenly,
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .fillMaxWidth()
                .height(44.dp) // 기존 버튼 대비 약 30% 축소
        ) {
            when (state) {
                TrailState.Idle -> {
                    FilledTonalButton(
                        onClick = onStartClick,
                        contentPadding = PaddingValues(horizontal = 12.dp, vertical = 6.dp),
                        modifier = Modifier.weight(1f)
                    ) {
                        Text("Start", style = MaterialTheme.typography.labelLarge)
                    }
                }

                TrailState.Running -> {
                    OutlinedButton(
                        onClick = onPauseClick,
                        contentPadding = PaddingValues(horizontal = 12.dp, vertical = 6.dp),
                        modifier = Modifier.weight(1f)
                    ) {
                        Text("Pause", style = MaterialTheme.typography.labelLarge)
                    }
                    FilledTonalButton(
                        onClick = onStopClick,
                        contentPadding = PaddingValues(horizontal = 12.dp, vertical = 6.dp),
                        modifier = Modifier
                            .weight(1f)
                            .padding(start = 6.dp)
                    ) {
                        Text("Stop", style = MaterialTheme.typography.labelLarge)
                    }
                }

                TrailState.Paused -> {
                    Button(
                        onClick = onResumeClick,
                        contentPadding = PaddingValues(horizontal = 12.dp, vertical = 6.dp),
                        modifier = Modifier.weight(1f)
                    ) {
                        Text("Resume", style = MaterialTheme.typography.labelLarge)
                    }
                    FilledTonalButton(
                        onClick = onStopClick,
                        colors = ButtonDefaults.buttonColors(
                            containerColor = MaterialTheme.colorScheme.errorContainer,
                            contentColor = MaterialTheme.colorScheme.onErrorContainer
                        ),
                        contentPadding = PaddingValues(horizontal = 12.dp, vertical = 6.dp),
                        modifier = Modifier
                            .weight(1f)
                            .padding(start = 6.dp)
                    ) {
                        Text("Stop", style = MaterialTheme.typography.labelLarge)
                    }
                }

                TrailState.Stopped -> {
                    FilledTonalButton(
                        onClick = onStartClick,
                        contentPadding = PaddingValues(horizontal = 12.dp, vertical = 6.dp),
                        modifier = Modifier.weight(1f)
                    ) {
                        Text("Restart", style = MaterialTheme.typography.labelLarge)
                    }
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun TrailControlBarCompactPreview() {
    MaterialTheme {
        Column {
            TrailControlBarCompact(state = TrailState.Idle)
            Spacer(Modifier.height(4.dp))
            TrailControlBarCompact(state = TrailState.Running)
            Spacer(Modifier.height(4.dp))
            TrailControlBarCompact(state = TrailState.Paused)
        }
    }
}

@Composable
fun TrailControlBarCompact(state: TrailState) {
    TODO("Not yet implemented")
}