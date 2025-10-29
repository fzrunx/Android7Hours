package com.sesac.common.component

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.unit.Dp
import com.sesac.common.R

@Composable
fun CommonBoxChipInfo(
    chipText: String,
    icon: ImageVector? = null,
    chipCornerRound: Dp = dimensionResource(R.dimen.surface_corner_round),
    chipTextHorrizontalPadding: Dp = dimensionResource(R.dimen.tail_chip_text_horr_padding),
    chipTextVerticalPadding: Dp = chipTextHorrizontalPadding/2,
) {
    Row(
        horizontalArrangement = Arrangement.SpaceEvenly
    ) {
        icon?.run {
            Icon(
                imageVector = icon,
                contentDescription = "아이콘",
            )
        }

        Surface(
            shape = RoundedCornerShape(chipCornerRound),
            color = Color.Gray.copy(alpha = 0.1f)
        ) {
            Text(
                chipText,
                style = MaterialTheme.typography.bodySmall,
                modifier = Modifier.padding(horizontal = chipTextHorrizontalPadding, vertical = chipTextVerticalPadding)
            )
        }

    }
}
