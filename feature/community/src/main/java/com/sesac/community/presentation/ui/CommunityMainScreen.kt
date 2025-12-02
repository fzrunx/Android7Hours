package com.sesac.community.presentation.ui

import android.annotation.SuppressLint
import android.widget.Toast
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.repeatOnLifecycle
import com.sesac.common.component.CommonFilterTabs
import com.sesac.common.component.CommonSearchBarContent
import com.sesac.common.model.UiEvent
import com.sesac.common.ui.theme.Gray400
import com.sesac.common.ui.theme.Primary
import com.sesac.community.component.CommunityCommentSheetContent
import com.sesac.community.presentation.CommunityViewModel
import com.sesac.domain.model.Post
import com.sesac.domain.type.PostType
import com.sesac.domain.result.AuthUiState
import com.sesac.domain.result.ResponseUiState
import java.util.Date
import com.sesac.common.R as cR

@SuppressLint("UnrememberedMutableState")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CommunityMainScreen(
    nav2LoginScreen: () -> Unit,
    uiState: AuthUiState,
    viewModel: CommunityViewModel = hiltViewModel()
) {
    val context = LocalContext.current
    val token = uiState.token
    val lifecycleOwner = LocalLifecycleOwner.current
//    val snackbarHostState = remember { SnackbarHostState() }

    // StateFlows
    val isCommentsOpen by viewModel.isCommentsOpen.collectAsStateWithLifecycle()
    val comments by viewModel.comments.collectAsStateWithLifecycle()
    val newCommentContent by viewModel.newCommentContent.collectAsStateWithLifecycle()
    val modalSheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)

    val selectedPost by viewModel.selectedPostForComments.collectAsStateWithLifecycle()
    val filteredPosts by viewModel.filteredPosts.collectAsStateWithLifecycle()
    val searchQuery by viewModel.searchQuery.collectAsStateWithLifecycle()
    val activeFilter by viewModel.activeFilter.collectAsStateWithLifecycle()

    val isCreateDialogOpen by viewModel.isCreateDialogOpen.collectAsStateWithLifecycle()
    val editingPost by viewModel.editingPost.collectAsStateWithLifecycle()
    val isEditDialogOpen by viewModel.isEditDialogOpen.collectAsStateWithLifecycle()

    val postDetail by viewModel.post.collectAsStateWithLifecycle()
    val createPostState by viewModel.createPostState.collectAsStateWithLifecycle()
    val updatePostState by viewModel.updatePostState.collectAsStateWithLifecycle()
    val deletePostState by viewModel.deletePostState.collectAsStateWithLifecycle()

    val postEditorCategories = listOf(PostType.REVIEW, PostType.INFO)

    // 검색 상태
    var isSearchOpen by remember { mutableStateOf(false) }

    LaunchedEffect(lifecycleOwner) {
        lifecycleOwner.lifecycle.repeatOnLifecycle(Lifecycle.State.STARTED) {
            viewModel.closeComments()
        }
    }

    LaunchedEffect(uiState) {
        viewModel.getPostList(uiState.token)
    }

    // 게시글 생성 결과 처리
    LaunchedEffect(createPostState) {
        when (val result = createPostState) {
            is ResponseUiState.Success -> {
                Toast.makeText(context, result.message, Toast.LENGTH_SHORT).show()
                viewModel.getPostList(token)  // 👈 token 전달
            }
            is ResponseUiState.Error -> {
                Toast.makeText(context, result.message, Toast.LENGTH_SHORT).show()
            }
            else -> {}
        }
    }

    // 게시글 수정 결과 처리
    LaunchedEffect(updatePostState) {
        when (val result = updatePostState) {
            is ResponseUiState.Success -> {
                Toast.makeText(context, result.message, Toast.LENGTH_SHORT).show()
                viewModel.getPostList(token)  // 👈 token 전달
                viewModel.editingPost.value = null
            }
            is ResponseUiState.Error -> {
                Toast.makeText(context, result.message, Toast.LENGTH_SHORT).show()
            }
            else -> {}
        }
    }

    // 게시글 삭제 결과 처리
    LaunchedEffect(deletePostState) {
        when (val result = deletePostState) {
            is ResponseUiState.Success -> {
                Toast.makeText(context, result.message, Toast.LENGTH_SHORT).show()
                viewModel.getPostList(token)  // 👈 token 전달
            }
            is ResponseUiState.Error -> {
                Toast.makeText(context, result.message, Toast.LENGTH_SHORT).show()
            }
            else -> {}
        }
    }

    // 게시글 상세 로드 후 처리 (수정용)
    LaunchedEffect(postDetail) {
        if (postDetail is ResponseUiState.Success && editingPost != null) {
            // 수정 모드에서만 editingPost 설정
            viewModel.editingPost.value = (postDetail as ResponseUiState.Success<Post>).result
        }
    }

    val scrollBehavior = TopAppBarDefaults.pinnedScrollBehavior()

    // -------------------- UI --------------------
    Scaffold(
        modifier = Modifier.nestedScroll(scrollBehavior.nestedScrollConnection),
        floatingActionButton = {
            FloatingActionButton(
                onClick = { viewModel.isCreateDialogOpen.value = true },
                containerColor = Primary,
                contentColor = Color.White
            ) {
                Icon(Icons.Default.Add, contentDescription = "새 게시글 작성")
            }
        },
        topBar = {
            Column {
                if (isSearchOpen) {
                    TopAppBar(
                        title = {
                            CommonSearchBarContent(
                                isSearchOpen = true,
                                query = searchQuery,
                                onQueryChange = { viewModel.onSearchQueryChange(it) }
                            )
                        },
                        navigationIcon = {
                            IconButton(onClick = { isSearchOpen = false }) {
                                Icon(Icons.Filled.Close, contentDescription = "검색 닫기")
                            }
                        },
                        scrollBehavior = scrollBehavior,
                        colors = TopAppBarDefaults.topAppBarColors(
                            containerColor = MaterialTheme.colorScheme.primaryContainer,
                            scrolledContainerColor = MaterialTheme.colorScheme.primaryContainer
                        )
                    )
                } else {
                    CenterAlignedTopAppBar(
                        title = { Text("커뮤니티", fontWeight = FontWeight.Bold) },
                        actions = {
                            IconButton(onClick = { isSearchOpen = true }) {
                                Icon(Icons.Filled.Search, contentDescription = "검색 열기")
                            }
                        },
                        scrollBehavior = scrollBehavior,
                        colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
                            containerColor = MaterialTheme.colorScheme.primaryContainer,
                            scrolledContainerColor = MaterialTheme.colorScheme.primaryContainer
                        )
                    )
                }

                CommonFilterTabs(
                    filterOptions = listOf("전체", "인기글", "산책후기", "정보공유"),
                    selectedFilter = activeFilter,
                    onFilterSelected = { viewModel.onFilterChange(it) }
                )
            }
        }
    ) { paddingValues ->

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            if (filteredPosts.isEmpty()) {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        stringResource(cR.string.community_placeholder_post_empty),
                        color = Gray400
                    )
                }
            } else {
                LazyColumn(modifier = Modifier.fillMaxSize()) {
                    items(filteredPosts, key = { it.id }) { post ->
                        PostCardView(
                            post = post,
                            isMyPost = post.userId == uiState.user?.id,
                            onLikeToggle = { postId ->
                                viewModel.toggleLike(token, postId)
                            },
                            onEdit = { postId ->
                                viewModel.getPostDetail(token, postId, isForEdit = true)
                            },
                            onDelete = { postId ->
                                token?.let {
                                    viewModel.deletePost(it, postId)
                                }
                            },
                            onCommentClick = { postId ->
                                // 해당 게시글의 정보를 찾아서 댓글창 열기
                                val post = filteredPosts.find { it.id == postId }
                                post?.let {
                                    viewModel.openComments(post)
                                }
                            }
                        )
                    }
                }
            }
        }

        // 게시글 작성 다이얼로그
        if (isCreateDialogOpen) {
            PostEditorDialogView(
                categories = postEditorCategories,
                onDismiss = { viewModel.isCreateDialogOpen.value = false },
                onSave = { title: String, content: String, postType: PostType ->
                    val newPost = Post(
                        id = -1,
                        title = title,
                        content = content,
                        image = null,
                        postType = postType,
                        userId = uiState.user?.id ?: -1,
                        authUserNickname = uiState.user?.nickname ?: "",
                        authUserProfileImageUrl = null,
                        likeCount = 0,
                        commentCount = 0,
                        bookmarkCount = 0,
                        viewCount = 0,
                        isLiked = false,
                        isBookmarked = false,
                        comments = null,
                        createdAt = Date(),
                        updatedAt = Date()
                    )
                    viewModel.createPost(token, newPost)
                    viewModel.isCreateDialogOpen.value = false
                }
            )
        }

        // 게시글 수정 다이얼로그
        if (isEditDialogOpen && editingPost != null) {
            PostEditorDialogView(
                categories = postEditorCategories,
                initialPost = editingPost,
                onDismiss = { viewModel.editingPost.value = null },
                onSave = { title: String, content: String, postType: PostType ->
                    editingPost?.let { post ->
                        val updatedPost = post.copy(
                            title = title,
                            content = content,
                            postType = postType,
                            updatedAt = Date()
                        )
                        token?.let {
                            viewModel.updatePost(it, post.id, updatedPost)
                        }
                    }
                }
            )
        }
    }

    // -------------------- 댓글 BottomSheet --------------------
    if (selectedPost != null && isCommentsOpen) {
        ModalBottomSheet(
            onDismissRequest = { viewModel.closeComments() },
            sheetState = modalSheetState
        ) {
            selectedPost?.let { post ->
                CommunityCommentSheetContent(
                    comments = comments,
                    newCommentContent = newCommentContent,
                    onNewCommentChange = { viewModel.onNewCommentChange(it) },
                    onAddComment = { viewModel.addComment(token, post.id) }
                )
            }
        }
    }
}