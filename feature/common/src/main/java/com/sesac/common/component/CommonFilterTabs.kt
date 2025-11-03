package com.sesac.common.component

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.twotone.AddAlert
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.tooling.preview.Preview
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
    filterOptions: List<String>,
    selectedFilter: String?,
    onFilterSelected: (String) -> Unit,
    fiterIcons: List<ImageVector>? = null,
    surfaceColor: Color? = Color.Unspecified,
) {
    val lazyRow = LazyRow(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = paddingMedium),
        contentPadding = PaddingValues(horizontal = paddingLarge),
        horizontalArrangement = Arrangement.spacedBy(paddingSmall)
    ) {
        itemsIndexed(filterOptions) { index, filter ->
            FilterChip(
                selected = selectedFilter == filter,
                onClick = { onFilterSelected(filter) },
                label = { Text(filter) },
                leadingIcon = {
                    fiterIcons?.getOrNull(index)?.let { icon ->
                        Icon(imageVector = icon, contentDescription = filter)
                    }
                } ,
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

    surfaceColor?.let { Surface(color = surfaceColor, shadowElevation = elevationSmall) { lazyRow } } ?: lazyRow

}

@Preview
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CommonFilterTabsPreview() {
    Android7HoursTheme {
        CommonFilterTabs(
            filterOptions = listOf("전체", "1번 옵션", "2번 옵션"),
            selectedFilter = "전체",
            onFilterSelected = {},
            fiterIcons = listOf(Icons.TwoTone.AddAlert),
        )
    }
}