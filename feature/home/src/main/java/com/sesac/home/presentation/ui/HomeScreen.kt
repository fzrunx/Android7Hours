package com.sesac.home.presentation.ui

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
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil3.compose.AsyncImage
import coil3.request.ImageRequest
import coil3.request.crossfade
import coil3.size.Scale
import com.sesac.common.R as cR
import com.sesac.common.component.CommonLazyRow
import com.sesac.common.ui.theme.Android7HoursTheme
import com.sesac.common.ui.theme.paddingLarge
import com.sesac.common.ui.theme.paddingMedium
import com.sesac.common.ui.theme.paddingMicro
import com.sesac.home.presentation.component.BannerSection
import com.sesac.home.presentation.component.ContentCard
import com.sesac.home.presentation.model.BannerData
import com.sesac.home.presentation.model.DogCafe
import com.sesac.home.presentation.model.TravelDestination
import com.sesac.home.presentation.model.WalkPath

// --- 1. 상수 ---
private val headerHeight = 80.dp
private val bannerHeight = 192.dp
private val cardHeight = 160.dp
private val cardWidth = 256.dp
private val cardShape = RoundedCornerShape(12.dp)
private val buttonShape = RoundedCornerShape(8.dp)

// --- 3. 데이터 소스 ---
object DataSource {
    val banners = listOf(
        BannerData(1, "drawable_banner_image", "반려견과 함께하는 특별한 시간", "7Hours와 함께 산책을 시작하세요"),
        BannerData(2, "https://images.unsplash.com/photo-1629130646965-e86223170abc?...", "새로운 산책로를 발견하세요", "매주 업데이트되는 추천 코스"),
        BannerData(3, "https://images.unsplash.com/photo-1648976286170-fcc69115697b?...", "행복한 산책 기록", "소중한 추억을 남겨보세요")
    )

    val walkPaths = listOf(
        WalkPath(
            1,
            "한강공원 산책로",
            "https://images.unsplash.com/photo-1597475495184-7038d1cb7db2?...",
            "3.2km",
            "40분",
            "중급"
        ),
        WalkPath(2, "서울숲 산책로", "https://images.unsplash.com/photo-1753375676074-6a660c5ac264?...", "2.5km", "30분", "초급"),
        WalkPath(3, "올림픽공원 둘레길", "https://images.unsplash.com/photo-1597475495184-7038d1cb7db2?...", "4.1km", "50분", "고급"),
    )

    val travelDestinations = listOf(
        TravelDestination(
            1,
            "제주 애월 해안도로",
            "https://images.unsplash.com/photo-1649261887227-38725ab9bdac?...",
            "제주",
            "바다를 보며 걷는 산책"
        ),
        TravelDestination(2, "강원도 속초", "https://images.unsplash.com/photo-1649934515294-19726be7e02d?...", "강원도", "설악산과 바다를 함께"),
    )

    val dogCafes = listOf(
        DogCafe(
            1,
            "멍멍카페",
            "https://images.unsplash.com/photo-1730402739842-fbfe757d417e?...",
            "강남구",
            "넓은 정원이 있는 애견카페"
        ),
        DogCafe(2, "포도카페", "https://images.unsplash.com/photo-1739723745132-97df9db49db2?...", "홍대", "강아지 놀이터가 있는 카페"),
    )

    val communityImage = "https://images.unsplash.com/photo-1761055923632-3f9687160892?..."
}

// --- 4. HomePage ---
@Composable
fun HomeScreen(
    modifier: Modifier = Modifier,
    onNavigateToWalkPath: () -> Unit,
    onNavigateToCommunity: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
    ) {
        LazyColumn(modifier = Modifier.fillMaxSize()) {
            item { BannerSection(banners = DataSource.banners, modifier = Modifier.padding(top = paddingMedium)) }
            item { CommonLazyRow(
                title = "산책로 추천",
                items = DataSource.walkPaths,
            ) { item -> ContentCard(item, onNavigateToWalkPath, Modifier.width(cardWidth)) }
            }
            item { CommonLazyRow(
                title = "여행지 추천",
                items = DataSource.travelDestinations,
            ) { item -> ContentCard(item, {}, Modifier.width(cardWidth)) } }
            item { CommonLazyRow(
                title = "애견 카페",
                items = DataSource.dogCafes,
            ) { item -> ContentCard(item, {}, Modifier.width(cardWidth)) } }
            item { CommunityCard(image = DataSource.communityImage, onClick = onNavigateToCommunity, modifier = Modifier.padding(horizontal = paddingLarge, vertical = paddingMedium)) }
            item { Spacer(modifier = Modifier.height(headerHeight)) }
        }
    }
}

// CommunityCard
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CommunityCard(
    modifier: Modifier = Modifier,
    image: String,
    onClick: () -> Unit
) {
    val context = LocalContext.current
    Column(modifier = modifier.fillMaxWidth()) {
        Text(
            text = stringResource(cR.string.community),
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.fillMaxWidth().padding(bottom = paddingMedium)
        )
        Card(onClick = onClick, modifier = Modifier.fillMaxWidth().height(bannerHeight), shape = cardShape) {
            Box(modifier = Modifier.fillMaxSize()) {
                AsyncImage(
                    model = ImageRequest.Builder(context)
                        .data(image)
                        .crossfade(true)
                        .scale(Scale.FILL)
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
                    text = stringResource(cR.string.home_community_banner_text),
                    color = MaterialTheme.colorScheme.onPrimary,
                    modifier = Modifier.align(Alignment.BottomStart).padding(paddingLarge)
                )
            }
        }
    }
}

// --- 6. Preview ---
@Preview(showBackground = true)
@Composable
fun HomePagePreview() {
    Android7HoursTheme {
        HomeScreen(onNavigateToWalkPath = {}, onNavigateToCommunity = {})
    }
}
