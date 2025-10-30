package com.sesac.common.component


import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.outlined.Stars
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.SegmentedButton
import androidx.compose.material3.SegmentedButtonDefaults
import androidx.compose.material3.SingleChoiceSegmentedButtonRow
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

/**
 * '산책로' 화면의 탭 바 (추천, 따라가기, 기록)
 *
 * @param selectedTabIndex 현재 선택된 탭의 인덱스
 * @param onTabSelected 탭이 선택되었을 때 호출 (선택된 인덱스 전달)
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TrailTabRow(
    modifier: Modifier = Modifier,
    selectedTabIndex: Int,
    onTabSelected: (Int) -> Unit,
    rowPadding: Dp = 10.dp,
    buttonPadding: Dp = 10.dp,
) {
    val tabs = listOf(
        "추천" to Icons.Filled.Star,
        "따라가기" to Icons.Outlined.Stars,
        "기록" to Icons.Filled.Edit
    )

    SingleChoiceSegmentedButtonRow(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = rowPadding)
    ) {
        tabs.forEachIndexed { index, (label, icon) ->
            SegmentedButton(
                modifier = Modifier
                    .padding(horizontal = buttonPadding),
                selected = (selectedTabIndex == index),
                onClick = { onTabSelected(index) },
                shape = SegmentedButtonDefaults.baseShape,
                icon = {
                    Icon(imageVector = icon, contentDescription = "")
                },
                label = { Text(label) },
                colors = SegmentedButtonDefaults.colors(
                    // 이미지와 유사한 색상
                    activeContainerColor = Color(0xFF5A5A5A),
                    activeContentColor = Color.White,
                    inactiveContainerColor = Color.White,
                    inactiveContentColor = Color.Gray
                )
            )
        }
    }
}


@Preview(showBackground = true)
@Composable
fun TrailTabRowPreview() {
    var selectedIndex by remember { mutableStateOf(0) }
    TrailTabRow(
        selectedTabIndex = selectedIndex,
        onTabSelected = { newIndex ->
            selectedIndex = newIndex
        }
    )
}