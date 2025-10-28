package com.sesac.common.component

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Preview
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SegmentedButton
import androidx.compose.material3.SegmentedButtonDefaults
import androidx.compose.material3.SingleChoiceSegmentedButtonRow
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.sesac.common.R

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CommonSegmentedButton(
    tabOptions: List<String>,
    tabSelectedIndex: MutableState<Int>,
    verticalAlignment: Arrangement.Vertical = Arrangement.Center,
    horizontalArrangement: Arrangement.Horizontal = Arrangement.Absolute.Center,
    checkedIcons: List<ImageVector> = emptyList<ImageVector>(),
    unCheckedIcons: List<ImageVector> = emptyList<ImageVector>(),
    textSpace: Dp = dimensionResource(R.dimen.text_top_bottom_space),
    space: Dp = dimensionResource(R.dimen.default_space),
) {
    FlowRow(
        Modifier
            .padding(horizontal = space, vertical = textSpace)
            .fillMaxWidth(),
        horizontalArrangement = horizontalArrangement,
        verticalArrangement = verticalAlignment,
    ) {
        SingleChoiceSegmentedButtonRow {
            tabOptions.forEachIndexed { index, string ->
                SegmentedButton(
                    modifier = Modifier
                        .wrapContentWidth()
                        .width(100.dp),
                    shape = SegmentedButtonDefaults.itemShape(
                        index = index,
                        count = tabOptions.size
                    ),
                    icon = {
                        if(checkedIcons.isNotEmpty() and unCheckedIcons.isNotEmpty()){
                            Icon(
                                imageVector = if (tabSelectedIndex.value == index) checkedIcons[index] else unCheckedIcons[index],
                                contentDescription = string
                            )
                        }
                    },
                    onClick = { tabSelectedIndex.value = index },
                    selected = index == tabSelectedIndex.value,
                    label = { Text(string) }
                )
            }
        }
    }
}

@Preview(showBackground = true)
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CommonSegmentedButtonPreview() {
    MaterialTheme {
        val tabSelectedIndex = rememberSaveable { mutableStateOf(0) }
        CommonSegmentedButton(
            listOf("a", "b", "c"),
            tabSelectedIndex,
            checkedIcons = listOf(Icons.Default.Preview),
            unCheckedIcons = listOf(Icons.Default.Star),
        )
    }
}
