package com.sesac.community.presentation.ui

import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Share
import androidx.compose.material.icons.outlined.ChatBubbleOutline
import androidx.compose.material.icons.outlined.FavoriteBorder
import androidx.compose.material.icons.outlined.Visibility
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil3.compose.AsyncImage
import com.sesac.common.ui.theme.TextSecondary
import com.sesac.common.ui.theme.cardImageHeight
import com.sesac.common.ui.theme.iconSizeMedium
import com.sesac.common.ui.theme.paddingLarge
import com.sesac.common.ui.theme.paddingMedium
import com.sesac.common.ui.theme.paddingSmall
import com.sesac.domain.model.Post
import com.sesac.domain.type.toKoreanString


@Composable
fun PostCardView(
    post: Post,
    isMyPost: Boolean,
    onLikeToggle: (postId: Int) -> Unit,
    onEdit: (postId: Int) -> Unit,
    onDelete: (postId: Int) -> Unit,
    onCommentClick: (postId: Int) -> Unit
) {
    Surface(
        color = MaterialTheme.colorScheme.surface,
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = paddingSmall)
    ) {
        Column(modifier = Modifier.padding(paddingLarge)) {
            // --- 1. Header ---
            PostHeader(
                author = post.authUserNickname ?: "",
                authorImage = post.image ?: "",
                timeAgo = post.createdAt.toString(),
                isMyPost = isMyPost,
                onEdit = { onEdit(post.id) },
                onDelete = { onDelete(post.id) }
            )
            Spacer(modifier = Modifier.height(paddingMedium))

            // --- 2. Title ---
            Text(
                text = post.title,
                color = TextSecondary,
                fontSize = 14.sp
            )
            Spacer(modifier = Modifier.height(paddingMedium))

            // --- 3. Image ---
            post.image?.let { imageUrl ->
                AsyncImage(
                    model = imageUrl,
                    contentDescription = "Post image",
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(cardImageHeight)
                        .clip(MaterialTheme.shapes.medium),
                    contentScale = ContentScale.Crop
                )
                Spacer(modifier = Modifier.height(paddingMedium))
            }

            // --- 4. Category ---
            CategoryBadge(category = post.postType.toKoreanString())
            Spacer(modifier = Modifier.height(paddingSmall))

            // --- 5. Actions ---
            PostActions(
                likes = post.likeCount,
                comments = post.commentCount,
                views = post.viewCount,
                isLiked = post.isLiked,
                onLikeToggle = { onLikeToggle(post.id) },
                onCommentClick = { onCommentClick(post.id) }
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
        // 1. 작성자 프로필 이미지
        AsyncImage(
            model = authorImage,
            contentDescription = "Author image",
            modifier = Modifier
                .size(40.dp)
                .clip(CircleShape),
            contentScale = ContentScale.Crop
        )

        Spacer(modifier = Modifier.width(paddingSmall))

        // 2. 작성자 닉네임과 작성 시간
        Column(modifier = Modifier.weight(1f)) {
            Text(text = author, style = MaterialTheme.typography.titleMedium)
            Text(text = timeAgo, style = MaterialTheme.typography.bodySmall, color = TextSecondary)
        }

        // 3. 내 게시물일 경우 수정/삭제 메뉴
        if (isMyPost) {
            Row {
                Text("수정", modifier = Modifier.clickable(onClick = onEdit).padding(paddingSmall))
                Spacer(modifier = Modifier.width(paddingSmall))
                Text("삭제", modifier = Modifier.clickable(onClick = onDelete).padding(paddingSmall))
            }
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
    views: Int,
    isLiked: Boolean,
    onLikeToggle: () -> Unit,
    onCommentClick: () -> Unit
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Start
    ) {
        // 좋아요 버튼 - 패딩으로 터치 영역 확보
        Row(
            modifier = Modifier
                .clickable(
                    interactionSource = remember { MutableInteractionSource() },
                    indication = null
                ) { onLikeToggle() }
                .padding(horizontal = 8.dp, vertical = 8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = if (isLiked) Icons.Filled.Favorite else Icons.Outlined.FavoriteBorder,
                contentDescription = "Like",
                tint = if (isLiked) MaterialTheme.colorScheme.primary else TextSecondary,
                modifier = Modifier.size(iconSizeMedium)
            )
            Spacer(modifier = Modifier.width(4.dp))
            Text(
                text = likes.toString(),
                color = if (isLiked) MaterialTheme.colorScheme.primary else TextSecondary,
                style = MaterialTheme.typography.bodyMedium
            )
        }

        Spacer(modifier = Modifier.width(16.dp))

        // 댓글 버튼 - 패딩으로 터치 영역 확보
        Row(
            modifier = Modifier
                .clickable(
                    interactionSource = remember { MutableInteractionSource() },
                    indication = null
                ) { onCommentClick() }
                .padding(horizontal = 8.dp, vertical = 8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = Icons.Outlined.ChatBubbleOutline,
                contentDescription = "Comment",
                tint = TextSecondary,
                modifier = Modifier.size(iconSizeMedium)
            )
            Spacer(modifier = Modifier.width(4.dp))
            Text(
                text = comments.toString(),
                color = TextSecondary,
                style = MaterialTheme.typography.bodyMedium
            )
        }

        Spacer(modifier = Modifier.width(16.dp))

        // 조회수 (클릭 불가)
        Row(
            modifier = Modifier.padding(horizontal = 8.dp, vertical = 8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = Icons.Outlined.Visibility,
                contentDescription = "Views",
                tint = TextSecondary,
                modifier = Modifier.size(iconSizeMedium)
            )
            Spacer(modifier = Modifier.width(4.dp))
            Text(
                text = views.toString(),
                color = TextSecondary,
                style = MaterialTheme.typography.bodyMedium
            )
        }

        Spacer(modifier = Modifier.weight(1f))

        // 공유
        IconButton(onClick = { /* TODO: 공유 */ }) {
            Icon(
                imageVector = Icons.Default.Share,
                contentDescription = "Share",
                tint = TextSecondary,
                modifier = Modifier.size(iconSizeMedium)
            )
        }
    }
}