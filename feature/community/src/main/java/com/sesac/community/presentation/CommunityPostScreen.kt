package com.sesac.community.presentation

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.scrollable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.input.TextFieldState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.DirectionsWalk
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.Monitor
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.PhotoCamera
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import coil3.compose.AsyncImage
import com.sesac.common.component.CommonSearchBar
import com.sesac.domain.model.post.PostModel
import com.sesac.common.R as cR


// --- 데이터 클래스 정의 ---

// 2. 게시글 아이템
data class PostItemData(
    val id: Int,
    val title: String,
    val author: String,
    val likes: Int,
    val comments: Int,
    val imageUrl: String? = null // 첫 번째 아이템만 이미지 URL이 있음
)

// --- 샘플 데이터 ---

// 게시글 리스트
val samplePosts = listOf(
    PostItemData(1, "1번 게시글", "작성자", 4, 3, "https://images.dog.ceo/breeds/borzoi/n02090622_6911.jpg"), // 보더콜리 이미지 URL 예시
    PostItemData(2, "2번 게시글", "작성자", 2, 3, null),
    PostItemData(3, "3번 게시글", "작성자", 11, 8, null), // 이미지의 "II", "8"을 숫자로
    PostItemData(4, "4번 게시글", "작성자", 1, 2, null)
)


// --- 3. 메인 컨텐츠 (탭 + 게시글 목록) ---
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CommunityPostScreen(
    modifier: Modifier = Modifier,
    navController: NavController,
    communityViewModel: CommunityViewModel = hiltViewModel(),
) {
    // 탭의 현재 선택된 아이템 인덱스
    var selectedTabIndex by remember { mutableStateOf(0) }
    val tabs = listOf("전체글", "추천글", "친구글")
    val textFieldState: TextFieldState = TextFieldState()
    val scrollState = rememberScrollState()
    val onSearch = { s: String -> Unit }
    val searchResult = listOf("1", "22", "333")
    var mockPostList by remember { mutableStateOf<List<PostModel>>(emptyList()) }

    LaunchedEffect(mockPostList) {
        communityViewModel.requestPostList()
        mockPostList = communityViewModel.postListState.value
    }

    Column(
        modifier = modifier
//            .verticalScroll(scrollState),
    ) {
        // --- 3-1. 탭 + 메뉴 아이콘 ---
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // TabRow가 남는 공간을 모두 차지하도록 weight(1f)
            TabRow(
                selectedTabIndex = selectedTabIndex,
                modifier = Modifier.weight(1f),
                containerColor = Color.White
            ) {
                tabs.forEachIndexed { index, title ->
                    Tab(
                        selected = selectedTabIndex == index,
                        onClick = { selectedTabIndex = index },
                        text = { Text(title) }
                    )
                }
            }
            // 메뉴 아이콘
            IconButton(onClick = { /* TODO: 메뉴 클릭 */ }) {
                Icon(Icons.Default.Menu, contentDescription = "메뉴")
            }
        }

        CommonSearchBar(
            Alignment.CenterHorizontally,
            textFieldState,
            onSearch,
            searchResult,
        )

        // --- 3-2. 게시글 목록 ---
        LazyColumn(
            contentPadding = PaddingValues(10.dp), // 목록 전체의 패딩
            verticalArrangement = Arrangement.spacedBy(12.dp) // 아이템 사이의 간격
        ) {
//            items(samplePosts) { post ->
            items(mockPostList) { post ->
                PostItemCard(post = post)
            }
        }
    }
}

// --- 3-2-1. 개별 게시글 카드 ---
@Composable
fun PostItemCard(
//    post: PostItemData
    post: PostModel,
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        // 이미지와 같이 연한 회색 배경
        colors = CardDefaults.cardColors(
            containerColor = Color(0xFFF0F0F0)
        ),
        // 이미지의 흰색 테두리 효과
        border = BorderStroke(1.dp, Color(0xFFE0E0E0))
    ) {
        Row(
            modifier = Modifier.height(120.dp) // 아이템 높이 고정
        ) {
            // (1) 왼쪽: 이미지 또는 "사진" 텍스트
//            ImagePlaceholder(
//                imageUrl = post.imageUrl,
//                modifier = Modifier
//                    .width(100.dp)
//                    .fillMaxHeight()
//            )

            Image(
                painter = painterResource(post.imageResList?.component1() ?: cR.drawable.icons8_dog_50),
                contentDescription = "이미지"
            )

            // (2) 오른쪽: 텍스트 내용
            Column(
                modifier = Modifier
                    .weight(1f) // 남는 공간 모두 차지
                    .padding(horizontal = 12.dp, vertical = 8.dp)
            ) {
                Text(
                    text = post.title,
                    style = MaterialTheme.typography.titleMedium // "1번 게시글"
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = post.userName,
                    style = MaterialTheme.typography.bodySmall, // "작성자"
                    color = Color.Gray
                )

                // 이 Spacer가 "좋아요/댓글"을 하단으로 밀어냅니다.
                Spacer(modifier = Modifier.weight(1f))

                // 좋아요/댓글 (오른쪽 정렬)
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.End
                ) {
                    Text(
                        text = "${stringResource(cR.string.community_title_like)} ${post.likes}",
                        style = MaterialTheme.typography.bodySmall
                    )
                    Spacer(modifier = Modifier.width(12.dp))
                    Text(
                        text = "${stringResource(cR.string.community_title_comment)} ${post.comments}",
                        style = MaterialTheme.typography.bodySmall
                    )
                }
            }
        }
    }
}

// --- (1)-1. 이미지 / "사진" 플레이스홀더 ---
@Composable
fun ImagePlaceholder(imageUrl: String?, modifier: Modifier = Modifier) {
    if (imageUrl != null) {
        // Coil 라이브러리의 AsyncImage 사용
        AsyncImage(
            model = imageUrl,
            contentDescription = stringResource(cR.string.community_title_post_image),
            contentScale = ContentScale.Crop, // 꽉 차게 자르기
            modifier = modifier
        )
    } else {
        // 이미지가 없을 경우 "사진" 텍스트가 있는 회색 박스
        Box(
            modifier = modifier
                .background(Color(0xFFE0E0E0)), // 진한 회색 배경
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = "사진", //텍스트로 굳이 필요없는 부분 추후 수정 필요
                color = Color.DarkGray
            )
        }
    }
}

// --- Preview (미리보기) ---
//@Preview(showBackground = true, widthDp = 360, heightDp = 640)
//@Composable
//fun CommunityScreenPreview() {
//    // 프리뷰가 Material3 테마를 사용하도록 감싸줍니다.
//    MaterialTheme {
//        CommunityPostScreen()
//    }
//}