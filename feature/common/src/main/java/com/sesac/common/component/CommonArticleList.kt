package com.sesac.common.component

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.MaterialTheme.colorScheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.sesac.common.R

@Composable
fun CommonArticleList(
    listState: LazyListState,
    routes: List<PathInfo>,
    selectedRoute: PathInfo?,
    onRouteClick: (PathInfo) -> Unit,
) {
    LazyColumn(
        state = listState,
        contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        items(routes) { route ->
            CommonArticle(
                route = route,
                isSelected = selectedRoute?.id == route.id,
                onClick = { onRouteClick(route) }
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CommonArticle(
    onClick: () -> Unit,
    isSelected: Boolean,
    route: PathInfo,
    space: Dp = dimensionResource(R.dimen.default_space),
    cardElevation: Dp = dimensionResource(R.dimen.surface_elevation),
    cardRound: Dp = dimensionResource(R.dimen.surface_corner_round)
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick),
        elevation = CardDefaults.cardElevation(
            defaultElevation = if (isSelected) cardElevation*2 else cardElevation/2
        ),
        colors = CardDefaults.cardColors(
            containerColor = if (isSelected)
                colorScheme.primaryContainer
            else
                colorScheme.surface
        ),
        shape = RoundedCornerShape(cardRound)
    ) {
        Column(
            modifier = Modifier.padding(space)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    route.name,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold
                )
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        Icons.Default.Star,
                        contentDescription = null,
                        tint = Color(0xFFFFC107),
                        modifier = Modifier.size(16.dp)
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                        route.rating.toString(),
                        style = MaterialTheme.typography.bodyMedium,
                        fontWeight = FontWeight.SemiBold
                    )
                }
            }

            Spacer(modifier = Modifier.height(space))

            Row(
                horizontalArrangement = Arrangement.spacedBy(space)
            ) {
                CommonBoxChipInfo("📍 ${route.distance}")
                CommonBoxChipInfo("⏱️ ${route.duration}")
                CommonBoxChipInfo("🐕 ${route.dogSize}")
            }

            Spacer(modifier = Modifier.height(space))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = "${stringResource(R.string.trail_title_level)}: ${route.difficulty}",
                    style = MaterialTheme.typography.bodySmall,
                    color = Color.Gray
                )
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        Icons.Default.Favorite,
                        contentDescription = null,
                        tint = Color(0xFFE91E63),
                        modifier = Modifier.size(14.dp)
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                        route.likes.toString(),
                        style = MaterialTheme.typography.bodySmall,
                        color = Color.Gray
                    )
                }
            }
        }
    }
}

// 나중에 model로 이동
data class PathInfo(
    val id: Int,
    val name: String,
    val distance: String,
    val duration: String,
    val difficulty: String,
    val dogSize: String,
    val rating: Float,
    val likes: Int
)
