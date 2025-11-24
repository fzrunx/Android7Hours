package com.sesac.community.presentation.ui

import android.annotation.SuppressLint
import android.widget.Toast
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.sesac.common.component.CommonCommentSheetContent
import com.sesac.common.component.CommonFilterTabs
import com.sesac.common.component.CommonSearchBarContent
import com.sesac.common.component.LocalIsSearchOpen
import com.sesac.common.ui.theme.Gray400
import com.sesac.common.ui.theme.Primary
import com.sesac.community.presentation.CommunityViewModel
import com.sesac.domain.model.Post
import kotlinx.coroutines.launch
import com.sesac.common.R as cR

@SuppressLint("UnrememberedMutableState")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CommunityMainScreen(
    viewModel: CommunityViewModel = hiltViewModel()
) {
    val coroutineScope = rememberCoroutineScope()

    val modalSheetState = rememberModalBottomSheetState(
        skipPartiallyExpanded = true,
        confirmValueChange = { it != SheetValue.PartiallyExpanded }
    )

    val snackbarHostState = remember { SnackbarHostState() }
    var isCreateDialogOpen by remember { mutableStateOf(false) }
    var editingPost by remember { mutableStateOf<Post?>(null) }
    val isEditDialogOpen by derivedStateOf { editingPost != null }

//    val filteredPosts by viewModel.filteredPosts.collectAsState()
//    val searchQuery by viewModel.searchQuery.collectAsStateWithLifecycle()
//    val activeFilter by viewModel.activeFilter.collectAsState()
    var activeFilter by remember { mutableStateOf("전체") }

    var searchQuery by remember { mutableStateOf("") }

    val isSearchOpenState = LocalIsSearchOpen.current
    val isSearchOpen = LocalIsSearchOpen.current.value
    val onSearchOpenChange: (Boolean) -> Unit = { newValue ->
        isSearchOpenState.value = newValue
    }

    val postDeleteMessage = stringResource(cR.string.community_snackbar_post_delecte)
    val postCreateMessage = stringResource(cR.string.community_snackbar_post_create)
    val postUpdateMessage = stringResource(cR.string.community_snackbar_post_update)
    val postEditorCategories = listOf("산책후기", "정보공유", "질문")

    /* BottomSheet와 ViewModel 상태 동기화 */
//    LaunchedEffect(viewModel.isCommentsOpen) {
//        if (viewModel.isCommentsOpen) modalSheetState.show()
//        else modalSheetState.hide()
//    }

    val scrollBehavior = TopAppBarDefaults.pinnedScrollBehavior()

    // -------------------- UI --------------------
    Scaffold(
        modifier = Modifier.nestedScroll(scrollBehavior.nestedScrollConnection),
        floatingActionButton = {
            FloatingActionButton(
                onClick = { isCreateDialogOpen = true },
                containerColor = Primary,
                contentColor = Color.White
            ) {
                Icon(Icons.Default.Add, contentDescription = "새 게시글 작성")
            }
        },
        snackbarHost = { SnackbarHost(snackbarHostState) },
        topBar = {
            Column {
                // 상태에 따라 TopBar 교체
                if (isSearchOpen) {
                    // 1️⃣ 검색 모드
                    TopAppBar(
                        title = {
                            CommonSearchBarContent(
                                isSearchOpen = true,
                                query = searchQuery,
                                onQueryChange = { searchQuery = it }
                            )
                        },
                        navigationIcon = {
                            IconButton(onClick = { onSearchOpenChange(false) }) {
                                // Icons.Default -> Icons.Filled 로 변경
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
                    // 2️⃣ 평상시 모드
                    CenterAlignedTopAppBar(
                        title = { Text("커뮤니티", fontWeight = FontWeight.Bold) },
                        navigationIcon = {
                            IconButton(onClick = { /* 커뮤니티 홈 로직 */ }) {
                                // Icons.Default -> Icons.Filled 로 변경
                                Icon(Icons.Filled.Search, contentDescription = "커뮤니티 홈")
                            }
                        },
                        actions = {
                            IconButton(onClick = { onSearchOpenChange(true) }) {
                                // Icons.Default -> Icons.Filled 로 변경
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

                // 필터 탭
                CommonFilterTabs(
                    filterOptions = listOf("전체", "인기글", "산책후기", "정보공유"),
                    selectedFilter = activeFilter,
                    onFilterSelected = { activeFilter = it }
                )
            }
        }
    ) { paddingValues ->

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
//
//            // 게시물 목록
//            if (filteredPosts.isEmpty()) {
//                Box(
//                    modifier = Modifier.fillMaxSize(),
//                    contentAlignment = Alignment.Center
//                ) {
//                    Text(
//                        stringResource(cR.string.community_placeholder_post_empty),
//                        color = Gray400
//                    )
//                }
//            } else {
//                LazyColumn(modifier = Modifier.fillMaxSize()) {
//                    items(filteredPosts, key = { it.id }) { post ->
//                        PostCardView(
//                            post = post,
//                            isMyPost = post.author == "나",
//                            onLikeToggle = { viewModel.onLikeToggle(post.id) },
//                            onEdit = { editingPost = post },
//                            onDelete = {
//                                viewModel.deletePost(post.id)
//                                coroutineScope.launch {
//                                    snackbarHostState.showSnackbar(postDeleteMessage)
//                                }
//                            },
//                            onCommentClick = { viewModel.handleOpenComments(post) }
//                        )
//                    }
//                }
//            }
//
//            // 새 게시글 작성 다이얼로그
//            if (isCreateDialogOpen) {
//                PostEditorDialogView(
//                    categories = postEditorCategories,
//                    onDismiss = { isCreateDialogOpen = false },
//                    onSave = { content, image, category ->
//                        viewModel.createPost(content, image, category)
//                        isCreateDialogOpen = false
//                        coroutineScope.launch {
//                            snackbarHostState.showSnackbar(postCreateMessage)
//                        }
//                    }
//                )
//            }
//
//            // 게시글 수정 다이얼로그
//            if (isEditDialogOpen) {
//                PostEditorDialogView(
//                    categories = postEditorCategories,
//                    initialPost = editingPost,
//                    onDismiss = { editingPost = null },
//                    onSave = { content, image, category ->
//                        editingPost?.let {
//                            viewModel.updatePost(
//                                it.copy(
//                                    content = content,
//                                    image = image.takeIf { !it.isNullOrBlank() },
//                                    category = category
//                                )
//                            )
//                        }
//                        editingPost = null
//                        coroutineScope.launch {
//                            snackbarHostState.showSnackbar(postUpdateMessage)
//                        }
//                    }
//                )
//            }
        }
    }

    // -------------------- 댓글 BottomSheet --------------------
//    if (viewModel.selectedPostForComments != null) {
//        val context = LocalContext.current
//        val comments by viewModel.comments.collectAsStateWithLifecycle()
//
//        ModalBottomSheet(
//            onDismissRequest = { viewModel.handleCloseComments() },
//            sheetState = modalSheetState
//        ) {
//            CommonCommentSheetContent(
//                modifier = Modifier.fillMaxHeight(0.9f),
//                post = viewModel.selectedPostForComments!!,
//                comments = comments,
//                newCommentContent = viewModel.newCommentContent,
//                onNewCommentChange = { viewModel.newCommentContent = it },
//                onAddComment = {
//                    val success = viewModel.handleAddComment()
//                    Toast.makeText(
//                        context,
//                        if (success) "댓글이 작성되었습니다" else "댓글 내용을 입력해주세요",
//                        Toast.LENGTH_SHORT
//                    ).show()
//                },
//            )
//        }
//    }
}