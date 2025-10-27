package com.sesac.monitor.presentation

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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp


@Composable
fun MonitorGPSScreen (modifier: Modifier = Modifier) {
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
                buttonLabels = listOf("영상", "GPS")
            ) { label ->
                when (label) {
                    "영상" -> { /* 영상 화면으로 이동 */ }
                    "GPS" -> { /* GPS 화면으로 이동 */ }
                }
            }
        }
        Spacer(Modifier.height(30.dp))
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .aspectRatio(1 / 1.5f)
                .background(Color.Gray, shape = RoundedCornerShape(8.dp)),
            contentAlignment = Alignment.Center

        ){
            Text( // 추후 text 지우고 지도 코드로 변경하면 됨
                text = "지도 화면 ",
                color = Color.White,
                fontWeight = FontWeight.Bold
            )

        }
    }
}


@Preview(showBackground = true, showSystemUi = true)
@Composable
fun MonitorGPSScreenPreview() {
    MonitorGPSScreen()
}