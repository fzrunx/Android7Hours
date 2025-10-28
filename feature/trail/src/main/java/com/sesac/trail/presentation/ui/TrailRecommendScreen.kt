package com.sesac.trail.presentation.ui

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
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.sesac.trail.presentation.component.SegmentedMenu
import com.sesac.trail.presentation.component.SegmentedMenuItem

// 데이터 모델
data class WalkRoute(
    val id: Int,
    val name: String,
    val distance: String,
    val duration: String,
    val difficulty: String,
    val dogSize: String,
    val rating: Float,
    val likes: Int
)

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
@Preview(showSystemUi = true)
@Composable
fun TrailRecommendScreen(
    current: String = "recommend",                         // 현재 선택된 메뉴 id (임시 초기값
    onSelectMenu: (String) -> Unit = {} // (String) -> Unit           // 메뉴 선택 이벤트 콜백
) {
    // 상단 SegmentedMenu
    val menuItems = listOf(
        SegmentedMenuItem("recommend", "추천"),
        SegmentedMenuItem("follow", "따라가기"),
        SegmentedMenuItem("record", "기록")
    )


    val sheetState = rememberBottomSheetScaffoldState()

    var selectedRoute by remember { mutableStateOf<WalkRoute?>(null) }
    var showFilterMode by remember { mutableStateOf(false) }
    var filterState by remember { mutableStateOf(FilterState()) }
    val listState = rememberLazyListState()

    val sampleRoutes = remember {
        listOf(
            WalkRoute(1, "한강 산책로", "2.5km", "40분", "하", "전체", 4.8f, 234),
            WalkRoute(2, "남산 둘레길", "4.2km", "80분", "중", "중형 이상", 4.6f, 189),
            WalkRoute(3, "올림픽공원 코스", "3.8km", "60분", "하", "전체", 4.9f, 412),
            WalkRoute(4, "북한산 입구", "1.8km", "35분", "중", "소형", 4.5f, 156),
            WalkRoute(5, "양재천 산책로", "3.2km", "50분", "하", "전체", 4.7f, 298),
        )
    }

    BottomSheetScaffold(
        scaffoldState = sheetState,
        sheetPeekHeight = 160.dp,
        sheetDragHandle = null,
        sheetContent = {
            // 모드에 따라 다른 컨텐츠 표시
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
                SegmentedMenu(
                    items = menuItems,
                    selectedItem = current,
                    onItemSelected = onSelectMenu,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(5.dp)
                )


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

            // 오른쪽 하단 맵 컨트롤 (예시)
            Column(
                modifier = Modifier
                    .align(Alignment.BottomEnd)
                    .padding(16.dp)
                    .padding(bottom = 160.dp), // Bottom Sheet 높이만큼 여백
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                // 현재 위치 버튼
                Surface(
                    shape = RoundedCornerShape(12.dp),
                    shadowElevation = 4.dp,
                    color = Color.White,
                    modifier = Modifier.size(48.dp)
                ) {
                    Box(contentAlignment = Alignment.Center) {
                        Icon(
                            Icons.Default.MyLocation,
                            contentDescription = "현재 위치",
                            tint = colorScheme.primary
                        )
                    }
                }

                // 맵 타입 변경 버튼
                Surface(
                    shape = RoundedCornerShape(12.dp),
                    shadowElevation = 4.dp,
                    color = Color.White,
                    modifier = Modifier.size(48.dp)
                ) {
                    Box(contentAlignment = Alignment.Center) {
                        Icon(
                            Icons.Default.Layers,
                            contentDescription = "맵 타입",
                            tint = colorScheme.primary
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun WalkListBottomSheet(
    routes: List<WalkRoute>,
    selectedRoute: WalkRoute?,
    listState: LazyListState,
    filterCount: Int,
    onRouteClick: (WalkRoute) -> Unit,
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
                "주변 산책로",
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
                        "필터",
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

        // 산책로 리스트
        LazyColumn(
            state = listState,
            contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            items(routes) { route ->
                WalkRouteCard(
                    route = route,
                    isSelected = selectedRoute?.id == route.id,
                    onClick = { onRouteClick(route) }
                )
            }
        }
    }
}

@Composable
fun FilterSheetContent(
    filterState: FilterState,
    onFilterChange: (FilterState) -> Unit,
    onBack: () -> Unit,
    onApply: () -> Unit
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

        // 필터 헤더: 뒤로가기 + 제목 + 초기화
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 8.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Surface(
                    onClick = onBack,
                    shape = RoundedCornerShape(8.dp),
                    color = Color.Transparent
                ) {
                    Icon(
                        Icons.Default.ArrowBack,
                        contentDescription = "뒤로가기",
                        modifier = Modifier
                            .padding(4.dp)
                            .size(24.dp)
                    )
                }
                Text(
                    "필터 옵션",
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Bold
                )
            }

            TextButton(onClick = {
                onFilterChange(FilterState())
            }) {
                Text("초기화")
            }
        }

        // 필터 내용을 스크롤 가능하게
        LazyColumn(
            contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            item {
                FilterSection(
                    title = "거리",
                    icon = Icons.Default.Place,
                    options = listOf("1km 이하", "1-3km", "3-5km", "5km 이상"),
                    selected = filterState.distance,
                    onSelect = { onFilterChange(filterState.copy(distance = it)) }
                )
            }

            item {
                FilterSection(
                    title = "시간",
                    icon = Icons.Default.Schedule,
                    options = listOf("30분 이하", "30-60분", "1-2시간", "2시간 이상"),
                    selected = filterState.time,
                    onSelect = { onFilterChange(filterState.copy(time = it)) }
                )
            }

            item {
                FilterSection(
                    title = "난이도",
                    icon = Icons.Default.TrendingUp,
                    options = listOf("하", "중", "상"),
                    selected = filterState.difficulty,
                    onSelect = { onFilterChange(filterState.copy(difficulty = it)) }
                )
            }

            item {
                FilterSection(
                    title = "반려견 크기",
                    icon = Icons.Default.Pets,
                    options = listOf("소형", "중형", "대형"),
                    selected = filterState.dogSize,
                    onSelect = { onFilterChange(filterState.copy(dogSize = it)) }
                )
            }

            item {
                Button(
                    onClick = onApply,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(56.dp),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Text("적용하기", style = MaterialTheme.typography.titleMedium)
                }
            }

            // 하단 여백
            item {
                Spacer(modifier = Modifier.height(16.dp))
            }
        }
    }
}

@Composable
fun WalkRouteCard(
    route: WalkRoute,
    isSelected: Boolean,
    onClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick),
        elevation = CardDefaults.cardElevation(
            defaultElevation = if (isSelected) 8.dp else 2.dp
        ),
        colors = CardDefaults.cardColors(
            containerColor = if (isSelected)
                colorScheme.primaryContainer
            else
                colorScheme.surface
        ),
        shape = RoundedCornerShape(16.dp)
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    route.name,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold
                )
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        Icons.Default.Star,
                        contentDescription = null,
                        tint = Color(0xFFFFC107),
                        modifier = Modifier.size(16.dp)
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                        route.rating.toString(),
                        style = MaterialTheme.typography.bodyMedium,
                        fontWeight = FontWeight.SemiBold
                    )
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            Row(
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                InfoChip("📍 ${route.distance}")
                InfoChip("⏱️ ${route.duration}")
                InfoChip("🐕 ${route.dogSize}")
            }

            Spacer(modifier = Modifier.height(8.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    "난이도: ${route.difficulty}",
                    style = MaterialTheme.typography.bodySmall,
                    color = Color.Gray
                )
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        Icons.Default.Favorite,
                        contentDescription = null,
                        tint = Color(0xFFE91E63),
                        modifier = Modifier.size(14.dp)
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                        route.likes.toString(),
                        style = MaterialTheme.typography.bodySmall,
                        color = Color.Gray
                    )
                }
            }
        }
    }
}

@Composable
fun InfoChip(text: String) {
    Surface(
        shape = RoundedCornerShape(8.dp),
        color = Color.Gray.copy(alpha = 0.1f)
    ) {
        Text(
            text,
            style = MaterialTheme.typography.bodySmall,
            modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
        )
    }
}

@Composable
fun FilterSection(
    title: String,
    icon: ImageVector,
    options: List<String>,
    selected: String?,
    onSelect: (String?) -> Unit
) {
    Column {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.padding(bottom = 8.dp)
        ) {
            Icon(
                icon,
                contentDescription = null,
                modifier = Modifier.size(20.dp),
                tint = colorScheme.primary
            )
            Spacer(modifier = Modifier.width(8.dp))
            Text(
                title,
                style = MaterialTheme.typography.titleSmall,
                fontWeight = FontWeight.SemiBold
            )
        }

        Row(
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            modifier = Modifier.fillMaxWidth()
        ) {
            options.forEach { option ->
                FilterChip(
                    selected = selected == option,
                    onClick = {
                        onSelect(if (selected == option) null else option)
                    },
                    label = { Text(option) },
                    modifier = Modifier.weight(1f)
                )
            }
        }
    }
}