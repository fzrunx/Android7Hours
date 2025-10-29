package com.sesac.home.presentation.ui


import androidx.annotation.DrawableRes
import androidx.compose.foundation.Image
import androidx.compose.foundation.gestures.snapping.SnapPosition
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.rememberLazyListState
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
import androidx.compose.material3.ExperimentalMaterial3Api
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
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.isTraversalGroup
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.sesac.common.component.CommonArticleList
import com.sesac.common.component.CommonArticlePreview
import com.sesac.common.component.CommonArticlePreviewListItem
import com.sesac.common.component.CommonSearchBar
import com.sesac.common.component.CommonSegmentedButton
import com.sesac.home.R
import com.sesac.home.presentation.component.HomeCarousel
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
    val tabOptions = listOf(
        stringResource(commonR.string.home_tab_trail),
        stringResource(commonR.string.home_tab_trip),
        stringResource(commonR.string.home_tab_hospital)
    )
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
        CommonArticlePreviewListItem(
            title = "title1",
            sumnail = painterResource(R.drawable.dog_sample_banner_1),
            content = "배고파".repeat(30),
            icon = Icons.Outlined.AccountCircle,
            author = "Hong"
        ),
        CommonArticlePreviewListItem(title = "title2", content = "졸려".repeat(30), icon = Icons.Filled.AccountCircle, author = "Gil"),
        CommonArticlePreviewListItem(title = "title2", sumnail = painterResource(R.drawable.dog_sample_banner_3), content = "lol".repeat(30), icon = Icons.Default.AccountCircle, author = "Dong"),
    )
    // ...

    val bannerPagerState = rememberPagerState(initialPage = 0, pageCount = { bannerImages.size })
    val homeScreenScrollState = rememberScrollState()
    val searchResultsScrollState = rememberScrollState()
    val lazyListState = rememberLazyListState()

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

            HomeCarousel(text = stringResource(commonR.string.home_carousel_title_trail_recommendation), recommendImages =  pathRecommendImages)
            HomeCarousel(text =  stringResource(commonR.string.home_carousel_title_community_recommendation), recommendImages = tripRecommendImages)

            CommonSegmentedButton(
                tabOptions =  tabOptions,
                tabSelectedIndex =  tabSelectedIndex,
                checkedIcons = checkedIcons,
                unCheckedIcons = unCheckedIcons
            )

            CommonArticleList(items = sampleCoummunityItems, articleHorrPadding = space) {
                item ->
                CommonArticlePreview(item = item)
            }
        }

        CommonSearchBar(
            Alignment.TopCenter,
            TextFieldState(),
            onSearch,
            searchResults,
            searchResultsScrollState)

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