package com.sesac.common.component

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Send
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.sesac.domain.model.Comment
import com.sesac.domain.model.PostListItem

@Composable
fun CommonCommentSheetContent(
    modifier: Modifier = Modifier,
    post: PostListItem,
    comments: List<Comment>,
    newCommentContent: String,
    onNewCommentChange: (String) -> Unit,
    onAddComment: () -> Unit,
) {
    val postComments by remember(post, comments) {
        derivedStateOf { comments.filter { it.postId == post.id.toInt() } }
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        // Header
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "댓글 (${postComments.size})",
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        // 댓글 리스트 (weight로 공간 차지)
        LazyColumn(
            modifier = Modifier
                .weight(1f)  // ⭐ fillMaxSize() 대신 weight 사용
                .fillMaxWidth(),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            if (postComments.isEmpty()) {
                item {
                    Text("댓글이 없습니다.", color = Color.Gray)
                }
            } else {
                items(postComments, key = { it.id }) { comment ->
                    CommonCommentItem(comment)
                }
            }
        }

        Spacer(modifier = Modifier.height(8.dp))

        // 입력창 (하단에 자연스럽게 배치)
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            TextField(
                value = newCommentContent,
                onValueChange = onNewCommentChange,
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
                onClick = onAddComment,
                enabled = newCommentContent.isNotBlank()
            ) {
                Icon(
                    Icons.Default.Send,
                    contentDescription = "댓글 작성",
                    tint = if (newCommentContent.isNotBlank())
                        MaterialTheme.colorScheme.primary else Color.Gray
                )
            }
        }
    }
}