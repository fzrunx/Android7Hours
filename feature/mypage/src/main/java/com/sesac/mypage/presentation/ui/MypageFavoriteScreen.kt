package com.sesac.mypage.presentation.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Chat
import androidx.compose.material.icons.filled.Chat
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.twotone.Nature
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil3.compose.AsyncImage
import com.sesac.common.R
import com.sesac.common.component.CommonFilterTabs
import com.sesac.common.ui.theme.Error
import com.sesac.common.ui.theme.OnPrimaryContainer
import com.sesac.common.ui.theme.Primary
import com.sesac.common.ui.theme.Surface
import com.sesac.common.ui.theme.TextDisabled
import com.sesac.common.ui.theme.TextPrimary
import com.sesac.common.ui.theme.TextSecondary
import com.sesac.common.ui.theme.avatarSize
import com.sesac.common.ui.theme.cardImageSize
import com.sesac.common.ui.theme.paddingLarge
import com.sesac.common.ui.theme.paddingMedium
import com.sesac.common.ui.theme.paddingMicro
import com.sesac.common.ui.theme.paddingSmall
import com.sesac.common.ui.theme.postImageHeight
import com.sesac.common.ui.theme.primaryContainer
import com.sesac.common.ui.theme.shapeCard
import com.sesac.common.ui.theme.shapeImage
import com.sesac.common.ui.theme.star


// 1. 데이터 모델 (React 코드 기반)
data class WalkPath(
    val id: Int,
    val name: String,
    val location: String,
    val distance: String,
    val image: String,
    val rating: Double,
    val uploader: String?,
    val time: String?,
    val likes: Int?,
    val distanceFromMe: String?,
    val color: String?
)

data class CommunityPost(
    val id: Int,
    val author: String,
    val authorImage: String,
    val timeAgo: String,
    val content: String,
    val image: String?,
    val likes: Int,
    val comments: Int,
    val isLiked: Boolean,
    val isFavorite: Boolean,
    val category: String
)

// 2. UI 상태
enum class FavoriteTab { WALKPATHS, COMMUNITY }

data class FavoritesUiState(
    val activeTab: FavoriteTab = FavoriteTab.WALKPATHS,
    val favoriteWalkPaths: List<WalkPath> = emptyList(),
    val favoritePosts: List<CommunityPost> = emptyList()
)

@Composable
fun MypageFavoriteScreen(
    onNavigateToPost: (Int) -> Unit = {},
    onNavigateToPath: (Int) -> Unit = {},
) {
    // 임시 목업데이터
    var activeTab by remember { mutableStateOf(FavoriteTab.WALKPATHS) }
    val uiState = FavoritesUiState(
        activeTab = FavoriteTab.WALKPATHS,
        favoriteWalkPaths = listOf(
            WalkPath(
                id = 1,
                name = "한강 산책로",
                location = "서울 영등포구",
                distance = "2.5km",
                image = "https://example.com/hanriver.jpg",
                rating = 4.8,
                uploader = "산책왕",
                time = "30분 코스",
                likes = 120,
                distanceFromMe = "1.2km",
                color = "#AEEEEE"
            ),
            WalkPath(
                id = 2,
                name = "남산 둘레길",
                location = "서울 중구",
                distance = "3.2km",
                image = "https://example.com/namsan.jpg",
                rating = 4.9,
                uploader = "도시러버",
                time = "40분 코스",
                likes = 98,
                distanceFromMe = "2.0km",
                color = "#B0E0E6"
            )
        ),
        favoritePosts = listOf(
            CommunityPost(
                id = 1,
                author = "멍멍이집사",
                authorImage = "https://example.com/user1.jpg",
                timeAgo = "1시간 전",
                content = "내일 같이 한강 산책하실 분~?",
                image = "https://example.com/post1.jpg",
                likes = 24,
                comments = 3,
                isLiked = true,
                isFavorite = true,
                category = "모임"
            ),
            CommunityPost(
                id = 2,
                author = "강아지사랑",
                authorImage = "https://example.com/user2.jpg",
                timeAgo = "3시간 전",
                content = "남산 코스 너무 좋았어요!",
                image = null,
                likes = 12,
                comments = 1,
                isLiked = false,
                isFavorite = true,
                category = "후기"
            )
        )
    )
    
    // ✅ 임시 목업 데이터
    val uiStatetab = FavoritesUiState(
        activeTab = activeTab,
        favoriteWalkPaths = listOf(/* ... */),
        favoritePosts = listOf(/* ... */)
    )
    Column(
        modifier = Modifier
            .fillMaxSize()
    ) {
        // --- 1. 탭 버튼 ---
        /** TabBar */
        FavoritesTabBar(
            activeTab = uiStatetab.activeTab,
            onTabSelected = {selectedTab ->
                activeTab = selectedTab
            }
        )
        // --- 2. 콘텐츠 리스트 ---
        when (uiStatetab.activeTab) {
            FavoriteTab.WALKPATHS -> {
                FavoriteListContainer(
                    title = "즐겨찾는 산책로",
                    items = uiState.favoriteWalkPaths,
                    emptyStateMessage = "즐겨찾는 산책로가 없습니다",
                    emptyStateSubMessage = "산책로 페이지에서 ⭐를 눌러 추가해보세요",
                    itemContent = { path ->
                        FavoriteWalkPathCard(
                            path = path,
                            onPathClick = { onNavigateToPath(path.id) },
                            onRemoveClick = {  }
                        )
                    }
                )
            }
            FavoriteTab.COMMUNITY -> {
                FavoriteListContainer(
                    title = "즐겨찾는 게시글",
                    items = uiState.favoritePosts,
                    emptyStateMessage = "즐겨찾는 게시글이 없습니다",
                    emptyStateSubMessage = "커뮤니티에서 ♥를 눌러 추가해보세요",
                    itemContent = { post ->
                        FavoriteCommunityPostCard(
                            post = post,
                            onPostClick = { onNavigateToPost(post.id) },
                            onRemoveClick = { }
                        )
                    }
                )
            }
        }
    }
}

@Composable
fun FavoritesTabBar(
    activeTab: FavoriteTab,
    onTabSelected: (FavoriteTab) -> Unit,
    modifier: Modifier = Modifier
) {
    val tabLabels = listOf("산책로", "커뮤니티")
    val tabEnums = listOf(FavoriteTab.WALKPATHS, FavoriteTab.COMMUNITY)
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .background(Color.White)
            .padding(horizontal = 16.dp, vertical = 12.dp),
        contentAlignment = Alignment.Center
    ) {
        CommonFilterTabs(
            modifier = modifier,
            filterOptions = tabLabels,
            selectedFilter = tabLabels[tabEnums.indexOf(activeTab)],
            onFilterSelected = { selected ->
                val index = tabLabels.indexOf(selected)
                if (index >= 0) onTabSelected(tabEnums[index])
            },
            fiterIcons = listOf(Icons.TwoTone.Nature, Icons.Default.Chat),
            horizontalArrangement = Arrangement.spacedBy(12.dp, Alignment.CenterHorizontally)
        )
    }
}

/**
 * [리팩토링]
 * 산책로 탭과 커뮤니티 탭의 공통 레이아웃
 */
@Composable
fun <T> FavoriteListContainer(
    title: String,
    items: List<T>,
    emptyStateMessage: String,
    emptyStateSubMessage: String,
    itemContent: @Composable (T) -> Unit
) {
    Column(modifier = Modifier.padding(horizontal = paddingLarge)) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = paddingMedium),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = title,
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                color = TextPrimary
            )
            Surface(
                shape = CircleShape,
                color = primaryContainer
            ) {
                Text(
                    text = "${items.size}개",
                    color = MaterialTheme.colorScheme.onPrimaryContainer,
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(horizontal = 12.dp, vertical = 4.dp)
                )
            }
        }

        if (items.isEmpty()) {
            EmptyState(
                message = emptyStateMessage,
                subMessage = emptyStateSubMessage
            )
        } else {
            LazyColumn(
                verticalArrangement = Arrangement.spacedBy(paddingSmall)
            ) {
                items(items.size) { index ->
                    itemContent(items[index])
                }
            }
        }
    }
}

@Composable
fun EmptyState(message: String, subMessage: String) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 48.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Icon(
            Icons.Default.Star,
            contentDescription = "Empty",
            modifier = Modifier.size(64.dp),
            tint = TextDisabled
        )
        Spacer(modifier = Modifier.height(paddingMedium))
        Text(
            text = message,
            color = TextDisabled,
            fontWeight = FontWeight.Bold,
            fontSize = 16.sp
        )
        Spacer(modifier = Modifier.height(paddingSmall))
        Text(
            text = subMessage,
            color = TextDisabled,
            fontSize = 14.sp,
            textAlign = TextAlign.Center
        )
    }
}

// 여기 카드 부분은 굳이 없어도 되지만 UI 예시 보여주기위해 붙여넣음

@Composable
fun FavoriteWalkPathCard(
    path: WalkPath,
    onPathClick: () -> Unit,
    onRemoveClick: () -> Unit
) {
    Card(
        onClick = onPathClick,
        shape = shapeCard,
        colors = CardDefaults.cardColors(containerColor = Surface),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(paddingMedium),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier.size(cardImageSize),
                contentAlignment = Alignment.TopEnd
            ) {
                AsyncImage(
                    model = path.image,
                    contentDescription = path.name,
                    modifier = Modifier
                        .fillMaxSize()
                        .clip(shapeImage),
                    contentScale = ContentScale.Crop,
                    placeholder = painterResource(id = R.drawable.placeholder) // ⚠️ placeholder
                )
                Box(
                    modifier = Modifier
                        .padding(paddingMicro)
                        .size(24.dp)
                        .background(Surface.copy(alpha = 0.9f), CircleShape),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        Icons.Default.Star,
                        contentDescription = "Favorite",
                        tint = star,
                        modifier = Modifier.size(16.dp)
                    )
                }
            }
            Spacer(modifier = Modifier.width(paddingMedium))
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = path.name,
                    fontWeight = FontWeight.Bold,
                    color = TextPrimary,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
                Spacer(modifier = Modifier.height(paddingMicro))
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        Icons.Default.LocationOn,
                        contentDescription = "Location",
                        tint = TextDisabled,
                        modifier = Modifier.size(16.dp)
                    )
                    Spacer(modifier = Modifier.width(paddingMicro))
                    Text(
                        text = path.location,
                        fontSize = 12.sp,
                        color = TextSecondary,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                }
                Spacer(modifier = Modifier.height(paddingSmall))
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(
                        path.distance,
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold,
                        color = Primary
                    )
                    Spacer(modifier = Modifier.width(paddingSmall))
                    Text(
                        "★ ${path.rating}",
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold,
                        color = star
                    )
                }
            }
            IconButton(
                onClick = onRemoveClick,
                modifier = Modifier.align(Alignment.Top)
            ) {
                Icon(
                    Icons.Default.Delete,
                    contentDescription = "즐겨찾기에서 제거",
                    tint = Error
                )
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FavoriteCommunityPostCard(
    post: CommunityPost,
    onPostClick: () -> Unit,
    onRemoveClick: () -> Unit
) {
    Card(
        onClick = onPostClick,
        shape = shapeCard,
        colors = CardDefaults.cardColors(containerColor = Surface),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(modifier = Modifier.padding(paddingMedium)) {
            // --- 1. 헤더 (작성자, 삭제 버튼) ---
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                AsyncImage(
                    model = post.authorImage,
                    contentDescription = post.author,
                    modifier = Modifier
                        .size(avatarSize)
                        .clip(CircleShape),
                    contentScale = ContentScale.Crop,
                    placeholder = painterResource(id = R.drawable.placeholder)
                )
                Spacer(modifier = Modifier.width(paddingSmall))
                Column(modifier = Modifier.weight(1f)) {
                    Text(post.author, fontWeight = FontWeight.Bold, color = TextPrimary)
                    Text(post.timeAgo, fontSize = 12.sp, color = TextDisabled)
                }
                IconButton(onClick = onRemoveClick) {
                    Icon(
                        Icons.Default.Delete,
                        contentDescription = "즐겨찾기에서 제거",
                        tint = Error
                    )
                }
            }
            Spacer(modifier = Modifier.height(paddingSmall))

            // --- 2. 본문 ---
            Text(
                text = post.content,
                color = TextSecondary,
                maxLines = 2,
                overflow = TextOverflow.Ellipsis
            )

            // --- 3. 이미지 ---
            if (post.image != null) {
                AsyncImage(
                    model = post.image,
                    contentDescription = "Post image",
                    modifier = Modifier
                        .padding(top = paddingSmall)
                        .fillMaxWidth()
                        .height(postImageHeight)
                        .clip(shapeImage),
                    contentScale = ContentScale.Crop,
                    placeholder = painterResource(id = R.drawable.placeholder)
                )
            }
            Spacer(modifier = Modifier.height(paddingMedium))

            // --- 4. 푸터 (카테고리, 좋아요/댓글) ---
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Surface(
                    shape = CircleShape,
                    color = primaryContainer
                ) {
                    Text(
                        text = post.category,
                        color = OnPrimaryContainer,
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Medium,
                        modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp)
                    )
                }

                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(paddingMedium)
                ) {
                    // --- PostStat을 바로 여기서 구현 ---
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            imageVector = Icons.Default.Favorite,
                            contentDescription = null,
                            tint = Primary,
                            modifier = Modifier.size(16.dp)
                        )
                        Spacer(modifier = Modifier.width(paddingMicro))
                        Text(post.likes.toString(), fontSize = 14.sp, color = Primary)
                    }
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.Chat,
                            contentDescription = null,
                            tint = TextSecondary,
                            modifier = Modifier.size(16.dp)
                        )
                        Spacer(modifier = Modifier.width(paddingMicro))
                        Text(post.comments.toString(), fontSize = 14.sp, color = TextSecondary)
                    }
                }
            }
        }
    }
}


@Preview(showBackground = true, showSystemUi = true)
@Composable
fun MypageFavoriteScreenPreview() {
    MypageFavoriteScreen(
        onNavigateToPost = {},
        onNavigateToPath = {}
    )

}