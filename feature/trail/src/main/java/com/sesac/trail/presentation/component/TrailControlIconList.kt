package com.sesac.trail.presentation.component

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme.colorScheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.unit.Dp
import com.sesac.common.R as cR

@Composable
fun BoxScope.TrailControlIconList(
    iconList: List<ImageVector>,
    columnAlignment: Alignment = Alignment.CenterEnd,
    space: Dp = dimensionResource(cR.dimen.default_space),
    surfaceCornerRound: Dp = dimensionResource(cR.dimen.trail_surface_icon_size),
    surfaceElevation: Dp = dimensionResource(cR.dimen.surface_elevation),
    surfaceIconSize: Dp = dimensionResource(cR.dimen.trail_surface_icon_size),
) {
    // 오른쪽 하단 맵 컨트롤 (예시)
    Column(
        modifier = Modifier
            .align(columnAlignment)
            .padding(end = space),
        verticalArrangement = Arrangement.spacedBy(space)
    ) {
        iconList.forEach { icon ->
            Surface(
                shape = RoundedCornerShape(surfaceCornerRound),
                shadowElevation = surfaceElevation,
                color = Color.White,
                modifier = Modifier.size(surfaceIconSize)
            ) {
                Box(contentAlignment = Alignment.Center) {
                    Icon(
                        icon,
                        contentDescription = "현재 위치",
                        tint = colorScheme.primary
                    )
                }
            }

        }

    }
}