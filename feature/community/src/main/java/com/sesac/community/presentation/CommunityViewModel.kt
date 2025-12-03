package com.sesac.community.presentation

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sesac.common.model.UiEvent
import com.sesac.domain.model.Comment
import com.sesac.domain.model.Post
import com.sesac.domain.result.AuthResult
import com.sesac.domain.result.ResponseUiState
import com.sesac.domain.type.BookmarkType
import com.sesac.domain.type.CommentType
import com.sesac.domain.type.LikeType
import com.sesac.domain.type.PostType
import com.sesac.domain.usecase.bookmark.BookmarkUseCase
import com.sesac.domain.usecase.comment.CommentUseCase
import com.sesac.domain.usecase.like.LikeUseCase
import com.sesac.domain.usecase.post.PostUseCase
import com.sesac.domain.usecase.session.SessionUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class CommunityViewModel @Inject constructor(
    private val sessionUseCase: SessionUseCase,
    private val postUseCase: PostUseCase,
    private val commentUseCase: CommentUseCase,
    private val bookmarkUseCase: BookmarkUseCase,
    private val likeUseCase: LikeUseCase
) : ViewModel() {
    // -------------------- UI 이벤트 --------------------
    private val _invalidToken = Channel<UiEvent>()
    val invalidToken = _invalidToken.receiveAsFlow()

    // -------------------- 게시글 상태 --------------------
    private val _postList = MutableStateFlow<ResponseUiState<List<Post>>>(ResponseUiState.Idle)
    val postList = _postList.asStateFlow()

    private val _myPosts = MutableStateFlow<ResponseUiState<List<Post>>>(ResponseUiState.Idle)
    val myPosts = _myPosts.asStateFlow()

    private val _post = MutableStateFlow<ResponseUiState<Post>>(ResponseUiState.Idle)
    val post = _post.asStateFlow()

    // -------------------- CRUD 상태 --------------------
    private val _createPostState = MutableStateFlow<ResponseUiState<Post>>(ResponseUiState.Idle)
    val createPostState = _createPostState.asStateFlow()

    private val _updatePostState = MutableStateFlow<ResponseUiState<Post>>(ResponseUiState.Idle)
    val updatePostState = _updatePostState.asStateFlow()

    private val _deletePostState = MutableStateFlow<ResponseUiState<Unit>>(ResponseUiState.Idle)
    val deletePostState = _deletePostState.asStateFlow()

    // -------------------- 다이얼로그 상태 --------------------
    val isCreateDialogOpen = MutableStateFlow(false)
    val editingPost = MutableStateFlow<Post?>(null)
    val isEditDialogOpen = editingPost.map { it != null }
        .stateIn(viewModelScope, SharingStarted.Lazily, false)

    // -------------------- 검색 / 필터 --------------------
    private val _searchQuery = MutableStateFlow("")
    val searchQuery = _searchQuery.asStateFlow()

    private val _activeFilter = MutableStateFlow("전체")
    val activeFilter = _activeFilter.asStateFlow()

    // 인기글 기준 (서버와 동기화 필요 시 수정)
    private val popularPostThreshold = 10

    fun onSearchQueryChange(query: String) { _searchQuery.value = query }
    fun onFilterChange(filter: String) { _activeFilter.value = filter }
    fun onStartEditing(post: Post) { editingPost.value = post }

    // -------------------- 댓글 --------------------
    private val _isCommentsOpen = MutableStateFlow(false)
    val isCommentsOpen = _isCommentsOpen.asStateFlow()

    private val _selectedPostForComments = MutableStateFlow<Post?>(null)
    val selectedPostForComments = _selectedPostForComments.asStateFlow()

    private val _comments = MutableStateFlow<List<Comment>>(emptyList())
    val comments = _comments.asStateFlow()

    private val _newCommentContent = MutableStateFlow("")
    val newCommentContent = _newCommentContent.asStateFlow()

    fun onNewCommentChange(newContent: String) { _newCommentContent.value = newContent }

    // -------------------- 필터링된 게시글 --------------------
    val filteredPosts: StateFlow<List<Post>> = combine(
        _postList, _searchQuery, _activeFilter
    ) { response, query, filter ->
        val posts = if (response is ResponseUiState.Success) response.result else emptyList()
        posts.filter { post ->
            val matchesQuery = query.isBlank() ||
                    post.title.contains(query, ignoreCase = true) ||
                    post.content.contains(query, ignoreCase = true)
            val matchesFilter = when (filter) {
                "인기글" -> post.likeCount >= popularPostThreshold
                "산책후기" -> post.postType == PostType.REVIEW
                "정보공유" -> post.postType == PostType.INFO
                else -> true
            }
            matchesQuery && matchesFilter
        }
    }.stateIn(viewModelScope, SharingStarted.Lazily, emptyList())


    // ---------------------------------------------------------
    // 🔥 게시글 목록
    // ---------------------------------------------------------
    fun getPostList(token: String?, query: String? = null) {
        viewModelScope.launch {
            if (token.isNullOrEmpty()) {
                _invalidToken.send(UiEvent.ToastEvent("유저 정보가 없습니다."))
                return@launch
            }

            _postList.value = ResponseUiState.Loading
            postUseCase.getPostListUseCase(token, query)
                .catch { e ->
                    Log.e("CommunityVM", "게시글 리스트 로드 실패: ${e.message}")
                }
                .collectLatest { result ->
                    when (result) {
                        is AuthResult.Success -> {
                            Log.d("CommunityVM", "게시글 목록 로드 성공: ${result.resultData.size}개")
                            // 첫 번째 게시글의 isLiked 상태 로깅
                            result.resultData.firstOrNull()?.let { post ->
                                Log.d("CommunityVM", "첫 게시글 - ID: ${post.id}, isLiked: ${post.isLiked}, likeCount: ${post.likeCount}")
                            }
                            _postList.value = ResponseUiState.Success("포스트를 목록을 불러왔습니다.", result.resultData)
                        }
                        is AuthResult.NetworkError -> {
//                            _invalidToken.send(UiEvent.ToastEvent(result.exception.message ?: "네트워크 오류"))
                            _postList.value = ResponseUiState.Error(result.exception.message ?: "네트워크 오류")
                        }
                        else -> Unit
                    }
                }
        }
    }

    fun getPostDetail(token: String?, id: Int) {
        viewModelScope.launch {
            if (token.isNullOrEmpty()) {
                _invalidToken.send(UiEvent.ToastEvent("유저 정보가 없습니다."))
                return@launch
            }

            _post.value = ResponseUiState.Loading
            postUseCase.getPostDetailUseCase(token, id)
                .catch { e -> _post.value = ResponseUiState.Error(e.message ?: "알 수 없는 오류") }
                .collectLatest { result ->
                    when (result) {
                        is AuthResult.Success -> {
                            val detail = result.resultData
                            _post.value = ResponseUiState.Success("조회 성공", detail)
                            editingPost.value = detail
                        }
                        is AuthResult.NetworkError -> {
                            _post.value = ResponseUiState.Error(result.exception.message ?: "네트워크 오류")
                            _invalidToken.send(UiEvent.ToastEvent(result.exception.message ?: "오류 발생"))
                        }
                        else -> Unit
                    }
                }
        }
    }

    // ---------------------------------------------------------
    // 🔥 CRUD
    // ---------------------------------------------------------
    fun createPost(token: String?, post: Post) {
        if (token.isNullOrEmpty()) {
            _createPostState.value = ResponseUiState.Error("로그인이 필요합니다.")
            return
        }
        viewModelScope.launch {
            _createPostState.value = ResponseUiState.Loading
            postUseCase.createPostUseCase(token, post)
                .catch { e -> _createPostState.value = ResponseUiState.Error(e.message ?: "알 수 없는 오류") }
                .collectLatest { result ->
                    val user = sessionUseCase.getUserInfo().first()
                    when (result) {
                        is AuthResult.Success -> {
                            _createPostState.value = ResponseUiState.Success("생성 성공", result.resultData)
                            val newPostDetail = result.resultData.copy(
                                userId = user?.id ?: -1, // 👈 FIX: Set current user's ID
                                authUserNickname = user?.nickname,
                                authUserProfileImageUrl = user?.profileImageUrl
                            )

                            (_postList.value as? ResponseUiState.Success)?.let { currentList ->
                                _postList.value = ResponseUiState.Success(currentList.message, listOf(newPostDetail) + currentList.result)
                            }

                            (_myPosts.value as? ResponseUiState.Success)?.let { successState ->
                                _myPosts.value = ResponseUiState.Success(
                                    successState.message,
                                    listOf(newPostDetail) + successState.result
                                )
                            }
                            _createPostState.value = ResponseUiState.Idle
                        }
                        is AuthResult.NetworkError -> {
                            _createPostState.value = ResponseUiState.Error(result.exception.message ?: "네트워크 오류")
//                            _invalidToken.send(UiEvent.ToastEvent("게시글 생성 실패"))
                        }
                        else -> Unit
                    }
                }
        }
    }

    fun updatePost(token: String, id: Int, post: Post) {
        viewModelScope.launch {
            _updatePostState.value = ResponseUiState.Loading
            postUseCase.updatePostUseCase(token, id, post)
                .catch { e -> _updatePostState.value = ResponseUiState.Error(e.message ?: "알 수 없는 오류") }
                .collectLatest { result ->
                    when (result) {
                        is AuthResult.Success -> {
                            _updatePostState.value = ResponseUiState.Success("수정 성공", result.resultData)
//                            _updatePostState.value = ResponseUiState.Idle
                        }
                        is AuthResult.NetworkError -> {
                            _updatePostState.value = ResponseUiState.Error(result.exception.message ?: "네트워크 오류")
                        }
                        else -> Unit
                    }
                }
        }
    }

    fun deletePost(token: String, id: Int) {
        viewModelScope.launch {
            _deletePostState.value = ResponseUiState.Loading
            postUseCase.deletePostUseCase(token, id)
                .catch { e -> _deletePostState.value = ResponseUiState.Error(e.message ?: "알 수 없는 오류") }
                .collectLatest { result ->
                    when (result) {
                        is AuthResult.Success -> {
                            _deletePostState.value = ResponseUiState.Success("삭제 성공", Unit)
                            (_postList.value as? ResponseUiState.Success)?.let { currentList ->
                                val new = currentList.result - result.resultData
//                                _postList.value = ResponseUiState.Success(currentList.message, new as List<Post>)
                                _postList.value = ResponseUiState.Success(currentList.message, currentList.result.filter { it.id != id })
                            }

                            (_myPosts.value as? ResponseUiState.Success)?.let { successState ->
                                _myPosts.value = ResponseUiState.Success(
                                    successState.message,
                                    successState.result.filter { post -> post.id != id }
                                )
                            }
                            _deletePostState.value = ResponseUiState.Idle
                        }
                        is AuthResult.NetworkError -> {
                            _deletePostState.value = ResponseUiState.Error(result.exception.message ?: "네트워크 오류")
                        }
                        else -> Unit
                    }
                }
        }
    }

    // ---------------------------------------------------------
    // ❤️ 좋아요
    // ---------------------------------------------------------
    fun toggleLike(token: String?, postId: Int) {
        if (token.isNullOrEmpty()) return

        viewModelScope.launch {
            likeUseCase.toggleLikeUseCase(token, postId, LikeType.POST)
                .catch { e -> Log.e("CommunityVM", "좋아요 오류: ${e.message}") }
                .collectLatest { result ->
                    if (result is AuthResult.Success) {
                        val likeCount = result.resultData.likeCount
                        val isLiked = result.resultData.isLiked

                        // 목록 갱신
                        (_postList.value as? ResponseUiState.Success)?.let { currentList ->
                            val updatedPosts = currentList.result.map { post ->
                                if (post.id == postId) post.copy(likeCount = likeCount, isLiked = isLiked)
                                else post
                            }
                            _postList.value = ResponseUiState.Success(currentList.message, updatedPosts)
                        }

                        // 댓글창이 열려있고 해당 게시글인 경우에만 업데이트
                        _selectedPostForComments.value?.let { selected ->
                            if (selected.id == postId) {
                                _selectedPostForComments.value = selected.copy(
                                    likeCount = likeCount,
                                    isLiked = isLiked
                                )
                            }
                        }
                    }
                }
        }
    }

    // ---------------------------------------------------------
    // ⭐ 북마크
    // ---------------------------------------------------------
    fun toggleBookmark(token: String?, postId: Int) {
        if (token.isNullOrEmpty()) return

        viewModelScope.launch {
            bookmarkUseCase.toggleBookmarkUseCase(token, postId, BookmarkType.POST)
                .catch { e -> Log.e("CommunityVM", "북마크 오류: ${e.message}") }
                .collectLatest { result ->
                    if (result is AuthResult.Success) {
                        val bookmarkCount = result.resultData.bookmarkCount
                        val isBookmarked = result.resultData.isBookmarked

                        // 목록 업데이트
                        (_postList.value as? ResponseUiState.Success)?.let { currentList ->
                            val updatedPosts = currentList.result.map { post ->
                                if (post.id == postId) post.copy(bookmarkCount = bookmarkCount, isBookmarked = isBookmarked)
                                else post
                            }
                            _postList.value = ResponseUiState.Success(currentList.message, updatedPosts)
                        }

                        // 댓글창이 열려있고 해당 게시글인 경우에만 업데이트
                        _selectedPostForComments.value?.let { selected ->
                            if (selected.id == postId) {
                                _selectedPostForComments.value = selected.copy(
                                    bookmarkCount = bookmarkCount,
                                    isBookmarked = isBookmarked
                                )
                            }
                        }

                    }
                }
        }
    }

    // ---------------------------------------------------------
    // 💬 댓글
    // ---------------------------------------------------------
    fun openComments(post: Post) {
        _selectedPostForComments.value = post
        _isCommentsOpen.value = true
        getComments(post.id)
    }

    fun closeComments() {
        _selectedPostForComments.value = null
        _isCommentsOpen.value = false
        _comments.value = emptyList()
    }

    fun getComments(postId: Int) {
        viewModelScope.launch {
            commentUseCase.getCommentsUseCase(postId, CommentType.POST)
                .catch { e -> Log.e("CommunityVM", "댓글 로드 오류: ${e.message}") }
                .collectLatest { result ->
                    if (result is AuthResult.Success) {
                        _comments.value = result.resultData
                    }
                }
        }
    }

    fun addComment(token: String?, postId: Int) {
        val content = _newCommentContent.value
        if (token.isNullOrEmpty() || content.isBlank()) return

        viewModelScope.launch {
            commentUseCase.createCommentUseCase(token, postId, content, CommentType.POST)
                .catch { e -> Log.e("CommunityVM", "댓글 추가 오류: ${e.message}") }
                .collectLatest { result ->
                    if (result is AuthResult.Success) {
                        _comments.update { it + result.resultData }
                        _newCommentContent.value = ""

                        // 댓글 수 업데이트
                        _selectedPostForComments.value?.let { selected ->
                            _selectedPostForComments.value = selected.copy(
                                commentCount = selected.commentCount + 1
                            )
                        }

                        // 목록의 댓글 수도 업데이트
                        (_postList.value as? ResponseUiState.Success)?.let { currentList ->
                            val updatedPosts = currentList.result.map { post ->
                                if (post.id == postId) post.copy(commentCount = post.commentCount + 1) else post
                            }
                            _postList.value = ResponseUiState.Success(currentList.message, updatedPosts)
                        }
                    }
                }
        }
    }

    fun deleteComment(token: String?, postId: Int, commentId: Int) {
        if (token.isNullOrEmpty()) return
        viewModelScope.launch {
            commentUseCase.deleteCommentUseCase(token, postId, commentId, CommentType.POST)
                .catch { e -> Log.e("CommunityVM", "댓글 삭제 오류: ${e.message}") }
                .collectLatest { result ->
                    if (result is AuthResult.Success) {
                        _comments.update { it.filterNot { comment -> comment.id == commentId } }

                        // 댓글 수 감소
                        _selectedPostForComments.value?.let { selected ->
                            _selectedPostForComments.value = selected.copy(
                                commentCount = maxOf(0, selected.commentCount - 1)
                            )
                        }

                        // 목록의 댓글 수도 감소
                        (_postList.value as? ResponseUiState.Success)?.let { currentList ->
                            val updatedPosts = currentList.result.map { post ->
                                if (post.id == postId) post.copy(commentCount = maxOf(0, post.commentCount - 1))
                                else post
                            }
                            _postList.value = ResponseUiState.Success(currentList.message, updatedPosts)
                        }
                    }
                }
        }
    }


    fun updatePostList(postId: Int, newPost: Post) {
        (_postList.value as? ResponseUiState.Success)?.let { currentList ->
            val updatedPosts = currentList.result.map { post ->
                if (post.id == postId) newPost else post
            }
            _postList.value = ResponseUiState.Success(currentList.message, updatedPosts)
        }
    }
}