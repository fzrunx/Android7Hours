package com.sesac.community.presentation

import android.R
import com.sesac.common.R as commonR
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.text.input.TextFieldState
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
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.sesac.common.component.CommonSearchBar


// --- 데이터 클래스 정의 ---

// 1. 하단 네비게이션 아이템
data class BottomNavItem(
    val label: String,
    val icon: ImageVector
)

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

// 하단 네비게이션 아이템 리스트
val bottomNavItems = listOf(
    BottomNavItem("홈", Icons.Default.Home),
    BottomNavItem("산책로", Icons.AutoMirrored.Filled.DirectionsWalk),
    BottomNavItem("모니터링", Icons.Default.Monitor),
    BottomNavItem("커뮤니티", Icons.Default.PhotoCamera), // 이미지에 있는 카메라 아이콘
    BottomNavItem("마이페이지", Icons.Default.Person)
)

// 게시글 리스트
val samplePosts = listOf(
    PostItemData(1, "1번 게시글", "작성자", 4, 3, "https://images.dog.ceo/breeds/borzoi/n02090622_6911.jpg"), // 보더콜리 이미지 URL 예시
    PostItemData(2, "2번 게시글", "작성자", 2, 3, null),
    PostItemData(3, "3번 게시글", "작성자", 11, 8, null), // 이미지의 "II", "8"을 숫자로
    PostItemData(4, "4번 게시글", "작성자", 1, 2, null)
)

// --- 메인 화면 Composable ---
// ... (import 문들은 기존과 동일하게 유지)


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CommunityScreen() {
    // 하단 네비게이션의 현재 선택된 아이템 인덱스
    // 3 = "커뮤니티" (0부터 시작)
    var selectedBottomIndex by remember { mutableStateOf(3) }

    Scaffold(
        // 1. 상단 앱 바
//        topBar = { CommunityTopAppBar() },

        // 2. 하단 네비게이션 바
//        bottomBar = {
//            CommunityBottomNav(
//                items = bottomNavItems,
//                selectedIndex = selectedBottomIndex,
//                onItemSelected = { selectedBottomIndex = it }
//            )
//        },
        containerColor = Color.White // Scaffold 배경색을 흰색으로
    ) { paddingValues ->
        // 3. 메인 컨텐츠 (탭 + 게시글 목록)
        CommunityContent(
            modifier = Modifier
                .padding(paddingValues)
                .fillMaxSize()
        )
    }
}

// --- 1. 상단 앱 바 ---
//@OptIn(ExperimentalMaterial3Api::class)
//@Composable
//fun CommunityTopAppBar() {
//    TopAppBar(
//        title = {
//            Text(
//                text = "Community",
//                modifier = Modifier.fillMaxWidth(),
//                textAlign = TextAlign.Center // 제목 중앙 정렬
//            )
//        },
//        actions = {
//            IconButton(onClick = { /* TODO: 검색 클릭 */ }) {
//                Icon(Icons.Default.Search, contentDescription = "검색")
//            }
//        },
//        // 이미지와 같이 흰색 배경, 검은색 아이콘
//        colors = TopAppBarDefaults.topAppBarColors(
//            containerColor = Color.White,
//            titleContentColor = Color.Black,
//            actionIconContentColor = Color.Black
//        )
//    )
//}

// --- 2. 하단 네비게이션 바 ---
@Composable
fun CommunityBottomNav(
    items: List<BottomNavItem>,
    selectedIndex: Int,
    onItemSelected: (Int) -> Unit
) {
    NavigationBar(
        containerColor = Color(0xFFE0F7FA) // 이미지의 연한 하늘색 배경
    ) {
        items.forEachIndexed { index, item ->
            NavigationBarItem(
                selected = selectedIndex == index,
                onClick = { onItemSelected(index) },
                icon = { Icon(item.icon, contentDescription = item.label) },
                label = { Text(item.label) },
                // 선택/비선택 시 색상 지정
                colors = NavigationBarItemDefaults.colors(
                    selectedIconColor = MaterialTheme.colorScheme.primary, // 선택 시 (파란색)
                    selectedTextColor = MaterialTheme.colorScheme.primary,
                    unselectedIconColor = Color.Black, // 비선택 시 (검은색)
                    unselectedTextColor = Color.Black
                )
            )
        }
    }
}

// --- 3. 메인 컨텐츠 (탭 + 게시글 목록) ---
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CommunityContent(
    modifier: Modifier = Modifier
) {
    // 탭의 현재 선택된 아이템 인덱스
    var selectedTabIndex by remember { mutableStateOf(0) }
    val tabs = listOf("전체글", "추천글", "친구글")
    val textFieldState: TextFieldState = TextFieldState()
    var expanded by rememberSaveable { mutableStateOf(false) }
    val onSearch = { s: String -> Unit }
    val searchResult = listOf("1", "22", "333")

    Column(modifier = modifier) {
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
            items(samplePosts) { post ->
                PostItemCard(post = post)
            }
        }
    }
}

// --- 3-2-1. 개별 게시글 카드 ---
@Composable
fun PostItemCard(post: PostItemData) {
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
            ImagePlaceholder(
                imageUrl = post.imageUrl,
                modifier = Modifier
                    .width(100.dp)
                    .fillMaxHeight()
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
                    text = post.author,
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
                        text = "${stringResource(commonR.string.community_title_like)} ${post.likes}",
                        style = MaterialTheme.typography.bodySmall
                    )
                    Spacer(modifier = Modifier.width(12.dp))
                    Text(
                        text = "${stringResource(commonR.string.community_title_comment)} ${post.comments}",
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
            contentDescription = stringResource(commonR.string.community_title_post_image),
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
@Preview(showBackground = true, widthDp = 360, heightDp = 640)
@Composable
fun CommunityScreenPreview() {
    // 프리뷰가 Material3 테마를 사용하도록 감싸줍니다.
    MaterialTheme {
        CommunityScreen()
    }
}