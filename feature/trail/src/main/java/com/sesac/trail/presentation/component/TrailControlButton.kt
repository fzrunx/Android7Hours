package com.sesac.trail.presentation.component

import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.FilledTonalButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.Dp
import com.sesac.common.R as cR

@Composable
fun ColumnScope.TrailControlButton(
    buttonText: String = stringResource(cR.string.trail_button_start),
    onClick: () -> Unit = {},
    alignment: Alignment.Horizontal = Alignment.CenterHorizontally,
    paddingSpace: Dp = dimensionResource(cR.dimen.default_space),
) {
    Spacer(Modifier.weight(1f))
    FilledTonalButton(
        onClick = {},
        modifier = Modifier
            .fillMaxWidth()
            .align(alignment)
            .padding(bottom = paddingSpace)
    ) {
        Text(buttonText, style = MaterialTheme.typography.labelLarge)
    }
}

@Composable
fun BoxScope.TrailControlButton(
    buttonText: String = stringResource(cR.string.trail_button_start),
    onClick: () -> Unit = {},
    alignment: Alignment = Alignment.BottomEnd,
    paddingSpace: Dp = dimensionResource(cR.dimen.default_space),
) {
    FilledTonalButton(
        onClick = {},
        modifier = Modifier
            .fillMaxWidth()
            .align(alignment)
            .padding(bottom = paddingSpace)
    ) {
        Text(buttonText, style = MaterialTheme.typography.labelLarge)
    }
}