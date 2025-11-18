package com.sesac.community.presentation.ui

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.outlined.ChatBubbleOutline
import androidx.compose.material.icons.outlined.FavoriteBorder
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import coil3.compose.AsyncImage
import com.sesac.common.ui.theme.Gray400
import com.sesac.common.ui.theme.TextSecondary
import com.sesac.common.ui.theme.avatarSizeMedium
import com.sesac.common.ui.theme.cardImageHeight
import com.sesac.common.ui.theme.iconSizeMedium
import com.sesac.common.ui.theme.paddingLarge
import com.sesac.common.ui.theme.paddingMedium
import com.sesac.common.ui.theme.paddingMicro
import com.sesac.common.ui.theme.paddingSmall
import com.sesac.domain.model.Post


@Composable
fun PostCardView(
    post: Post,
    isMyPost: Boolean,
    onLikeToggle: () -> Unit,
    onEdit: () -> Unit,
    onDelete: () -> Unit,
    onCommentClick: () -> Unit
) {
    Surface(
        color = MaterialTheme.colorScheme.surface,
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = paddingSmall) // 게시물 사이 간격
    ) {
        Column(modifier = Modifier.Companion.padding(paddingLarge)) {
            // --- 1. 헤더 (작성자 정보) ---
            PostHeader(
                author = post.author,
                authorImage = post.authorImage,
                timeAgo = post.timeAgo,
                isMyPost = isMyPost,
                onEdit = onEdit,
                onDelete = onDelete
            )
            Spacer(modifier = Modifier.Companion.height(paddingMedium))

            // --- 2. 본문 ---
            Text(
                text = post.content,
                color = TextSecondary,
                fontSize = 14.sp
            )
            Spacer(modifier = Modifier.Companion.height(paddingMedium))

            // --- 3. 이미지 ---
            if (post.image != null) {
                AsyncImage(
                    model = post.image,
                    contentDescription = "Post image",
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(cardImageHeight)
                        .clip(MaterialTheme.shapes.medium),
                    contentScale = ContentScale.Crop,
//                    placeholder = painterResource(id = R.drawable.placeholder) // ⚠️ placeholder 이미지 추가
                )
                Spacer(modifier = Modifier.Companion.height(paddingMedium))
            }

            // --- 4. 카테고리 ---
            CategoryBadge(category = post.category)
            Spacer(modifier = Modifier.Companion.height(paddingSmall))

            // --- 5. 액션 버튼 (좋아요, 댓글, 공유) ---
            PostActions(
                likes = post.likes,
                comments = post.comments,
                isLiked = post.isLiked,
                onLikeToggle = onLikeToggle,
                onCommentClick = onCommentClick
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
            modifier = Modifier.Companion
                .size(avatarSizeMedium)
                .clip(CircleShape),
            contentScale = ContentScale.Crop,
//            placeholder = painterResource(id = R.drawable.placeholder) // ⚠️ placeholder 이미지 추가
        )
        Spacer(modifier = Modifier.Companion.width(paddingSmall))
        Column(modifier = Modifier.weight(1f)) {
            Text(author, fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.onSurface)
            Text(timeAgo, fontSize = 12.sp, color = Gray400)
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
                tint = TextSecondary
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
                        modifier = Modifier.Companion.size(iconSizeMedium)
                    )
                }
            )
            DropdownMenuItem(
                text = { Text("삭제", color = MaterialTheme.colorScheme.error) },
                onClick = {
                    onDelete()
                    expanded = false
                },
                leadingIcon = {
                    Icon(
                        Icons.Default.Delete,
                        contentDescription = "삭제",
                        tint = MaterialTheme.colorScheme.error,
                        modifier = Modifier.Companion.size(iconSizeMedium)
                    )
                }
            )
        }
    }
}

@Composable
fun CategoryBadge(category: String) {
    Surface(
        color = MaterialTheme.colorScheme.primaryContainer,
        shape = CircleShape
    ) {
        Text(
            text = category,
            style = MaterialTheme.typography.labelSmall,
            modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp)
        )
    }
}

@Composable
fun PostActions(
    likes: Int,
    comments: Int,
    isLiked: Boolean,
    onLikeToggle: () -> Unit,
    onCommentClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = paddingSmall),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(paddingLarge)
    ) {
        // 좋아요 버튼
        Row(
            modifier = Modifier.clickable(onClick = onLikeToggle),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = if (isLiked) Icons.Filled.Favorite else Icons.Outlined.FavoriteBorder,
                contentDescription = "Like",
                tint = if (isLiked) MaterialTheme.colorScheme.primary else TextSecondary,
                modifier = Modifier.Companion.size(iconSizeMedium)
            )
            Spacer(modifier = Modifier.Companion.width(paddingMicro))
            Text(
                text = likes.toString(),
                color = if (isLiked) MaterialTheme.colorScheme.primary else TextSecondary,
                fontSize = 14.sp
            )
        }
        // 댓글 버튼
        Row(
            modifier = Modifier.clickable(onClick = onCommentClick),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = Icons.Outlined.ChatBubbleOutline,
                contentDescription = "Comment",
                tint = TextSecondary,
                modifier = Modifier.Companion.size(iconSizeMedium)
            )
            Spacer(modifier = Modifier.Companion.width(paddingMicro))
            Text(
                text = comments.toString(),
                color = TextSecondary,
                fontSize = 14.sp
            )
        }
        Spacer(modifier = Modifier.weight(1f))
        // 공유 버튼
        IconButton(onClick = { /* TODO: 공유하기 */ }) {
            Icon(
                imageVector = Icons.Default.Share,
                contentDescription = "Share",
                tint = TextSecondary,
                modifier = Modifier.Companion.size(iconSizeMedium)
            )
        }
    }
}