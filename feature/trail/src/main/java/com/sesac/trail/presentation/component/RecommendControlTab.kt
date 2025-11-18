package com.sesac.trail.presentation.component

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.sesac.common.ui.theme.PrimaryGreenLight
import com.sesac.common.ui.theme.Purple600
import com.sesac.common.ui.theme.White
import com.sesac.common.ui.theme.paddingMicro
import com.sesac.common.ui.theme.paddingSmall
import com.sesac.domain.model.UserPath


@Composable
fun RecommendedTabContent(
    paths: List<UserPath>,
    onPathClick: (UserPath) -> Unit,
    onFollowClick: (UserPath) -> Unit
) {
    LazyColumn(
        contentPadding = PaddingValues(vertical = paddingSmall),
        verticalArrangement = Arrangement.spacedBy(paddingMicro)
    ) {
        items(paths) { path ->
            RecommendedPathItem(
                path = path,
                onPathClick = onPathClick,
                onFollowClick = onFollowClick
            )
        }
    }
}

@Composable
fun RecommendedPathItem(
    path: UserPath,
    onPathClick: (UserPath) -> Unit,
    onFollowClick: (UserPath) -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth().clickable { onPathClick(path) },
        colors = CardDefaults.cardColors(containerColor = White),
        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
    ) {
        Row(
            modifier = Modifier.padding(paddingSmall),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(48.dp)
                    .background(PrimaryGreenLight, RoundedCornerShape(8.dp))
            )
            Spacer(Modifier.width(paddingSmall))
            Column(modifier = Modifier.weight(1f)) {
                Text(path.name, fontWeight = FontWeight.Bold, fontSize = 16.sp)
                Text("@${path.uploader}", fontSize = 12.sp, color = Color.Gray)
                Text("${path.distance} · ${path.time} 코스", fontSize = 14.sp)
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(Icons.Filled.Favorite, contentDescription = "Likes", tint = Purple600, modifier = Modifier.size(16.dp))
                    Text(" ${path.likes}", fontSize = 12.sp, color = Purple600)
                    Spacer(Modifier.width(paddingSmall))
                    Icon(Icons.Filled.LocationOn, contentDescription = "Distance", tint = Color.Gray, modifier = Modifier.size(16.dp))
                    Text(" ${path.distanceFromMe} 거리", fontSize = 12.sp, color = Color.Gray)
                }
            }
            Button(
                onClick = { onFollowClick(path) },
                colors = ButtonDefaults.buttonColors(containerColor = Purple600),
                contentPadding = PaddingValues(horizontal = paddingSmall, vertical = paddingMicro)
            ) {
                Text("따라가기")
            }
        }
    }
}