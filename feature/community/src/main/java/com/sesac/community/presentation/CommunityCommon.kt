package com.sesac.community.presentation

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.outlined.ChatBubbleOutline
import androidx.compose.material.icons.outlined.FavoriteBorder
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil3.compose.AsyncImage
import coil3.request.ImageRequest
import coil3.request.crossfade
import com.sesac.common.R // ⚠️ 본인의 R 패키지 경로로 수정하세요.

@Composable
fun CommunityAppBar(
    isSearchOpen: Boolean,
    onSearchToggle: () -> Unit,
    onNavigateToHome: () -> Unit
) {
    Surface(
        color = AppTheme.header,
        shadowElevation = if (isSearchOpen) 0.dp else 4.dp // 검색창 열리면 그림자 제거
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(AppTheme.headerHeight)
                .padding(horizontal = AppTheme.paddingLarge),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            val context = LocalContext.current
            // ⚠️ 'logo_image'를 res/drawable에 추가한 리소스 이름으로 변경하세요.
            IconButton(onClick = onNavigateToHome) {
                AsyncImage(
                    model = ImageRequest.Builder(context)
                        .data(R.drawable.image7hours)
                        .crossfade(true)
                        .build(),
                    contentDescription = "7hours",
                    contentScale = ContentScale.Crop,
                    modifier = Modifier.size(40.dp)
                )
            }
            Text(
                text = "커뮤니티",
                color = AppTheme.textPrimary,
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
            )
            IconButton(onClick = onSearchToggle) {
                Icon(
                    imageVector = Icons.Default.Search,
                    contentDescription = "Search",
                    tint = AppTheme.textSecondary,
                    modifier = Modifier.size(24.dp)
                )
            }
        }
    }
}

@Composable
fun SearchContent(query: String, onQueryChange: (String) -> Unit) {
    Surface(color = AppTheme.header) {
        OutlinedTextField(
            value = query,
            onValueChange = onQueryChange,
            placeholder = { Text("커뮤니티 검색") },
            leadingIcon = {
                Icon(
                    Icons.Default.Search,
                    contentDescription = null,
                    tint = AppTheme.textDisabled
                )
            },
            trailingIcon = {
                if (query.isNotEmpty()) {
                    IconButton(onClick = { onQueryChange("") }) {
                        Icon(
                            Icons.Default.Close,
                            contentDescription = "Clear search",
                            tint = AppTheme.textDisabled
                        )
                    }
                }
            },
            modifier = Modifier
                .fillMaxWidth()
                .padding(
                    start = AppTheme.paddingLarge,
                    end = AppTheme.paddingLarge,
                    bottom = AppTheme.paddingMedium
                ),
            shape = AppTheme.buttonShape,
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = AppTheme.primary,
                unfocusedBorderColor = AppTheme.border,
                focusedContainerColor = AppTheme.surface,
                unfocusedContainerColor = AppTheme.surface,
            ),
            singleLine = true
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FilterTabs(selectedFilter: String, onFilterSelected: (String) -> Unit) {
    val filters = listOf("전체", "인기글", "산책후기", "정보공유", "질문")

    Surface(
        color = AppTheme.surface,
        shadowElevation = 2.dp
    ) {
        LazyRow(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = AppTheme.paddingMedium),
            contentPadding = PaddingValues(horizontal = AppTheme.paddingLarge),
            horizontalArrangement = Arrangement.spacedBy(AppTheme.paddingSmall)
        ) {
            items(filters) { filter ->
                FilterChip(
                    selected = selectedFilter == filter,
                    onClick = { onFilterSelected(filter) },
                    label = { Text(filter) },
                    colors = FilterChipDefaults.filterChipColors(
                        containerColor = AppTheme.buttonSecondary,
                        labelColor = AppTheme.onButtonSecondary,
                        selectedContainerColor = AppTheme.primary,
                        selectedLabelColor = Color.White
                    ),
                    border = null
                )
            }
        }
    }
}

@Composable
fun PostCard(
    post: Post,
    isMyPost: Boolean,
    onLikeToggle: () -> Unit,
    onEdit: () -> Unit,
    onDelete: () -> Unit
) {
    Surface(
        color = AppTheme.surface,
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = AppTheme.paddingSmall) // 게시물 사이 간격
    ) {
        Column(modifier = Modifier.padding(AppTheme.paddingLarge)) {
            // --- 1. 헤더 (작성자 정보) ---
            PostHeader(
                author = post.author,
                authorImage = post.authorImage,
                timeAgo = post.timeAgo,
                isMyPost = isMyPost,
                onEdit = onEdit,
                onDelete = onDelete
            )
            Spacer(modifier = Modifier.height(AppTheme.paddingMedium))

            // --- 2. 본문 ---
            Text(
                text = post.content,
                color = AppTheme.textSecondary,
                fontSize = 14.sp
            )
            Spacer(modifier = Modifier.height(AppTheme.paddingMedium))

            // --- 3. 이미지 ---
            if (post.image != null) {
                AsyncImage(
                    model = post.image,
                    contentDescription = "Post image",
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(AppTheme.cardImageHeight)
                        .clip(AppTheme.cardShape),
                    contentScale = ContentScale.Crop,
//                    placeholder = painterResource(id = R.drawable.placeholder) // ⚠️ placeholder 이미지 추가
                )
                Spacer(modifier = Modifier.height(AppTheme.paddingMedium))
            }

            // --- 4. 카테고리 ---
            CategoryBadge(category = post.category)
            Spacer(modifier = Modifier.height(AppTheme.paddingSmall))

            // --- 5. 액션 버튼 (좋아요, 댓글, 공유) ---
            PostActions(
                likes = post.likes,
                comments = post.comments,
                isLiked = post.isLiked,
                onLikeToggle = onLikeToggle
            )
        }
    }
}

@Composable
fun PostHeader(
    author: String,
    authorImage: String,
    timeAgo: String,
    isMyPost: Boolean,
    onEdit: () -> Unit,
    onDelete: () -> Unit
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
    ) {
        AsyncImage(
            model = authorImage,
            contentDescription = "$author profile picture",
            modifier = Modifier
                .size(AppTheme.avatarSize)
                .clip(CircleShape),
            contentScale = ContentScale.Crop,
//            placeholder = painterResource(id = R.drawable.placeholder) // ⚠️ placeholder 이미지 추가
        )
        Spacer(modifier = Modifier.width(AppTheme.paddingSmall))
        Column(modifier = Modifier.weight(1f)) {
            Text(author, fontWeight = FontWeight.Bold, color = AppTheme.textPrimary)
            Text(timeAgo, fontSize = 12.sp, color = AppTheme.textDisabled)
        }
        if (isMyPost) {
            PostMenu(onEdit = onEdit, onDelete = onDelete)
        }
    }
}

@Composable
fun PostMenu(onEdit: () -> Unit, onDelete: () -> Unit) {
    var expanded by remember { mutableStateOf(false) }
    Box {
        IconButton(onClick = { expanded = true }) {
            Icon(
                Icons.Default.MoreVert,
                contentDescription = "게시물 옵션",
                tint = AppTheme.textSecondary
            )
        }
        DropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false }
        ) {
            DropdownMenuItem(
                text = { Text("수정") },
                onClick = {
                    onEdit()
                    expanded = false
                },
                leadingIcon = {
                    Icon(
                        Icons.Default.Edit,
                        contentDescription = "수정",
                        modifier = Modifier.size(AppTheme.iconSize)
                    )
                }
            )
            DropdownMenuItem(
                text = { Text("삭제", color = AppTheme.error) },
                onClick = {
                    onDelete()
                    expanded = false
                },
                leadingIcon = {
                    Icon(
                        Icons.Default.Delete,
                        contentDescription = "삭제",
                        tint = AppTheme.error,
                        modifier = Modifier.size(AppTheme.iconSize)
                    )
                }
            )
        }
    }
}

@Composable
fun CategoryBadge(category: String) {
    Surface(
        color = AppTheme.primaryContainer,
        shape = CircleShape
    ) {
        Text(
            text = category,
            color = AppTheme.onPrimaryContainer,
            fontSize = 12.sp,
            modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp)
        )
    }
}

@Composable
fun PostActions(
    likes: Int,
    comments: Int,
    isLiked: Boolean,
    onLikeToggle: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = AppTheme.paddingSmall),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(AppTheme.paddingLarge)
    ) {
        // 좋아요 버튼
        Row(
            modifier = Modifier.clickable(onClick = onLikeToggle),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = if (isLiked) Icons.Filled.Favorite else Icons.Outlined.FavoriteBorder,
                contentDescription = "Like",
                tint = if (isLiked) AppTheme.primary else AppTheme.textSecondary,
                modifier = Modifier.size(AppTheme.iconSize)
            )
            Spacer(modifier = Modifier.width(AppTheme.paddingMicro))
            Text(
                text = likes.toString(),
                color = if (isLiked) AppTheme.primary else AppTheme.textSecondary,
                fontSize = 14.sp
            )
        }
        // 댓글 버튼
        Row(
            modifier = Modifier.clickable { /* TODO: 댓글 화면 이동 */ },
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = Icons.Outlined.ChatBubbleOutline,
                contentDescription = "Comment",
                tint = AppTheme.textSecondary,
                modifier = Modifier.size(AppTheme.iconSize)
            )
            Spacer(modifier = Modifier.width(AppTheme.paddingMicro))
            Text(
                text = comments.toString(),
                color = AppTheme.textSecondary,
                fontSize = 14.sp
            )
        }
        Spacer(modifier = Modifier.weight(1f))
        // 공유 버튼
        IconButton(onClick = { /* TODO: 공유하기 */ }) {
            Icon(
                imageVector = Icons.Default.Share,
                contentDescription = "Share",
                tint = AppTheme.textSecondary,
                modifier = Modifier.size(AppTheme.iconSize)
            )
        }
    }
}

/**
 * [리팩토링]
 * 생성/수정 다이얼로그를 하나로 통합
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PostEditorDialog(
    initialPost: Post? = null, // null이면 '새 글' 모드, 아니면 '수정' 모드
    onDismiss: () -> Unit,
    onSave: (content: String, image: String, category: String) -> Unit
) {
    val isEditMode = initialPost != null
    val title = if (isEditMode) "게시글 수정" else "게시글 작성"
    val buttonText = if (isEditMode) "수정" else "작성"

    var content by remember { mutableStateOf(initialPost?.content ?: "") }
    var image by remember { mutableStateOf(initialPost?.image ?: "") }
    var category by remember { mutableStateOf(initialPost?.category ?: "산책후기") }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text(title) },
        text = {
            Column(modifier = Modifier.verticalScroll(rememberScrollState())) {
                // 1. 카테고리 선택
                Text("카테고리", style = MaterialTheme.typography.labelMedium)
                Spacer(modifier = Modifier.height(AppTheme.paddingSmall))
                LazyRow(horizontalArrangement = Arrangement.spacedBy(AppTheme.paddingSmall)) {
                    val categories = listOf("산책후기", "정보공유", "질문")
                    items(categories) { cat ->
                        FilterChip(
                            selected = category == cat,
                            onClick = { category = cat },
                            label = { Text(cat) },
                            colors = FilterChipDefaults.filterChipColors(
                                selectedContainerColor = AppTheme.primary,
                                selectedLabelColor = Color.White
                            )
                        )
                    }
                }
                Spacer(modifier = Modifier.height(AppTheme.paddingLarge))

                // 2. 내용 입력
                Text("내용", style = MaterialTheme.typography.labelMedium)
                Spacer(modifier = Modifier.height(AppTheme.paddingSmall))
                OutlinedTextField(
                    value = content,
                    onValueChange = { content = it },
                    placeholder = { Text("무슨 생각을 하고 계신가요?") },
                    modifier = Modifier
                        .fillMaxWidth()
                        .heightIn(min = 120.dp),
                )
                Spacer(modifier = Modifier.height(AppTheme.paddingLarge))

                // 3. 이미지 URL 입력
                Text("이미지 URL (선택)", style = MaterialTheme.typography.labelMedium)
                Spacer(modifier = Modifier.height(AppTheme.paddingSmall))
                OutlinedTextField(
                    value = image,
                    onValueChange = { image = it },
                    placeholder = { "https://..." },
                    modifier = Modifier.fillMaxWidth(),
                    leadingIcon = { Icon(Icons.Default.Image, null) }
                )
                if (image.isNotBlank()) {
                    AsyncImage(
                        model = image,
                        contentDescription = "Image preview",
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = AppTheme.paddingSmall)
                            .height(120.dp)
                            .clip(AppTheme.cardShape),
                        contentScale = ContentScale.Crop,
//                        placeholder = painterResource(id = R.drawable.placeholder)
                    )
                }
            }
        },
        confirmButton = {
            Button(
                onClick = { onSave(content, image, category) },
                enabled = content.isNotBlank()
            ) {
                Text(buttonText)
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("취소")
            }
        }
    )
}