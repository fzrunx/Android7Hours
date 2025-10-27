package com.sesac.community.presentation

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Route
import androidx.compose.material.icons.filled.Monitor
import androidx.compose.material.icons.filled.Chat
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.outlined.FavoriteBorder
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.sesac.common.ui.theme.Android7HoursTheme

//import com.sesac.common.ui.theme.Android7HoursTheme

data class Post(
    val id: Int,
    val nickname: String,
    val content: String,
    val hashtag: String,
    val imageUrl: String // 사진 URL (지금은 임시로 Text로 표시)
)
/**
 * 화면 전체의 레이아웃 (상단바, 하단바, FAB, 콘텐츠)
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CommunityMainScreen(modifier: Modifier = Modifier) {
    Scaffold(
        modifier = modifier,
//        topBar = { CommunityTopBar() },
//        bottomBar = { CommunityBottomNav() },
        floatingActionButton = {
            FloatingActionButton(onClick = { /* TODO: 새 글 작성 로직 */ }) {
                Icon(
                    imageVector = Icons.Default.Add,
                    contentDescription = "새 글 작성",
                    modifier = Modifier.size(36.dp)
                )
            }
        }
    ) { innerPadding ->
        // Scaffold의 콘텐츠 영역
        Column(
            modifier = Modifier
                .padding(innerPadding) // 상단바와 하단바 영역을 제외한 패딩
                .fillMaxSize()
                .background(Color.White)
        ) {
            // "SNS" / "게시판" 탭 섹션
            TabSection()

            // 게시물 피드 (스크롤 가능)
            PostFeed()
        }
    }
}

/**
 * 1. 상단 "Community" 타이틀 바
 */
//@Composable
//fun CommunityTopBar() {
//    Surface(
//        modifier = Modifier.fillMaxWidth(),
//        color = Color(0xFFF5F5F5), // 이미지와 유사한 연한 회색
//        shadowElevation = 2.dp
//    ) {
//        Text(
//            text = "Community",
//            textAlign = TextAlign.Center,
//            modifier = Modifier.padding(vertical = 16.dp),
//            fontSize = 20.sp,
//            fontWeight = FontWeight.Bold
//        )
//    }
//}

/**
 * 2. "SNS" / "게시판" 탭 버튼
 */
@Composable
fun TabSection() {
    var selectedTab by remember { mutableStateOf(0) } // 0 = SNS, 1 = 게시판

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp, horizontal = 16.dp),
        horizontalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // SNS 버튼
        Button(
            onClick = { selectedTab = 0 },
            colors = ButtonDefaults.buttonColors(
                containerColor = if (selectedTab == 0) Color.LightGray else Color(0xFFF0F0F0),
                contentColor = Color.Black
            ),
            modifier = Modifier.weight(1f),
            border = BorderStroke(1.dp, Color.Gray)
        ) {
            Text("SNS", fontSize = 16.sp)
        }

        // 게시판 버튼
        Button(
            onClick = { selectedTab = 1 },
            colors = ButtonDefaults.buttonColors(
                containerColor = if (selectedTab == 1) Color.LightGray else Color(0xFFF0F0F0),
                contentColor = Color.Black
            ),
            modifier = Modifier.weight(1f),
            border = BorderStroke(1.dp, Color.Gray)
        ) {
            Text("게시판", fontSize = 16.sp)
        }
    }
}

/**
 * 3. 스크롤 가능한 게시물 피드
 */
@Composable
fun PostFeed() {
    // 1. 표시할 데이터 리스트를 준비합니다.
    // (위에서 정의한 Post 데이터 클래스 사용)
    val postList = listOf(
        Post(1, "닉네임A", "사진에 대한 내용입니다.", "#해시태그1", "url_to_image_1"),
        Post(2, "닉네임B", "산책로에 대한 내용입니다.", "#해시태그2 #산책", "url_to_image_2"),
        Post(3, "닉네임C", "사진 + 산책로 내용입니다.", "#해시태그3", "url_to_image_3")
    )

    // 스크롤 가능한 리스트
    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp),
        verticalArrangement = Arrangement.spacedBy(24.dp) // 각 아이템 사이의 간격
    ) {
        // 2. 기존 item { ... } 대신 items(postList) { ... } 를 사용합니다.
        items(postList) { post ->
            // postList에 있는 각 'post' 데이터에 대해
            // PostItem Composable을 실행합니다.
            PostItem(post = post) // PostItem에 데이터를 전달합니다.
        }
    }
}

/**
 * 4. 개별 게시물 아이템
 */
@Composable
fun PostItem(post: Post) { // <-- (1) post: Post 파라미터를 받도록 수정
    Column(modifier = Modifier.fillMaxWidth()) {
        // 1. 프로필 영역 (아이콘 + 닉네임)
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.padding(bottom = 8.dp)
        ) {
            Box(
                modifier = Modifier
                    .size(40.dp)
                    .clip(CircleShape)
                    .background(Color(0xFF673AB7)) // 이미지의 보라색
            )
            Spacer(modifier = Modifier.width(8.dp))

            // (2) "닉네임" -> post.nickname 으로 수정
            Text(post.nickname, fontWeight = FontWeight.Bold, fontSize = 16.sp)
        }

        // 2. 사진 영역
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .aspectRatio(1f) // 1:1 정사각형 비율
                .background(Color(0xFFEEEEEE)), // 연한 회색
            contentAlignment = Alignment.Center
        ) {
            // (3) 실제로는 AsyncImage(model = post.imageUrl, ...) 등을 사용
            Text("사진 (${post.imageUrl})", color = Color.Gray, fontSize = 24.sp)
        }

        // 3. 아이콘 버튼 (좋아요, 저장, 댓글)
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 8.dp),
            horizontalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Icon(Icons.Outlined.FavoriteBorder, contentDescription = "좋아요")
            /* Icon(Icons.Outlined.StarBorder, contentDescription = "저장")
            Icon(Icons.Outlined.ChatBubbleOutline, contentDescription = "댓글") */
        }

        // 4. 본문
        Text(
            // (4) 본문 텍스트 -> post.content 로 수정
            text = post.content,
            fontSize = 14.sp,
            modifier = Modifier.padding(vertical = 8.dp)
        )

        // 5. 해시태그
        Text(
            // (5) 해시태그 텍스트 -> post.hashtag 로 수정
            text = post.hashtag,
            fontSize = 14.sp,
            color = Color.Gray
        )
    }
}

/**
 * 5. 하단 네비게이션 바
 */
@Composable
fun CommunityBottomNav() {
    // "커뮤니티" 탭이 선택된 상태 (인덱스 3)
    var selectedItem by remember { mutableStateOf(3) }
    val items = listOf(
        "홈" to Icons.Default.Home,
        "산책로" to Icons.Default.Route, // Icons.Default.DirectionsWalk
        "모니터링" to Icons.Default.Monitor, // Icons.Default.Monitor
        "커뮤니티" to Icons.Default.Chat, // Icons.Default.Chat
        "마이페이지" to Icons.Default.Person
    )

    NavigationBar {
        items.forEachIndexed { index, item ->
            NavigationBarItem(
                icon = { Icon(item.second, contentDescription = item.first) },
                label = { Text(item.first) },
                selected = (selectedItem == index),
                onClick = { selectedItem = index }
            )
        }
    }
}

// 이 프리뷰를 통해 Android Studio에서 디자인을 실시간으로 볼 수 있습니다.
@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    Android7HoursTheme {
        CommunityMainScreen()
    }
}
