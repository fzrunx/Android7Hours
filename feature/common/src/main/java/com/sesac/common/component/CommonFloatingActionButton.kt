package com.sesac.common.component


import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview

/**
 * 커뮤니티 화면의 '글쓰기' Floating Action Button (FAB)
 *
 * @param onClick 버튼 클릭 시 동작
 */
@Composable
fun CommunityFAB(
    onClick: () -> Unit
) {
    FloatingActionButton(
        onClick = onClick,
        // 이미지와 유사하게 검은색 배경, 흰색 아이콘으로 설정
        containerColor = Color.Black,
        contentColor = Color.White
    ) {
        Icon(
            imageVector = Icons.Filled.Add,
            contentDescription = "글쓰기"
        )
    }
}

@Preview
@Composable
fun CommunityFABPreview() {
    CommunityFAB(onClick = {})
}