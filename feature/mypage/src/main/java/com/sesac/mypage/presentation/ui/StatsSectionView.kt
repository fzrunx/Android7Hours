package com.sesac.mypage.presentation.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import com.sesac.common.ui.theme.Android7HoursTheme
import com.sesac.common.ui.theme.elevationSmall
import com.sesac.common.ui.theme.iconSizeLarge
import com.sesac.common.ui.theme.paddingLarge
import com.sesac.common.ui.theme.paddingMedium
import com.sesac.common.ui.theme.paddingMicro
import com.sesac.common.ui.theme.paddingSmall
import com.sesac.mypage.presentation.model.StatItem
import kotlin.collections.forEach
import com.sesac.common.R as cR

@Composable
fun StatsSectionView(stats: List<StatItem>) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = paddingLarge, horizontal = paddingSmall)
    ) {
        Text(
            text = stringResource(cR.string.mypage_stats_title),
            modifier = Modifier.padding(start = paddingSmall, bottom = paddingMedium),
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.Bold
        )
        Row(
            modifier = Modifier.fillMaxHeight(),
//            horizontalArrangement = Arrangement.spacedBy(paddingSmall),
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            stats.forEach { stat ->
                StatCard(
                    item = stat,
                    modifier = Modifier.weight(1f)
                )
            }
        }
    }
}

@Composable
fun StatCard(item: StatItem, modifier: Modifier = Modifier) {
    Card(
        modifier = modifier
            .padding(horizontal = paddingMicro)
            .fillMaxHeight(),
        shape = MaterialTheme.shapes.medium,
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = elevationSmall)
    ) {
        Column(
            modifier = Modifier.fillMaxWidth()
            .padding(vertical = paddingMedium),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            Box(
                modifier = Modifier
                    .size(iconSizeLarge)
                    .clip(MaterialTheme.shapes.medium)
                    .background(item.brush),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = item.icon,
                    contentDescription = item.label,
                    tint = Color.White
                )
            }
            Spacer(modifier = Modifier.height(paddingSmall))
            Text(
                text = item.label,
                style = MaterialTheme.typography.bodySmall,
            )
            Text(
                text = item.value,
//                modifier = Modifier.padding(top = paddingSmall),
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold
            )
        }
    }
}

@Preview
@Composable
fun StatsSectionPreview() {
    Android7HoursTheme {
        StatsSectionView(
            MyPageDataSource.stats,
        )
    }
}