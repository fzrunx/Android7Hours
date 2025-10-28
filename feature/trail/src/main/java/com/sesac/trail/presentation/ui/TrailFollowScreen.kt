package com.sesac.trail.presentation.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DirectionsWalk
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.sesac.trail.presentation.component.TrailControlBar
import com.sesac.trail.presentation.component.TrailControlBarViewModel


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TrailFollowScreen(
    viewModel: TrailControlBarViewModel,
    modifier: Modifier = Modifier
) {
    val state by viewModel.trailState.collectAsState()

    Scaffold(
        bottomBar = {
            TrailControlBar(
                state = state,
                onStartClick = viewModel::start,
                onPauseClick = viewModel::pause,
                onResumeClick = viewModel::resume,
                onStopClick = viewModel::stop
            )
        }
    ) { paddingValues ->

        Column(
            modifier = modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            // 화면 콘텐츠
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color(0xFFFFF3E0))
            ) {
                Column(
                    modifier = Modifier.align(Alignment.Center),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Icon(
                        Icons.Default.DirectionsWalk,
                        contentDescription = null,
                        modifier = Modifier.size(80.dp),
                        tint = Color.Gray.copy(alpha = 0.3f)
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                    Text(
                        "따라가기 화면",
                        style = MaterialTheme.typography.headlineMedium,
                        fontWeight = FontWeight.Bold,
                        color = Color.Gray
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        "실시간 산책로 네비게이션",
                        style = MaterialTheme.typography.bodyMedium,
                        color = Color.Gray
                    )
                }
            }
        }
    }
}



@OptIn(ExperimentalMaterial3Api::class)
@Preview(showSystemUi = true)
@Composable
fun TrailFollowScreenPreview() {
    val dummyViewModel = remember { TrailControlBarViewModel() }

    TrailFollowScreen(
        viewModel = dummyViewModel,
        modifier = Modifier
    )
}
