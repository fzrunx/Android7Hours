package com.sesac.trail.presentation.ui

import android.content.Intent
import android.net.Uri
import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil3.compose.AsyncImage
import com.sesac.common.component.CommonCommentItem
import com.sesac.common.ui.theme.*
import com.sesac.domain.model.Comment
import com.sesac.domain.model.Coord
import com.sesac.domain.model.Post
import com.sesac.domain.model.Path
import com.sesac.trail.presentation.TrailViewModel
import com.sesac.trail.presentation.component.TagFlow
import kotlinx.coroutines.launch
import java.util.Date
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.automirrored.filled.TrendingUp
import androidx.compose.material.icons.filled.Send
import androidx.compose.ui.unit.dp
import com.sesac.common.component.CommonCommentItem


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun InfoDetailScreen(
    selectedDetailPath: Path?,
    onStartFollowing: (Path) -> Unit, // 길찾기 기능 연결
    onBackClick: () -> Unit = {},
    viewModel: TrailViewModel? = null,
    postForPreview: Post? = null,
    commentsForPreview: List<Comment>? = null,
    newCommentContentPreview: String = ""
) {
    val context = LocalContext.current
    var isFavorite by remember { mutableStateOf(false) }

    // 🔴 UserPath에 imageUri가 없으므로 임시 변수 사용 (병원 기본 이미지)
    val defaultHospitalImage =
        "https://images.unsplash.com/photo-1519494026892-80bbd2d6fd0d?ixlib=rb-4.0.3&auto=format&fit=crop&w=1080&q=80"
    // 추후 UserPath에 image 필드가 생기면: val imageUri = selectedDetailPath?.imageUri
    val imageUri: String? = null

    val snackbarHostState = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()

    // 즐겨찾기 핸들러
    val handleFavorite: () -> Unit = {
        isFavorite = !isFavorite
        scope.launch {
            val message = if (isFavorite) "즐겨찾기에 추가되었습니다" else "즐겨찾기에서 제거되었습니다"
            snackbarHostState.showSnackbar(message)
        }
    }

    // 전화걸기 핸들러
    val handleCall = {
        val phoneNumber = "02-123-4567" // 실제 데이터 연결 필요
        val intent = Intent(Intent.ACTION_DIAL, Uri.parse("tel:$phoneNumber"))
        context.startActivity(intent)
    }

    Scaffold(
        snackbarHost = { SnackbarHost(snackbarHostState) },
        bottomBar = {
            // 하단 고정 액션 바 (전화하기 / 길찾기)
            Surface(
                shadowElevation = 16.dp,
                color = White,
                shape = RoundedCornerShape(topStart = 16.dp, topEnd = 16.dp)
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(paddingLarge)
                        .safeDrawingPadding(),
                    horizontalArrangement = Arrangement.spacedBy(paddingSmall)
                ) {
                    // 전화하기 버튼
                    Button(
                        onClick = handleCall,
                        modifier = Modifier
                            .weight(1f)
                            .height(52.dp),
                        shape = RoundedCornerShape(12.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = NoteBox,
                            contentColor = Purple600
                        )
                    ) {
                        Icon(Icons.Default.Call, contentDescription = null)
                        Spacer(Modifier.width(8.dp))
                        Text("전화하기", fontWeight = FontWeight.Bold)
                    }

                    // 길찾기 버튼
                    Button(
                        onClick = { selectedDetailPath?.let { onStartFollowing(it) } },
                        modifier = Modifier
                            .weight(2f),
                        shape = RoundedCornerShape(12.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = Purple600,
                            contentColor = White
                        )
                    ) {
                        Icon(Icons.Default.NearMe, contentDescription = null)
                        Spacer(Modifier.width(8.dp))
                        Text("길찾기", fontWeight = FontWeight.Bold)
                    }
                }
            }
        }
    ) { innerPadding ->
        selectedDetailPath?.let { path ->
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding)
            ) {
                // 1. 상단 이미지 헤더
                item {
                    PathImageHeader(
                        pathName = path.pathName,
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
                                text = path.pathName,
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
                                value = "${selectedDetailPath.likes}개",
                                modifier = Modifier.weight(1f)
                            )
                            InfoCard(
                                icon = Icons.AutoMirrored.Filled.TrendingUp,
                                label = "내 위치에서",
                                value = path.distanceFromMe.toString(),
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
                        LaunchedEffect(selectedDetailPath) {
                            selectedDetailPath?.let { viewModel.handleOpenComments(it) }
                        }
                    }
                }

                // 댓글 헤더
                item {
                    val commentCount =
                        commentsForPreview?.filter { it.postId == postForPreview?.id?.toInt() }?.size
                            ?: 0

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
                    val postComments =
                        commentsForPreview.filter { it.postId == postForPreview.id.toInt() }

                    items(postComments, key = { it.id }) { comment ->
                        Column(
                            modifier = Modifier.padding(
                                horizontal = paddingLarge,
                                vertical = 8.dp
                            )
                        ) {
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
}

@Preview(showBackground = true)
@Composable
fun InfoDetailScreenPreview() {
    val mockPath = Path(
        id = 1,
        pathName = "튼튼 동물병원",
        uploader = "admin",
        distance = 1.2f,
        duration = 0,
        level = null,
        likes = 350,
        distanceFromMe = 0.8f,
        coord = listOf(Coord(0.0, 0.0)),
        tags = listOf("24시간", "응급진료", "고양이친화", "주차가능"),
        pathComment = "최신 의료 장비와 최고의 의료진이 함께하는 튼튼 동물병원입니다.\n[진료과목]\n내과, 외과, 영상의학과, 치과",
        bookmarksCount = 0,
        isBookmarked = false,
    )

//    val mockPost = Post(
//        id = 1L,
//        author = "강아지맘",
//        authorImage = "",
//        timeAgo = "1일 전",
//        content = "내용",
//        image = null,
//        likes = 5,
//        comments = 2,
//        isLiked = false,
//        category = "리뷰",
//        createdAt = Date()
//    )

    val mockComments = listOf(
        Comment(
            id = 1,
            postId = 1,
            author = "고양이집사",
            timeAgo = "3시간 전",
            content = "선생님이 정말 친절하세요!",
            authorImage = ""
        )
    )

    Android7HoursTheme {
        InfoDetailScreen(
            selectedDetailPath = mockPath,
            onStartFollowing = {},
            onBackClick = {},
            postForPreview = null,
            commentsForPreview = mockComments,
            newCommentContentPreview = ""
        )
    }
}