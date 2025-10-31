package com.sesac.trail.presentation.component

import androidx.compose.animation.*
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Help
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.sesac.common.R// ⚠️ 본인의 R 패키지 경로로 수정하세요.
import com.sesac.trail.presentation.ui.RecommendedPath
import com.sesac.trail.presentation.ui.UserPath
import com.sesac.trail.presentation.ui.WalkMode

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun WalkPathAppBar(title: String, onNavigateToHome: () -> Unit) {
    TopAppBar(
        title = {
            Text(
                text = title,
                color = AppTheme.textPrimary,
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold
            )
        },
        navigationIcon = {
            IconButton(onClick = onNavigateToHome) {
                // ⚠️ 'logo_image'를 res/drawable에 추가한 리소스 이름으로 변경하세요.
//                Image(
//                    painter = painterResource(id = R.drawable.logo_image),
//                    contentDescription = "7Hours Home",
//                    modifier = Modifier.size(40.dp)
//                )
            }
        },
        actions = {
            Spacer(modifier = Modifier.width(48.dp)) // 중앙 정렬용
        },
        colors = TopAppBarDefaults.topAppBarColors(containerColor = AppTheme.header)
    )
}

@Composable
fun ModeFilterButtons(
    activeMode: WalkMode,
    onModeChange: (WalkMode) -> Unit,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = AppTheme.paddingLarge),
        horizontalArrangement = Arrangement.spacedBy(AppTheme.paddingSmall)
    ) {
        ModeButton(
            text = "추천",
            icon = Icons.Default.Star,
            isSelected = activeMode == WalkMode.RECOMMENDED,
            onClick = { onModeChange(WalkMode.RECOMMENDED) },
            modifier = Modifier.weight(1f)
        )
        ModeButton(
            text = "따라가기",
            icon = Icons.Default.Navigation,
            isSelected = activeMode == WalkMode.FOLLOW,
            onClick = { onModeChange(WalkMode.FOLLOW) },
            modifier = Modifier.weight(1f)
        )
        ModeButton(
            text = "기록",
            // React의 SVG 아이콘 대신 Material 아이콘 사용
            icon = Icons.Default.History,
            isSelected = activeMode == WalkMode.RECORD,
            onClick = { onModeChange(WalkMode.RECORD) },
            modifier = Modifier.weight(1f)
        )
    }
}

@Composable
fun ModeButton(
    text: String,
    icon: ImageVector,
    isSelected: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Button(
        onClick = onClick,
        modifier = modifier,
        shape = CircleShape,
        colors = ButtonDefaults.buttonColors(
            containerColor = if (isSelected) AppTheme.primary else AppTheme.surface.copy(alpha = 0.9f),
            contentColor = if (isSelected) Color.White else AppTheme.textPrimary
        ),
        elevation = ButtonDefaults.buttonElevation(defaultElevation = 4.dp)
    ) {
        Icon(
            imageVector = icon,
            contentDescription = null,
            modifier = Modifier.size(16.dp)
        )
        Spacer(modifier = Modifier.width(AppTheme.paddingSmall))
        Text(text = text, fontSize = 14.sp)
    }
}

@Composable
fun RecordingControls(
    isRecording: Boolean,
    recordingTime: Long,
    onToggleRecording: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier.padding(bottom = 32.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(AppTheme.paddingMedium)
    ) {
        AnimatedVisibility(
            visible = isRecording,
            enter = slideInVertically { it } + fadeIn(),
            exit = slideOutVertically { it } + fadeOut()
        ) {
            RecordingInfoChip(recordingTime = recordingTime)
        }

        Button(
            onClick = onToggleRecording,
            modifier = Modifier.size(80.dp),
            shape = CircleShape,
            colors = ButtonDefaults.buttonColors(
                containerColor = if (isRecording) AppTheme.error else AppTheme.primary
            ),
            elevation = ButtonDefaults.buttonElevation(defaultElevation = 8.dp)
        ) {
            Icon(
                imageVector = if (isRecording) Icons.Default.Stop else Icons.Default.PlayArrow,
                contentDescription = if (isRecording) "기록 중지" else "기록 시작",
                modifier = Modifier.size(40.dp),
                tint = Color.White
            )
        }
    }
}

@Composable
fun RecordingInfoChip(recordingTime: Long) {
    Surface(
        shape = RoundedCornerShape(16.dp),
        color = AppTheme.surface,
//        elevation = 8.dp
    ) {
        Column(
            modifier = Modifier.padding(horizontal = AppTheme.paddingLarge, vertical = AppTheme.paddingMedium),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Box(
                    modifier = Modifier
                        .size(12.dp)
                        .background(AppTheme.error, CircleShape)
                ) // TODO: Animate pulse
                Spacer(modifier = Modifier.width(AppTheme.paddingSmall))
                Text("기록 중", fontWeight = FontWeight.Bold)
            }
            Text(
                // TODO: 실제 타이머 로직으로 대체
                text = "${recordingTime / 60}:${(recordingTime % 60).toString().padStart(2, '0')}",
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold,
                color = AppTheme.textPrimary
            )
            Row(
                horizontalArrangement = Arrangement.spacedBy(AppTheme.paddingSmall),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text("0.0 km", fontSize = 14.sp, color = AppTheme.textSecondary)
                Text("•", fontSize = 14.sp, color = AppTheme.textSecondary)
                Text("0 걸음", fontSize = 14.sp, color = AppTheme.textSecondary)
            }
        }
    }
}

@Composable
fun ReopenSheetButton(mode: WalkMode, onClick: () -> Unit, modifier: Modifier = Modifier) {
    Button(
        onClick = onClick,
        modifier = modifier,
        shape = CircleShape,
        colors = ButtonDefaults.buttonColors(
            containerColor = AppTheme.surface,
            contentColor = AppTheme.textPrimary
        ),
        elevation = ButtonDefaults.buttonElevation(defaultElevation = 8.dp)
    ) {
        Icon(
            imageVector = Icons.Default.Pets, // 'Footprints' 아이콘 대체
            contentDescription = null,
            tint = AppTheme.primary,
            modifier = Modifier.size(16.dp)
        )
        Spacer(modifier = Modifier.width(AppTheme.paddingSmall))
        Text(
            text = if (mode == WalkMode.RECOMMENDED) "추천 산책로" else "주변 산책로",
            fontWeight = FontWeight.Bold
        )
    }
}

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
            .padding(horizontal = AppTheme.paddingLarge)
    ) {
        Text(
            text = title,
            style = MaterialTheme.typography.titleLarge,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(bottom = AppTheme.paddingMedium)
        )

        LazyColumn(
            modifier = Modifier
                .fillMaxWidth()
                .heightIn(max = AppTheme.bottomSheetMaxHeight),
            verticalArrangement = Arrangement.spacedBy(AppTheme.paddingSmall)
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
        Spacer(modifier = Modifier.height(AppTheme.paddingLarge)) // 바닥 여백
    }
}

@Composable
fun RecommendedPathCard(path: RecommendedPath) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .border(1.dp, AppTheme.border, RoundedCornerShape(12.dp)),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = AppTheme.surface),
        onClick = { /* 추천 경로는 클릭 액션 없음 */ }
    ) {
        Row(
            modifier = Modifier.padding(AppTheme.paddingSmall),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(AppTheme.cardIconSize)
                    .clip(RoundedCornerShape(8.dp))
                    .background(path.color)
            )
            Spacer(modifier = Modifier.width(AppTheme.paddingSmall))
            Column(modifier = Modifier.weight(1f)) {
                Text(path.name, fontWeight = FontWeight.Bold, color = AppTheme.textPrimary)
                Text(
                    "약 ${path.distance} · ${path.time} 코스",
                    fontSize = 14.sp,
                    color = AppTheme.textSecondary
                )
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(Icons.Default.Star, contentDescription = null, tint = Color.Yellow, modifier = Modifier.size(16.dp))
                    Text(
                        "${path.rating}",
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Bold,
                        color = AppTheme.textPrimary
                    )
                    Text(
                        " · 리뷰 ${path.reviews}개",
                        fontSize = 14.sp,
                        color = AppTheme.textDisabled
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
            .border(1.dp, AppTheme.border, RoundedCornerShape(12.dp)),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = AppTheme.surface),
        onClick = onClick
    ) {
        Row(
            modifier = Modifier.padding(AppTheme.paddingSmall),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(AppTheme.cardIconSize)
                    .clip(RoundedCornerShape(8.dp))
                    .background(path.color)
            )
            Spacer(modifier = Modifier.width(AppTheme.paddingSmall))
            Column(modifier = Modifier.weight(1f)) {
                Text(path.name, fontWeight = FontWeight.Bold, color = AppTheme.textPrimary)
                Text(
                    "@${path.uploader}",
                    fontSize = 12.sp,
                    color = AppTheme.textDisabled,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
                Text(
                    "${path.distance} · ${path.time} 코스",
                    fontSize = 14.sp,
                    color = AppTheme.textSecondary
                )
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(AppTheme.paddingMedium)
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(Icons.Default.Favorite, contentDescription = "Likes", tint = AppTheme.primary, modifier = Modifier.size(16.dp))
//                        Spacer(modifier = Modifier.width(AppHalf.paddingMicro))
                        Text("${path.likes}", fontSize = 14.sp, color = AppTheme.primary, fontWeight = FontWeight.Bold)
                    }
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(Icons.Default.LocationOn, contentDescription = "Distance", tint = AppTheme.textDisabled, modifier = Modifier.size(16.dp))
//                        Spacer(modifier = Modifier.width(AppTheme.paddingMicrocro))
                        Text("${path.distanceFromMe} 거리", fontSize = 14.sp, color = AppTheme.textDisabled)
                    }
                }
            }
        }
    }
}