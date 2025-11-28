package com.sesac.common.component

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Navigation
import androidx.compose.material.icons.twotone.AddAlert
import androidx.compose.material.icons.twotone.Videocam
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import com.sesac.common.ui.theme.Android7HoursTheme
import com.sesac.common.ui.theme.ButtonSecondary
import com.sesac.common.ui.theme.OnButtonSecondary
import com.sesac.common.ui.theme.paddingLarge
import com.sesac.common.ui.theme.paddingMedium
import com.sesac.common.ui.theme.paddingSmall
import com.sesac.common.ui.theme.elevationSmall

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CommonFilterTabs(
    modifier: Modifier = Modifier,
    filterOptions: List<String>,
    selectedFilter: String?,
    onFilterSelected: (String) -> Unit,
    horizontalArrangement: Arrangement.Horizontal = Arrangement.Center,
    fiterIcons: List<ImageVector>? = null,
    surfaceColor: Color = Color.Unspecified,
) {
    val content: @Composable () -> Unit = {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = paddingMedium),
            horizontalArrangement = horizontalArrangement,
        ) {
            filterOptions.forEachIndexed { index, filter ->
                FilterChip(
                    modifier = modifier
                        .width(120.dp)
                        .height(40.dp),
                    selected = selectedFilter == filter,
                    onClick = { onFilterSelected(filter) },
                    label = {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.Center
                        ) {
                            Text(filter)
                        }
                    },
                    leadingIcon = {
                        fiterIcons?.getOrNull(index)?.let { icon ->
                            Icon(imageVector = icon, contentDescription = filter)
                        }
                    },
                    colors = FilterChipDefaults.filterChipColors(
                        containerColor = ButtonSecondary,
                        labelColor = OnButtonSecondary,
                        selectedContainerColor = MaterialTheme.colorScheme.primary,
                        selectedLabelColor = Color.White
                    ),
                    border = null
                )
            }
        }
    }

    if (surfaceColor != Color.Unspecified) {
        Surface(color = surfaceColor, shadowElevation = elevationSmall) {
            content()
        }
    } else {
        content()
    }
}



@Preview
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CommonFilterTabsPreview() {
    Android7HoursTheme {
        CommonFilterTabs(
            modifier = Modifier.padding(horizontal = paddingSmall),
            horizontalArrangement = Arrangement.Center,
            filterOptions = listOf("영상", "GPS"),
            selectedFilter = "전체",
            onFilterSelected = {},
            fiterIcons = listOf(Icons.TwoTone.Videocam, Icons.Default.Navigation),
        )
    }
}