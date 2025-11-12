package com.sesac.trail.presentation.component

import android.util.Log
import androidx.compose.foundation.background
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
import androidx.compose.material.icons.filled.Pets
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
import com.sesac.common.ui.theme.PrimaryGreenDark
import com.sesac.common.ui.theme.Purple600
import com.sesac.common.ui.theme.paddingMicro
import com.sesac.common.ui.theme.paddingSmall
import com.sesac.domain.local.model.MyRecord
import com.sesac.trail.presentation.TrailViewModel


@Composable
fun MyRecordsTabContent(
    viewModel: TrailViewModel,
    records: List<MyRecord>,
    onFollowClick: () -> Unit,
    onRegisterClick: () -> Unit
) {
    LazyColumn(
        contentPadding = PaddingValues(vertical = paddingSmall),
        verticalArrangement = Arrangement.spacedBy(paddingMicro)
    ) {
        items(records) { record ->
            MyRecordItem(
                viewModel = viewModel,
                record = record,
                onFollowClick = onFollowClick,
                onRegisterClick = onRegisterClick
            )
        }
    }
}

@Composable
fun MyRecordItem(
    viewModel: TrailViewModel,
    record: MyRecord,
    onFollowClick: () -> Unit,
    onRegisterClick: () -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
    ) {
        Row(
            modifier = Modifier.padding(paddingSmall),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(48.dp)
                    .background(record.color as Color, RoundedCornerShape(8.dp)),
                contentAlignment = Alignment.Center
            ) {
                Icon(Icons.Filled.Pets, contentDescription = "Walk", tint = Color.White)
            }
            Spacer(Modifier.width(paddingSmall))
            Column(modifier = Modifier.weight(1f)) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(record.name, fontWeight = FontWeight.Bold, fontSize = 16.sp)
                    Text(record.date.toString(), fontSize = 12.sp, color = Color.Gray)
                }
                Text("${record.distance} · ${record.time}", fontSize = 14.sp)
                Row {
                    Text("👣 ${record.steps.toLocaleString()}", fontSize = 12.sp)
                    Text(" • ", fontSize = 12.sp)
                    Text("🔥 ${record.calories}kcal", fontSize = 12.sp)
                }
            }
            Spacer(Modifier.width(paddingSmall))
            Column(verticalArrangement = Arrangement.spacedBy(paddingMicro)) {
                Button(
                    onClick = onFollowClick,
                    colors = ButtonDefaults.buttonColors(containerColor = Purple600),
                    contentPadding = PaddingValues(horizontal = paddingSmall, vertical = paddingMicro)
                ) { Text("따라가기") }
                Button(
                    onClick = {
                        viewModel.updateSelectedPath(record.toUserPath())
                        Log.d("Tag-MyRecordControlTab", "record = $record")
                        onRegisterClick()
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = PrimaryGreenDark),
                    contentPadding = PaddingValues(horizontal = paddingSmall, vertical = paddingMicro)
                ) { Text("등록하기") }
            }
        }
    }
}

// LocaleString 포맷 헬퍼
private fun Int.toLocaleString(): String {
    return "%,d".format(this)
}