package com.sesac.common.component

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ChevronRight
import androidx.compose.material3.Badge
import androidx.compose.material3.BadgedBox
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.vector.ImageVector
import com.sesac.common.ui.theme.Gray400
import com.sesac.common.ui.theme.White
import com.sesac.common.ui.theme.cardIconSize
import com.sesac.common.ui.theme.iconSizeMedium
import com.sesac.common.ui.theme.paddingLarge
import com.sesac.common.ui.theme.paddingMedium
import com.sesac.common.ui.theme.paddingMicro
import com.sesac.common.ui.theme.paddingSmall
import com.sesac.domain.model.MypageMenuItem

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CommonMenuItem(
    item: MypageMenuItem,
    onClick: () -> Unit,
    isUseChevronRightIcon: Boolean = true,
    ) {

    Surface(
        onClick = onClick,
        modifier = Modifier.padding(horizontal = paddingLarge, vertical = paddingMicro/2)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = paddingSmall),
            verticalAlignment = Alignment.CenterVertically
        ) {
            BadgedBox(
                badge = {
                    item.badgeCount?.let {
                        Badge(
                            containerColor = MaterialTheme.colorScheme.onError,
                            contentColor = White
                        ) {
                            Text(it.toString())
                        }
                    }
                },
                modifier = Modifier.padding(horizontal = paddingSmall)
            ) {
                Box(
                    modifier = Modifier
                        .size(cardIconSize)
                        .clip(MaterialTheme.shapes.medium)
                        .background(MaterialTheme.colorScheme.secondaryContainer),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = item.icon as ImageVector,
                        contentDescription = item.labels.first(),
                        tint = MaterialTheme.colorScheme.onPrimaryContainer,
                        modifier = Modifier.size(iconSizeMedium)
                    )
                }
            }
            Spacer(modifier = Modifier.width(paddingMedium))
            Column(
                modifier = Modifier
                    .weight((1f)),
                verticalArrangement = Arrangement.SpaceEvenly,
            ) {
                item.labels.forEach { label ->
                    Text(
                        text = label,
//                        modifier = Modifier.weight(1f)
                    )

                }

            }
            if (isUseChevronRightIcon){
                Icon(
                    Icons.Default.ChevronRight,
                    contentDescription = null,
                    tint = Gray400
                )
            }
        }
    }
}

