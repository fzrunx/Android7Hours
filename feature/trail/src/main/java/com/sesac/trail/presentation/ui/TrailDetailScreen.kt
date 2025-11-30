package com.sesac.trail.presentation.ui

import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Arrangement.spacedBy
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.TrendingUp
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Navigation
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Schedule
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import coil3.compose.AsyncImage
import com.sesac.common.ui.theme.GrayTabText
import com.sesac.common.ui.theme.PaddingSection
import com.sesac.common.ui.theme.PrimaryPurpleLight
import com.sesac.common.ui.theme.Purple600
import com.sesac.common.ui.theme.White
import com.sesac.common.ui.theme.paddingLarge
import com.sesac.common.ui.theme.paddingMicro
import com.sesac.common.ui.theme.paddingSmall
import com.sesac.domain.model.Path
import com.sesac.domain.result.AuthUiState
import com.sesac.domain.result.ResponseUiState
import com.sesac.trail.presentation.TrailViewModel
import com.sesac.common.component.CommonCommentSection
import com.sesac.trail.nav_graph.TrailNavigationRoute
import com.sesac.trail.presentation.component.TagFlow


// --- Main Composable ---
@Composable
fun TrailDetailScreen(
    uiState: AuthUiState,
    viewModel: TrailViewModel = hiltViewModel<TrailViewModel>(),
    navController: NavController,
    selectedDetailPath: Path?,
    onStartFollowing: (Path) -> Unit,
    onEditClick: (Path) -> Unit,
    onDeleteClick: (Path) -> Unit,
) {
    val context = LocalContext.current
    val selectedDetailPathState by viewModel.selectedPath.collectAsStateWithLifecycle()
    val bookmarkedPathsState by viewModel.bookmarkedPaths.collectAsStateWithLifecycle()
    val commentsState by viewModel.commentsState.collectAsStateWithLifecycle()

    val isBookmarked by remember(bookmarkedPathsState, selectedDetailPathState) {
        derivedStateOf {
            val paths = (bookmarkedPathsState as? ResponseUiState.Success)?.result ?: emptyList()
            val currentPathId = selectedDetailPathState?.id
            if (currentPathId == null) false else paths.any { it.id == currentPathId }
        }
    }

    LaunchedEffect(selectedDetailPath) {
        selectedDetailPath?.let { selected ->
            viewModel.updateSelectedPath(selected)
            viewModel.getComments(selected.id)
            viewModel.getUserBookmarkedPaths(uiState.token)
        }
    }

    selectedDetailPathState?.let { selected ->
        val handleBookmark: () -> Unit = {
            viewModel.toggleBookmark(uiState.token, selected.id)
            val message = if (isBookmarked) "즐겨찾기에서 제거합니다." else "즐겨찾기에 추가합니다."
            Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
        }

        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
        ) {
            PathImageHeader(
                pathName = selected.pathName,
                isBookmarked = isBookmarked,
                onBookmarkClick = handleBookmark,
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
                        text = selected.pathName,
                        style = MaterialTheme.typography.headlineSmall,
                        fontWeight = FontWeight.Bold
                    )
                    Spacer(Modifier.height(paddingMicro))
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(
                                Icons.Filled.Person,
                                contentDescription = "Uploader",
                                modifier = Modifier.size(16.dp),
                                tint = GrayTabText
                            )
                            Spacer(Modifier.width(paddingMicro))
                            Text(
                                text = "@${selected.uploader}",
                                style = MaterialTheme.typography.bodyMedium,
                                color = GrayTabText
                            )
                        }
                        if (selected.uploader == uiState.user?.nickname) {
                            Row {
                                TextButton(
//                                    onClick = { onEditClick(selected) },
                                    onClick = { navController.navigate(TrailNavigationRoute.TrailCreateTab) },
                                    modifier = Modifier.height(32.dp)
                                ) {
                                    Text("수정", color = GrayTabText, fontSize = 14.sp)
                                }
                                TextButton(
//                                    onClick = { onDeleteClick(selected) },
                                    onClick = {
                                        viewModel.deletePath(uiState.token, selected.id)
                                        navController.popBackStack()
                                    },
                                    modifier = Modifier.height(32.dp)
                                ) {
                                    Text("삭제", color = GrayTabText, fontSize = 14.sp)
                                }
                            }
                        }
                    }
                }
                // Follow Button
                Button(
                    onClick = {
                        onStartFollowing(selected)
                        navController.navigate(TrailNavigationRoute.TrailMainTab)
                    },
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
                            value = selected.distance.toString(),
                            modifier = Modifier.weight(1f)
                        )
                        InfoCard(
                            icon = Icons.Filled.Schedule,
                            label = "소요시간",
                            value = selected.duration.toString(),
                            modifier = Modifier.weight(1f)
                        )
                    }
                    Row(horizontalArrangement = spacedBy(paddingSmall)) {
                        InfoCard(
                            icon = Icons.Filled.Favorite,
                            label = "좋아요",
                            value = "${selected.bookmarksCount}개",
                            modifier = Modifier.weight(1f)
                        )
                        InfoCard(
                            icon = Icons.AutoMirrored.Filled.TrendingUp,
                            label = "내 위치에서",
                            value = selected.distanceFromMe.toString(),
                            modifier = Modifier.weight(1f)
                        )
                    }
                }
                // Route Features
                PathSection(title = "코스 특징") {
                    if (selectedDetailPath!!.tags.isNotEmpty()) {
                        TagFlow(
                            selectedTags = selected.tags,
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
                        text = selected.pathComment ?: "소개글이 없습니다.",
                        style = MaterialTheme.typography.bodyMedium,
                        color = GrayTabText,
                        lineHeight = 24.sp
                    )
                }


                // Reviews
                PathSection(title = "이용자 후기") {
                    CommonCommentSection(
                        commentsState = commentsState,
                        currentUserId = uiState.user?.id ?: -1,
                        onPostComment = { content ->
                            uiState.token?.let { token ->
                                viewModel.createComment(token, selected.id, content)
                            } ?: Toast.makeText(context, "로그인이 필요합니다.", Toast.LENGTH_SHORT).show()
                        },
                        onUpdateComment = { commentId, content ->
                            uiState.token?.let { token ->
                                viewModel.updateComment(token, selected.id, commentId, content)
                            } ?: Toast.makeText(context, "로그인이 필요합니다.", Toast.LENGTH_SHORT).show()
                        },
                        onDeleteComment = { commentId ->
                            uiState.token?.let { token ->
                                viewModel.deleteComment(token, selected.id, commentId)
                            } ?: Toast.makeText(context, "로그인이 필요합니다.", Toast.LENGTH_SHORT).show()
                        }
                    )
                }
            }
        }
    }
}


// --- Image Header ---
@Composable
fun PathImageHeader(
    pathName: String,
    isBookmarked: Boolean,
    onBookmarkClick: () -> Unit,
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
            onClick = onBookmarkClick,
            modifier = Modifier
                .align(Alignment.TopEnd)
                .padding(paddingLarge),
            containerColor = if (isBookmarked) Purple600 else MaterialTheme.colorScheme.surface,
            contentColor = if (isBookmarked) White else GrayTabText,
            shape = CircleShape
        ) {
            Icon(
                imageVector = if (isBookmarked) Icons.Filled.Favorite else Icons.Filled.FavoriteBorder,
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