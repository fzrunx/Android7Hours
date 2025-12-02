package com.sesac.trail.presentation.ui

import android.widget.Toast
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.TrendingUp
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Schedule
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import com.sesac.common.ui.theme.*
import com.sesac.domain.model.Comment
import com.sesac.domain.model.PostListItem
import com.sesac.domain.model.Path
import com.sesac.trail.presentation.TrailViewModel
import kotlinx.coroutines.launch
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.filled.Send
import androidx.compose.ui.Alignment
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.sesac.common.component.CommonCommentItem


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun InfoDetailScreen(
    selectedDetailPath: Path?,
    onStartFollowing: (Path) -> Unit,
    viewModel: TrailViewModel? = null,
    postForPreview: PostListItem? = null,
    commentsForPreview: List<Comment>? = null,
    newCommentContentPreview: String = ""
) {
    var isLiked by remember { mutableStateOf(false) }
    var isFavorite by remember { mutableStateOf(false) }

    val snackbarHostState = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()

    val handleLike = {}

    val handleFavorite: () -> Unit = {
        isFavorite = !isFavorite
        scope.launch {
            val message = if (isFavorite) "즐겨찾기에 추가되었습니다" else "즐겨찾기에서 제거되었습니다"
            snackbarHostState.showSnackbar(message)
        }
    }

    selectedDetailPath?.let {
        LazyColumn(
            modifier = Modifier.fillMaxSize()
        ) {
            // 헤더
            item {
                PathImageHeader(
                    pathName = it.pathName,
                    isBookmarked = isFavorite,
                    onBookmarkClick = handleFavorite
                )
            }

            // 제목
            item {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(paddingLarge),
                    verticalArrangement = Arrangement.spacedBy(PaddingSection)
                ) {
                    Column {
                        Text(
                            text = it.pathName,
                            style = MaterialTheme.typography.headlineSmall,
                            fontWeight = FontWeight.Bold
                        )
                        Spacer(Modifier.height(paddingMicro))
                    }
                }
            }

            // 정보 카드들
            item {
                Column(
                    modifier = Modifier.padding(horizontal = paddingLarge),
                    verticalArrangement = Arrangement.spacedBy(paddingSmall)
                ) {
                    InfoCard(
                        icon = Icons.Filled.LocationOn,
                        label = "주소",
                        value = "서울시 주소주소주소",
                        modifier = Modifier.fillMaxWidth()
                    )
                    InfoCard(
                        icon = Icons.Filled.Schedule,
                        label = "영업중인 상태 등등...",
                        value = "영업중이든 뭐든 정보 집어넣기",
                        modifier = Modifier.fillMaxWidth()
                    )

                    Row(horizontalArrangement = Arrangement.spacedBy(paddingSmall)) {
                        InfoCard(
                            icon = Icons.Filled.Favorite,
                            label = "좋아요",
                            value = "${it.likes}개",
                            modifier = Modifier.weight(1f)
                        )
                        InfoCard(
                            icon = Icons.AutoMirrored.Filled.TrendingUp,
                            label = "내 위치에서",
                            value = it.distanceFromMe.toString(),
                            modifier = Modifier.weight(1f)
                        )
                    }
                }
            }

            item {
                Spacer(Modifier.height(paddingMicro))
            }

            // 정보 섹션
            item {
                Column(modifier = Modifier.padding(horizontal = paddingLarge)) {
                    PathSection(title = "정보") {
                        Text(
                            text = "병원 또는 내용",
                            style = MaterialTheme.typography.bodyMedium,
                            color = GrayTabText,
                            lineHeight = 24.sp
                        )
                    }
                }
            }

            // ⭐ viewModel LaunchedEffect
            item {
                if (viewModel != null) {
                    LaunchedEffect(it) {
                        it.let { viewModel.handleOpenComments(it) }
                    }
                }
            }

            // 댓글 헤더
            item {
                val commentCount = commentsForPreview?.filter { it.postId == postForPreview?.id?.toInt() }?.size ?: 0

                if (postForPreview != null) {
                    Text(
                        text = "댓글 ($commentCount)",
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.padding(horizontal = paddingLarge, vertical = 16.dp)
                    )
                }
            }

            // 댓글 리스트
            if (postForPreview != null && commentsForPreview != null) {
                val postComments = commentsForPreview.filter { it.postId == postForPreview.id.toInt() }

                items(postComments, key = { it.id }) { comment ->
                    Column(modifier = Modifier.padding(horizontal = paddingLarge, vertical = 8.dp)) {
                        CommonCommentItem(comment = comment)
                    }
                }
            }

            // 댓글 입력창 (스크롤 영역 안에 배치)
            item {
                if (viewModel != null) {
                    viewModel.selectedPostForComments?.let { post ->
                        val context = LocalContext.current
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(paddingLarge)
                        ) {
                            Spacer(modifier = Modifier.height(8.dp))

                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                TextField(
                                    value = viewModel.newCommentContent,
                                    onValueChange = { viewModel.newCommentContent = it },
                                    placeholder = { Text("댓글 달기...") },
                                    modifier = Modifier.weight(1f),
                                    shape = RoundedCornerShape(24.dp),
                                    colors = TextFieldDefaults.colors(
                                        focusedIndicatorColor = Color.Transparent,
                                        unfocusedIndicatorColor = Color.Transparent
                                    )
                                )

                                Spacer(modifier = Modifier.width(8.dp))

                                IconButton(
                                    onClick = {
                                        val success = viewModel.handleAddComment()
                                        Toast.makeText(
                                            context,
                                            if (success) "댓글이 작성되었습니다" else "댓글 내용을 입력해주세요",
                                            Toast.LENGTH_SHORT
                                        ).show()
                                    },
                                    enabled = viewModel.newCommentContent.isNotBlank()
                                ) {
                                    Icon(
                                        Icons.Default.Send,
                                        contentDescription = "댓글 작성",
                                        tint = if (viewModel.newCommentContent.isNotBlank())
                                            MaterialTheme.colorScheme.primary else Color.Gray
                                    )
                                }
                            }
                        }
                    }
                } else if (postForPreview != null) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(paddingLarge)
                    ) {
                        Spacer(modifier = Modifier.height(8.dp))

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            TextField(
                                value = newCommentContentPreview,
                                onValueChange = {},
                                placeholder = { Text("댓글 달기...") },
                                modifier = Modifier.weight(1f),
                                shape = RoundedCornerShape(24.dp),
                                colors = TextFieldDefaults.colors(
                                    focusedIndicatorColor = Color.Transparent,
                                    unfocusedIndicatorColor = Color.Transparent
                                )
                            )

                            Spacer(modifier = Modifier.width(8.dp))

                            IconButton(
                                onClick = {},
                                enabled = false
                            ) {
                                Icon(
                                    Icons.Default.Send,
                                    contentDescription = "댓글 작성",
                                    tint = Color.Gray
                                )
                            }
                        }
                    }
                }
            }

            // 하단 여백
            item {
                Spacer(Modifier.height(16.dp))
            }
        }
    }
}


//@Composable
//@Preview(showBackground = true)
//fun InfoDetailScreenPreview() {
//    val dummyPos = LatLngPoint(0.5, 0.5)
//    val mockPath = UserPath(
//        id = 1,
//        name = "병원 정보",
//        uploader = "산책왕123",
//        distance = 1.5f,
//        time = 15,
//        likes = 45,
//        distanceFromMe = 0.3f,
//        coord = listOf(Coord(0.5,0.5)),
//        tags = listOf("🌳 자연친화적", "🐕 반려견 동반 가능")
//    )
//
//    // ⭐ Mock 데이터를 실제로 넣어주기
//    val mockPost = Post.EMPTY
//
//    val mockComments = listOf(
//        Comment(
//            id = 1,
//            postId = 1,
//            author = "댓글유저1",
//            timeAgo = "1일 전",
//            content = "좋은 정보 감사합니다!",
//            authorImage = ""
//        ),
//        Comment(
//            id = 2,
//            postId = 1,
//            author = "댓글유저2",
//            timeAgo = "3일 전",
//            content = "여기 진짜 좋아요!",
//            authorImage = ""
//        )
//    )
//
//    Android7HoursTheme {
//        InfoDetailScreen(
//            selectedDetailPath = mockPath,
//            onStartFollowing = {},
//            // ✅ null이 아닌 실제 데이터 전달
//            postForPreview = mockPost,
//            commentsForPreview = mockComments,
//            newCommentContentPreview = ""
//        )
//    }
//}