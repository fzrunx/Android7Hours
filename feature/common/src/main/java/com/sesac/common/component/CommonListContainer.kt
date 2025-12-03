package com.sesac.common.component

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.sesac.common.ui.theme.Android7HoursTheme
import com.sesac.common.ui.theme.TextPrimary
import com.sesac.common.ui.theme.paddingLarge
import com.sesac.common.ui.theme.paddingMedium
import com.sesac.common.ui.theme.paddingSmall
import com.sesac.common.ui.theme.primaryContainer
import com.sesac.domain.model.Post

@Composable
fun <T> CommonListContainer(
    title: String,
    itemList: List<T>,
    emptyStateMessage: String,
    emptyStateSubMessage: String,
    itemContent: @Composable (T) -> Unit
) {
    Column(modifier = Modifier.padding(horizontal = paddingLarge)) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = paddingMedium),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = title,
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                color = TextPrimary
            )
            Surface(
                shape = CircleShape,
                color = primaryContainer
            ) {
                Text(
                    text = "${itemList.size}개",
                    color = MaterialTheme.colorScheme.onPrimaryContainer,
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(horizontal = 12.dp, vertical = 4.dp)
                )
            }
        }

        if (itemList.isEmpty()) {
            CommonEmptyState(
                message = emptyStateMessage,
                subMessage = emptyStateSubMessage
            )
        } else {
            LazyColumn(
                verticalArrangement = Arrangement.spacedBy(paddingSmall)
            ) {
                items(itemList) { item ->
                    itemContent(item)
                }
            }
        }

    }
}

@Preview
@Composable
fun ListContainerViewPreview() {
    Android7HoursTheme {
        CommonListContainer(
            title = "리스트트",
//            itemList = listOf(Post.EMPTY),
            itemList = emptyList<Post>(),
            emptyStateMessage = "빈화면",
            emptyStateSubMessage = "빈빈화면",
            itemContent = {},
        )
    }
}