package com.sesac.common.component


import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview

/**
 * '산책로' 화면의 상단 앱 바
 *
 * @param onMenuClick 햄버거 메뉴 클릭 시 동작
 * @param onProfileClick 프로필 아이콘 클릭 시 동작
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TrailTopAppBar(
    onMenuClick: () -> Unit,
    onProfileClick: () -> Unit
) {
    CenterAlignedTopAppBar(
        title = {
            Text(
                text = "산책로",
                fontWeight = FontWeight.Bold
            )
        },
        navigationIcon = {
            IconButton(onClick = onMenuClick) {
                Icon(
                    imageVector = Icons.Filled.Menu,
                    contentDescription = "메뉴"
                )
            }
        },
        actions = {
            IconButton(onClick = onProfileClick) {
                Icon(
                    imageVector = Icons.Filled.AccountCircle,
                    contentDescription = "프로필"
                )
            }
        },
        colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
            // 이미지와 유사한 연한 보라색 배경
            containerColor = Color(0xFFF2F0FA),
            titleContentColor = Color.Black,
            navigationIconContentColor = Color.Black,
            actionIconContentColor = Color.Black
        )
    )
}

@Preview(showBackground = true)
@Composable
fun TrailTopAppBarPreview() {
    TrailTopAppBar(
        onMenuClick = {},
        onProfileClick = {}
    )
}