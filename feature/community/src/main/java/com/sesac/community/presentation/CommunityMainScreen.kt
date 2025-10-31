package com.sesac.community.presentation
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.outlined.ChatBubbleOutline
import androidx.compose.material.icons.outlined.FavoriteBorder
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import coil3.compose.AsyncImage
import com.sesac.common.R // ⚠️ 본인의 R 패키지 경로로 수정하세요.
import com.sesac.common.ui.theme.Android7HoursTheme
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CommunityScreen(
    onNavigateToHome: () -> Unit,
    viewModel: CommunityViewModel = viewModel()
) {
    // ViewModel에서 상태 구독
    val filteredPosts by viewModel.filteredPosts.collectAsState()
    val searchQuery by viewModel.searchQuery.collectAsState()
    val activeFilter by viewModel.activeFilter.collectAsState()

    // UI 상태
    var isSearchOpen by remember { mutableStateOf(false) }
    var isCreateDialogOpen by remember { mutableStateOf(false) }
    var editingPost by remember { mutableStateOf<Post?>(null) } // 수정할 게시물
    val isEditDialogOpen by remember { derivedStateOf { editingPost != null } }

    // 스낵바 (React의 toast 대체)
    val snackbarHostState = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()

    Scaffold(
        topBar = {
            CommunityAppBar(
                isSearchOpen = isSearchOpen,
                onSearchToggle = { isSearchOpen = !isSearchOpen },
                onNavigateToHome = onNavigateToHome
            )
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = { isCreateDialogOpen = true },
                containerColor = AppTheme.primary,
                contentColor = Color.White
            ) {
                Icon(Icons.Default.Add, contentDescription = "새 게시글 작성")
            }
        },
        snackbarHost = { SnackbarHost(snackbarHostState) }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .padding(paddingValues)
                .fillMaxSize()
                .background(AppTheme.background)
        ) {
            // --- 검색 바 ---
            AnimatedVisibility(visible = isSearchOpen) {
                SearchContent(
                    query = searchQuery,
                    onQueryChange = viewModel::onSearchQueryChange
                )
            }

            // --- 필터 탭 ---
            FilterTabs(
                selectedFilter = activeFilter,
                onFilterSelected = viewModel::onFilterChange
            )

            // --- 게시물 목록 ---
            if (filteredPosts.isEmpty()) {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    Text("게시글이 없습니다", color = AppTheme.textDisabled)
                }
            } else {
                LazyColumn(
                    modifier = Modifier.fillMaxSize(),
                    contentPadding = PaddingValues(bottom = 80.dp) // FAB에 가려지지 않게
                ) {
                    items(filteredPosts, key = { it.id }) { post ->
                        PostCard(
                            post = post,
                            isMyPost = post.author == "나",
                            onLikeToggle = { viewModel.onLikeToggle(post.id) },
                            onEdit = { editingPost = post },
                            onDelete = {
                                viewModel.deletePost(post.id)
                                scope.launch {
                                    snackbarHostState.showSnackbar("게시글이 삭제되었습니다.")
                                }
                            }
                        )
                    }
                }
            }
        }
    }

    // --- 새 게시글 작성 다이얼로그 ---
    if (isCreateDialogOpen) {
        PostEditorDialog(
            onDismiss = { isCreateDialogOpen = false },
            onSave = { content, image, category ->
                viewModel.createPost(content, image, category)
                isCreateDialogOpen = false
                scope.launch {
                    snackbarHostState.showSnackbar("게시글이 작성되었습니다.")
                }
            }
        )
    }

    // --- 게시글 수정 다이얼로그 ---
    if (isEditDialogOpen) {
        PostEditorDialog(
            initialPost = editingPost,
            onDismiss = { editingPost = null },
            onSave = { content, image, category ->
                editingPost?.let {
                    viewModel.updatePost(
                        it.copy(
                            content = content,
                            image = image.takeIf { !it.isNullOrBlank() },
                            category = category
                        )
                    )
                }
                editingPost = null
                scope.launch {
                    snackbarHostState.showSnackbar("게시글이 수정되었습니다.")
                }
            }
        )
    }
}
// 이 프리뷰를 통해 Android Studio에서 디자인을 실시간으로 볼 수 있습니다.
@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    Android7HoursTheme {
        CommunityScreen(
            onNavigateToHome = {}
        )
    }
}
