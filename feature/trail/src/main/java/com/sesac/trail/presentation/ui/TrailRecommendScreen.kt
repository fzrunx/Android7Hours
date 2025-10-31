package com.sesac.trail.presentation.ui

import android.util.Log
import com.sesac.common.R as cR
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FilterList
import androidx.compose.material.icons.filled.Layers
import androidx.compose.material.icons.filled.Map
import androidx.compose.material.icons.filled.MyLocation
import androidx.compose.material.icons.filled.Pets
import androidx.compose.material.icons.filled.Place
import androidx.compose.material.icons.filled.Schedule
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.filled.TrendingUp
import androidx.compose.material3.Badge
import androidx.compose.material3.BottomSheetScaffold
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilterChip
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.MaterialTheme.colorScheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.rememberBottomSheetScaffoldState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.sesac.common.component.CommonArticle
import com.sesac.common.component.CommonArticleList
import com.sesac.common.component.CommonSegmentedButton
import com.sesac.common.component.PathInfo
import com.sesac.trail.presentation.component.TrailControlIconList
import com.sesac.trail.presentation.component.SegmentedMenuItem
import com.sesac.trail.presentation.component.TrailControlButton
import com.sesac.trail.utils.FilterSheetContent

data class FilterState(
    val distance: String? = null,
    val time: String? = null,
    val difficulty: String? = null,
    val dogSize: String? = null
) {
    val activeCount: Int
        get() = listOfNotNull(distance, time, difficulty, dogSize).size
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TrailRecommendScreen(
    current: String = "recommend",                         // 현재 선택된 메뉴 id (임시 초기값
    trailSelectedMenu: MutableState<String>,       // 메뉴 선택 이벤트 콜백
) {
    val recommend = stringResource(cR.string.trail_button_recommend)
    // 상단 SegmentedMenu
    val menuItems = listOf(
        SegmentedMenuItem("recommend", stringResource(cR.string.trail_button_recommend)),
        SegmentedMenuItem("follow", stringResource(cR.string.trail_button_follow)),
        SegmentedMenuItem("record", stringResource(cR.string.trail_button_record))
    )

    val sheetPeekHeight = dimensionResource(cR.dimen.trail_botton_sheet_peek_height)

    val tabOptions = listOf(stringResource(cR.string.trail_button_recommend), stringResource(cR.string.trail_button_follow), stringResource(cR.string.trail_button_record))
    val tabSelectedIndex = remember { mutableStateOf(0) }

    val surfaceIconList = listOf(Icons.Default.MyLocation, Icons.Default.Layers)

    val sheetState = rememberBottomSheetScaffoldState()

    var selectedRoute by remember { mutableStateOf<PathInfo?>(null) }
    var showFilterMode by remember { mutableStateOf(false) }
    var filterState by remember { mutableStateOf(FilterState()) }
    val listState = rememberLazyListState()

    val sampleRoutes = remember {
        listOf(
            PathInfo(1, "한강 산책로", "2.5km", "40분", "하", "전체", 4.8f, 234),
            PathInfo(2, "남산 둘레길", "4.2km", "80분", "중", "중형 이상", 4.6f, 189),
            PathInfo(3, "올림픽공원 코스", "3.8km", "60분", "하", "전체", 4.9f, 412),
            PathInfo(4, "북한산 입구", "1.8km", "35분", "중", "소형", 4.5f, 156),
            PathInfo(5, "양재천 산책로", "3.2km", "50분", "하", "전체", 4.7f, 298),
        )
    }

    BottomSheetScaffold(
        scaffoldState = sheetState,
        sheetPeekHeight = sheetPeekHeight,
        sheetDragHandle = null,
        sheetContent = {
            // 모드에 따라 다른 컨텐츠 표시
            if(trailSelectedMenu.value == recommend) {
                if (showFilterMode) {
                    FilterSheetContent(
                        filterState = filterState,
                        onFilterChange = { filterState = it },
                        onBack = { showFilterMode = false },
                        onApply = { showFilterMode = false }
                    )
                } else {
                    WalkListBottomSheet(
                        routes = sampleRoutes,
                        selectedRoute = selectedRoute,
                        listState = listState,
                        filterCount = filterState.activeCount,
                        onRouteClick = { selectedRoute = it },
                        onFilterClick = { showFilterMode = true }
                    )
                }
            }
        }
    ) { padding ->
        // 맵 이미지 영역
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
        ) {
            // 맵 배경
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color(0xFFE8F5E9))
            ) {

                Log.d("TagTrailRecommend", trailSelectedMenu.value)
                if(trailSelectedMenu.value == recommend) {
                    CommonSegmentedButton(
                        tabOptions =  tabOptions,
                        tabSelectedIndex = tabSelectedIndex,
                        selectedMenuState = trailSelectedMenu,
                    )
                }

                Column(
                    modifier = Modifier.align(Alignment.Center),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Icon(
                        Icons.Default.Map,
                        contentDescription = null,
                        modifier = Modifier.size(80.dp),
                        tint = Color.Gray.copy(alpha = 0.3f)
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        "맵 이미지 영역",
                        style = MaterialTheme.typography.titleLarge,
                        color = Color.Gray
                    )
                }

            }

            TrailControlIconList(surfaceIconList)

            if(trailSelectedMenu.value != recommend) {
                TrailControlButton()
            }

        }
    }
}

@Composable
fun WalkListBottomSheet(
    routes: List<PathInfo>,
    selectedRoute: PathInfo?,
    listState: LazyListState,
    filterCount: Int,
    onRouteClick: (PathInfo) -> Unit,
    onFilterClick: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .heightIn(min = 160.dp)
    ) {
        // 드래그 핸들
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 12.dp),
            contentAlignment = Alignment.Center
        ) {
            Box(
                modifier = Modifier
                    .width(40.dp)
                    .height(4.dp)
                    .clip(RoundedCornerShape(2.dp))
                    .background(Color.Gray.copy(alpha = 0.3f))
            )
        }

        // 헤더: 제목 + 필터 버튼
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 8.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                stringResource(cR.string.trail_title_nearby_trails),
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold
            )

            // 필터 버튼
            Surface(
                onClick = onFilterClick,
                shape = RoundedCornerShape(20.dp),
                color = if (filterCount > 0)
                    colorScheme.primaryContainer
                else
                    colorScheme.surfaceVariant,
                modifier = Modifier.height(36.dp)
            ) {
                Row(
                    modifier = Modifier.padding(horizontal = 12.dp, vertical = 8.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    Icon(
                        Icons.Default.FilterList,
                        contentDescription = "필터",
                        modifier = Modifier.size(18.dp),
                        tint = if (filterCount > 0)
                            colorScheme.onPrimaryContainer
                        else
                            colorScheme.onSurfaceVariant
                    )
                    Text(
                        stringResource(cR.string.trail_title_filter),
                        style = MaterialTheme.typography.labelLarge,
                        color = if (filterCount > 0)
                            colorScheme.onPrimaryContainer
                        else
                            colorScheme.onSurfaceVariant
                    )
                    if (filterCount > 0) {
                        Badge(
                            containerColor = colorScheme.primary,
                            contentColor = colorScheme.onPrimary
                        ) {
                            Text(
                                filterCount.toString(),
                                style = MaterialTheme.typography.labelSmall
                            )
                        }
                    }
                }
            }
        }

        CommonArticleList(
            listState = listState,
            items = routes,
        ) { route ->
            CommonArticle(
                route = route,
                isSelected = selectedRoute?.id == route.id,
                onClick = { onRouteClick(route) }
            )
        }

    }
}

@Preview(showBackground = true)
@Composable
fun TrailRecommendScreenPreview() {
    val recommend = stringResource(cR.string.trail_button_recommend)
    TrailRecommendScreen(
        trailSelectedMenu = remember { mutableStateOf(recommend) }
    )
}