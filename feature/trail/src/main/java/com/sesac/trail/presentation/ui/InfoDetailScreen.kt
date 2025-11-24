package com.sesac.trail.presentation.ui

import android.content.Intent
import android.net.Uri
import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawingPadding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.AccessTime
import androidx.compose.material.icons.filled.Call
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.NearMe
import androidx.compose.material.icons.filled.Send
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
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
import com.sesac.common.ui.theme.Android7HoursTheme
import com.sesac.common.ui.theme.Gray200
import com.sesac.common.ui.theme.GrayTabText
import com.sesac.common.ui.theme.NoteBox
import com.sesac.common.ui.theme.PaddingSection
import com.sesac.common.ui.theme.PrimaryGreenDark
import com.sesac.common.ui.theme.PrimaryGreenLight
import com.sesac.common.ui.theme.Purple600
import com.sesac.common.ui.theme.Red500
import com.sesac.common.ui.theme.White
import com.sesac.common.ui.theme.paddingLarge
import com.sesac.common.ui.theme.paddingSmall
import com.sesac.domain.model.Comment
import com.sesac.domain.model.Coord
import com.sesac.domain.model.Path
import com.sesac.domain.model.Post
import com.sesac.trail.presentation.TrailViewModel
import com.sesac.trail.presentation.component.TagFlow
import kotlinx.coroutines.launch


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

                // 2. 병원 기본 정보 & 상세 정보
                item {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(paddingLarge),
                        verticalArrangement = Arrangement.spacedBy(PaddingSection)
                    ) {
                        // 타이틀 및 평점
                        Column {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Text(
                                    text = "진료중",
                                    color = PrimaryGreenDark,
                                    style = MaterialTheme.typography.labelMedium,
                                    fontWeight = FontWeight.Bold,
                                    modifier = Modifier
                                        .background(PrimaryGreenLight, RoundedCornerShape(4.dp))
                                        .padding(horizontal = 6.dp, vertical = 2.dp)
                                )
                                Spacer(Modifier.width(8.dp))
                                Text(
                                    text = "동물병원",
                                    color = GrayTabText,
                                    style = MaterialTheme.typography.labelMedium
                                )
                            }
                            Spacer(Modifier.height(4.dp))
                            Text(
                                text = path.pathName,
                                style = MaterialTheme.typography.headlineSmall,
                                fontWeight = FontWeight.Bold
                            )
                            Spacer(Modifier.height(8.dp))
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Icon(Icons.Filled.Star, contentDescription = null, tint = Color(0xFFFFC107), modifier = Modifier.size(18.dp))
                                Text(" 4.8 (128명)", style = MaterialTheme.typography.bodyMedium, fontWeight = FontWeight.Bold)
                                Text(" · 거리 ${path.distanceFromMe}km", style = MaterialTheme.typography.bodyMedium, color = GrayTabText)
                            }
                        }

                        Divider(color = Gray200.copy(alpha = 0.3f))

                        // 상세 정보 리스트 (주소, 시간, 전화)
                        Column(verticalArrangement = Arrangement.spacedBy(16.dp)) {
                            InfoRowItem(
                                icon = Icons.Default.LocationOn,
                                text = "서울시 강남구 테헤란로 123", // 추후 path.address 사용
                                subText = "주소 복사",
                                onClick = {
                                    Toast.makeText(context, "주소가 복사되었습니다.", Toast.LENGTH_SHORT).show()
                                }
                            )
                            InfoRowItem(
                                icon = Icons.Default.AccessTime,
                                text = "오늘 09:00 - 18:00", // 추후 path.operatingHours 사용
                                subText = "접수마감 17:30",
                                isHighlight = false
                            )
                            InfoRowItem(
                                icon = Icons.Default.Call,
                                text = "02-123-4567", // 추후 path.phoneNumber 사용
                                subText = null,
                                onClick = handleCall
                            )
                        }

                        Divider(color = Gray200.copy(alpha = 0.3f))

                        // 시설 태그
                        if (path.tags.isNotEmpty()) {
                            Column {
                                Text("시설 정보", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
                                Spacer(Modifier.height(8.dp))
                                TagFlow(selectedTags = path.tags, editable = false)
                            }
                        }

                        // 병원 소개
                        Column {
                            Text("병원 소개", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
                            Spacer(Modifier.height(8.dp))
                            Text(
                                text = path.pathComment ?: "병원 소개글이 없습니다.",
                                style = MaterialTheme.typography.bodyMedium,
                                color = GrayTabText,
                                lineHeight = 24.sp
                            )
                        }
                    }
                }

                // 3. 댓글 로직 시작 (ViewModel 연결)
                item {
                    if (viewModel != null) {
                        LaunchedEffect(path) {
                            viewModel.handleOpenComments(path)
                        }
                    }
                }

                // 4. 댓글 헤더
                item {
                    val commentCount = commentsForPreview?.filter { it.postId == postForPreview?.id?.toInt() }?.size ?: 0
                    Text(
                        text = "방문자 리뷰 ($commentCount)",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.padding(horizontal = paddingLarge, vertical = 8.dp)
                    )
                }

                // 5. 댓글 리스트
                if (postForPreview != null && commentsForPreview != null) {
                    val postComments = commentsForPreview.filter { it.postId == postForPreview.id.toInt() }

                    if (postComments.isEmpty()) {
                        item {
                            Box(modifier = Modifier.fillMaxWidth().padding(20.dp), contentAlignment = Alignment.Center) {
                                Text("아직 작성된 리뷰가 없습니다.", color = GrayTabText)
                            }
                        }
                    } else {
                        items(postComments, key = { it.id }) { comment ->
                            Column(modifier = Modifier.padding(horizontal = paddingLarge, vertical = 8.dp)) {
                                CommonCommentItem(comment = comment)
                                Divider(color = Gray200.copy(alpha = 0.3f), modifier = Modifier.padding(top = 16.dp))
                            }
                        }
                    }
                }

                // 6. ✅ 댓글 입력창 (복구됨)
                item {
                    // ViewModel이 있거나 Preview 모드일 때 입력창 표시
                    if (viewModel != null) {
                        // 선택된 포스트가 있거나, 화면에 들어왔을 때(일반적으로 상세화면 들어오면 댓글 입력 가능)
                        // 원본 코드 로직 유지: viewModel.selectedPostForComments 체크
                        val post = viewModel.selectedPostForComments
                        if (post != null) {
                            CommentInputArea(
                                value = viewModel.newCommentContent,
                                onValueChange = { viewModel.newCommentContent = it },
                                onSendClick = {
                                    val success = viewModel.handleAddComment()
                                    Toast.makeText(
                                        context,
                                        if (success) "리뷰가 등록되었습니다" else "내용을 입력해주세요",
                                        Toast.LENGTH_SHORT
                                    ).show()
                                }
                            )
                        }
                    } else if (postForPreview != null) {
                        // 프리뷰용 입력창
                        CommentInputArea(
                            value = newCommentContentPreview,
                            onValueChange = {},
                            onSendClick = {},
                            enabled = false
                        )
                    }

                    // 하단 여백
                    Spacer(modifier = Modifier.height(20.dp))
                }
            }
        }
    }
}

// --- 분리된 컴포넌트들 ---

@Composable
fun CommentInputArea(
    value: String,
    onValueChange: (String) -> Unit,
    onSendClick: () -> Unit,
    enabled: Boolean = true
) {
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
                value = value,
                onValueChange = onValueChange,
                placeholder = { Text("리뷰를 남겨주세요...") },
                modifier = Modifier.weight(1f),
                shape = RoundedCornerShape(24.dp),
                colors = TextFieldDefaults.colors(
                    focusedIndicatorColor = Color.Transparent,
                    unfocusedIndicatorColor = Color.Transparent
                )
            )

            Spacer(modifier = Modifier.width(8.dp))

            IconButton(
                onClick = onSendClick,
                enabled = enabled && value.isNotBlank()
            ) {
                Icon(
                    Icons.Default.Send,
                    contentDescription = "댓글 작성",
                    tint = if (value.isNotBlank()) MaterialTheme.colorScheme.primary else Color.Gray
                )
            }
        }
    }
}

@Composable
private fun PathImageHeader(
    pathName: String,
    isFavorite: Boolean,
    onFavoriteClick: () -> Unit,
    onBackClick: () -> Unit,
    imageUrl: String
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(240.dp)
    ) {
        AsyncImage(
            model = imageUrl,
            contentDescription = pathName,
            modifier = Modifier.fillMaxSize(),
            contentScale = ContentScale.Crop
        )
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(80.dp)
                .background(
                    androidx.compose.ui.graphics.Brush.verticalGradient(
                        colors = listOf(Color.Black.copy(alpha = 0.6f), Color.Transparent)
                    )
                )
        )

        IconButton(
            onClick = onBackClick,
            modifier = Modifier
                .align(Alignment.TopStart)
                .padding(top = 40.dp, start = 16.dp)
        ) {
            Icon(
                imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                contentDescription = "Back",
                tint = White
            )
        }

        IconButton(
            onClick = onFavoriteClick,
            modifier = Modifier
                .align(Alignment.TopEnd)
                .padding(top = 40.dp, end = 16.dp)
        ) {
            Icon(
                imageVector = if (isFavorite) Icons.Filled.Favorite else Icons.Filled.FavoriteBorder,
                contentDescription = "좋아요",
                tint = if (isFavorite) Red500 else White
            )
        }
    }
}

@Composable
fun InfoRowItem(
    icon: ImageVector,
    text: String,
    subText: String? = null,
    isHighlight: Boolean = false,
    onClick: (() -> Unit)? = null
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(enabled = onClick != null, onClick = { onClick?.invoke() })
            .padding(vertical = 4.dp),
        verticalAlignment = Alignment.Top
    ) {
        Icon(
            imageVector = icon,
            contentDescription = null,
            tint = if (isHighlight) PrimaryGreenDark else GrayTabText,
            modifier = Modifier.size(24.dp)
        )
        Spacer(Modifier.width(16.dp))
        Column {
            Text(
                text = text,
                style = MaterialTheme.typography.bodyLarge,
                color = Color.Black,
                fontWeight = if (isHighlight) FontWeight.Bold else FontWeight.Normal
            )
            if (subText != null) {
                Spacer(Modifier.height(4.dp))
                Text(
                    text = subText,
                    style = MaterialTheme.typography.bodySmall,
                    color = if(onClick != null) Purple600 else GrayTabText,
                    fontWeight = if(onClick != null) FontWeight.Bold else FontWeight.Normal
                )
            }
        }
    }
}

@Composable
@Preview(showBackground = true)
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
            authorImage = "",
            authorId = -1,
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