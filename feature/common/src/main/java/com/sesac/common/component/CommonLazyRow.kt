package com.sesac.common.component

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import com.sesac.common.ui.theme.paddingLarge
import com.sesac.common.ui.theme.paddingMedium

@Composable
fun <T> CommonLazyRow(
    modifier: Modifier = Modifier,
    title: String,
    items: List<T>,
    content: @Composable (T)-> Unit
) {
    Column(modifier = modifier.fillMaxWidth().padding(vertical = paddingMedium)) {
        Text(
            modifier = Modifier.fillMaxWidth().padding(horizontal = paddingLarge).padding(bottom = paddingMedium),
            text = title,
            color = MaterialTheme.colorScheme.onBackground,
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.Bold
        )
        LazyRow(
            contentPadding = PaddingValues(horizontal = paddingLarge),
            horizontalArrangement = Arrangement.spacedBy(paddingMedium)
        ) {
            items(items) { item ->
                content(item)
            }
        }
    }
}