package com.sesac.home.presentation


import android.graphics.Paint
import android.graphics.drawable.Icon
import androidx.annotation.DrawableRes
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.scrollable
import androidx.compose.foundation.gestures.snapping.SnapPosition
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.input.TextFieldState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.LocalHospital
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Map
import androidx.compose.material.icons.outlined.AccountCircle
import androidx.compose.material.icons.outlined.LocalHospital
import androidx.compose.material.icons.outlined.LocationOn
import androidx.compose.material.icons.outlined.Map
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CardElevation
import androidx.compose.material3.ColorScheme
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.ListItem
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedIconToggleButton
import androidx.compose.material3.SearchBar
import androidx.compose.material3.SearchBarColors
import androidx.compose.material3.SearchBarDefaults
import androidx.compose.material3.SegmentedButton
import androidx.compose.material3.SegmentedButtonDefaults
import androidx.compose.material3.SegmentedButtonDefaults.Icon
import androidx.compose.material3.SingleChoiceSegmentedButtonRow
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldColors
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.carousel.HorizontalUncontainedCarousel
import androidx.compose.material3.carousel.rememberCarouselState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.layout.ModifierLocalBeyondBoundsLayout
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.semantics.isTraversalGroup
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.semantics.traversalIndex
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.sesac.common.component.CommonArticlePreviewList
import com.sesac.common.component.CommonArticlePreviewListItem
import com.sesac.common.component.CommonSearchBar
import com.sesac.common.component.CommonSegmentedButton
import com.sesac.home.R
import com.sesac.common.R as commonR

data class CarouselItem(
    val id: Int,
    @DrawableRes val imageResId: Int,
    val contentDescription: String,
)

data class SampleCommunityItem(
    val tile: String,
    val sumnail: Painter,
    val content: String,
    val icon: ImageVector,
    val author: String,
)



@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    textFieldState: TextFieldState,
    onSearch: (String) -> Unit,
    searchResults: List<String>,
    modifier: Modifier = Modifier,
) {

    var expanded by rememberSaveable { mutableStateOf(false) }
    var tabSelectedIndex = rememberSaveable { mutableIntStateOf(0) }

    val length = 20
    val space = dimensionResource(commonR.dimen.default_space)
    val imageWidth = dimensionResource(commonR.dimen.carousel_image_width)
    val imageHeight = dimensionResource(commonR.dimen.carousel_image_height)
    val textSpace = dimensionResource(commonR.dimen.text_top_bottom_space)
    val tabSpace = dimensionResource(commonR.dimen.tab_space)

    // 임시 data
    val tabOptions = listOf("산책로", "여행지", "병원")
    val unCheckedIcons =
        listOf(
            Icons.Outlined.LocationOn,
            Icons.Outlined.Map,
            Icons.Outlined.LocalHospital,
        )
    val checkedIcons =
        listOf(
            Icons.Filled.LocationOn,
            Icons.Filled.Map,
            Icons.Filled.LocalHospital,
        )
    val bannerImages = listOf<Int>(
        R.drawable.dog_sample_banner_1,
        R.drawable.dog_sample_banner_2,
        R.drawable.dog_sample_banner_3
    )
    val pathRecommendImages = remember {
        listOf(
            CarouselItem(0, R.drawable.dog_sample_banner_1, "smaple_banner1"),
            CarouselItem(1, R.drawable.dog_sample_banner_2, "smaple_banner2"),
            CarouselItem(2, R.drawable.dog_sample_banner_3, "smaple_banner3"),
        )
    }
    val tripRecommendImages = remember {
        listOf(
            CarouselItem(0, R.drawable.dog_sample_trip_1, "sample_trip_image_1"),
            CarouselItem(1, R.drawable.dog_sample_trip_2, "sample_trip_image_2"),
            CarouselItem(2, R.drawable.dog_sample_trip_3, "sample_trip_image_3"),
        )
    }
    val sampleCoummunityItems = listOf(
        CommonArticlePreviewListItem(title = "title1", sumnail = painterResource(R.drawable.dog_sample_banner_1), content = "배고파".repeat(30), icon = Icons.Outlined.AccountCircle, author = "Hong"),
        CommonArticlePreviewListItem(title = "title2", content = "졸려".repeat(30), icon = Icons.Filled.AccountCircle, author = "Gil"),
        CommonArticlePreviewListItem(title = "title2", sumnail = painterResource(R.drawable.dog_sample_banner_3), content = "lol".repeat(30), icon = Icons.Default.AccountCircle, author = "Dong"),
    )
    // ...

    val bannerPagerState = rememberPagerState(initialPage = 0, pageCount = { bannerImages.size })
    val homeScreenScrollState = rememberScrollState()
    val searchResultsScrollState = rememberScrollState()

    Box(modifier.fillMaxSize()) {
        Column(
            Modifier
                .fillMaxSize()
                .verticalScroll(homeScreenScrollState)
                .semantics { isTraversalGroup = true }
        ) {
            Spacer(Modifier.height(88.dp))

            HorizontalPager(
                state = bannerPagerState,
                contentPadding = PaddingValues(horizontal = space),
                snapPosition = SnapPosition.Center,
            ) { pageIndex ->
                Image(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = space)
                        .height(imageHeight * 2)
                        .clip(RoundedCornerShape(12.dp)),
                    painter = painterResource(bannerImages[pageIndex]),
                    contentDescription = "banner",
                    contentScale = ContentScale.FillWidth,
                )
            }

            CustomHomeCarousel(text = "산책로 추천", recommendImages =  pathRecommendImages,)
            CustomHomeCarousel(text = "여행지 추천", recommendImages = tripRecommendImages,)

            CommonSegmentedButton(
                tabOptions,
                tabSelectedIndex,
                checkedIcons,
                unCheckedIcons
            )

            CommonArticlePreviewList(items = sampleCoummunityItems)
        }

        CommonSearchBar(
            Alignment.TopCenter,
            TextFieldState(),
            onSearch,
            searchResults,
            searchResultsScrollState)

    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ColumnScope.CustomHomeCarousel(
    modifier: Modifier = Modifier,
    text: String?,
    recommendImages: List<CarouselItem>,
    textSpace: Dp = dimensionResource(commonR.dimen.text_top_bottom_space),
    space: Dp = dimensionResource(commonR.dimen.default_space),
    imageWidth: Dp = dimensionResource(commonR.dimen.carousel_image_width),
    imageHeight: Dp = dimensionResource(commonR.dimen.carousel_image_height),
) {
    Column {
        if(!text.isNullOrBlank()){
            Row {
                Text(
                    modifier = Modifier
                        .wrapContentSize(Alignment.CenterStart)
                        .weight(1f)
                        .padding(horizontal = textSpace, vertical = space),
                    text = text
                )

                Text(
                    modifier = Modifier
                        .wrapContentSize(Alignment.CenterEnd)
                        .weight(1f)
                        .padding(horizontal = textSpace, vertical = space),
                    color = Color(0xfff08130),
                    text = "전체보기"
                )
            }
        }

        HorizontalUncontainedCarousel(
            state = rememberCarouselState { recommendImages.count() },
            modifier = modifier
                .align(Alignment.Start)
                .wrapContentWidth()
                .padding(bottom = 0.dp),
            itemWidth = imageWidth,
            itemSpacing = space,
            contentPadding = PaddingValues(horizontal = space)
        ) { i ->
            val item = recommendImages[i]
            Image(
                modifier = Modifier
                    .height(imageHeight)
                    .maskClip(MaterialTheme.shapes.medium),
                painter = painterResource(id = item.imageResId),
                contentDescription = item.contentDescription,
                contentScale = ContentScale.FillBounds
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun HomeScreenPreview() {
    val textFieldState = remember { TextFieldState() }
    HomeScreen(
        textFieldState = textFieldState,
        onSearch = {},
        searchResults = listOf("cat", "dog", "bird")
    )
}