package com.sesac.trail.presentation.ui

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.layout.Arrangement.spacedBy
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.TrendingUp
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import coil3.compose.AsyncImage
import kotlinx.coroutines.launch
import com.sesac.common.ui.theme.*
import com.sesac.domain.model.Coord
import com.sesac.domain.model.UserPath
import com.sesac.trail.presentation.TrailViewModel
import com.sesac.trail.presentation.component.TagFlow


// --- Main Composable ---
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TrailDetailScreen(
//    path: UserPath,
    viewModel: TrailViewModel = hiltViewModel<TrailViewModel>(),
    navController: NavController,
    onStartFollowing: (UserPath) -> Unit
) {
    val context = LocalContext.current
    var isFavorite by remember { mutableStateOf(false) }
//    var likeCount by remember { mutableStateOf(path.likes) }
    val selectedDetailPath by viewModel.selectedPath.collectAsStateWithLifecycle()
//    var isFavorite by remember { mutableStateOf(false) }

    val snackbarHostState = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()

    val handleLike = {
//        if (isLiked) likeCount-- else likeCount++
//        isLiked = !isLiked
        isFavorite = viewModel.updateSelectedPathLikes(isFavorite)
    }

    val handleFavorite: () -> Unit = {
//        isFavorite = !isFavorite
        scope.launch {
//            val message = if (isFavorite) "즐겨찾기에 추가되었습니다" else "즐겨찾기에서 제거되었습니다"
            val message = if (isFavorite) "즐겨찾기에 추가되었습니다" else "즐겨찾기에서 제거되었습니다"
            isFavorite = viewModel.updateSelectedPathLikes(isFavorite)
            Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
        }
    }

//    Scaffold(
//    ) { _ ->
    selectedDetailPath?.let {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
        ) {
            PathImageHeader(
                pathName = it.name,
                isFavorite = isFavorite,
                onFavoriteClick = handleFavorite
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
                        text = it.name,
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
                            text = "@${it.uploader}",
                            style = MaterialTheme.typography.bodyMedium,
                            color = GrayTabText
                        )
                    }
                }

                // Follow Button
                Button(
                    onClick = { onStartFollowing(it) },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(52.dp),
                    shape = MaterialTheme.shapes.large,
                    colors = ButtonDefaults.buttonColors(containerColor = Purple600)
                ) {
                    Icon(Icons.Filled.Navigation, contentDescription = null)
                    Spacer(Modifier.width(paddingMicro))
                    Text("이 산책로 따라가기", fontWeight = FontWeight.Bold, color = White)
                }

                // Stats Grid
                Column(verticalArrangement = Arrangement.spacedBy(paddingSmall)) {
                    Row(horizontalArrangement = spacedBy(paddingSmall)) {
                        InfoCard(
                            icon = Icons.Filled.LocationOn,
                            label = "거리",
                            value = it.distance.toString(),
                            modifier = Modifier.weight(1f)
                        )
                        InfoCard(
                            icon = Icons.Filled.Schedule,
                            label = "소요시간",
                            value = it.time.toString(),
                            modifier = Modifier.weight(1f)
                        )
                    }
                    Row(horizontalArrangement = spacedBy(paddingSmall)) {
                        InfoCard(
                            icon = Icons.Filled.Favorite,
                            label = "좋아요",
                            value = "${ selectedDetailPath!!.likes}개",
                            modifier = Modifier.weight(1f)
                        )
                        InfoCard(
                            icon = Icons.AutoMirrored.Filled.TrendingUp,
                            label = "내 위치에서",
                            value =  it.distanceFromMe.toString(),
                            modifier = Modifier.weight(1f)
                        )
                    }
                }
                // Route Features
                PathSection(title = "코스 특징") {
                    if ( selectedDetailPath!!.tags.isNotEmpty()) {
                        TagFlow(
                            selectedTags =  selectedDetailPath!!.tags,
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
                        text = it.description ?: "소개글이 없습니다.",
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
//}


// --- Image Header ---
@Composable
fun PathImageHeader(
    pathName: String,
    isFavorite: Boolean,
    onFavoriteClick: () -> Unit,
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
            onClick = onFavoriteClick,
            modifier = Modifier
                .align(Alignment.TopEnd)
                .padding(paddingLarge),
            containerColor = if (isFavorite) Purple600 else MaterialTheme.colorScheme.surface,
            contentColor = if (isFavorite) Color.White else GrayTabText,
            shape = CircleShape
        ) {
            Icon(
                imageVector = if (isFavorite) Icons.Filled.Favorite else Icons.Filled.FavoriteBorder,
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
//@Preview(showBackground = true)
//@Composable
//fun WalkPathDetailPagePreview() {
//    val dummyPosition = Coord(
//        latitude = 0.5,
//        longitude = 0.5,
//    )
//    val mockPath = UserPath(
//        id = 1,
//        name = "강남역 주변 산책로",
//        userId = -1,
//        distance = 1.5f,
//        time = 15,
//        likes = 45,
//        distanceFromMe = 0.3f,
//        coord = listOf(dummyPosition),
//        tags = listOf("🌳 자연 친화적", "🐕 반려견 동반 가능", "🌸 꽃길","👨‍👩‍👧‍👦 가족 동반")
//    )
//
//    val navController = rememberNavController()
//    Android7HoursTheme {
//        TrailDetailScreen(
////            path = mockPath,
//            navController = navController,
//            onStartFollowing = {}
//        )
//    }
//}