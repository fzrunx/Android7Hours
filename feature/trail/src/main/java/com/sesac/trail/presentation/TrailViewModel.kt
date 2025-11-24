package com.sesac.trail.presentation

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sesac.domain.model.Coord
import com.sesac.domain.model.UiEvent
import com.sesac.domain.model.Path
import com.sesac.domain.result.AuthResult
import com.sesac.domain.usecase.path.PathUseCase
import com.sesac.trail.presentation.ui.WalkPathTab
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import javax.inject.Inject
import com.naver.maps.map.NaverMap // ⭐ 추가
import com.naver.maps.map.overlay.Marker // ⭐ 추가
import com.naver.maps.map.overlay.PolylineOverlay // ⭐ 추가
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import com.sesac.domain.model.BookmarkType
import com.sesac.domain.model.BookmarkedPath
import com.sesac.domain.model.Comment
import com.sesac.domain.model.Post
import com.sesac.domain.result.ResponseUiState
import com.sesac.domain.usecase.bookmark.BookmarkUseCase
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.update


@HiltViewModel
class TrailViewModel @Inject constructor(
    private val pathUseCase: PathUseCase,
    private val bookmarkUseCase: BookmarkUseCase
): ViewModel() {
    private val _invalidToken = Channel<UiEvent>()
    val invalidToken = _invalidToken.receiveAsFlow()
    private val _recommendedPaths = MutableStateFlow<ResponseUiState<List<Path>>>(ResponseUiState.Idle)
    val recommendedPaths = _recommendedPaths.asStateFlow()
    // 폴리라인 인스턴스를 ViewModel State로 관리
    private val _polylineOverlay = MutableStateFlow<PolylineOverlay?>(null)
    val polylineOverlay = _polylineOverlay.asStateFlow()

    // 마커 리스트를 ViewModel 내부의 MutableList로 관리
    val currentMarkers: MutableList<Marker> = mutableListOf()

    fun clearAllMapObjects(naverMap: NaverMap?) {
        if (naverMap == null) return

        // 1. 폴리라인 제거 및 초기화
        _polylineOverlay.value?.map = null // 지도에서 명시적으로 제거
        _polylineOverlay.value = null      // ViewModel 상태 초기화

        // 2. 마커 제거 및 초기화
        currentMarkers.forEach { marker ->
            marker.map = null // 지도에서 명시적으로 제거
        }
        currentMarkers.clear() // 리스트 비우기

        // 디버깅 용
        println("🧹 TrailViewModel: 지도 객체 (폴리라인/마커) 초기화 완료")
    }

    fun setPolylineInstance(polyline: PolylineOverlay) {
        _polylineOverlay.value = polyline
    }

    private val _myPaths = MutableStateFlow<ResponseUiState<List<Path>>>(ResponseUiState.Idle)
    val myPaths = _myPaths.asStateFlow()
    private val _bookmarkedPaths = MutableStateFlow<ResponseUiState<List<BookmarkedPath>>>(ResponseUiState.Idle)
    val bookmarkedPaths = _bookmarkedPaths.asStateFlow()
    private val _isSheetOpen = MutableStateFlow(false)
    val isSheetOpen get() = _isSheetOpen.asStateFlow()
    private val _isPaused = MutableStateFlow(false)
    val isPaused = _isPaused.asStateFlow()
    private val _isRecording = MutableStateFlow(false)
    private val _isFollowingPath = MutableStateFlow(false)
    val isFollowingPath get() = _isFollowingPath.asStateFlow()
    val isRecoding get() = _isRecording.asStateFlow()
    private val _recordingTime = MutableStateFlow<Long>(0L)
    val recordingTime = _recordingTime.asStateFlow()
    private val _activeTab = MutableStateFlow(WalkPathTab.RECOMMENDED)
    val activeTab get() = _activeTab.asStateFlow()

    private val _isEditMode = MutableStateFlow(false)
    val isEditMode get() = _isEditMode.asStateFlow()

    private val _selectedPath = MutableStateFlow<Path?>(null)
    val selectedPath get() = _selectedPath.asStateFlow()

//    init {
//        getRecommendedPaths()
//        getMyRecords()
//    }

    fun getRecommendedPaths(coord: Coord, radius: Float = 5000f) {
        viewModelScope.launch {
            _recommendedPaths.value = ResponseUiState.Loading
            pathUseCase.getAllRecommendedPathsUseCase(coord, radius)
                .catch { e ->
                    _recommendedPaths.value = ResponseUiState.Error(e.message ?: "알 수 없는 오류가 발생했습니다.")
                }
                .collectLatest { pathsResult ->
                when (pathsResult) {
                    is AuthResult.Success -> {
                        _recommendedPaths.value = ResponseUiState.Success("추천 경로를 불러왔습니다.", pathsResult.resultData)
                    }
                    is AuthResult.NetworkError -> {
                        _recommendedPaths.value = ResponseUiState.Error(pathsResult.exception.message ?: "unknown")
                    }
                    else -> Unit
                }
            }
        }
    }

    fun getMyPaths(token: String?) {
        viewModelScope.launch {
            _myPaths.value = ResponseUiState.Loading
            if (token == null) {
                _myPaths.value = ResponseUiState.Error("로그인이 필요합니다.")
                return@launch
            }
            pathUseCase.getMyPaths(token)
                .catch { e ->
                    _myPaths.value = ResponseUiState.Error(e.message ?: "알 수 없는 오류가 발생했습니다.")
                }
                .collectLatest { pathsResult ->
                when (pathsResult) {
                    is AuthResult.Success -> {
                        _myPaths.value = ResponseUiState.Success("내 경로를 불러왔습니다.", pathsResult.resultData)
                    }
                    is AuthResult.NetworkError -> {
                        _myPaths.value = ResponseUiState.Error(pathsResult.exception.message ?: "unknown")
                    }
                    else -> Unit
                }
            }
        }
    }

//    fun getMyRecords() {
//        viewModelScope.launch {
//            trailUseCase.getAllMyRecordUseCase().collectLatest { records ->
//                _myPaths.value = records.filterNotNull()
//            }
//        }
//    }

    fun savePath(token: String?, currentCoord: Coord?, radius: Float = 5000f) {
        viewModelScope.launch {
            if (token.isNullOrEmpty()) { _invalidToken.send(UiEvent.ToastEvent("유저 정보가 없습니다.")) }
            else{
                _selectedPath.value?.let { path ->
                    pathUseCase.createPathUseCase(token, path).collectLatest { createdPath ->
                        // You might want to refresh the list or navigate
                        // For now, just update the selected path with the created one
                        if (createdPath is AuthResult.Success) {
                            val coord = currentCoord ?: createdPath.resultData.coord?.first() ?: Coord.DEFAULT
                            _selectedPath.value = createdPath.resultData
                            getRecommendedPaths(coord, radius) // Refresh recommended paths
                        }
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

    fun getUserBookmarkedPaths(token: String?) {
        viewModelScope.launch {
            _bookmarkedPaths.value = ResponseUiState.Loading
            if (token == null) {
                _bookmarkedPaths.value = ResponseUiState.Error("로그인이 필요합니다.")
                return@launch
            }

            bookmarkUseCase.getMyBookmarksUseCase(token)
                .catch { e ->
                    _bookmarkedPaths.value = ResponseUiState.Error(e.message ?: "알 수 없는 오류가 발생했습니다.")
                }
                .collectLatest { bookmarksResult ->
                    when (bookmarksResult) {
                        is AuthResult.Success -> {
                            val pathList = bookmarksResult.resultData.mapNotNull { it.bookmarkedItem as? BookmarkedPath }
                            _bookmarkedPaths.value = ResponseUiState.Success("북마크를 불러왔습니다.", pathList)
                        }
                        is AuthResult.NetworkError -> {
                            _bookmarkedPaths.value = ResponseUiState.Error(bookmarksResult.exception.message ?: "unknown")
                        }
                        else -> {
                            // Other AuthResult states are not handled here.
                        }
                    }
                }
        }
    }

    fun toggleBookmark(token: String?, id: Int) {
        viewModelScope.launch {
            if (token == null) {
                Log.e("MypageViewModel", "Toggle bookmark failed: token is null")
                return@launch
            }
            bookmarkUseCase.toggleBookmarkUseCase(token, id, BookmarkType.PATH)
                .collectLatest { bookmarkResponse ->
                    if (bookmarkResponse is AuthResult.Success) {
                        // Refresh the list on success
                        getUserBookmarkedPaths(token)
                        _selectedPath.value = _selectedPath.value?.copy(bookmarksCount = bookmarkResponse.resultData.bookmarksCount)
                    } else if (bookmarkResponse is AuthResult.NetworkError) {
                        Log.e("MypageViewModel", "Toggle bookmark failed: ${bookmarkResponse.exception}")
                    }
                }
        }
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

    fun updateSelectedPath(path: Path?) {
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

    fun updatePath(token: String?) {
        viewModelScope.launch {
            if (token.isNullOrEmpty()) {
                _invalidToken.send(UiEvent.ToastEvent("유저 정보가 없습니다."))
                return@launch
            }
            _selectedPath.value?.let { path ->
                pathUseCase.updatePathUseCase(token, path.id, path).collectLatest { result ->
                    if (result is AuthResult.Success) {
                        getMyPaths(token)
                    }
                }
            }
        }
    }

    fun deletePath(token: String?, pathId: Int) {
        viewModelScope.launch {
            if (token.isNullOrEmpty()) {
                _invalidToken.send(UiEvent.ToastEvent("유저 정보가 없습니다."))
                return@launch
            }
            pathUseCase.deletePathUseCase(token, pathId).collectLatest { result ->
                if (result is AuthResult.Success) {
                    getMyPaths(token)
                }
            }
        }
    }

    fun updateIsEditMode(isEditing: Boolean? = null) {
        _isEditMode.value = isEditing ?: !_isEditMode.value
    }

    // ⭐ Draft 기능 관련 StateFlow 추가
    private val _drafts = MutableStateFlow<List<Path>>(emptyList())
    val drafts: StateFlow<List<Path>> get() = _drafts.asStateFlow()

    // Draft 목록 불러오기 (suspend)
    suspend fun loadDrafts(): List<Path> {
        return try {
            val list = pathUseCase.getAllDraftsUseCase().first() // Flow -> 단일값 추출
            _drafts.value = list
            list
        } catch (e: Exception) {
            emptyList()
        }
    }

    // Draft 저장 (suspend)
    suspend fun saveDraft(draft: Path): Boolean {
        return try {
            val success = pathUseCase.saveDraftUseCase(draft).first() // Flow -> 단일값
            if (success) loadDrafts()
            success
        } catch (e: Exception) {
            false
        }
    }

    // Draft 삭제 (suspend)
    suspend fun deleteDraft(draft: Path): Boolean {
        return try {
            val success = pathUseCase.deleteDraftUseCase(draft).first() // Flow -> 단일값
            if (success) loadDrafts() // 삭제 후 목록 갱신
            success
        } catch (e: Exception) {
            false
        }
    }

    // Draft 전체 삭제 (suspend)
    suspend fun clearAllDrafts(): Boolean {
        return try {
            val success = pathUseCase.clearAllDraftsUseCase().first() // Flow -> 단일값
            if (success) _drafts.value = emptyList()
            success
        } catch (e: Exception) {
            false
        }
    }

    // UI에서 화면 나가기 직전에 호출할 임시 저장 함수
    suspend fun saveDraftIfNotEmpty() {
        _selectedPath.value?.let {
            if (it.id == -1 && (it.coord?.isNotEmpty() == true || it.pathComment?.let { d -> d.trim().isNotEmpty() } == true)) {
                saveDraft(it)
            }
        }
    }

    fun saveDraftAsync(draft: Path) {
        viewModelScope.launch {
            saveDraft(draft)
        }
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

    fun handleOpenComments(path: Path) {
        // Create a synthetic Post object from the UserPath
        selectedPostForComments = Post.EMPTY
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
        selectedPostForComments = selectedPostForComments?.copy(commentsCount = selectedPostForComments!!.commentsCount + 1)


        newCommentContent = ""
        return true
    }
}