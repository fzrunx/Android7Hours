package com.sesac.home.presentation

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil3.compose.AsyncImage
import coil3.request.ImageRequest
import coil3.request.crossfade
import com.sesac.common.R // ⚠️ 본인의 R 패키지 경로로 수정하세요.

// --- 1. 테마 및 상수 ---
object AppTheme {
    val background = Color(0xFFF9FAFB)
    val surface = Color.White
    val header = Color(0xFFDBE8CC)
    val accent = Color(0xFF8B5CF6)
    val textPrimary = Color(0xFF1F2937)
    val textSecondary = Color(0xFF6B7280)
    val textOnPrimary = Color.White
    val textHint = Color(0xFF9CA3AF)
    val icon = Color(0xFF4B5563)
    val border = Color(0xFFE5E7EB)
    val indicator = Color(0xFFD1D5DB)

    val paddingLarge = 16.dp
    val paddingMedium = 12.dp
    val paddingSmall = 8.dp
    val paddingMicro = 4.dp

    val headerHeight = 80.dp
    val bannerHeight = 192.dp
    val cardHeight = 160.dp
    val cardWidth = 256.dp

    val cardShape = RoundedCornerShape(12.dp)
    val buttonShape = RoundedCornerShape(8.dp)
}

// --- 2. 데이터 모델 ---
sealed class HomeCardData(
    open val id: Int,
    open val name: String,
    open val image: String,
)

data class BannerData(
    val id: Int,
    val image: String,
    val title: String,
    val subtitle: String
)

data class WalkPath(
    override val id: Int,
    override val name: String,
    override val image: String,
    val distance: String,
    val time: String,
    val difficulty: String,
) : HomeCardData(id, name, image)

data class TravelDestination(
    override val id: Int,
    override val name: String,
    override val image: String,
    val location: String,
    val description: String,
) : HomeCardData(id, name, image)

data class DogCafe(
    override val id: Int,
    override val name: String,
    override val image: String,
    val location: String,
    val description: String,
) : HomeCardData(id, name, image)

// --- 3. 데이터 소스 ---
object DataSource {
    val banners = listOf(
        BannerData(1, "drawable_banner_image", "반려견과 함께하는 특별한 시간", "7Hours와 함께 산책을 시작하세요"),
        BannerData(2, "https://images.unsplash.com/photo-1629130646965-e86223170abc?...", "새로운 산책로를 발견하세요", "매주 업데이트되는 추천 코스"),
        BannerData(3, "https://images.unsplash.com/photo-1648976286170-fcc69115697b?...", "행복한 산책 기록", "소중한 추억을 남겨보세요")
    )

    val walkPaths = listOf(
        WalkPath(1, "한강공원 산책로", "https://images.unsplash.com/photo-1597475495184-7038d1cb7db2?...", "3.2km", "40분", "중급"),
        WalkPath(2, "서울숲 산책로", "https://images.unsplash.com/photo-1753375676074-6a660c5ac264?...", "2.5km", "30분", "초급"),
        WalkPath(3, "올림픽공원 둘레길", "https://images.unsplash.com/photo-1597475495184-7038d1cb7db2?...", "4.1km", "50분", "고급"),
    )

    val travelDestinations = listOf(
        TravelDestination(1, "제주 애월 해안도로", "https://images.unsplash.com/photo-1649261887227-38725ab9bdac?...", "제주", "바다를 보며 걷는 산책"),
        TravelDestination(2, "강원도 속초", "https://images.unsplash.com/photo-1649934515294-19726be7e02d?...", "강원도", "설악산과 바다를 함께"),
    )

    val dogCafes = listOf(
        DogCafe(1, "멍멍카페", "https://images.unsplash.com/photo-1730402739842-fbfe757d417e?...", "강남구", "넓은 정원이 있는 애견카페"),
        DogCafe(2, "포도카페", "https://images.unsplash.com/photo-1739723745132-97df9db49db2?...", "홍대", "강아지 놀이터가 있는 카페"),
    )

    val communityImage = "https://images.unsplash.com/photo-1761055923632-3f9687160892?..."
}

// --- 4. HomePage ---
@Composable
fun HomePage(
    onNavigateToWalkPath: () -> Unit,
    onNavigateToCommunity: () -> Unit
) {
    var isSearchOpen by remember { mutableStateOf(false) }
    var searchText by remember { mutableStateOf("") }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(AppTheme.background)
    ) {
        HomePageHeader(onSearchToggle = { isSearchOpen = !isSearchOpen })

        AnimatedVisibility(visible = isSearchOpen) {
            SearchBarContent(searchText = searchText, onSearchTextChanged = { searchText = it })
        }

        LazyColumn(modifier = Modifier.fillMaxSize()) {
            item { BannerSection(banners = DataSource.banners, modifier = Modifier.padding(top = AppTheme.paddingMedium)) }
            item { ContentSection(title = "산책로 추천", items = DataSource.walkPaths, onCardClick = { onNavigateToWalkPath() }) }
            item { ContentSection(title = "여행지 추천", items = DataSource.travelDestinations, onCardClick = {}) }
            item { ContentSection(title = "애견 카페", items = DataSource.dogCafes, onCardClick = {}) }
            item { CommunityCard(image = DataSource.communityImage, onClick = onNavigateToCommunity, modifier = Modifier.padding(horizontal = AppTheme.paddingLarge, vertical = AppTheme.paddingMedium)) }
            item { Spacer(modifier = Modifier.height(AppTheme.headerHeight)) }
        }
    }
}

// --- 5. Components ---
@Composable
fun HomePageHeader(onSearchToggle: () -> Unit) {
    val context = LocalContext.current
    Surface(color = AppTheme.header, shadowElevation = AppTheme.paddingSmall) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(AppTheme.headerHeight)
                .padding(horizontal = AppTheme.paddingLarge),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            AsyncImage(
                modifier = Modifier
                    .size(40.dp),
                model = ImageRequest.Builder(context)
                    .data(R.drawable.image7hours)
                    .crossfade(true)
                    .scale(coil3.size.Scale.FILL)
                    .build(),
                contentDescription = "7hours",
                contentScale = ContentScale.Crop,
            )

            Text(text = "7Hours", color = AppTheme.textPrimary, fontSize = 20.sp, fontWeight = FontWeight.Bold)
            IconButton(onClick = onSearchToggle) {
                Icon(imageVector = Icons.Default.Search, contentDescription = "Search", tint = AppTheme.icon, modifier = Modifier.size(24.dp))
            }
        }
    }
}

@Composable
fun SearchBarContent(searchText: String, onSearchTextChanged: (String) -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(AppTheme.header)
            .padding(start = AppTheme.paddingLarge, end = AppTheme.paddingLarge, bottom = AppTheme.paddingMedium)
    ) {
        OutlinedTextField(
            value = searchText,
            onValueChange = onSearchTextChanged,
            placeholder = { Text("Hinted search text") },
            leadingIcon = { Icon(imageVector = Icons.Default.Search, contentDescription = null, tint = AppTheme.textHint) },
            modifier = Modifier.fillMaxWidth(),
            shape = AppTheme.buttonShape,
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = AppTheme.accent,
                unfocusedBorderColor = AppTheme.border,
                focusedContainerColor = AppTheme.surface,
                unfocusedContainerColor = AppTheme.surface,
                focusedLeadingIconColor = AppTheme.accent,
                unfocusedLeadingIconColor = AppTheme.textHint
            ),
            singleLine = true
        )
    }
}

// BannerSection + BannerCardContent
@Composable
fun BannerSection(banners: List<BannerData>, modifier: Modifier = Modifier) {
    val pagerState = rememberPagerState(pageCount = { banners.size })
    Column(modifier = modifier.fillMaxWidth(), horizontalAlignment = Alignment.CenterHorizontally) {
        HorizontalPager(state = pagerState, contentPadding = PaddingValues(horizontal = AppTheme.paddingLarge), pageSpacing = AppTheme.paddingMedium) { page ->
            val banner = banners[page]
            Card(modifier = Modifier.fillMaxWidth().height(AppTheme.bannerHeight), shape = AppTheme.cardShape) {
                BannerCardContent(banner = banner)
            }
        }
        Row(modifier = Modifier.fillMaxWidth().padding(top = AppTheme.paddingMedium), horizontalArrangement = Arrangement.Center) {
            repeat(banners.size) { index -> PageIndicator(isSelected = index == pagerState.currentPage) }
        }
    }
}

@Composable
fun BannerCardContent(banner: BannerData) {
    val context = LocalContext.current
    Box(modifier = Modifier.fillMaxSize()) {
        if (banner.image == "drawable_banner_image") {
            Image(
                painter = painterResource(id = R.drawable.icons8_dog_50),
                contentDescription = banner.title,
                modifier = Modifier.fillMaxSize(),
                contentScale = ContentScale.Crop
            )
        } else {
            AsyncImage(
                model = ImageRequest.Builder(context)
                    .data(banner.image)
                    .crossfade(true)
//                    .size(width = 800, height = 400) // 화면에 맞게 축소
                    .scale(coil3.size.Scale.FILL)
                    .build(),
                contentDescription = banner.title,
                modifier = Modifier.fillMaxSize(),
                contentScale = ContentScale.Crop,
            )
        }

        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    Brush.verticalGradient(
                        colors = listOf(Color.Transparent, Color.Black.copy(alpha = 0.7f)),
                        startY = 300f
                    )
                )
        )

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(AppTheme.paddingLarge),
            verticalArrangement = Arrangement.Bottom
        ) {
            Text(
                text = banner.title,
                color = AppTheme.textOnPrimary,
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold
            )
            Spacer(modifier = Modifier.height(AppTheme.paddingMicro))
            Text(
                text = banner.subtitle,
                color = AppTheme.textOnPrimary.copy(alpha = 0.9f),
                fontSize = 14.sp
            )
        }
    }
}

// PageIndicator
@Composable
fun PageIndicator(isSelected: Boolean) {
    val width = if (isSelected) 24.dp else 8.dp
    Box(
        modifier = Modifier
            .padding(horizontal = AppTheme.paddingMicro)
            .height(8.dp)
            .width(width)
            .clip(CircleShape)
            .background(if (isSelected) AppTheme.accent else AppTheme.indicator)
    )
}

// ContentSection + ContentCard
@Composable
fun <T : HomeCardData> ContentSection(title: String, items: List<T>, onCardClick: (T) -> Unit, modifier: Modifier = Modifier) {
    Column(modifier = modifier.fillMaxWidth().padding(vertical = AppTheme.paddingMedium)) {
        Text(
            text = title,
            color = AppTheme.textPrimary,
            fontSize = 18.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.fillMaxWidth().padding(horizontal = AppTheme.paddingLarge).padding(bottom = AppTheme.paddingMedium)
        )
        LazyRow(contentPadding = PaddingValues(horizontal = AppTheme.paddingLarge), horizontalArrangement = Arrangement.spacedBy(AppTheme.paddingMedium)) {
            items(items) { item -> ContentCard(data = item, onClick = { onCardClick(item) }, modifier = Modifier.width(AppTheme.cardWidth)) }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ContentCard(data: HomeCardData, onClick: () -> Unit, modifier: Modifier = Modifier) {
    val context = LocalContext.current
    Card(onClick = onClick, modifier = modifier.height(AppTheme.cardHeight), shape = AppTheme.cardShape) {
        Box(modifier = Modifier.fillMaxSize()) {
            AsyncImage(
                model = ImageRequest.Builder(context)
                    .data(data.image)
                    .crossfade(true)
//                    .size(width = 400, height = 400)
                    .scale(coil3.size.Scale.FILL)
                    .build(),
                contentDescription = data.name,
                modifier = Modifier.fillMaxSize(),
                contentScale = ContentScale.Crop,
            )

            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(
                        Brush.verticalGradient(
                            colors = listOf(Color.Transparent, Color.Black.copy(alpha = 0.7f)),
                            startY = 200f
                        )
                    )
            )

            Column(
                modifier = Modifier.fillMaxSize().padding(AppTheme.paddingMedium),
                verticalArrangement = Arrangement.Bottom
            ) {
                Text(
                    text = data.name,
                    color = AppTheme.textOnPrimary,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
            }
        }
    }
}

// CommunityCard
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CommunityCard(image: String, onClick: () -> Unit, modifier: Modifier = Modifier) {
    val context = LocalContext.current
    Column(modifier = modifier.fillMaxWidth()) {
        Text(
            text = "커뮤니티",
            color = AppTheme.textPrimary,
            fontSize = 18.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.fillMaxWidth().padding(bottom = AppTheme.paddingMedium)
        )
        Card(onClick = onClick, modifier = Modifier.fillMaxWidth().height(AppTheme.bannerHeight), shape = AppTheme.cardShape) {
            Box(modifier = Modifier.fillMaxSize()) {
                AsyncImage(
                    model = ImageRequest.Builder(context)
                        .data(image)
                        .crossfade(true)
//                        .size(width = 800, height = 400)
                        .scale(coil3.size.Scale.FILL)
                        .build(),
                    contentDescription = "Community",
                    modifier = Modifier.fillMaxSize(),
                    contentScale = ContentScale.Crop,
                )

                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(
                            Brush.verticalGradient(
                                colors = listOf(Color.Transparent, Color.Black.copy(alpha = 0.6f))
                            )
                        )
                )
                Text(
                    text = "반려견 친구들과 소통해요",
                    color = AppTheme.textOnPrimary,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Medium,
                    modifier = Modifier.align(Alignment.BottomStart).padding(AppTheme.paddingLarge)
                )
            }
        }
    }
}

// --- 6. Preview ---
@Preview(showBackground = true)
@Composable
fun HomePagePreview() {
    HomePage(onNavigateToWalkPath = {}, onNavigateToCommunity = {})
}
