package com.sesac.common.component


import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DirectionsWalk
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Monitor
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.PhotoCamera
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.tooling.preview.Preview

/**
 * 앱의 메인 하단 네비게이션 바
 *
 * @param currentRoute 현재 선택된 라우트 (또는 식별자)
 * @param onItemClick 탭 클릭 시 호출 (클릭된 탭의 라우트 전달)
 */
@Composable
fun MainBottomNavBar(
    currentRoute: String?,
    onItemClick: (String) -> Unit
) {
    val items = listOf(
        // (라우트 이름은 NavHost에서 정의한 것과 일치해야 함)
        NavBottomItem("home", "홈", Icons.Filled.Home),
        NavBottomItem("trail", "산책로", Icons.Filled.DirectionsWalk),
        NavBottomItem("monitor", "모니터링", Icons.Filled.Monitor),
        NavBottomItem("community", "커뮤니티", Icons.Filled.PhotoCamera),
        NavBottomItem("mypage", "마이페이지", Icons.Filled.Person)
    )

    NavigationBar(
        // 이미지와 유사한 연한 민트/하늘색 배경
        containerColor = Color(0xFFE6F8F8)
    ) {
        items.forEach { item ->
            val selected = (currentRoute == item.route)
            NavigationBarItem(
                selected = selected,
                onClick = { onItemClick(item.route) },
                icon = {
                    Icon(
                        imageVector = item.icon,
                        contentDescription = item.label
                    )
                },
                label = { Text(item.label) },
                colors = NavigationBarItemDefaults.colors(
                    // 이미지와 유사하게 선택/비선택 색상 지정
                    selectedIconColor = Color.Black,
                    selectedTextColor = Color.Black,
                    unselectedIconColor = Color.Gray,
                    unselectedTextColor = Color.Gray,
                    indicatorColor = Color.Transparent // 선택 시 배경색 없음
                )
            )
        }
    }
}

// 아이템 관리를 위한 데이터 클래스
private data class NavBottomItem(
    val route: String,
    val label: String,
    val icon: ImageVector
)

@Preview(showBackground = true)
@Composable
fun MainBottomNavBarPreview() {
    // 프리뷰에서 '산책로' 탭이 선택된 상태를 테스트
    var currentRoute by remember { mutableStateOf("trail") }

    MainBottomNavBar(
        currentRoute = currentRoute,
        onItemClick = { newRoute ->
            currentRoute = newRoute
        }
    )
}