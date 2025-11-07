package com.sesac.trail.presentation.component

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.foundation.layout.Arrangement.spacedBy
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import com.sesac.common.ui.theme.*

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun TagFlow(
    selectedTags: List<String>,             // 선택된 태그 (보기용/선택용 공통)
    onTagToggle: (String) -> Unit = {},     // 태그 클릭 이벤트 (보기 전용일 때는 무시)
    editable: Boolean = false               // true = 생성/선택 화면, false = 보기용
) {
    val allTags = listOf(
        "🌳 자연 친화적", "🐕 반려견 동반 가능", "📸 포토존", "🚻 화장실 있음",
        "☕ 카페 근처", "🌃 야경 명소", "🏃 조깅 코스", "🚶 산책로",
        "👨‍👩‍👧‍👦 가족 동반", "🌸 꽃길"
    )
    // 편집 모드면 전체 태그, 보기 모드면 선택된 태그만 표시
    val displayTags = if (editable) allTags else selectedTags

    FlowRow(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = spacedBy(paddingMicro),
        verticalArrangement = spacedBy(paddingMicro)
    ) {
        val displayTags = if (editable) allTags else selectedTags

        displayTags.forEach { tag ->
            val isSelected = selectedTags.contains(tag)

            val bgColor = when {
                editable && isSelected -> Purple600
                editable -> Color.Transparent
                else -> Purple100
            }

            val textColor = when {
                editable && isSelected -> Color.White
                editable -> SheetHandle
                else -> Purple700
            }

            val borderColor = when {
                editable -> SheetHandle
                else -> Color.Transparent
            }

            Surface(
                onClick = {
                    if (editable) onTagToggle(tag)
                },
                shape = RoundedCornerShape(50),
                color = bgColor,
                border = if (editable) androidx.compose.foundation.BorderStroke(1.dp, borderColor) else null,
                tonalElevation = 1.dp
            ) {
                Text(
                    text = tag,
                    modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp),
                    color = textColor,
                    style = MaterialTheme.typography.bodyMedium
                )
            }
        }
    }
}