package com.sesac.home.presentation.ui

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.PagerState
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
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
import androidx.compose.ui.unit.dp
import coil3.compose.AsyncImage
import coil3.request.ImageRequest
import coil3.request.crossfade
import coil3.size.Scale
import com.sesac.common.R
import com.sesac.common.ui.theme.bannerHeight
import com.sesac.common.ui.theme.buttonRound
import com.sesac.common.ui.theme.paddingLarge
import com.sesac.common.ui.theme.paddingMedium
import com.sesac.common.ui.theme.paddingMicro
import com.sesac.domain.model.BannerData

@Composable
fun BannerSectionView(
    banners: List<BannerData?>,
    modifier: Modifier = Modifier,
    pagerState: PagerState,
    ) {
    Column(modifier = modifier.fillMaxWidth(), horizontalAlignment = Alignment.CenterHorizontally) {
        HorizontalPager(
            state = pagerState,
            contentPadding = PaddingValues(horizontal = paddingLarge),
            pageSpacing = paddingMedium
        ) { page ->
            val banner = banners[page]
            Card(
                modifier = Modifier.fillMaxWidth().height(bannerHeight),
                shape = RoundedCornerShape(buttonRound)
            ) {
                BannerCardContent(banner = banner)
            }
        }
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = paddingMedium),
            horizontalArrangement = Arrangement.Center
        ) {
            repeat(banners.size) { index ->
                PageIndicator(isSelected = index == pagerState.currentPage)
            }
        }
    }
}

// PageIndicator
@Composable
fun PageIndicator(isSelected: Boolean) {
    val width = if (isSelected) 24.dp else 8.dp
    Box(
        modifier = Modifier
            .padding(horizontal = paddingMicro)
            .height(8.dp)
            .width(width)
            .clip(CircleShape)
            .background(if (isSelected) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.surfaceVariant)
    )
}


@Composable
fun BannerCardContent(banner: BannerData?) {
    val context = LocalContext.current

    Box(modifier = Modifier.fillMaxSize()) {
        if (banner?.image == "drawable_banner_image") {
            Image(
                painter = painterResource(id = R.drawable.icons8_dog_50),
                contentDescription = banner.title,
                modifier = Modifier.fillMaxSize(),
                contentScale = ContentScale.FillWidth
            )
        } else {
            AsyncImage(
                model = ImageRequest.Builder(context)
                    .data(banner?.image)
                    .crossfade(true)
                    .scale(Scale.FILL)
                    .build(),
                contentDescription = banner?.title,
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
                .padding(paddingLarge),
            verticalArrangement = Arrangement.Bottom
        ) {
            Text(
                text = banner?.title ?: "배너 없음",
                color = MaterialTheme.colorScheme.onPrimary,
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold
            )
            Spacer(modifier = Modifier.height(paddingMicro))
            Text(
                text = banner?.subtitle ?: "배너 없음",
                color = MaterialTheme.colorScheme.onPrimary.copy(alpha = 0.8f),
                style = MaterialTheme.typography.bodySmall,
                overflow = TextOverflow.Ellipsis
            )
        }
    }
}
