package com.sesac.common.navigation

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.sesac.common.R
import com.sesac.common.ui.theme.Android7HoursTheme
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Icon


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CommonHeader(
    modifier: Modifier = Modifier,
    title: String,
    onNavigateToHome: (() -> Unit)? = null, // 홈 버튼이 없는 화면도 가능
    onBack: (() -> Unit)? = null,           // 뒤로가기 버튼
    logoResId: Int? = R.drawable.image7hours,
    backgroundColor: Color = Color(0xFFDBE8CC),
    contentColor: Color = Color(0xFF1F2937),
    height: Int = 56
) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .background(backgroundColor)
            .statusBarsPadding()// 시스템바 영역 피하기
    ) {
        Row(
            modifier = modifier
                .fillMaxWidth()
                .height(height.dp)
                .padding(horizontal = 16.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            when {
                logoResId != null && onNavigateToHome != null -> {
                    IconButton(onClick = onNavigateToHome) {
                        Image(
                            painter = painterResource(id = logoResId),
                            contentDescription = "Home Logo",
                            modifier = Modifier.size(40.dp)
                        )
                    }
                }
                onBack != null -> {
                    IconButton(onClick = onBack) {
                        Icon(
                            Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "뒤로가기",
                            tint = contentColor
                        )
                    }
                }
                else -> {
                    Spacer(modifier = Modifier.width(40.dp))
                }
            }

            Text(
                text = title,
                color = contentColor,
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold
            )

            Spacer(modifier = Modifier.width(40.dp))
        }
    }
}

@Preview
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CommonHeaderPreview() {
    Android7HoursTheme {
        CommonHeader(
            title = "모니터링",
            onNavigateToHome = { /* 홈 이동 */ }
        )
    }
}