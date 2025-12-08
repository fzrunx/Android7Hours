//package com.sesac.trail.utils
//
//import androidx.compose.foundation.background
//import androidx.compose.foundation.layout.Arrangement
//import androidx.compose.foundation.layout.Box
//import androidx.compose.foundation.layout.Column
//import androidx.compose.foundation.layout.PaddingValues
//import androidx.compose.foundation.layout.Row
//import androidx.compose.foundation.layout.Spacer
//import androidx.compose.foundation.layout.fillMaxWidth
//import androidx.compose.foundation.layout.height
//import androidx.compose.foundation.layout.heightIn
//import androidx.compose.foundation.layout.padding
//import androidx.compose.foundation.layout.size
//import androidx.compose.foundation.layout.width
//import androidx.compose.foundation.lazy.LazyColumn
//import androidx.compose.foundation.shape.RoundedCornerShape
//import androidx.compose.material.icons.Icons
//import androidx.compose.material.icons.filled.ArrowBack
//import androidx.compose.material.icons.filled.Pets
//import androidx.compose.material.icons.filled.Place
//import androidx.compose.material.icons.filled.Schedule
//import androidx.compose.material.icons.filled.TrendingUp
//import androidx.compose.material3.Button
//import androidx.compose.material3.FilterChip
//import androidx.compose.material3.Icon
//import androidx.compose.material3.MaterialTheme
//import androidx.compose.material3.MaterialTheme.colorScheme
//import androidx.compose.material3.Surface
//import androidx.compose.material3.Text
//import androidx.compose.material3.TextButton
//import androidx.compose.runtime.Composable
//import androidx.compose.ui.Alignment
//import androidx.compose.ui.Modifier
//import androidx.compose.ui.draw.clip
//import androidx.compose.ui.graphics.Color
//import androidx.compose.ui.graphics.vector.ImageVector
//import androidx.compose.ui.res.stringResource
//import androidx.compose.ui.text.font.FontWeight
//import androidx.compose.ui.unit.dp
//import com.sesac.common.R as cR
//import com.sesac.trail.presentation.ui.FilterState
//
//@Composable
//fun FilterSheetContent(
//    filterState: FilterState,
//    onFilterChange: (FilterState) -> Unit,
//    onBack: () -> Unit,
//    onApply: () -> Unit
//) {
//    Column(
//        modifier = Modifier
//            .fillMaxWidth()
//            .heightIn(min = 160.dp)
//    ) {
//        // 드래그 핸들
//        Box(
//            modifier = Modifier
//                .fillMaxWidth()
//                .padding(vertical = 12.dp),
//            contentAlignment = Alignment.Center
//        ) {
//            Box(
//                modifier = Modifier
//                    .width(40.dp)
//                    .height(4.dp)
//                    .clip(RoundedCornerShape(2.dp))
//                    .background(Color.Gray.copy(alpha = 0.3f))
//            )
//        }
//
//        // 필터 헤더: 뒤로가기 + 제목 + 초기화
//        Row(
//            modifier = Modifier
//                .fillMaxWidth()
//                .padding(horizontal = 16.dp, vertical = 8.dp),
//            horizontalArrangement = Arrangement.SpaceBetween,
//            verticalAlignment = Alignment.CenterVertically
//        ) {
//            Row(
//                verticalAlignment = Alignment.CenterVertically,
//                horizontalArrangement = Arrangement.spacedBy(8.dp)
//            ) {
//                Surface(
//                    onClick = onBack,
//                    shape = RoundedCornerShape(8.dp),
//                    color = Color.Transparent
//                ) {
//                    Icon(
//                        Icons.Default.ArrowBack,
//                        contentDescription = "뒤로가기",
//                        modifier = Modifier
//                            .padding(4.dp)
//                            .size(24.dp)
//                    )
//                }
//                Text(
//                    stringResource(cR.string.trail_title_filter_option),
//                    style = MaterialTheme.typography.titleLarge,
//                    fontWeight = FontWeight.Bold
//                )
//            }
//
//            TextButton(onClick = {
//                onFilterChange(FilterState())
//            }) {
//                Text(stringResource(cR.string.trail_title_reset))
//            }
//        }
//
//        // 필터 내용을 스크롤 가능하게
//        LazyColumn(
//            contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp),
//            verticalArrangement = Arrangement.spacedBy(16.dp)
//        ) {
//            item {
//                FilterSection(
//                    title = stringResource(cR.string.trail_title_distace),
//                    icon = Icons.Default.Place,
//                    options = listOf(stringResource(cR.string.trail_distance_under_1km),
//                        stringResource(cR.string.trail_distance_1_to_3km),
//                        stringResource(cR.string.trail_distance_3_to_5km),
//                        stringResource(cR.string.trail_distance_over_5km)),
//                    selected = filterState.distance,
//                    onSelect = { onFilterChange(filterState.copy(distance = it)) }
//                )
//            }
//
//            item {
//                FilterSection(
//                    title = stringResource(cR.string.trail_title_hours),
//                    icon = Icons.Default.Schedule,
//                    options = listOf(stringResource(cR.string.trail_time_under_30min),
//                        stringResource(cR.string.trail_time_30_to_60min),
//                        stringResource(cR.string.trail_time_1_to_2_hours),
//                        stringResource(cR.string.trail_time_over_2_hours)),
//                    selected = filterState.time,
//                    onSelect = { onFilterChange(filterState.copy(time = it)) }
//                )
//            }
//
//            item {
//                FilterSection(
//                    title = stringResource(cR.string.trail_title_level),
//                    icon = Icons.Default.TrendingUp,
//                    options = listOf(stringResource(cR.string.trail_level_low),
//                        stringResource(cR.string.trail_level_medium),
//                        stringResource(cR.string.trail_level_high)),
//                    selected = filterState.difficulty,
//                    onSelect = { onFilterChange(filterState.copy(difficulty = it)) }
//                )
//            }
//
//            item {
//                FilterSection(
//                    title = stringResource(cR.string.trail_title_dog_size),
//                    icon = Icons.Default.Pets,
//                    options = listOf( stringResource(cR.string.dog_size_small),
//                        stringResource(cR.string.dog_size_medium),
//                        stringResource(cR.string.dog_size_large)),
//                    selected = filterState.dogSize,
//                    onSelect = { onFilterChange(filterState.copy(dogSize = it)) }
//                )
//            }
//
//            item {
//                Button(
//                    onClick = onApply,
//                    modifier = Modifier
//                        .fillMaxWidth()
//                        .height(56.dp),
//                    shape = RoundedCornerShape(12.dp)
//                ) {
//                    Text(stringResource(cR.string.trail_button_apply), style = MaterialTheme.typography.titleMedium)
//                }
//            }
//
//            // 하단 여백
//            item {
//                Spacer(modifier = Modifier.height(16.dp))
//            }
//        }
//    }
//}
//
//
//@Composable
//fun FilterSection(
//    title: String,
//    icon: ImageVector,
//    options: List<String>,
//    selected: String?,
//    onSelect: (String?) -> Unit
//) {
//    Column {
//        Row(
//            verticalAlignment = Alignment.CenterVertically,
//            modifier = Modifier.padding(bottom = 8.dp)
//        ) {
//            Icon(
//                icon,
//                contentDescription = null,
//                modifier = Modifier.size(20.dp),
//                tint = colorScheme.primary
//            )
//            Spacer(modifier = Modifier.width(8.dp))
//            Text(
//                title,
//                style = MaterialTheme.typography.titleSmall,
//                fontWeight = FontWeight.SemiBold
//            )
//        }
//
//        Row(
//            horizontalArrangement = Arrangement.spacedBy(8.dp),
//            modifier = Modifier.fillMaxWidth()
//        ) {
//            options.forEach { option ->
//                FilterChip(
//                    selected = selected == option,
//                    onClick = {
//                        onSelect(if (selected == option) null else option)
//                    },
//                    label = { Text(option) },
//                    modifier = Modifier.weight(1f)
//                )
//            }
//        }
//    }
//}