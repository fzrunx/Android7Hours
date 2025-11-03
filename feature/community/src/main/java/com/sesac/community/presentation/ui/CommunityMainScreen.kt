package com.sesac.community.presentation.ui
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.sesac.common.component.CommonFilterTabs
import com.sesac.common.component.CommonSearchBarContent
import com.sesac.common.ui.theme.Android7HoursTheme
import com.sesac.common.ui.theme.Gray400
import com.sesac.community.presentation.CommunityViewModel
import com.sesac.community.presentation.Post
import com.sesac.community.presentation.component.PostCard
import com.sesac.community.presentation.component.PostEditorDialog
import com.sesac.domain.model.post.PostModel
import com.sesac.domain.repository.PostRepository
import com.sesac.domain.usecase.post.GetAllPostsUseCase
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.launch
import java.util.Date
import com.sesac.common.R as cR

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CommunityMainScreen(
    viewModel: CommunityViewModel = hiltViewModel()
) {
    // static
    val postDeleteMessage = stringResource(cR.string.community_snackbar_post_delecte)
    val postCreateMessage = stringResource(cR.string.community_snackbar_post_create)
    val postUpdateMessage = stringResource(cR.string.community_snackbar_post_update)
    val postEditorCategories = listOf("산책후기", "정보공유", "질문")

    // ViewModel에서 상태 구독
    val filteredPosts by viewModel.filteredPosts.collectAsState()
    val searchQuery by viewModel.searchQuery.collectAsStateWithLifecycle()
    val activeFilter by viewModel.activeFilter.collectAsState()

    // UI 상태
    var isSearchOpen by remember { mutableStateOf(false) }
    var isCreateDialogOpen by remember { mutableStateOf(false) }
    var editingPost by remember { mutableStateOf<Post?>(null) } // 수정할 게시물
    val isEditDialogOpen by remember { derivedStateOf { editingPost != null } }

    // 스낵바 (React의 toast 대체)
    val snackbarHostState = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()

    Column(
        modifier = Modifier
            .fillMaxSize()
    ) {
        // --- 검색 바 ---
        CommonSearchBarContent(
            isSearchOpen = isSearchOpen,
            query = searchQuery,
            onQueryChange = viewModel::onSearchQueryChange,
        )

        // --- 필터 탭 ---
        CommonFilterTabs(
            filterOptions = listOf("전체", "인기글", "산책후기", "정보공유"),
            selectedFilter = activeFilter,
            onFilterSelected = viewModel::onFilterChange,
        )

        // --- 게시물 목록 ---
        if (filteredPosts.isEmpty()) {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                Text(stringResource(cR.string.community_placeholder_post_empty), color = Gray400)
            }
        } else {
            LazyColumn(
                modifier = Modifier.fillMaxSize(),
//                    contentPadding = PaddingValues(bottom = 80.dp) // FAB에 가려지지 않게
            ) {
                items(items = filteredPosts, key = { it.id }) { post ->
                    PostCard(
                        post = post,
                        isMyPost = post.author == "나",
                        onLikeToggle = { viewModel.onLikeToggle(post.id) },
                        onEdit = { editingPost = post },
                        onDelete = {
                            viewModel.deletePost(post.id)
                            scope.launch {
                                snackbarHostState.showSnackbar(postDeleteMessage)
                            }
                        }
                    )
                }
            }
        }
    }


    // --- 새 게시글 작성 다이얼로그 ---
    if (isCreateDialogOpen) {
        PostEditorDialog(
            categories = postEditorCategories,
            onDismiss = { isCreateDialogOpen = false },
            onSave = { content, image, category ->
                viewModel.createPost(content, image, category)
                isCreateDialogOpen = false
                scope.launch {
                    snackbarHostState.showSnackbar(postCreateMessage)
                }
            }
        )
    }

    // --- 게시글 수정 다이얼로그 ---
    if (isEditDialogOpen) {
        PostEditorDialog(
            categories = postEditorCategories,
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
                    snackbarHostState.showSnackbar(postUpdateMessage)
                }
            }
        )
    }
}
// 이 프리뷰를 통해 Android Studio에서 디자인을 실시간으로 볼 수 있습니다.
@Preview(showBackground = true)
@Composable
fun CommunityMainScreePreview() {
    // Preview를 위한 가짜(Fake) 데이터 및 ViewModel 생성
    class FakePostRepository : PostRepository {
        override fun getAllPosts(): Flow<List<PostModel>> = flowOf(
            listOf(
                PostModel(
                    postId = 0,
                    title = "주말 산책",
                    userName = "멍멍이집사",
                    content = "부산 근처 산에 다녀왔어요. 자연과 함께한 힐링 타임 🌿",
                    create_at = Date(System.currentTimeMillis() - 1000 * 60 * 60 * 2) // 2 hours ago
                ),
                PostModel(
                    postId = 1,
                    title = "팀원들과 점심",
                    userName = "강아지사랑",
                    content = "팀원들과 함께한 점심시간 🍜 새로운 아이디어도 많이 나왔어요!",
                    create_at = Date(System.currentTimeMillis() - 1000 * 60 * 60 * 5), // 5 hours ago
                    likes = 120 // for "인기글" filter test
                )
            )
        )
        override fun getPostDetail(postId: Int): Flow<PostModel?> = flowOf(null)
        override fun getSearchPosts(query: String): Flow<List<PostModel>> = flowOf(emptyList())
    }

    val fakeViewModel = CommunityViewModel(GetAllPostsUseCase(FakePostRepository()))

    Android7HoursTheme {
        CommunityMainScreen(viewModel = fakeViewModel)
    }
}