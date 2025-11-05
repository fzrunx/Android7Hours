package com.sesac.home.presentation.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil3.compose.AsyncImage
import coil3.request.ImageRequest
import coil3.request.crossfade
import coil3.size.Scale
import com.sesac.common.R as cR
import com.sesac.common.component.CommonLazyRow
import com.sesac.common.ui.theme.Android7HoursTheme
import com.sesac.common.ui.theme.bannerHeight
import com.sesac.common.ui.theme.cardWidth
import com.sesac.common.ui.theme.paddingLarge
import com.sesac.common.ui.theme.paddingMedium
import com.sesac.domain.model.BannerData
import com.sesac.domain.model.HomeCardData
import com.sesac.home.presentation.HomeViewModel


// --- 4. HomePage ---
@Composable
fun HomeScreen(
    modifier: Modifier = Modifier,
    viewModel: HomeViewModel = hiltViewModel(),
    onNavigateToWalkPath: () -> Unit = {},
    onNavigateToCommunity: () -> Unit = {},
) {
    val banners by viewModel.bannerList.collectAsStateWithLifecycle()
    val dogCafeList by viewModel.dogCafeList.collectAsStateWithLifecycle()
    val travelDestinationList by viewModel.travelDestinationList.collectAsStateWithLifecycle()
    val walkPathList by viewModel.walkPathList.collectAsStateWithLifecycle()
    val communityList by viewModel.communityList.collectAsStateWithLifecycle()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
    ) {
        LazyColumn(modifier = Modifier.fillMaxSize()) {
            item { BannerSectionView(banners = banners, modifier = Modifier.padding(top = paddingMedium)) }
            item {
                CommonLazyRow(
                    title = "산책로 추천",
                    items = walkPathList,
                ) { item ->
                    ContentCardView(
                        data = item,
                        onClick = onNavigateToWalkPath,
                        modifier = Modifier.width(cardWidth)
                    )
                }
            }
            item { CommonLazyRow(
                title = "여행지 추천",
                items = travelDestinationList,
            ) { item -> ContentCardView(item, {}, Modifier.width(cardWidth)) } }
            item { CommonLazyRow(
                title = "애견 카페",
                items = dogCafeList,
            ) { item -> ContentCardView(item, {}, Modifier.width(cardWidth)) } }
            item { CommunityCard(
                image = communityList.component1()?.imageResList?.component1() ?: cR.drawable.icons8_dog_50,
                onClick = onNavigateToCommunity,
                modifier = Modifier.padding(horizontal = paddingLarge, vertical = paddingMedium))
            }
        }
    }
}

// CommunityCard
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CommunityCard(
    modifier: Modifier = Modifier,
    image: Any,
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
        Card(
            onClick = onClick,
            modifier = Modifier.fillMaxWidth().height(bannerHeight),
        ) {
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
