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
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CalendarToday
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Schedule
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
import com.sesac.common.ui.theme.StatBlue
import com.sesac.common.ui.theme.StatGreen
import com.sesac.common.ui.theme.StatPurple
import com.sesac.common.ui.theme.elevationSmall
import com.sesac.common.ui.theme.iconSizeLarge
import com.sesac.common.ui.theme.paddingLarge
import com.sesac.common.ui.theme.paddingMedium
import com.sesac.common.ui.theme.paddingMicro
import com.sesac.common.ui.theme.paddingSmall
import com.sesac.domain.model.MypageStat
import com.sesac.common.R as cR

@Composable
fun StatsSectionView(stats: List<MypageStat>) {
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
fun StatCard(item: MypageStat, modifier: Modifier = Modifier) {
    val icon = when (item.iconName) {
        "LocationOn" -> Icons.Default.LocationOn
        "Schedule" -> Icons.Default.Schedule
        "CalendarToday" -> Icons.Default.CalendarToday
        else -> Icons.Default.LocationOn
    }
    val brush = when (item.colorName) {
        "Purple" -> StatPurple
        "Blue" -> StatBlue
        "Green" -> StatGreen
        else -> StatPurple
    }

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
                    .background(brush),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = icon,
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
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold
            )
        }
    }
}

@Preview
@Composable
fun StatsSectionPreview() {
    val previewStats = listOf(
        MypageStat("LocationOn", "산책 거리", "42.5km", "Purple"),
        MypageStat("Schedule", "산책 시간", "12.5시간", "Blue"),
        MypageStat("CalendarToday", "산책 횟수", "28회", "Green")
    )
    Android7HoursTheme {
        StatsSectionView(
            stats = previewStats,
        )
    }
}