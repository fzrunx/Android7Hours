package com.sesac.trail.presentation.component

import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Pets
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.sesac.common.ui.theme.TextPrimary
import com.sesac.common.ui.theme.elevationLarge
import com.sesac.common.ui.theme.iconSizeSmall
import com.sesac.common.ui.theme.paddingSmall
import com.sesac.trail.presentation.ui.WalkMode

@Composable
fun ReopenSheetButton(mode: WalkMode, onClick: () -> Unit, modifier: Modifier = Modifier) {
    Button(
        onClick = onClick,
        modifier = modifier,
        shape = CircleShape,
        colors = ButtonDefaults.buttonColors(
            containerColor = MaterialTheme.colorScheme.surface,
            contentColor = TextPrimary
        ),
        elevation = ButtonDefaults.buttonElevation(defaultElevation = elevationLarge)
    ) {
        Icon(
            imageVector = Icons.Default.Pets, // 'Footprints' 아이콘 대체
            contentDescription = null,
            tint = MaterialTheme.colorScheme.primary,
            modifier = Modifier.size(iconSizeSmall)
        )
        Spacer(modifier = Modifier.width(paddingSmall))
        Text(
            text = if (mode == WalkMode.RECOMMENDED) "추천 산책로" else "주변 산책로",
            fontWeight = FontWeight.Bold
        )
    }
}