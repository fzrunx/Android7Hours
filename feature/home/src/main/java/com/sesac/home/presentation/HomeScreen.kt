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
    var tabSelectedIndex by rememberSaveable { mutableIntStateOf(0) }

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
        SampleCommunityItem("title1", painterResource(R.drawable.dog_sample_banner_1), "배고파", Icons.Outlined.AccountCircle, "Hong"),
        SampleCommunityItem("title2", painterResource(R.drawable.dog_sample_banner_2), "졸려", Icons.Filled.AccountCircle, "Gil"),
        SampleCommunityItem("title2", painterResource(R.drawable.dog_sample_banner_3), "lol", Icons.Default.AccountCircle, "Dong"),
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

            FlowRow(
                Modifier
                    .padding(horizontal = space, vertical = textSpace)
                    .fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(tabSpace),
                verticalArrangement = Arrangement.spacedBy(textSpace),
            ) {
                SingleChoiceSegmentedButtonRow {
                    tabOptions.forEachIndexed { index, string ->
                        SegmentedButton(
                            modifier = Modifier
                                .wrapContentWidth()
                                .width(100.dp),
                            shape = SegmentedButtonDefaults.itemShape(
                                index = index,
                                count = tabOptions.size
                            ),
                            icon = {
                                Icon(
                                    imageVector = if (tabSelectedIndex == index) checkedIcons[index] else unCheckedIcons[index],
                                    contentDescription = string
                                )
                            },
                            onClick = { tabSelectedIndex = index },
                            selected = index == tabSelectedIndex,
                            label = { Text(string) }
                        )
                    }
                }
            }

            CustomHomeCommunityCard(communityItems = sampleCoummunityItems)
        }

        SearchBar(
            modifier = Modifier
                .align(Alignment.TopCenter)
                .fillMaxWidth()
                .padding(space)
                .semantics { traversalIndex = -1f },
            colors = SearchBarDefaults.colors(inputFieldColors = TextFieldDefaults.colors(Color.Black)),
            inputField = {
                SearchBarDefaults.InputField(
                    query = textFieldState.text.toString(),
                    onQueryChange = { textFieldState.edit { replace(0, Int.MAX_VALUE, it) } },
                    onSearch = {
                        onSearch(textFieldState.text.toString())
                        expanded = false
                    },
                    expanded = expanded,
                    onExpandedChange = { expanded = it },
                    placeholder = { Text("Search") }
                )
            },
            expanded = expanded,
            onExpandedChange = { expanded = it }
        ) {
            // Display search results in a scrollable column
            Column(Modifier.verticalScroll(searchResultsScrollState)) {
                searchResults.forEach { result ->
                    ListItem(
                        headlineContent = { Text(result) },
                        modifier = Modifier
                            .clickable {
                                textFieldState.edit { replace(0, length, result) }
                                expanded = false
                            }
                            .fillMaxWidth()
                    )
                }
            }
        }
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

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CustomHomeCommunityCard(
    modifier: Modifier = Modifier,
    communityItems: List<SampleCommunityItem>,
    space: Dp = dimensionResource(commonR.dimen.default_space),
    imageHeight: Dp = dimensionResource(commonR.dimen.carousel_image_height)
) {
    Column(
        verticalArrangement = Arrangement.spacedBy(space),
        modifier = modifier.padding(bottom = space)
    ) {
        communityItems.forEach { communityItem ->
            Card(
                modifier = Modifier
                    .padding(horizontal = space)
                    .fillMaxWidth()
                    .height(imageHeight),
                shape = RoundedCornerShape(15.dp),
                colors = CardDefaults.cardColors(containerColor = Color(0xfff2e2d1)),
                elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
            ) {
                Row {
                    Image(
                        modifier = Modifier
                            .padding(horizontal = space, vertical = space)
                            .fillMaxHeight()
                            .weight(1f),
                        painter = communityItem.sumnail,
                        contentDescription = "community sample image"
                    )

                    Column(
                        modifier = Modifier
                            .weight(2f)
                            .fillMaxHeight(),
                        verticalArrangement = Arrangement.Center
                    ) {
                        Text(
                            modifier = Modifier
                                .padding(start = space, top = space)
                                .weight(1f),
                            fontSize = 13.sp,
                            fontWeight = FontWeight.ExtraBold,
                            maxLines = 1,
                            text = communityItem.tile
                        )

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(
                                modifier = Modifier
                                    .wrapContentSize(Alignment.CenterStart)
                                    .padding(start = space)
                                    .size(15.dp),
                                imageVector = communityItem.icon,
                                contentDescription = "icon"
                            )

                            Text(
                                modifier = Modifier
                                    .padding(horizontal = space/3),
                                fontSize = 10.sp,
                                maxLines = 1,
                                text = communityItem.author
                            )
                        }

                        Text(
                            modifier = Modifier
                                .weight(2f)
                                .padding(start = space),
                            maxLines = 2,
                            overflow = TextOverflow.Ellipsis,
                            text = communityItem.content.repeat(100)    // ui 테스트용
                        )

                    }

                    Spacer(Modifier.padding(space))

                }
            }

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