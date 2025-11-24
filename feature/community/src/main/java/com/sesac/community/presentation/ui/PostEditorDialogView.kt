package com.sesac.community.presentation.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Image
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.Icon
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
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil3.compose.AsyncImage
import com.sesac.common.ui.theme.paddingLarge
import com.sesac.common.ui.theme.paddingSmall
import com.sesac.domain.model.Post


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PostEditorDialogView(
    categories: List<String>,
    initialPost: Post? = null, // null이면 '새 글' 모드, 아니면 '수정' 모드
    onDismiss: () -> Unit,
    onSave: (content: String, image: String, category: String) -> Unit
) {
    val isEditMode = initialPost != null
    val title = if (isEditMode) "게시글 수정" else "게시글 작성"
    val buttonText = if (isEditMode) "수정" else "작성"

    var content by remember { mutableStateOf(initialPost?.content ?: "") }
//    var image by remember { mutableStateOf(initialPost?.image ?: "") }
//    var category by remember { mutableStateOf(initialPost?.category ?: "산책후기") }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text(title) },
        text = {
            Column(modifier = Modifier.verticalScroll(rememberScrollState())) {
                // 1. 카테고리 선택
                Text("카테고리", style = MaterialTheme.typography.labelMedium)
                Spacer(modifier = Modifier.height(paddingSmall))
//                LazyRow(horizontalArrangement = Arrangement.spacedBy(paddingSmall)) {
//                    items(categories) { cat ->
//                        FilterChip(
//                            selected = category == cat,
//                            onClick = { category = cat },
//                            label = { Text(cat) },
//                            colors = FilterChipDefaults.filterChipColors(
//                                selectedContainerColor = MaterialTheme.colorScheme.primary,
//                                selectedLabelColor = Color.White
//                            )
//                        )
//                    }
//                }
                Spacer(modifier = Modifier.height(paddingLarge))

                // 2. 내용 입력
                Text("내용", style = MaterialTheme.typography.labelMedium)
                Spacer(modifier = Modifier.height(paddingSmall))
                OutlinedTextField(
                    value = content,
                    onValueChange = { content = it },
                    placeholder = { Text("무슨 생각을 하고 계신가요?") },
                    modifier = Modifier
                        .fillMaxWidth()
                        .heightIn(min = 120.dp),
                )
                Spacer(modifier = Modifier.height(paddingLarge))

                // 3. 이미지 URL 입력
                Text("이미지 URL (선택)", style = MaterialTheme.typography.labelMedium)
                Spacer(modifier = Modifier.height(paddingSmall))
//                OutlinedTextField(
//                    value = image,
//                    onValueChange = { image = it },
//                    placeholder = { "https://..." },
//                    modifier = Modifier.fillMaxWidth(),
//                    leadingIcon = { Icon(Icons.Default.Image, null) }
//                )
//                if (image.isNotBlank()) {
//                    AsyncImage(
//                        model = image,
//                        contentDescription = "Image preview",
//                        modifier = Modifier
//                            .fillMaxWidth()
//                            .padding(top = paddingSmall)
//                            .height(120.dp)
//                            .clip(MaterialTheme.shapes.medium),
//                        contentScale = ContentScale.Crop,
////                        placeholder = painterResource(id = R.drawable.placeholder)
//                    )
//                }
            }
        },
        confirmButton = {
//            Button(
//                onClick = { onSave(content, image, category) },
//                enabled = content.isNotBlank()
//            ) {
//                Text(buttonText)
//            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("취소")
            }
        }
    )
}
