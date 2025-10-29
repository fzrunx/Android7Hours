package com.sesac.monitor.presentation

import com.sesac.common.R as commonR
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.FiberManualRecord
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp




@Composable
fun MonitorCamScreen(modifier: Modifier = Modifier) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.Top
        ) {
            CustomButton(
                buttonLabels = listOf(
                    stringResource(commonR.string.monitor_button_webcam),
                    stringResource(commonR.string.monitor_button_GPS)
                )
            ) { label ->
                when (label) {
                    "영상" -> { /* 영상 화면으로 이동 */ }
                    "GPS" -> { /* GPS 화면으로 이동 */ }
                }
            }
        }
        Spacer(Modifier.height(24.dp))
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .aspectRatio(1 / 1f)
                .background(Color.Gray, shape = RoundedCornerShape(8.dp)),
            contentAlignment = Alignment.Center

        ){
            Text(
                text = "영상 영역",
                color = Color.White,
                fontWeight = FontWeight.Bold
            )

        }
        Spacer(Modifier.height(60.dp))
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(24.dp),
            horizontalArrangement = Arrangement.SpaceEvenly,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Button(
                onClick = { /* TODO: 재생 기능 */ },
                modifier = Modifier.size(70.dp),
                colors = ButtonDefaults
                    .buttonColors(
                        containerColor = Color(0xFFADFB95),
                        contentColor = Color.Black
                    )
            ) {
                Icon(
                    imageVector = Icons.Filled.PlayArrow,
                    contentDescription = "재생",
                    modifier = Modifier.size(36.dp)
                )

            }
            Button(
                onClick = { /* TODO: 녹화 기능 */ },
                modifier = Modifier.size(70.dp),
                colors = ButtonDefaults
                    .buttonColors(
                        containerColor = Color(0xFFFB9898),
                        contentColor = Color.Black
                    )
            ) {
                Icon(
                    imageVector = Icons.Filled.FiberManualRecord,
                    contentDescription = "녹화",
                    modifier = Modifier.size(36.dp)
                )
            }
        }
    }
}

@Composable
fun CustomButton(buttonLabels: List<String>, modifier: Modifier = Modifier, onClick: (label: String) -> Unit = {}) {
    Row(
        modifier = modifier
            .fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(15.dp) // 버튼 사이 간격
    ) {
        buttonLabels.forEach { label ->
            Button(
                onClick = { onClick(label) },
                modifier = modifier
                    .weight(1f)
                    .height(60.dp),
                shape = RoundedCornerShape(16.dp),
                colors = ButtonDefaults
                    .buttonColors(
                        containerColor = Color.Gray,
                        contentColor = Color.White
                    ),
                elevation = ButtonDefaults.buttonElevation(defaultElevation = 8.dp)
            ) {
                Text(
                    text = label,
                    fontSize = 20.sp,
                    fontWeight = FontWeight.SemiBold
                )
            }
        }
    }
}


@Preview(showBackground = true, showSystemUi = true)
@Composable
fun MonitorCamScreenPreview() {
        MonitorCamScreen()
}

