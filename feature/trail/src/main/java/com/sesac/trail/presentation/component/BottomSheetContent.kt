package com.sesac.trail.presentation.component

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.sesac.common.ui.theme.Gray400
import com.sesac.common.ui.theme.TextPrimary
import com.sesac.common.ui.theme.TextSecondary
import com.sesac.common.ui.theme.bottomSheetMaxHeight
import com.sesac.common.ui.theme.iconSizeLarge
import com.sesac.common.ui.theme.iconSizeSmall
import com.sesac.common.ui.theme.paddingLarge
import com.sesac.common.ui.theme.paddingMedium
import com.sesac.common.ui.theme.paddingSmall
import com.sesac.trail.presentation.model.RecommendedPath
import com.sesac.trail.presentation.model.UserPath
import com.sesac.trail.presentation.ui.WalkMode

@Composable
fun BottomSheetContent(
    mode: WalkMode,
    recommendedPaths: List<RecommendedPath>,
    userPaths: List<UserPath>,
    onPathClick: (UserPath) -> Unit
) {
    val title = if (mode == WalkMode.RECOMMENDED) "추천 산책로" else "주변 산책로"

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .navigationBarsPadding() // 하단 네비게이션 바 영역 확보
            .padding(horizontal = paddingLarge)
    ) {
        Text(
            text = title,
            style = MaterialTheme.typography.titleLarge,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(bottom = paddingMedium)
        )

        LazyColumn(
            modifier = Modifier
                .fillMaxWidth()
                .heightIn(max = bottomSheetMaxHeight),
            verticalArrangement = Arrangement.spacedBy(paddingSmall)
        ) {
            if (mode == WalkMode.RECOMMENDED) {
                items(recommendedPaths, key = { it.id }) { path ->
                    RecommendedPathCard(path = path)
                }
            } else {
                items(userPaths, key = { it.id }) { path ->
                    UserPathCard(path = path, onClick = { onPathClick(path) })
                }
            }
        }
        Spacer(modifier = Modifier.height(paddingLarge)) // 바닥 여백
    }
}

@Composable
fun RecommendedPathCard(path: RecommendedPath) {
    Card(
        modifier = Modifier
            .fillMaxWidth(),
//            .border(1.dp, ButtonSecondary, MaterialTheme.shapes.medium),
        shape = MaterialTheme.shapes.medium,
        colors = CardDefaults.cardColors(containerColor = Color.White),
        onClick = { /* 추천 경로는 클릭 액션 없음 */ }
    ) {
        Row(
            modifier = Modifier.padding(paddingSmall),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(iconSizeLarge)
                    .clip(MaterialTheme.shapes.small)
                    .background(path.color)
            )
            Spacer(modifier = Modifier.width(paddingSmall))
            Column(modifier = Modifier.weight(1f)) {
                Text(path.name, fontWeight = FontWeight.Bold, color = TextPrimary)
                Text(
                    "약 ${path.distance} · ${path.time} 코스",
                    fontSize = 14.sp,
                    color = TextSecondary
                )
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        Icons.Default.Star,
                        contentDescription = null,
                        tint = Color.Yellow,
                        modifier = Modifier.size(iconSizeSmall)
                    )
                    Text(
                        "${path.rating}",
                        style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Bold),
                    )
                    Text(
                        " · 리뷰 ${path.reviews}개",
                        style = MaterialTheme.typography.bodyMedium.copy(color = Gray400),
                    )
                }
            }
        }
    }
}

@Composable
fun UserPathCard(path: UserPath, onClick: () -> Unit) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .border(1.dp, MaterialTheme.colorScheme.outline, RoundedCornerShape(12.dp)),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        onClick = onClick
    ) {
        Row(
            modifier = Modifier.padding(paddingSmall),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(iconSizeLarge)
                    .clip(MaterialTheme.shapes.medium)
                    .background(path.color)
            )
            Spacer(modifier = Modifier.width(paddingSmall))
            Column(modifier = Modifier.weight(1f)) {
                Text(path.name, fontWeight = FontWeight.Bold, color = TextPrimary)
                Text(
                    "@${path.uploader}",
                    fontSize = 12.sp,
                    color = Gray400,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
                Text(
                    "${path.distance} · ${path.time} 코스",
                    fontSize = 14.sp,
                    color = TextSecondary
                )
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(paddingMedium)
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(Icons.Default.Favorite, contentDescription = "Likes", tint = MaterialTheme.colorScheme.primary, modifier = Modifier.size(16.dp))
//                        Spacer(modifier = Modifier.width(paddingMicro))
                        Text("${path.likes}", style = MaterialTheme.typography.bodyMedium.copy(color = MaterialTheme.colorScheme.primary, fontWeight = FontWeight.Bold))
                    }
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            Icons.Default.LocationOn,
                            contentDescription = "Distance",
                            tint = Gray400,
                            modifier = Modifier.size(
                                iconSizeSmall
                            ))
//                        Spacer(modifier = Modifier.width(paddingMicrocro))
                        Text("${path.distanceFromMe} 거리", style = MaterialTheme.typography.bodyMedium.copy(color = Gray400))
                    }
                }
            }
        }
    }
}