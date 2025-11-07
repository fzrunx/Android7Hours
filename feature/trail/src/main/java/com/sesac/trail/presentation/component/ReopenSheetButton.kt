package com.sesac.trail.presentation.component

import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Pets
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.sesac.common.ui.theme.Purple600
import com.sesac.common.ui.theme.paddingMicro

@Composable
fun ReopenSheetButton(onClick: () -> Unit) {
    Button(
        onClick = onClick,
        colors = ButtonDefaults.buttonColors(containerColor = Color.White),
        elevation = ButtonDefaults.buttonElevation(defaultElevation = 4.dp),
        shape = CircleShape
    ) {
        Icon(Icons.Filled.Pets, contentDescription = null, tint = Purple600)
        Spacer(Modifier.width(paddingMicro))
        Text("추천 산책로", color = Color.Black)
    }
}