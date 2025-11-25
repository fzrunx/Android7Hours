package com.sesac.mypage.presentation.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Chat
import androidx.compose.material.icons.twotone.Nature
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.sesac.common.component.CommonFilterTabs
import com.sesac.domain.model.BookmarkedPath
import com.sesac.domain.result.AuthResult
import com.sesac.domain.result.AuthUiState
import com.sesac.domain.result.ResponseUiState
import com.sesac.mypage.presentation.MypageViewModel

@Composable
fun MypageFavoriteScreen(
    uiStatus: AuthUiState,
    viewModel: MypageViewModel = hiltViewModel(),
    onNavigateToPost: (Int) -> Unit = {},
    onNavigateToPath: (Int) -> Unit = {},
) {
    val filterOptions = listOf("산책로", "커뮤니티")
    val coroutineScope = rememberCoroutineScope()
    val activeFilter by viewModel.activeFilter.collectAsStateWithLifecycle()
    val bookmarkedPaths by viewModel.bookmarkedPaths.collectAsStateWithLifecycle()
    val favoritePosts by viewModel.favoritePosts.collectAsStateWithLifecycle()

    LaunchedEffect(uiStatus) {
        viewModel.getUserBookmarkedPaths(uiStatus.token)
        viewModel.getFavoriteCommunityPost()
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
    ) {
        CommonFilterTabs(
            filterOptions = filterOptions,
            selectedFilter = activeFilter,
            onFilterSelected = viewModel::onFilterChange,
            fiterIcons = listOf(Icons.TwoTone.Nature, Icons.AutoMirrored.Filled.Chat),
            horizontalArrangement = Arrangement.spacedBy(12.dp, Alignment.CenterHorizontally)
        )

        when (val state = bookmarkedPaths) {
            is ResponseUiState.Loading -> {
                Column(
                    modifier = Modifier.fillMaxSize(),
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    CircularProgressIndicator()
                }
            }
            is ResponseUiState.Success -> {
                when (activeFilter) {
                    filterOptions[0] -> {
                        ListContainerView(
                            title = "즐겨찾는 산책로",
                            viewModel = viewModel,
                            itemList = state.result,
                            emptyStateMessage = "즐겨찾는 산책로가 없습니다",
                            emptyStateSubMessage = "산책로 페이지에서 ⭐를 눌러 추가해보세요",
                            itemContent = { path ->
                                BookmarkedPathCard(
                                    uiState = uiStatus,
                                    path = path,
                                    onPathClick = { onNavigateToPath(path.id) },
                                    onRemoveClick = viewModel::toggleBookmark,
                                )
                            }
                        )
                    }
                    filterOptions[1] -> {
                        ListContainerView(
                            title = "즐겨찾는 게시글",
                            viewModel = viewModel,
                            itemList = favoritePosts,
                            emptyStateMessage = "즐겨찾는 게시글이 없습니다",
                            emptyStateSubMessage = "커뮤니티에서 ♥를 눌러 추가해보세요",
                            itemContent = { post ->
                                FavoriteCommunityPostCard(
                                    post = post,
                                    onPostClick = { onNavigateToPost(post.id) },
                                    onRemoveClick = viewModel::deleteFavoriteCommunityPost
                                )
                            }
                        )
                    }
                }
            }
            is ResponseUiState.Error -> {
                Column(
                    modifier = Modifier.fillMaxSize(),
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(text = state.message)
                }
            }
            is ResponseUiState.Idle -> {
                // Do nothing or show a placeholder
            }
        }
    }
}