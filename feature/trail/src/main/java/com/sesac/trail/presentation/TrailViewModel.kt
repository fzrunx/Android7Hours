package com.sesac.trail.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sesac.domain.model.MyRecord
import com.sesac.domain.model.UserPath
import com.sesac.domain.usecase.trail.TrailUseCase
import com.sesac.trail.presentation.ui.WalkPathTab
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import com.sesac.domain.model.Comment
import com.sesac.domain.model.Post
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import java.util.Date


@HiltViewModel
class TrailViewModel @Inject constructor(
    private val trailUseCase: TrailUseCase,
): ViewModel() {
    private val _recommendedPaths = MutableStateFlow<List<UserPath>>(emptyList())
    val recommendedPaths = _recommendedPaths.asStateFlow()

    private val _myRecords = MutableStateFlow<List<MyRecord>>(emptyList())
    val myRecords = _myRecords.asStateFlow()

    private val _isSheetOpen = MutableStateFlow(false)
    val isSheetOpen get() = _isSheetOpen.asStateFlow()
    private val _isPaused = MutableStateFlow(false)
    val isPaused = _isPaused.asStateFlow()
    private val _isRecording = MutableStateFlow(false)
    private val _isFollowingPath = MutableStateFlow(false)
    val isFollowingPath get() = _isRecording.asStateFlow()
    val isRecoding get() = _isRecording.asStateFlow()
    private val _recordingTime = MutableStateFlow<Long>(0L)
    val recordingTime = _recordingTime.asStateFlow()
    private val _activeTab = MutableStateFlow(WalkPathTab.RECOMMENDED)
    val activeTab get() = _activeTab.asStateFlow()

    private val _selectedPath = MutableStateFlow<UserPath?>(null)
    val selectedPath get() = _selectedPath.asStateFlow()


    init {
        getRecommendedPaths()
        getMyRecords()
    }

    private fun getRecommendedPaths() {
        viewModelScope.launch {
            trailUseCase.getAllRecommendedPathsUseCase().collectLatest { paths ->
                _recommendedPaths.value = paths.filterNotNull()
            }
        }
    }

    private fun getMyRecords() {
        viewModelScope.launch {
            trailUseCase.getAllMyRecordUseCase().collectLatest { records ->
                _myRecords.value = records.filterNotNull()
            }
        }
    }

    fun savePath() {
        viewModelScope.launch {
            _selectedPath.value?.let { path ->
                trailUseCase.addMyRecordUseCase(path.toMyRecord()).collectLatest { success ->
                    if (success) {
                        getMyRecords() // Refresh the list
                    }
                }
            }
        }
    }

    fun clearSelectedPath() {
        _selectedPath.value = null
    }

    fun updateRecordingTime(changeRate: Long?) {
        _recordingTime.value += changeRate ?: -_recordingTime.value
    }

    fun updateIsSheetOpen(newState: Boolean?) {
        viewModelScope.launch { _isSheetOpen.value = newState ?: !_isSheetOpen.value }
    }

    fun updateIsFollowingPath(newState: Boolean?) {
        viewModelScope.launch { _isFollowingPath.value = newState ?: !_isFollowingPath.value }
    }

    fun updateIsPaused(newState: Boolean?) {
        viewModelScope.launch { _isPaused.value = newState ?: !_isPaused.value }
    }

    fun updateIsRecording(newState: Boolean?) {
        viewModelScope.launch { _isRecording.value = newState ?: !_isRecording.value }
    }

    fun updatePausedState() {
        viewModelScope.launch { _isPaused.value = !_isPaused.value }
    }

    fun updateActiveTab(walkPathTab: WalkPathTab) {
        viewModelScope.launch { _activeTab.value = walkPathTab }
    }

    fun updateSelectedPath(path: UserPath?) {
        viewModelScope.launch { _selectedPath.value = path }
    }

    fun updateSelectedPathLikes(isLiked: Boolean): Boolean {
        viewModelScope.launch {
            _selectedPath.value?.let {
                val preLikes = it.likes
                _selectedPath.value = it.copy(likes = if (isLiked) preLikes - 1 else preLikes + 1)
            }
        }
        return !isLiked
    }

    // 댓글 상태
    private val _comments = MutableStateFlow<List<Comment>>(emptyList())
    val comments: StateFlow<List<Comment>> get() = _comments.asStateFlow()

    // 선택된 게시물
    var selectedPostForComments by mutableStateOf<Post?>(null)
        private set

    // 댓글 시트 열림 여부
    var isCommentsOpen by mutableStateOf(false)
        private set

    // 새 댓글 내용
    var newCommentContent by mutableStateOf("")

    fun handleOpenComments(path: UserPath) {
        // Create a synthetic Post object from the UserPath
        selectedPostForComments = Post(
            id = path.id.toLong(),
            author = path.uploader,
            authorImage = "", // No author image in UserPath
            timeAgo = "", // No time info in UserPath
            content = path.name, // Use path name as content
            image = null, // No image in UserPath
            likes = path.likes,
            comments = 0, // We'll get comments from _comments
            isLiked = false, // Assuming not liked by default
            category = "", // No category in UserPath
            createdAt = Date() // Placeholder
        )
        isCommentsOpen = true
    }

    fun handleCloseComments() {
        isCommentsOpen = false
        selectedPostForComments = null
    }

    fun handleAddComment(): Boolean {
        val post = selectedPostForComments ?: return false
        if (newCommentContent.isBlank()) return false

        val newComment = Comment(
            id = System.currentTimeMillis(),
            postId = post.id.toInt(),
            author = "나", // TODO: Replace with actual user info
            authorImage = "https://picsum.photos/seed/me/200", // TODO: Replace with actual user profile
            timeAgo = "방금 전",
            content = newCommentContent
        )

        // Update comments list
        _comments.update { it + newComment }

        // We don't need to update a list of posts here, as we only have one "post"
        // But we could update the comment count on the selectedPostForComments
        selectedPostForComments = selectedPostForComments?.copy(comments = selectedPostForComments!!.comments + 1)


        newCommentContent = ""
        return true
    }
}