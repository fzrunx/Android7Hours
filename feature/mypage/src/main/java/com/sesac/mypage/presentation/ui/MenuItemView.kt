package com.sesac.mypage.presentation.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Help
import androidx.compose.material.icons.filled.CalendarToday
import androidx.compose.material.icons.filled.ChevronRight
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.Shield
import androidx.compose.material.icons.filled.Star
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
import androidx.compose.ui.graphics.Color
import com.sesac.common.ui.theme.Gray400
import com.sesac.common.ui.theme.cardIconSize
import com.sesac.common.ui.theme.iconSizeMedium
import com.sesac.common.ui.theme.paddingLarge
import com.sesac.common.ui.theme.paddingMedium
import com.sesac.common.ui.theme.paddingMicro
import com.sesac.common.ui.theme.paddingSmall
import com.sesac.domain.model.MypageMenuItem

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MenuItemView(item: MypageMenuItem, onClick: () -> Unit) {
    val icon = when (item.iconName) {
        "CalendarToday" -> Icons.Default.CalendarToday
        "Star" -> Icons.Default.Star
        "Settings" -> Icons.Default.Settings
        "Help" -> Icons.AutoMirrored.Filled.Help
        "Shield" -> Icons.Default.Shield
        else -> Icons.Default.CalendarToday
    }

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
                            contentColor = Color.White
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
                        imageVector = icon,
                        contentDescription = item.label,
                        tint = MaterialTheme.colorScheme.onPrimaryContainer,
                        modifier = Modifier.size(iconSizeMedium)
                    )
                }
            }
            Spacer(modifier = Modifier.width(paddingMedium))
            Text(
                text = item.label,
                modifier = Modifier.weight(1f)
            )
            Icon(
                Icons.Default.ChevronRight,
                contentDescription = null,
                tint = Gray400
            )
        }
    }
}

