package com.sesac.community.presentation

import com.sesac.common.R as commonR
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Reply
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.sesac.common.ui.theme.Android7HoursTheme

// 댓글 데이터를 위한 data class
data class Comment(
    val id: Int,
    val author: String,
    val date: String,
    val content: String
)


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CommunityDetailScreen() {

    // 하단 댓글 입력창의 상태
    var commentText by remember { mutableStateOf("") }

    // 댓글 목록 (예시 데이터)
    val comments = listOf(
        Comment(1, "작성자1", "작성 날짜", "댓글 내용1"),
        Comment(2, "작성자2", "작성 날짜", "댓글 내용2")
    )

    Scaffold(
        containerColor = Color.White,
        // 1. 상단 앱 바
//        topBar = {
//            CenterAlignedTopAppBar(
//                title = { Text("Community detail") },
//                navigationIcon = {
//                    IconButton(onClick = { /* TODO: 닫기 */ }) {
//                        Icon(Icons.Default.Close, contentDescription = "닫기")
//                    }
//                },
//                colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
//                    containerColor = Color.White
//                )
//            )
//        },
        // 2. 하단 댓글 입력창
        bottomBar = {
            Surface(
                shadowElevation = 8.dp, // 그림자 효과
                color = Color.White
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp, vertical = 8.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    // 댓글 입력 필드
                    TextField(
                        value = commentText,
                        onValueChange = { commentText = it },
                        modifier = Modifier.weight(1f),
                        placeholder = { Text(stringResource(commonR.string.community_placeholder_comment_write), fontSize = 14.sp) },
                        colors = TextFieldDefaults.colors(
                            focusedContainerColor = Color.Transparent,
                            unfocusedContainerColor = Color.Transparent,
                            focusedIndicatorColor = Color.Transparent, // 포커스 밑줄 제거
                            unfocusedIndicatorColor = Color.Transparent // 밑줄 제거
                        )
                    )

                    Spacer(modifier = Modifier.width(8.dp))

                    // '등록하기' 버튼 (테두리만 있는 버튼)
                    OutlinedButton(
                        onClick = { /* TODO: 댓글 등록 */ },
                        shape = RectangleShape // 직각 모서리
                    ) {
                        Text(stringResource(commonR.string.community_button_register))
                    }
                }
            }
        }
    ) { paddingValues ->
        // 3. 메인 콘텐츠 (스크롤 영역)
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues), // Scaffold가 제공하는 패딩 적용
            contentPadding = PaddingValues(horizontal = 16.dp, vertical = 12.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp) // 아이템 간 세로 간격
        ) {

            // --- 게시글 본문 ---
            item {
                Column(
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    // 1. 제목 및 날짜
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.Top
                    ) {
                        Text(
                            text = "1번 게시글", // 하드코딩 데이터로 추후 변경 필요
                            style = MaterialTheme.typography.headlineMedium,
                            fontWeight = FontWeight.Bold,
                            modifier = Modifier.weight(1f) // 남는 공간 모두 차지
                        )
                        Column(horizontalAlignment = Alignment.End) {
                            TextButton(
                                onClick = { /* TODO: 공유하기 */ },
                                contentPadding = PaddingValues(0.dp)
                            ) {
                                // 이미지의 아이콘과 유사한 'Reply' 아이콘 사용 (AutoMirrored)
                                Icon(
                                    Icons.AutoMirrored.Filled.Reply,
                                    contentDescription = stringResource(commonR.string.community_title_share),
                                    modifier = Modifier.size(18.dp)
                                )
                                Spacer(modifier = Modifier.width(4.dp))
                                Text(stringResource(commonR.string.community_title_share), fontSize = 14.sp)
                            }
                            Text(
                                text = stringResource(commonR.string.community_title_create_date),
                                style = MaterialTheme.typography.bodySmall,
                                color = Color.Gray
                            )
                        }
                    }

                    // 2. 작성자
                    Text(
                        text = stringResource(commonR.string.community_title_create_auth),
                        style = MaterialTheme.typography.bodyLarge,
                        color = Color.DarkGray
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    // 3. 이미지
                    AsyncImage(
                        model = ImageRequest.Builder(LocalContext.current)
                            .data("https://images.unsplash.com/photo-1517849845537-4d257902454a") // 예시 이미지 URL
                            .crossfade(true)
                            .build(),
                        contentDescription = stringResource(commonR.string.community_title_post_image),
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(250.dp)
                            .clip(RoundedCornerShape(8.dp)),
                        contentScale = ContentScale.Crop
                    )

                    // 4. 게시글 내용
                    Text("게시글 내용", style = MaterialTheme.typography.bodyLarge)

                    Spacer(modifier = Modifier.height(8.dp))

                    // 5. 좋아요 / 댓글 수
                    Row(horizontalArrangement = Arrangement.spacedBy(16.dp)) {
                        val likecount = 10 // 임시 데이터 추가  추후 아래 코드 변경 필요 데이터로 구조 변경하고나서는 postscreen에 있는거랑 동일하게
                        Text(
                            text = stringResource(commonR.string.community_title_like, likecount),
                            style = MaterialTheme.typography.bodyMedium,
                            color = Color.Gray
                        )
                        val commentcount = 3 // 임시 데이터 추가
                        Text(
                            text = stringResource(commonR.string.community_title_comment, commentcount),
                            style = MaterialTheme.typography.bodyMedium,
                            color = Color.Gray
                        )
                    }
                }
            }

            // --- 구분선 ---
            item {
                Divider(color = Color.LightGray, thickness = 1.dp)
            }

            // --- 댓글 목록 ---
            items(comments) { comment ->
                CommentItem(comment = comment)
            }

            // --- '목록' 버튼 ---
            item {
                Box(
                    modifier = Modifier.fillMaxWidth(),
                    contentAlignment = Alignment.CenterEnd // 우측 정렬
                ) {
                    TextButton(onClick = { /* TODO: 목록으로 가기 */ }) {
                        Text(stringResource(commonR.string.community_title_list), fontSize = 16.sp)
                    }
                }
            }
        }
    }
}

/**
 * 개별 댓글을 표시하는 Composable
 */
@Composable
fun CommentItem(comment: Comment) {
    Surface(
        color = Color(0xFFF5F5F5), // 연한 회색 배경
        shape = RoundedCornerShape(8.dp),
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(
            modifier = Modifier.padding(12.dp),
            verticalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(
                    text = comment.author,
                    fontWeight = FontWeight.Bold,
                    fontSize = 15.sp
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = comment.date,
                    style = MaterialTheme.typography.bodySmall,
                    color = Color.Gray
                )
            }
            Text(
                text = comment.content,
                style = MaterialTheme.typography.bodyMedium
            )
        }
    }
}

// Android Studio에서 미리보기
@Preview(showBackground = true)
@Composable
fun CommunityDetailScreenPreview() {
    // MaterialTheme으로 감싸주면 실제 앱과 더 유사한 디자인을 볼 수 있습니다.
    // YourAppTheme { CommunityDetailScreen() }
    Android7HoursTheme {
        CommunityDetailScreen()
    }
}