package com.sesac.community.presentation

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CommunityCreateScreen() {

    // 입력 필드의 상태를 관리합니다.
    var title by remember { mutableStateOf("") }
    var author by remember { mutableStateOf("") }
    var content by remember { mutableStateOf("") }

    Scaffold(
        // 1. 하단 버튼 (Bottom Bar)
        bottomBar = {
            Button(
                onClick = { /* TODO: 등록하기 동작 */ },
                modifier = Modifier.fillMaxWidth(),
                shape = RectangleShape, // 이미지처럼 사각형 모서리
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color(0xFFF5F5F5), // 이미지와 유사한 연한 회색
                    contentColor = Color.Black // 텍스트 색상
                )
            ) {
                Text(
                    text = "등록하기",
                    modifier = Modifier.padding(vertical = 12.dp),
                    fontSize = 16.sp
                )
            }
        },
        containerColor = Color.White // Scaffold 전체 배경색
    ) { paddingValues ->
        // 3. 메인 콘텐츠 (Content)
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues) // Scaffold가 제공하는 내부 패딩 적용
                .padding(horizontal = 16.dp) // 콘텐츠 좌우 여백
                .verticalScroll(rememberScrollState()) // 콘텐츠가 길어지면 스크롤
        ) {

            Spacer(modifier = Modifier.height(16.dp))

            // 'My Post' (제목) 입력 필드
            TextField(
                value = title,
                onValueChange = { title = it },
                modifier = Modifier.fillMaxWidth(),
                placeholder = {
                    Text(
                        "My Post",
                        style = MaterialTheme.typography.headlineSmall,
                        fontWeight = FontWeight.Bold,
                        color = Color.LightGray
                    )
                },
//                colors = TextFieldDefaults.textFieldColors(
//                    containerColor = Color.Transparent, // 배경 투명
//                    focusedIndicatorColor = Color.Gray, // 포커스 시 밑줄
//                    unfocusedIndicatorColor = Color.LightGray // 비포커스 시 밑줄
//                ),
                singleLine = true,
                textStyle = MaterialTheme.typography.headlineSmall.copy(fontWeight = FontWeight.Bold)
            )

            Spacer(modifier = Modifier.height(8.dp))


            Spacer(modifier = Modifier.height(24.dp))

            // '사진' 영역 (클릭 가능하도록)
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(220.dp)
                    .background(Color(0xFFEEEEEE), shape = RoundedCornerShape(4.dp))
                    .clickable { /* TODO: 사진 선택 로직 */ },
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "사진",
                    style = MaterialTheme.typography.titleMedium,
                    color = Color.Gray
                )
            }

            Spacer(modifier = Modifier.height(24.dp))

            // '게시글 내용' 입력 필드
            TextField(
                value = content,
                onValueChange = { content = it },
                modifier = Modifier
                    .fillMaxWidth()
                    .heightIn(min = 200.dp), // 최소 높이 지정
                placeholder = {
                    Text(
                        "게시글 내용",
                        style = MaterialTheme.typography.bodyLarge,
                        color = Color.LightGray
                    )
                },
//                colors = TextFieldDefaults.textFieldColors(
//                    containerColor = Color.Transparent,
//                    focusedIndicatorColor = Color.Transparent, // 내용 필드는 밑줄 제거
//                    unfocusedIndicatorColor = Color.Transparent
//                ),
                textStyle = MaterialTheme.typography.bodyLarge
            )

            Spacer(modifier = Modifier.height(16.dp)) // 스크롤을 위한 하단 여백
        }
    }
}

// Android Studio에서 미리보기
@Preview(showBackground = true)
@Composable
fun CommunityCreateScreenPreview() {
    // 앱의 테마로 감싸주면 더 정확한 미리보기가 가능합니다.
    // 예: YourAppTheme { CommunityCreateScreen() }
    CommunityCreateScreen()
}