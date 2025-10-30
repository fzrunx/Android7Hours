package com.sesac.mypage.presentation

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CalendarToday
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MyPageManageScreen() {

    // 텍스트 필드 상태
    var expenseText by remember { mutableStateOf("월 지출 내용") }
    var memoText by remember { mutableStateOf("") }

    Scaffold(
        containerColor = Color(0xFFF8F9FA), // 전체 배경색

        // 3. 메인 콘텐츠 (스크롤 영역)
        content = { paddingValues ->
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues)
                    .verticalScroll(rememberScrollState()) // 스크롤 가능하도록
                    .padding(16.dp), // 콘텐츠 전체 여백
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(24.dp) // 섹션 간 간격
            ) {
                // 상단 아이콘 메뉴 (관리, 내 정보, 설정)
                IconMenuSection()

                // 캘린더 섹션 (하드코딩된 원본)
                CalendarSection()

                // 지출 내역 섹션
                ExpenseSection(expenseText) { expenseText = it }

                // 메모 섹션
                MemoSection(memoText) { memoText = it }

                // 하단 정보 텍스트
                FooterInfoSection()
            }
        }
    )
}

/**
 * 상단 아이콘 메뉴 (관리, 내 정보, 설정)
 */
@Composable
fun IconMenuSection() {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 16.dp),
        horizontalArrangement = Arrangement.SpaceAround
    ) {
        InfoButton(icon = Icons.Default.CalendarToday, text = "관리")
        InfoButton(icon = Icons.Default.Info, text = "내 정보")
        InfoButton(icon = Icons.Default.Settings, text = "설정")
    }
}

@Composable
fun InfoButton(icon: ImageVector, text: String) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        Icon(
            imageVector = icon,
            contentDescription = text,
            modifier = Modifier.size(32.dp),
            tint = Color.DarkGray
        )
        Text(text, fontSize = 14.sp)
    }
}

/**
 * 캘린더 섹션 (하드코딩된 원본)
 */
@Composable
fun CalendarSection() {
    // 2025년 10월 1일은 수요일
    val days = listOf(
        "", "", "", "1", "2", "3", "4",
        "5", "6", "7", "8", "9", "10", "11",
        "12", "13", "14", "15", "16", "17", "18",
        "19", "20", "21", "22", "23", "24", "25",
        "26", "27", "28", "29", "30", "31", ""
    )
    val daysOfWeek = listOf("일", "월", "화", "수", "목", "금", "토")

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(Color.White, shape = RoundedCornerShape(8.dp))
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // 캘린더 제목
        Text(
            text = "10월 2025",
            style = MaterialTheme.typography.headlineSmall,
            fontWeight = FontWeight.Bold
        )
        Spacer(modifier = Modifier.height(16.dp))

        // 요일 헤더
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceAround
        ) {
            daysOfWeek.forEach { day ->
                Text(
                    text = day,
                    modifier = Modifier.weight(1f),
                    textAlign = TextAlign.Center,
                    fontWeight = FontWeight.SemiBold,
                    color = Color.Gray
                )
            }
        }
        Divider(modifier = Modifier.padding(vertical = 8.dp))

        // 날짜 (Grid)
        days.chunked(7).forEach { week ->
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp),
                horizontalArrangement = Arrangement.SpaceAround
            ) {
                week.forEach { day ->
                    Text(
                        text = day,
                        modifier = Modifier.weight(1f),
                        textAlign = TextAlign.Center,
                        fontSize = 16.sp,
                        fontWeight = FontWeight.SemiBold
                    )
                }
            }
        }
    }
}

/**
 * 지출 내역 섹션
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ExpenseSection(value: String, onValueChange: (String) -> Unit) {
    Column(modifier = Modifier.fillMaxWidth()) {
        Text("지출 내역", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
        Spacer(modifier = Modifier.height(8.dp))

        OutlinedTextField(
            value = value,
            onValueChange = onValueChange,
            modifier = Modifier
                .fillMaxWidth()
                .height(150.dp),
            shape = RoundedCornerShape(8.dp),
            // 👇 [수정됨] outlinedTextFieldColors -> colors
            //           containerColor -> focusedContainerColor, unfocusedContainerColor
            colors = TextFieldDefaults.colors(
                focusedContainerColor = Color.White,
                unfocusedContainerColor = Color.White
            )
        )
        Spacer(modifier = Modifier.height(8.dp))

        // "Get started" 버튼과 "승인" 칩
        Box(
            modifier = Modifier.fillMaxWidth(),
            contentAlignment = Alignment.CenterStart
        ) {
            Button(
                onClick = { /*TODO*/ },
                shape = RoundedCornerShape(8.dp)
            ) {
                Text(
                    "Get started",
                    modifier = Modifier.padding(vertical = 4.dp),
                    fontSize = 16.sp
                )
            }
            // 버튼 위에 "승인" 칩 겹치기
//            SuggestionChip(
//                onClick = { /*TODO*/ },
//                label = { Text("승인") },
//                colors = SuggestionChipDefaults.suggestionChipColors(
//                    containerColor = Color(0xFFFFEB3B) // 노란색 배경
//                ),
//                modifier = Modifier
//                    .align(Alignment.CenterStart)
//                    .offset(x = 100.dp, y = (-8).dp) // 버튼 기준으로 위치 조절
//            )
        }
    }
}

/**
 * 메모 섹션
 */
@Composable
fun MemoSection(value: String, onValueChange: (String) -> Unit) {
    Column(modifier = Modifier.fillMaxWidth()) {
        Text("메모", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
        Spacer(modifier = Modifier.height(8.dp))

        OutlinedTextField(
            value = value,
            onValueChange = onValueChange,
            modifier = Modifier
                .fillMaxWidth()
                .height(180.dp),
            shape = RoundedCornerShape(8.dp),
            // 👇 [수정됨] outlinedTextFieldColors -> colors
            //           containerColor -> focusedContainerColor, unfocusedContainerColor
            colors = TextFieldDefaults.colors(
                focusedContainerColor = Color.White,
                unfocusedContainerColor = Color.White
            ),
            placeholder = { Text("메모를 입력하세요.") }
        )
    }
}

/**
 * 하단 정보 텍스트
 */
@Composable
fun FooterInfoSection() {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(4.dp)
    ) {
        Text(
            "Tel:전화번호   버전정보:v.0000   안내상담:070-2525",
            fontSize = 12.sp,
            color = Color.Gray,
            textAlign = TextAlign.Center
        )
    }
}

// Android Studio에서 미리보기
@Preview(showBackground = true)
@Composable
fun MyPageScreenPreview() {
    // 앱의 테마로 감싸주면 더 정확한 미리보기가 가능합니다.
    // 예: YourAppTheme { MyPageScreen() }
    MyPageManageScreen()
}