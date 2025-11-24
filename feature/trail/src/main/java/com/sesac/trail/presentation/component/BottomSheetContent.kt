package com.sesac.trail.presentation.component

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.TabRowDefaults
import androidx.compose.material3.TabRowDefaults.tabIndicatorOffset
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.sesac.common.ui.theme.GrayTabText
import com.sesac.common.ui.theme.PrimaryGreenDark
import com.sesac.common.ui.theme.PrimaryGreenLight
import com.sesac.common.ui.theme.Purple600
import com.sesac.common.ui.theme.SheetHandle
import com.sesac.common.ui.theme.SheetHandleHeight
import com.sesac.common.ui.theme.SheetHandleWidth
import com.sesac.common.ui.theme.paddingLarge
import com.sesac.common.ui.theme.paddingMicro
import com.sesac.common.ui.theme.paddingSmall
import com.sesac.domain.model.Path
import com.sesac.domain.result.AuthUiState
import com.sesac.trail.presentation.TrailViewModel
import com.sesac.trail.presentation.ui.WalkPathTab

@Composable
fun BottomSheetContent(
    viewModel: TrailViewModel,
    uiState: AuthUiState,
    activeTab: WalkPathTab,
    recommendedPaths: List<Path?>,
    myPaths: List<Path?>,
    isEditMode: Boolean,
    onSheetOpenToggle: () -> Unit,
    onStartRecording: () -> Unit,
    onTabChange: (WalkPathTab) -> Unit,
    onPathClick: (Path) -> Unit,
    onFollowClick: (Path) -> Unit,
    onRegisterClick: () -> Unit,
    onEditModeToggle: () -> Unit,
    onModifyClick: (Path) -> Unit,
    onDeleteClick: (Int) -> Unit
) {
    Surface(
        shape = RoundedCornerShape(topStart = 24.dp, topEnd = 24.dp),
        shadowElevation = 8.dp,
        color = MaterialTheme.colorScheme.surface,
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Handle
            Box(
                modifier = Modifier
                    .padding(vertical = paddingSmall)
                    .width(SheetHandleWidth)
                    .height(SheetHandleHeight)
                    .background(SheetHandle, CircleShape)
                    .clickable { onSheetOpenToggle() }
            )

            // Record Button
            Button(
                onClick = onStartRecording,
                colors = ButtonDefaults.buttonColors(containerColor = PrimaryGreenLight),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = paddingLarge)
                    .padding(top = paddingMicro, bottom = paddingSmall)
                    .height(48.dp),
                shape = RoundedCornerShape(12.dp)
            ) {
                Icon(Icons.Filled.PlayArrow, contentDescription = null, tint = PrimaryGreenDark)
                Spacer(Modifier.width(paddingMicro))
                Text("산책로 기록", color = PrimaryGreenDark, fontWeight = FontWeight.Bold)
            }

            // Tabs
            TabRow(
                selectedTabIndex = activeTab.ordinal,
                containerColor = Color.White,
                contentColor = Purple600,
                indicator = { tabPositions ->
                    TabRowDefaults.Indicator(
                        Modifier.tabIndicatorOffset(tabPositions[activeTab.ordinal]),
                        color = Purple600
                    )
                },
                modifier = Modifier.padding(horizontal = paddingLarge)
            ) {
                Tab(
                    selected = activeTab == WalkPathTab.RECOMMENDED,
                    onClick = { onTabChange(WalkPathTab.RECOMMENDED) },
                    text = { Text("추천 산책로") },
                    selectedContentColor = Purple600,
                    unselectedContentColor = GrayTabText
                )
                Tab(
                    selected = activeTab == WalkPathTab.MY_RECORDS,
                    onClick = { onTabChange(WalkPathTab.MY_RECORDS) },
                    text = { Text("내 기록") },
                    selectedContentColor = Purple600,
                    unselectedContentColor = GrayTabText
                )
            }

            // Content
            Box(
                modifier = Modifier
                    .padding(horizontal = paddingLarge)
                    .heightIn(max = 240.dp) // max-h-[240px]
                    .padding(bottom = paddingLarge) // 하단 여백
            ) {
                when (activeTab) {
                    WalkPathTab.RECOMMENDED -> RecommendedTabContent(
                        paths = recommendedPaths.filterNotNull(),
                        onPathClick = onPathClick,
                        onFollowClick = onFollowClick
                    )
                    WalkPathTab.MY_RECORDS -> {
//                        viewModel.getUserBookmarkedPaths(uiState.token)
                        MyRecordsTabContent(
                            viewModel = viewModel,
                            myPaths = myPaths.filterNotNull(),
                            isEditMode = isEditMode,
                            onPathClick = onPathClick,
                            onFollowClick = { /* MyRecord에서 UserPath로 변환 필요 */ },
                            onRegisterClick = onRegisterClick,
                            onEditModeToggle = onEditModeToggle,
//                        onModifyClick = onModifyClick,
                            onDeleteClick = onDeleteClick,
                        )
                    }

                }
            }
        }
    }
}