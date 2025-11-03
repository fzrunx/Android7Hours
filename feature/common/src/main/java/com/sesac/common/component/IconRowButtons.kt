package com.sesac.common.component

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.sesac.common.R

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ColumnScope.IconRowButtons(
    modifier: Modifier,
    iconButtonOnClick: List<() -> Unit>? = null,
    buttonLists: List<Painter>,
    iconButtonSize: Dp = dimensionResource(R.dimen.monitor_icon_button_size)
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .weight(1f),
        horizontalArrangement = Arrangement.SpaceEvenly,
        verticalAlignment = Alignment.CenterVertically
    ) {
        buttonLists.forEachIndexed { index, button ->
            val onClick = iconButtonOnClick?.get(index) ?: {}
            Icon(
                modifier = Modifier.
                size(iconButtonSize)
                    .clickable(true, onClick = onClick),
                painter = button,
                contentDescription = "버튼",
                tint = Color.Unspecified
            )
        }

    }
}