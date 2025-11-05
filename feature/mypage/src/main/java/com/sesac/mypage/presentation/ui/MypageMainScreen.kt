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
import androidx.compose.material.icons.automirrored.filled.Logout
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.sesac.common.ui.theme.Android7HoursTheme
import com.sesac.common.ui.theme.elevationSmall
import com.sesac.common.ui.theme.iconSizeMedium
import com.sesac.common.ui.theme.paddingLarge
import com.sesac.common.ui.theme.paddingSmall
import com.sesac.domain.model.MypageMenuItem
import com.sesac.mypage.nav_graph.MypageNavigationRoute
import com.sesac.mypage.presentation.MypageViewModel

val menuItems = listOf(
    MypageMenuItem(key = "MANAGE", iconName = "CalendarToday", label = "일정관리"),
    MypageMenuItem(key = "FAVORITE", iconName = "Star", label = "즐겨찾기"),
    MypageMenuItem(key = "SETTING", iconName = "Settings", label = "설정"),
    MypageMenuItem(key = "HELP", iconName = "Help", label = "도움말"),
    MypageMenuItem(key = "POLICY", iconName = "Shield", label = "개인정보 처리방침")
)

@Composable
fun MypageMainScreen(
    navController: NavController,
    viewModel: MypageViewModel = hiltViewModel()
) {
    val stats by viewModel.stats.collectAsState(initial = emptyList())

    LazyColumn(
        modifier = Modifier
            .fillMaxSize(),
        contentPadding = PaddingValues(bottom = paddingLarge)
    ) {
        item {
            ProfileHeaderView(
                name = "김반려",
                email = "kimbanrye@email.com",
                imageUrl = "",
                onNavigateToProfile = { /* navController.navigate("profile") */ }
            )
        }

        item {
            StatsSectionView(stats = stats)
        }

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
        items(menuItems) { item ->
            MenuItemView(
                item = item,
                onClick = {
                    when (item.key) {
                        "MANAGE" -> navController.navigate(MypageNavigationRoute.ManageTab)
                        "FAVORITE" -> navController.navigate(MypageNavigationRoute.FavoriteTab)
                        "SETTING" -> navController.navigate(MypageNavigationRoute.SettingTab)
                        // Handle other keys or do nothing
                    }
                }
            )
        }

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

@Preview(showBackground = true)
@Composable
fun MyPageMainScreenPreview() {
    Android7HoursTheme {
        MypageMainScreen(
            navController = rememberNavController(),
        )
    }
}