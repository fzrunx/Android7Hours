package com.sesac.trail.presentation.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.layout.Arrangement.spacedBy
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil3.compose.AsyncImage
import kotlinx.coroutines.launch
import com.sesac.common.ui.theme.*
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import com.sesac.trail.presentation.component.TagFlow
import com.sesac.trail.presentation.model.UserPath


// --- Main Composable ---
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TrailDetailScreen(
    path: UserPath,
    onBack: () -> Unit,
    onStartFollowing: (UserPath) -> Unit
) {
    var isLiked by remember { mutableStateOf(false) }
    var likeCount by remember { mutableStateOf(path.likes) }
    var isFavorite by remember { mutableStateOf(false) }

    val snackbarHostState = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()

    val handleLike = {
        if (isLiked) likeCount-- else likeCount++
        isLiked = !isLiked
    }

    val handleFavorite: () -> Unit = {
        isFavorite = !isFavorite
        scope.launch {
            val message = if (isFavorite) "즐겨찾기에 추가되었습니다" else "즐겨찾기에서 제거되었습니다"
            snackbarHostState.showSnackbar(message)
        }
    }

    Scaffold(
    ) { _ ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
        ) {
            PathImageHeader(
                pathName = path.name,
                isLiked = isLiked,
                onLikeClick = handleLike
            )

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(paddingLarge),
                verticalArrangement = Arrangement.spacedBy(PaddingSection)
            ) {
                // Title & Uploader
                Column {
                    Text(
                        text = path.name,
                        style = MaterialTheme.typography.headlineSmall,
                        fontWeight = FontWeight.Bold
                    )
                    Spacer(Modifier.height(paddingMicro))
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            Icons.Filled.Person,
                            contentDescription = "Uploader",
                            modifier = Modifier.size(16.dp),
                            tint = GrayTabText
                        )
                        Spacer(Modifier.width(paddingMicro))
                        Text(
                            text = "@${path.uploader}",
                            style = MaterialTheme.typography.bodyMedium,
                            color = GrayTabText
                        )
                    }
                }

                // Follow Button
                Button(
                    onClick = { onStartFollowing(path) },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(52.dp),
                    shape = RoundedCornerShape(16.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = Purple600)
                ) {
                    Icon(Icons.Filled.Navigation, contentDescription = null)
                    Spacer(Modifier.width(paddingMicro))
                    Text("이 산책로 따라가기", fontSize = 16.sp, fontWeight = FontWeight.Bold)
                }

                // Stats Grid
                Column(verticalArrangement = Arrangement.spacedBy(paddingSmall)) {
                    Row(horizontalArrangement = spacedBy(paddingSmall)) {
                        InfoCard(
                            icon = Icons.Filled.LocationOn,
                            label = "거리",
                            value = path.distance,
                            modifier = Modifier.weight(1f)
                        )
                        InfoCard(
                            icon = Icons.Filled.Schedule,
                            label = "소요시간",
                            value = path.time,
                            modifier = Modifier.weight(1f)
                        )
                    }
                    Row(horizontalArrangement = spacedBy(paddingSmall)) {
                        InfoCard(
                            icon = Icons.Filled.Favorite,
                            label = "좋아요",
                            value = "${likeCount}개",
                            modifier = Modifier.weight(1f)
                        )
                        InfoCard(
                            icon = Icons.Filled.TrendingUp,
                            label = "내 위치에서",
                            value = path.distanceFromMe,
                            modifier = Modifier.weight(1f)
                        )
                    }
                }
                // Route Features
                PathSection(title = "코스 특징") {
                    if (path.tags.isNotEmpty()) {
                        TagFlow(
                            selectedTags = path.tags,
                            editable = false
                        )
                    } else {
                        Text(
                            text = "등록된 코스 특징이 없습니다.",
                            color = GrayTabText,
                            style = MaterialTheme.typography.bodyMedium
                        )
                    }
                }

                // Description
                PathSection(title = "산책로 소개") {
                    Text(
                        text = "이 산책로는 ${path.uploader}님이 공유한 멋진 코스입니다. " +
                                "도심 속에서 자연을 느낄 수 있는 특별한 장소로, " +
                                "반려견과 함께 걷기에 최적화되어 있습니다. " +
                                "적당한 난이도로 누구나 쉽게 즐길 수 있으며, " +
                                "주변 경관이 아름다워 사진 촬영 명소로도 유명합니다.",
                        style = MaterialTheme.typography.bodyMedium,
                        color = GrayTabText,
                        lineHeight = 24.sp
                    )
                }


                // Reviews
                PathSection(title = "이용자 후기") {
                    Column(verticalArrangement = Arrangement.spacedBy(paddingMicro)) {
                        ReviewItem(
                            userName = "산책러버",
                            date = "2일 전",
                            review = "강아지와 함께 산책하기 정말 좋았어요! 코스도 적당하고 경치가 아름답습니다 👍"
                        )
                        ReviewItem(
                            userName = "햇살맘",
                            date = "5일 전",
                            review = "주말에 가족들과 다녀왔는데 아이들도 너무 좋아했어요. 추천합니다! "
                        )
                    }
                }
            }
        }
    }
}


// --- Image Header ---
@Composable
fun PathImageHeader(
    pathName: String,
    isLiked: Boolean,
    onLikeClick: () -> Unit,
    imageUrl: String = "https://images.unsplash.com/photo-1675435842943-7d7385e9a835?crop=entropy&cs=tinysrgb&fit=max&fm=jpg&ixid=M3w3Nzg4Nzd8MHwxfHNlYXJjaHwxfHx3YWxraW5nJTIwcGF0aCUyMHBhcmt8ZW58MXx8fHwxNzYxODExNTY0fDA&ixlib=rb-4.1.0&q=80&w=1080"
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(256.dp)
    ) {
        AsyncImage(
            model = imageUrl,
            contentDescription = pathName,
            modifier = Modifier.fillMaxSize(),
            contentScale = ContentScale.Crop
        )
        FloatingActionButton(
            onClick = onLikeClick,
            modifier = Modifier
                .align(Alignment.TopEnd)
                .padding(paddingLarge),
            containerColor = if (isLiked) Purple600 else MaterialTheme.colorScheme.surface,
            contentColor = if (isLiked) Color.White else GrayTabText,
            shape = CircleShape
        ) {
            Icon(
                imageVector = if (isLiked) Icons.Filled.Favorite else Icons.Filled.FavoriteBorder,
                contentDescription = "좋아요"
            )
        }
    }
}

// --- InfoCard ---
@Composable
fun InfoCard(
    icon: ImageVector,
    label: String,
    value: String,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier.fillMaxWidth(),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = PrimaryPurpleLight)
    ) {
        Column(modifier = Modifier.padding(paddingSmall)) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.padding(bottom = paddingMicro)
            ) {
                Icon(icon, contentDescription = null, tint = Purple600, modifier = Modifier.size(20.dp))
                Spacer(Modifier.width(paddingMicro))
                Text(label, style = MaterialTheme.typography.bodySmall, color = GrayTabText)
            }
            Text(value, style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
        }
    }
}

// --- Section ---
@Composable
fun PathSection(title: String, content: @Composable ColumnScope.() -> Unit) {
    Column {
        Text(title, style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Bold)
        Spacer(Modifier.height(paddingMicro))
        content()
    }
}


// --- Review ---
@Composable
fun ReviewItem(userName: String, date: String, review: String) {
    Card(
        colors = CardDefaults.cardColors(containerColor = NoteBox),
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(12.dp)
    ) {
        Column(modifier = Modifier.padding(paddingSmall)) {
            Row(
                modifier = Modifier.padding(bottom = paddingMicro),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Box(
                    modifier = Modifier
                        .size(32.dp)
                        .clip(CircleShape)
                        .background(Purple100),
                    contentAlignment = Alignment.Center
                ) {
                    Text("👤")
                }
                Spacer(Modifier.width(paddingMicro))
                Column {
                    Text(userName, style = MaterialTheme.typography.bodyMedium, fontWeight = FontWeight.Bold)
                    Text(date, style = MaterialTheme.typography.bodySmall, color = GrayTabText)
                }
            }
            Text(review, style = MaterialTheme.typography.bodyMedium, color = GrayTabText)
        }
    }
}
@Preview(showBackground = true)
@Composable
fun WalkPathDetailPagePreview() {
    val dummyPosition = MapPosition(
        top = 0.5f,
        left = 0.5f
    )
    val mockPath = UserPath(
        id = 1,
        name = "강남역 주변 산책로",
        uploader = "산책왕123",
        distance = "1.5km",
        time = "15분",
        likes = 45,
        distanceFromMe = "0.3km",
        mapPosition = dummyPosition,
        tags = listOf("🌳 자연 친화적", "🐕 반려견 동반 가능", "🌸 꽃길","👨‍👩‍👧‍👦 가족 동반")
    )

    MaterialTheme {
        TrailDetailScreen(
            path = mockPath,
            onBack = {},
            onStartFollowing = {}
        )
    }
}