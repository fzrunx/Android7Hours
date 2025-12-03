package com.sesac.community.presentation.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.sesac.common.ui.theme.Android7HoursTheme
import com.sesac.common.ui.theme.White
import com.sesac.domain.model.Post
import com.sesac.domain.type.PostType
import com.sesac.domain.type.toKoreanString


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PostEditorDialogView(
    categories: List<PostType>,
    initialPost: Post? = null, // null이면 새 글, 아니면 수정
    onDismiss: () -> Unit,
    onSave: (title: String, content: String, postType: PostType) -> Unit
) {
    val isEditMode = initialPost != null
    val dialogTitle = if (isEditMode) "게시글 수정" else "게시글 작성"
    val buttonText = if (isEditMode) "수정" else "작성"

    var title by remember { mutableStateOf(initialPost?.title ?: "") }
    var content by remember { mutableStateOf(initialPost?.content ?: "") }
    var selectedCategory by remember {
        mutableStateOf(initialPost?.postType ?: PostType.REVIEW)
    }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text(dialogTitle) },
        text = {
            Column(modifier = Modifier.verticalScroll(rememberScrollState())) {
                // 1. 카테고리 선택
                Text("카테고리", style = MaterialTheme.typography.labelMedium)
                Spacer(modifier = Modifier.height(8.dp))
                LazyRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    items(categories) { cat ->
                        FilterChip(
                            selected = selectedCategory == cat,
                            onClick = { selectedCategory = cat },
                            label = { Text(cat.toKoreanString()) },
                            colors = FilterChipDefaults.filterChipColors(
                                selectedContainerColor = MaterialTheme.colorScheme.primary,
                                selectedLabelColor = White
                            )
                        )
                    }
                }
                Spacer(modifier = Modifier.height(16.dp))

                // 2. 제목 입력
                Text("제목", style = MaterialTheme.typography.labelMedium)
                Spacer(modifier = Modifier.height(8.dp))
                OutlinedTextField(
                    value = title,
                    onValueChange = { title = it },
                    placeholder = { Text("제목을 입력하세요") },
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true
                )
                Spacer(modifier = Modifier.height(16.dp))

                // 3. 내용 입력
                Text("내용", style = MaterialTheme.typography.labelMedium)
                Spacer(modifier = Modifier.height(8.dp))
                OutlinedTextField(
                    value = content,
                    onValueChange = { content = it },
                    placeholder = { Text("무슨 생각을 하고 계신가요?") },
                    modifier = Modifier
                        .fillMaxWidth()
                        .heightIn(min = 120.dp),
                    maxLines = 10
                )
            }
        },
        confirmButton = {
            Button(
                onClick = { onSave(title, content, selectedCategory) },
                enabled = title.isNotBlank() && content.isNotBlank()
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

@Preview
@Composable
fun PostEditorDialogViewPreview() {
    Android7HoursTheme {
        PostEditorDialogView(
            categories = listOf(PostType.REVIEW, PostType.INFO),
            initialPost = Post.EMPTY,
            onDismiss = {},
            onSave = { title, content, postType -> },
        )
    }
}