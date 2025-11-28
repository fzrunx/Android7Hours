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
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.AccessTime
import androidx.compose.material.icons.filled.Call
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Star
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
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
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
import com.sesac.common.component.CommonCommentSection
import com.sesac.common.ui.theme.Android7HoursTheme
import com.sesac.common.ui.theme.Gray200
import com.sesac.common.ui.theme.GrayTabText
import com.sesac.common.ui.theme.PaddingSection
import com.sesac.common.ui.theme.PrimaryGreenDark
import com.sesac.common.ui.theme.PrimaryGreenLight
import com.sesac.common.ui.theme.Purple600
import com.sesac.common.ui.theme.Red500
import com.sesac.common.ui.theme.White
import com.sesac.common.ui.theme.paddingLarge
import com.sesac.domain.model.CommentType
import com.sesac.domain.model.Place
import com.sesac.domain.model.PlaceCategory
import com.sesac.trail.presentation.TrailViewModel
import com.sesac.trail.presentation.component.TagFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PlaceInfoDetailScreen(
    place: Place,
    onBackClick: () -> Unit = {},
    viewModel: TrailViewModel
) {
    val context = LocalContext.current
    var isFavorite by remember(place.isBookmarked) { mutableStateOf(place.isBookmarked) }
    val snackbarHostState = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()

    // ViewModel에서 댓글 상태 가져오기
    val commentsState by viewModel.commentsState.collectAsState()
    val currentUserId = viewModel.currentUserId

    // 화면 진입 시 댓글 로드
    LaunchedEffect(place.id) {
        viewModel.loadPlaceComments(place.id)
    }

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

    // 주소 복사 핸들러
    val handleCopyAddress = {
        place.address?.let { address ->
            val clipboard = context.getSystemService(android.content.Context.CLIPBOARD_SERVICE)
                    as android.content.ClipboardManager
            val clip = android.content.ClipData.newPlainText("address", address)
            clipboard.setPrimaryClip(clip)
            Toast.makeText(context, "주소가 복사되었습니다.", Toast.LENGTH_SHORT).show()
        }
    }

    Scaffold(
        snackbarHost = { SnackbarHost(snackbarHostState) },

    ) { innerPadding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
                // 1. 상단 이미지 헤더
                item {
                    PlaceImageHeader(
                        placeName = place.title,
                        isFavorite = isFavorite,
                        onFavoriteClick = handleFavorite,
                        onBackClick = onBackClick,
                        imageUrl = place.imageUrl
                            ?: "https://images.unsplash.com/photo-1519494026892-80bbd2d6fd0d?ixlib=rb-4.0.3&auto=format&fit=crop&w=1080&q=80"
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
                                    text = place.category.name,
                                    color = GrayTabText,
                                    style = MaterialTheme.typography.labelMedium
                                )
                            }
                            Spacer(Modifier.height(4.dp))
                            Text(
                                text = place.title,
                                style = MaterialTheme.typography.headlineSmall,
                                fontWeight = FontWeight.Bold
                            )
                            Spacer(Modifier.height(8.dp))
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Icon(
                                    Icons.Filled.Star,
                                    contentDescription = null,
                                    tint = Color(0xFFFFC107),
                                    modifier = Modifier.size(18.dp)
                                )
                                Text(
                                    " ${place.rating} (${place.reviewCount}명)",
                                    style = MaterialTheme.typography.bodyMedium,
                                    fontWeight = FontWeight.Bold
                                )
                                place.distance?.let { dist ->
                                    Text(
                                        " · 거리 ${String.format("%.1f", dist)}km",
                                        style = MaterialTheme.typography.bodyMedium,
                                        color = GrayTabText
                                    )
                                }
                            }
                        }

                        Divider(color = Gray200.copy(alpha = 0.3f))

                        // 상세 정보 리스트 (주소, 시간, 전화)
                        Column(verticalArrangement = Arrangement.spacedBy(16.dp)) {
                            // 주소
                            place.address?.let { address ->
                                InfoRowItem(
                                    icon = Icons.Default.LocationOn,
                                    text = address,
                                    subText = "주소 복사",
                                    onClick = {
                                        val intent = Intent(Intent.ACTION_DIAL, Uri.parse("tel:${place.tel}"))
                                        context.startActivity(intent)
                                    }
                                )
                            }

                            // 영업시간
                            place.operatingHours?.let { hours ->
                                InfoRowItem(
                                    icon = Icons.Default.AccessTime,
                                    text = hours,
                                    subText = null
                                )
                            }

                            // 전화번호
                            place.tel?.let { phone ->
                                InfoRowItem(
                                    icon = Icons.Default.Call,
                                    text = phone,
                                    subText = null,
                                    onClick = handleCall
                                )
                            }
                        }

                        Divider(color = Gray200.copy(alpha = 0.3f))

                        // 시설 태그
                        if (place.tags.isNotEmpty()) {
                            Column {
                                Text(
                                    "시설 정보",
                                    style = MaterialTheme.typography.titleMedium,
                                    fontWeight = FontWeight.Bold
                                )
                                Spacer(Modifier.height(8.dp))
                                TagFlow(selectedTags = place.tags, editable = false)
                            }
                            Divider(color = Gray200.copy(alpha = 0.3f))
                        }

                        // 병원 소개
                        place.description?.let { desc ->
                            Column {
                                Text(
                                    "${place.category.name} 소개",
                                    style = MaterialTheme.typography.titleMedium,
                                    fontWeight = FontWeight.Bold
                                )
                                Spacer(Modifier.height(8.dp))
                                Text(
                                    text = desc,
                                    style = MaterialTheme.typography.bodyMedium,
                                    color = GrayTabText,
                                    lineHeight = 24.sp
                                )
                            }
                            Divider(color = Gray200.copy(alpha = 0.3f))
                        }
                    }
                }


                // 3. 댓글 로직 시작 (ViewModel 연결)
            item {
                Column(modifier = Modifier.padding(horizontal = paddingLarge)) {
                    Text(
                        text = "방문자 리뷰",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.padding(vertical = 8.dp)
                    )

                    CommonCommentSection(
                        commentsState = commentsState,
                        currentUserId = currentUserId,
                        onPostComment = { content ->
                            viewModel.postPlaceComment(place.id, content, CommentType.PATH)
                        },
                        onUpdateComment = { commentId, content ->
                            viewModel.updatePlaceComment(place.id, commentId, content, CommentType.PATH)
                        },
                        onDeleteComment = { commentId ->
                            viewModel.deletePlaceComment(place.id, commentId, CommentType.PATH)
                        }
                    )

                    Spacer(modifier = Modifier.height(20.dp))
                }
            }
        }
    }


}

// --- 분리된 컴포넌트들 ---

@Composable
private fun PlaceImageHeader(
    placeName: String,
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
            contentDescription = placeName,
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
                    color = if (onClick != null) Purple600 else GrayTabText,
                    fontWeight = if (onClick != null) FontWeight.Bold else FontWeight.Normal
                )
            }
        }
    }
}

