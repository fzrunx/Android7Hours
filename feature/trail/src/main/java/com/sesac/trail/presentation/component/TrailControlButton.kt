package com.sesac.trail.presentation.component

import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.FilledTonalButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import com.sesac.common.R as cR

@Composable
fun ColumnScope.TrailControlButton(
    buttonText: String = stringResource(cR.string.trail_button_start),
) {
    FilledTonalButton(
        onClick = {},
        modifier = Modifier
            .fillMaxWidth()
    ) {
        Text(buttonText, style = MaterialTheme.typography.labelLarge)
    }
}