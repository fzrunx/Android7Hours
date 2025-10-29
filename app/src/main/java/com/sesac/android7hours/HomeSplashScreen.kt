package com.sesac.android7hours


import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.sesac.android7hours.R.*
import kotlinx.coroutines.delay

// 스플래시 화면 배경색 (로고 2번 배경색과 유사하게)
val SplashScreenBackground = Color(0xFFD7E0CC)

@Composable
fun SplashScreen(
    onSplashFinished: () -> Unit // 스플래시가 끝났을 때 호출될 함수
) {
    // LaunchedEffect: 이 Composable이 처음 그려질 때 1번만 실행됩니다.
    LaunchedEffect(key1 = true) {
        // 2초간 대기
        delay(2000L)
        // 2초가 지나면 onSplashFinished 함수를 호출
        onSplashFinished()
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
        // [권장] 배경색을 로고와 어울리게 설정
        // .background(SplashScreenBackground)
        ,
        contentAlignment = Alignment.Center
    ) {
        // 1. res/drawable에 추가한 로고 이미지를 사용합니다.
        // R.drawable.logo_splash 는 실제 파일명으로 변경하세요.
        Image(
            painter = painterResource(id = R.drawable.logo_splash), // ⭐️ 수정 필요
            contentDescription = "7Hours Logo",
            modifier = Modifier.size(500.dp) // 로고 크기 조절
        )
    }
}

@Preview(showBackground = true)
@Composable
fun SplashScreenPreview() {
    SplashScreen(onSplashFinished = {})
}