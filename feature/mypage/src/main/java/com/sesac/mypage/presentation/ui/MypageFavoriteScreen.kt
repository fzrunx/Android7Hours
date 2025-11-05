package com.sesac.mypage.presentation.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Chat
import androidx.compose.material.icons.twotone.Nature
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
import com.sesac.mypage.presentation.MypageViewModel

@Composable
fun MypageFavoriteScreen(
    viewModel: MypageViewModel = hiltViewModel(),
    onNavigateToPost: (Int) -> Unit = {},
    onNavigateToPath: (Int) -> Unit = {},
) {
    val filterOptions = listOf("산책로", "커뮤니티")
    val coroutineScope = rememberCoroutineScope()
    val activeFilter by viewModel.activeFilter.collectAsStateWithLifecycle()
    val favoriteWalkPaths by viewModel.favoriteWalkPaths.collectAsStateWithLifecycle()
    val favoritePosts by viewModel.favoritePosts.collectAsStateWithLifecycle()
//    var isLoding by remember { mutableStateOf(true) }

    LaunchedEffect(favoriteWalkPaths) {
        viewModel.getFavoriteWalkPaths()
    }
    LaunchedEffect(favoritePosts) {
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

        when (activeFilter) {
            filterOptions[0] -> {
                ListContainerView(
                    title = "즐겨찾는 산책로",
                    viewModel = viewModel,
                    itemList = favoriteWalkPaths,
                    emptyStateMessage = "즐겨찾는 산책로가 없습니다",
                    emptyStateSubMessage = "산책로 페이지에서 ⭐를 눌러 추가해보세요",
                    itemContent = { path ->
                        FavoriteWalkPathCard(
                            path = path,
                            onPathClick = { onNavigateToPath(path.id) },
//                            viewModel = viewModel,
                            onRemoveClick = viewModel::deleteFavoriteWalkPath
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
//                            viewModel = viewModel,
                            onRemoveClick = viewModel::deleteFavoriteCommunityPost
                        )
                    }
                )
            }
        }
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun MypageFavoriteScreenPreview() {
    MypageFavoriteScreen(
        onNavigateToPost = {},
        onNavigateToPath = {}
    )

}