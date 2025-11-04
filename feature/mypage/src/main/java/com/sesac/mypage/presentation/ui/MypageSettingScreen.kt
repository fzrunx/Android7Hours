package com.sesac.mypage.presentation.ui


import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.runtime.remember
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.material.icons.filled.CameraAlt
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import com.sesac.common.navigation.CommonHeader
import com.sesac.common.ui.theme.paddingLarge
import com.sesac.common.ui.theme.*


// 1. 권한 키를 위한 Enum (타입 안정성)
enum class PermissionKey {
    CAMERA, GPS, NOTIFICATION
}

// 2. 권한 아이템 데이터 클래스
data class PermissionItem(
    val key: PermissionKey,
    val icon: ImageVector,
    val label: String,
    val description: String,
    val brush: Brush
)


@Composable
fun MypageSettingScreen (  onBack: () -> Unit,
) {
    // 임시 데이터
    val permissionItems = listOf(
        PermissionItem(PermissionKey.CAMERA, Icons.Default.CameraAlt, "카메라", "산책 중 사진 및 영상 촬영", brushPurple),
        PermissionItem(PermissionKey.GPS, Icons.Default.LocationOn, "GPS", "위치 기반 산책로 추천 및 기록", brushBlue),
        PermissionItem(PermissionKey.NOTIFICATION, Icons.Default.Notifications, "알림", "산책 알림 및 커뮤니티 소식", brushGreen)
    )
    val cameraEnabled = remember { mutableStateOf(true) }
    val gpsEnabled = remember { mutableStateOf(true) }
    val notificationEnabled = remember { mutableStateOf(true) }
    Scaffold(
        topBar = {
            CommonHeader(
                title = "설정",
                onBack = { /* 뒤로가기 */ }
            )
        },
        containerColor =  MaterialTheme.colorScheme.background
    ) { paddingValues ->
        LazyColumn(
            modifier = Modifier
                .padding(paddingValues)
                .fillMaxSize(),
            contentPadding = PaddingValues(bottom = paddingLarge)
        ) {
            // --- 1. 권한 설정 섹션 ---
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

                        // 권한 카드 리스트
                        permissionItems.forEach { item ->
                        PermissionCard(
                            item = item,
                            isEnabled = when(item.key) {
                                PermissionKey.CAMERA -> cameraEnabled.value
                                PermissionKey.GPS -> gpsEnabled.value
                                PermissionKey.NOTIFICATION -> notificationEnabled.value
                            },
                            onToggle = {
                                when(item.key) {
                                    PermissionKey.CAMERA -> cameraEnabled.value = !cameraEnabled.value
                                    PermissionKey.GPS -> gpsEnabled.value = !gpsEnabled.value
                                    PermissionKey.NOTIFICATION -> notificationEnabled.value = !notificationEnabled.value
                                }
                            }
                        )
                            Spacer(modifier = Modifier.height(paddingMedium))
                        }
                    }
                }
            }
            // --- 2. 권한 안내 섹션 ---
            item {
                InfoBox(
                    modifier = Modifier.padding(
                        horizontal = paddingLarge,
                        vertical = paddingMedium
                    )
                )
            }
            // --- 3. 개인정보 고지 ---
            item {
                PrivacyNote(
                    modifier = Modifier.padding(horizontal = paddingLarge)
                )
            }
        }
    }
}


@Composable
fun PermissionCard(
    item: PermissionItem,
    isEnabled: Boolean,
    onToggle: () -> Unit
) {
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
            // --- 그라데이션 아이콘  ---
            Box(
                modifier = Modifier
                    .size(iconBoxSize)
                    .clip(shapeIcon)
                    .background(item.brush),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = item.icon,
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
                            checkedThumbColor = Color.White,
                            checkedTrackColor = Primary,
                            uncheckedThumbColor = Color.White,
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
    MypageSettingScreen(
        onBack = {}
    )
}