package com.sesac.common.component

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.sesac.common.ui.theme.GrayTabText
import com.sesac.common.ui.theme.NoteBox
import com.sesac.common.ui.theme.Purple100
import com.sesac.common.ui.theme.paddingMicro
import com.sesac.common.ui.theme.paddingSmall


// --- Review ---
@Composable
fun ReviewItem(userName: String, date: String, review: String) {
    Card(
        colors = CardDefaults.cardColors(containerColor = NoteBox),
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(12.dp)
    ) {
        Column(modifier = Modifier.padding(paddingSmall)) {
            Row(
                modifier = Modifier.padding(bottom = paddingMicro),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Box(
                    modifier = Modifier
                        .size(32.dp)
                        .clip(CircleShape)
                        .background(Purple100),
                    contentAlignment = Alignment.Center
                ) {
                    Text("👤")
                }
                Spacer(Modifier.width(paddingMicro))
                Column {
                    Text(userName, style = MaterialTheme.typography.bodyMedium, fontWeight = FontWeight.Bold)
                    Text(date, style = MaterialTheme.typography.bodySmall, color = GrayTabText)
                }
            }
            Text(review, style = MaterialTheme.typography.bodyMedium, color = GrayTabText)
        }
    }
}