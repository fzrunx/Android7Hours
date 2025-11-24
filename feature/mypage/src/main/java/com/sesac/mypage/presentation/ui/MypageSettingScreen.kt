package com.sesac.mypage.presentation.ui

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CameraAlt
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateMapOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.snapshots.SnapshotStateMap
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import com.sesac.common.ui.theme.Border
import com.sesac.common.ui.theme.NoteBox
import com.sesac.common.ui.theme.Primary
import com.sesac.common.ui.theme.Surface
import com.sesac.common.ui.theme.TextPrimary
import com.sesac.common.ui.theme.TextSecondary
import com.sesac.common.ui.theme.White
import com.sesac.common.ui.theme.badgeEnabledBg
import com.sesac.common.ui.theme.badgeEnabledText
import com.sesac.common.ui.theme.brushBlue
import com.sesac.common.ui.theme.brushGreen
import com.sesac.common.ui.theme.brushPurple
import com.sesac.common.ui.theme.iconBoxSize
import com.sesac.common.ui.theme.iconSize
import com.sesac.common.ui.theme.infoBoxBg
import com.sesac.common.ui.theme.infoBoxBorder
import com.sesac.common.ui.theme.infoBoxText
import com.sesac.common.ui.theme.infoBoxTitle
import com.sesac.common.ui.theme.paddingLarge
import com.sesac.common.ui.theme.paddingMedium
import com.sesac.common.ui.theme.paddingSmall
import com.sesac.common.ui.theme.permEnabledBg
import com.sesac.common.ui.theme.permEnabledBorder
import com.sesac.common.ui.theme.shapeCard
import com.sesac.common.ui.theme.shapeIcon
import com.sesac.domain.model.MypagePermission
import com.sesac.mypage.presentation.MypageViewModel

val permissions = listOf(
    MypagePermission("CAMERA", "CameraAlt", "카메라", "산책 중 사진 및 영상 촬영", "Purple"),
    MypagePermission("GPS", "LocationOn", "GPS", "위치 기반 산책로 추천 및 기록", "Blue"),
    MypagePermission("NOTIFICATION", "Notifications", "알림", "산책 알림 및 커뮤니티 소식", "Green")
)

@Composable
fun MypageSettingScreen(
    viewModel: MypageViewModel = hiltViewModel(),
    permissionStates: SnapshotStateMap<String, Boolean> = remember { mutableStateMapOf<String, Boolean>() },
) {
//    val permissions by viewModel.permissions.collectAsState(initial = emptyList())
//    val permissionStates = remember { mutableStateMapOf<String, Boolean>() }

    // Initialize states from the collected permissions
    LaunchedEffect(permissions) {
        permissions.forEach { permission ->
            if (!permissionStates.containsKey(permission.key)) {
                permissionStates[permission.key] = true
            }
        }
    }

    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(bottom = paddingLarge)
    ) {
        item {
            Surface(color = MaterialTheme.colorScheme.surface) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(paddingLarge)
                ) {
                    Text(
                        text = "권한 설정",
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold,
                        color = TextPrimary
                    )
                    Spacer(modifier = Modifier.height(paddingSmall))
                    Text(
                        text = "앱 기능 사용을 위한 권한을 관리합니다",
                        fontSize = 14.sp,
                        color = TextSecondary
                    )
                    Spacer(modifier = Modifier.height(paddingLarge))

                    permissions.forEach { item ->
                        PermissionCard(
                            item = item,
                            isEnabled = permissionStates[item.key] ?: false,
                            onToggle = {
                                val newState = !(permissionStates[item.key] ?: false)
                                permissionStates[item.key] = newState
                                viewModel.updatePermission(item.key, newState)
                            }
                        )
                        Spacer(modifier = Modifier.height(paddingMedium))
                    }
                }
            }
        }
        item {
            InfoBox(
                modifier = Modifier.padding(
                    horizontal = paddingLarge,
                    vertical = paddingMedium
                )
            )
        }
        item {
            PrivacyNote(
                modifier = Modifier.padding(horizontal = paddingLarge)
            )
        }
    }
}

@Composable
fun PermissionCard(
    item: MypagePermission,
    isEnabled: Boolean,
    onToggle: () -> Unit
) {
    val icon = when (item.iconName) {
        "CameraAlt" -> Icons.Default.CameraAlt
        "LocationOn" -> Icons.Default.LocationOn
        "Notifications" -> Icons.Default.Notifications
        else -> Icons.Default.CameraAlt
    }
    val brush = when (item.colorName) {
        "Purple" -> brushPurple
        "Blue" -> brushBlue
        "Green" -> brushGreen
        else -> brushPurple
    }

    val borderColor = if (isEnabled) permEnabledBorder else Border
    val backgroundColor = if (isEnabled) permEnabledBg else Surface

    Surface(
        shape = shapeCard,
        color = backgroundColor,
        border = BorderStroke(2.dp, borderColor)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(paddingMedium),
            verticalAlignment = Alignment.Top
        ) {
            Box(
                modifier = Modifier
                    .size(iconBoxSize)
                    .clip(shapeIcon)
                    .background(brush),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = icon,
                    contentDescription = item.label,
                    tint = Color.White,
                    modifier = Modifier.size(iconSize)
                )
            }

            Spacer(modifier = Modifier.width(paddingMedium))

            Column(modifier = Modifier.weight(1f)) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = item.label,
                        fontWeight = FontWeight.Bold,
                        color = TextPrimary,
                        fontSize = 16.sp
                    )
                    Switch(
                        checked = isEnabled,
                        onCheckedChange = { onToggle() },
                        colors = SwitchDefaults.colors(
                            checkedThumbColor = White,
                            checkedTrackColor = Primary,
                            uncheckedThumbColor = White,
                            uncheckedTrackColor = Border
                        )
                    )
                }

                Text(
                    text = item.description,
                    fontSize = 14.sp,
                    color = TextSecondary
                )

                AnimatedVisibility(visible = isEnabled) {
                    EnabledBadge(
                        modifier = Modifier.padding(top = paddingSmall)
                    )
                }
            }
        }
    }
}

@Composable
fun EnabledBadge(modifier: Modifier = Modifier) {
    Surface(
        modifier = modifier,
        shape = RoundedCornerShape(8.dp),
        color = badgeEnabledBg
    ) {
        Text(
            text = "활성화됨",
            color = badgeEnabledText,
            fontSize = 12.sp,
            fontWeight = FontWeight.Medium,
            modifier = Modifier.padding(horizontal = paddingSmall, vertical = 4.dp)
        )
    }
}

@Composable
fun InfoBox(modifier: Modifier = Modifier) {
    Surface(
        modifier = modifier,
        shape = shapeCard,
        color = infoBoxBg,
        border = BorderStroke(1.dp, infoBoxBorder)
    ) {
        Column(modifier = Modifier.padding(paddingLarge)) {
            Text(
                text = "권한 안내",
                fontWeight = FontWeight.Bold,
                color = infoBoxTitle,
                fontSize = 16.sp
            )
            Spacer(modifier = Modifier.height(paddingSmall))
            Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                listOf(
                    "카메라: 산책 중 추억을 사진과 영상으로 남길 수 있습니다",
                    "GPS: 산책 경로를 기록하고 주변 산책로를 추천받을 수 있습니다",
                    "알림: 중요한 산책 일정과 커뮤니티 소식을 받을 수 있습니다"
                ).forEach { text ->
                    Text(
                        text = "• $text",
                        color = infoBoxText,
                        fontSize = 14.sp,
                        lineHeight = 20.sp
                    )
                }
            }
        }
    }
}

@Composable
fun PrivacyNote(modifier: Modifier = Modifier) {
    Surface(
        modifier = modifier,
        shape = shapeCard,
        color = NoteBox
    ) {
        Text(
            text = "모든 권한은 언제든지 변경할 수 있으며,\n수집된 정보는 안전하게 보호됩니다.",
            color = TextSecondary,
            fontSize = 14.sp,
            textAlign = TextAlign.Center,
            modifier = Modifier
                .fillMaxWidth()
                .padding(paddingLarge),
            lineHeight = 20.sp
        )
    }
}


@Preview(showBackground = true, showSystemUi = true)
@Composable
fun MypageSettingScreenPreview() {
    MypageSettingScreen()
}