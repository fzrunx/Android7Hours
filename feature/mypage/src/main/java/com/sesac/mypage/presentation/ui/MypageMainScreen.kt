package com.sesac.mypage.presentation.ui

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Help
import androidx.compose.material.icons.automirrored.filled.Logout
import androidx.compose.material.icons.filled.CalendarToday
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Schedule
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.Shield
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.sesac.common.ui.theme.Android7HoursTheme
import com.sesac.common.ui.theme.StatBlue
import com.sesac.common.ui.theme.StatGreen
import com.sesac.common.ui.theme.StatPurple
import com.sesac.common.ui.theme.elevationSmall
import com.sesac.common.ui.theme.iconSizeMedium
import com.sesac.common.ui.theme.paddingLarge
import com.sesac.common.ui.theme.paddingSmall
import com.sesac.mypage.presentation.component.MenuItemRow
import com.sesac.mypage.presentation.component.ProfileHeader
import com.sesac.mypage.presentation.component.StatsSection
import com.sesac.mypage.presentation.model.MenuItem
import com.sesac.mypage.presentation.model.StatItem

object MyPageDataSource {
    val stats = listOf(
        StatItem(Icons.Default.LocationOn, "산책 거리", "42.5km", StatPurple),
        StatItem(Icons.Default.Schedule, "산책 시간", "12.5시간", StatBlue),
        StatItem(Icons.Default.CalendarToday, "산책 횟수", "28회", StatGreen)
    )

    val menuItems = listOf(
        MenuItem(Icons.Default.CalendarToday, "일정관리", route = "schedule"),
        MenuItem(Icons.Default.Star, "즐겨찾기", route = "favorites"),
        MenuItem(Icons.Default.Settings, "설정", route = "settings"),
//        MenuItem(Icons.Default.Notifications, "알림", badgeCount = 3, route = "notifications"),
        MenuItem(Icons.AutoMirrored.Filled.Help, "도움말", route = "help"),
        MenuItem(Icons.Default.Shield, "개인정보 처리방침", route = "privacy")
    )
}
/**
 * React 코드의 'main' 상태 UI
 */
@Composable
fun MypageMainScreen(
    navController: NavController,
) {

    LazyColumn(
        modifier = Modifier
            .fillMaxSize(),
        contentPadding = PaddingValues(bottom = paddingLarge)
    ) {
        // --- 프로필 섹션 ---
        item {
            ProfileHeader(
                name = "김반려",
                email = "kimbanrye@email.com",
                imageUrl = "data:image/jpeg;base64,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",
                onNavigateToProfile = {
                    navController.navigate("profile")
                }
            )
        }

        // --- 통계 섹션 ---
        item {
            StatsSection(stats = MyPageDataSource.stats)
        }

        // --- 메뉴 섹션 ---
        item {
            Text(
                text = "메뉴",
                style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                modifier = Modifier.padding(
                    start = paddingLarge,
                    bottom = paddingSmall
                )
            )
        }
        items(MyPageDataSource.menuItems) { item ->
            MenuItemRow(
                item = item,
                onClick = {
                    navController.navigate(item.route)
                }
            )
        }

        // --- 로그아웃 버튼 ---
        item {
            LogoutButton(
                onClick = { /* TODO: 로그아웃 로직 */ },
                modifier = Modifier.padding(paddingLarge)
            )
        }
    }

}

@Composable
fun LogoutButton(onClick: () -> Unit, modifier: Modifier = Modifier) {
    Button(
        onClick = onClick,
        modifier = modifier.fillMaxWidth(),
        shape = MaterialTheme.shapes.large,
        colors = ButtonDefaults.buttonColors(
            containerColor = Color.White,
            contentColor = MaterialTheme.colorScheme.error
        ),
        border = BorderStroke(1.dp, MaterialTheme.colorScheme.onError),
        elevation = ButtonDefaults.buttonElevation(defaultElevation = elevationSmall)
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center
        ) {
            Icon(
                Icons.AutoMirrored.Filled.Logout,
                contentDescription = null,
                modifier = Modifier.size(iconSizeMedium)
            )
            Spacer(modifier = Modifier.width(paddingSmall))
            Text(
                text = "로그아웃",
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(vertical = paddingSmall)
            )
        }
    }
}

// --- Preview ---
@Preview(showBackground = true)
@Composable
fun MyPageMainScreenPreview() {
    Android7HoursTheme {
        MypageMainScreen(
            navController = rememberNavController(), // preview에서는 navigate 호출 금지
        )

    }
}