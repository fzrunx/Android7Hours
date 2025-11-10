package com.sesac.community.presentation.ui

import android.annotation.SuppressLint
import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Send
import androidx.compose.material3.*
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.sesac.common.component.CommonFilterTabs
import com.sesac.common.component.CommonSearchBarContent
import com.sesac.common.ui.theme.Gray400
import com.sesac.common.ui.theme.Primary
import com.sesac.community.presentation.CommunityViewModel
import com.sesac.community.presentation.Post
import com.sesac.community.presentation.Comment
import coil3.compose.rememberAsyncImagePainter
import kotlinx.coroutines.launch
import com.sesac.common.R as cR

@SuppressLint("UnrememberedMutableState")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CommunityMainScreen(
    isSearchOpen: MutableState<Boolean>,
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

    val filteredPosts by viewModel.filteredPosts.collectAsState()
    val searchQuery by viewModel.searchQuery.collectAsStateWithLifecycle()
    val activeFilter by viewModel.activeFilter.collectAsState()

    val postDeleteMessage = stringResource(cR.string.community_snackbar_post_delecte)
    val postCreateMessage = stringResource(cR.string.community_snackbar_post_create)
    val postUpdateMessage = stringResource(cR.string.community_snackbar_post_update)
    val postEditorCategories = listOf("산책후기", "정보공유", "질문")

    // BottomSheet와 ViewModel 상태 동기화
    LaunchedEffect(viewModel.isCommentsOpen) {
        if (viewModel.isCommentsOpen) modalSheetState.show()
        else modalSheetState.hide()
    }

    // -------------------- UI --------------------
    Scaffold(
        floatingActionButton = {
            FloatingActionButton(
                onClick = { isCreateDialogOpen = true },
                containerColor = Primary,
                contentColor = Color.White
            ) {
                Icon(Icons.Default.Add, contentDescription = "새 게시글 작성")
            }
        },
        snackbarHost = { SnackbarHost(snackbarHostState) }
    ) { paddingValues ->

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            // 검색바 & 필터
            CommonSearchBarContent(
                isSearchOpen = isSearchOpen.value,
                query = searchQuery,
                onQueryChange = viewModel::onSearchQueryChange
            )
            CommonFilterTabs(
                filterOptions = listOf("전체", "인기글", "산책후기", "정보공유"),
                selectedFilter = activeFilter,
                onFilterSelected = viewModel::onFilterChange
            )

            // 게시물 목록
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
                            isMyPost = post.author == "나",
                            onLikeToggle = { viewModel.onLikeToggle(post.id) },
                            onEdit = { editingPost = post },
                            onDelete = {
                                viewModel.deletePost(post.id)
                                coroutineScope.launch {
                                    snackbarHostState.showSnackbar(postDeleteMessage)
                                }
                            },
                            onCommentClick = { viewModel.handleOpenComments(post) }
                        )
                    }
                }
            }

            // 새 게시글 작성 다이얼로그
            if (isCreateDialogOpen) {
                PostEditorDialogView(
                    categories = postEditorCategories,
                    onDismiss = { isCreateDialogOpen = false },
                    onSave = { content, image, category ->
                        viewModel.createPost(content, image, category)
                        isCreateDialogOpen = false
                        coroutineScope.launch {
                            snackbarHostState.showSnackbar(postCreateMessage)
                        }
                    }
                )
            }

            // 게시글 수정 다이얼로그
            if (isEditDialogOpen) {
                PostEditorDialogView(
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
                        coroutineScope.launch {
                            snackbarHostState.showSnackbar(postUpdateMessage)
                        }
                    }
                )
            }
        }
    }

    // -------------------- 댓글 BottomSheet --------------------
    if (viewModel.selectedPostForComments != null) {
        ModalBottomSheet(
            onDismissRequest = { viewModel.handleCloseComments() },
            sheetState = modalSheetState
        ) {
            CommentSheetContent(
                post = viewModel.selectedPostForComments!!,
                viewModel = viewModel,
                onClose = { viewModel.handleCloseComments() }
            )
        }
    }
}

// -------------------- 댓글 관련 Composable --------------------
@Composable
fun CommentSheetContent(
    post: Post,
    viewModel: CommunityViewModel,
    onClose: () -> Unit
) {
    val context = LocalContext.current
    val comments by viewModel.comments.collectAsStateWithLifecycle()
    val postComments by remember(post, comments) {
        derivedStateOf { comments.filter { it.postId == post.id.toInt() } }
    }

    Column(
        modifier = Modifier
            .fillMaxHeight(0.9f)
            .padding(16.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "댓글 (${postComments.size})",
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold
            )
            IconButton(onClick = onClose) {
                Icon(Icons.Default.Close, contentDescription = "닫기")
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        LazyColumn(
            modifier = Modifier.weight(1f),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            items(postComments, key = { it.id }) { comment ->
                CommentItem(comment)
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            TextField(
                value = viewModel.newCommentContent,
                onValueChange = { viewModel.newCommentContent = it },
                placeholder = { Text("댓글 달기...") },
                modifier = Modifier.weight(1f),
                shape = RoundedCornerShape(24.dp)
            )

            Spacer(modifier = Modifier.width(8.dp))

            IconButton(
                onClick = {
                    val success = viewModel.handleAddComment()
                    Toast.makeText(
                        context,
                        if (success) "댓글이 작성되었습니다" else "댓글 내용을 입력해주세요",
                        Toast.LENGTH_SHORT
                    ).show()
                },
                enabled = viewModel.newCommentContent.isNotBlank()
            ) {
                Icon(
                    Icons.Default.Send,
                    contentDescription = "댓글 작성",
                    tint = if (viewModel.newCommentContent.isNotBlank())
                        MaterialTheme.colorScheme.primary else Color.Gray
                )
            }
        }
    }
}

@Composable
fun CommentItem(comment: Comment) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.Top
    ) {
        Image(
            painter = rememberAsyncImagePainter(comment.authorImage),
            contentDescription = "댓글 작성자 프로필",
            modifier = Modifier
                .size(32.dp)
                .clip(CircleShape),
        )
        Spacer(modifier = Modifier.width(12.dp))
        Column {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(text = comment.author, fontWeight = FontWeight.Bold, fontSize = 14.sp)
                Spacer(modifier = Modifier.width(8.dp))
                Text(text = comment.timeAgo, fontSize = 12.sp, color = Color.Gray)
            }
            Spacer(modifier = Modifier.height(4.dp))
            Text(text = comment.content, fontSize = 14.sp)
        }
    }
}
