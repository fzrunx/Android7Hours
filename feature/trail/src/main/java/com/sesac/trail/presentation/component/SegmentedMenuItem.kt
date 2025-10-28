package com.sesac.trail.presentation.component

import androidx.compose.animation.animateColorAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.text.font.FontWeight

// 메뉴 아이템 정의
data class SegmentedMenuItem<T>(
    val id: T,
    val label: String,
    val icon: ImageVector? = null,
    val enabled: Boolean = true
)

/**
 * Material3 스타일 SegmentedMenu
 *
 * @param items 메뉴 항목 리스트
 * @param selectedItem 현재 선택된 항목 id
 * @param onItemSelected 메뉴 선택 시 콜백
 * @param modifier Modifier
 */

@Composable
fun <T> SegmentedMenu(
    items: List<SegmentedMenuItem<T>>,
    selectedItem: T,
    onItemSelected: (T) -> Unit,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier
//            .background(MaterialTheme.colorScheme.surfaceVariant, RoundedCornerShape(24.dp))
//            .padding(4.dp)
    ) {
        items.forEach { item ->
            val isSelected = item.id == selectedItem
            val backgroundColor by animateColorAsState(
                targetValue = if (isSelected) MaterialTheme.colorScheme.primary
                else MaterialTheme.colorScheme.surface
            )
            val textColor by animateColorAsState(
                targetValue = if (isSelected) MaterialTheme.colorScheme.onPrimary
                else MaterialTheme.colorScheme.onSurfaceVariant
            )

            Surface(
                color = backgroundColor,
                shape = RoundedCornerShape(20.dp),
                shadowElevation = 8.dp,                 // 그림자 높이 조절
                modifier = Modifier
                    .weight(1f)
                    .height(40.dp)
                    .padding(horizontal = 2.dp)
                    .clickable { onItemSelected(item.id) },
            ) {
                Box(contentAlignment = Alignment.Center, modifier = Modifier.fillMaxSize()) {
                    Text(
                        item.label,
                        color = textColor,
                        fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal
                    )
                }
            }
        }
    }
}

