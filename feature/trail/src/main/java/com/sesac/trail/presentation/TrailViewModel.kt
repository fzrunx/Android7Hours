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
import com.naver.maps.geometry.LatLng // ⭐ 추가
import com.naver.maps.map.NaverMap
import com.naver.maps.map.overlay.Marker
import com.naver.maps.map.overlay.PolylineOverlay
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import com.sesac.domain.model.BookmarkType
import com.sesac.domain.model.BookmarkedPath
import com.sesac.domain.model.Comment
import com.sesac.domain.model.Post
import com.sesac.domain.result.ResponseUiState
import com.sesac.domain.usecase.bookmark.BookmarkUseCase
import com.sesac.domain.usecase.comment.CommentUseCases
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.update

@HiltViewModel
class TrailViewModel @Inject constructor(
    private val pathUseCase: PathUseCase,
    private val bookmarkUseCase: BookmarkUseCase,
    private val commentUseCases: CommentUseCases
): ViewModel() {
    private val _invalidToken = Channel<UiEvent>()
    val invalidToken = _invalidToken.receiveAsFlow()

    // =================================================================
    // 📌 1. 지도 녹화 관련 데이터 (MainScreen에서 사용)
    // =================================================================

    // ✅ 수정: LatLng 타입으로 변경 (UI 레이어에서 사용하는 타입)
    private val _tempPathCoords = MutableStateFlow<List<LatLng>>(emptyList())
    val tempPathCoords = _tempPathCoords.asStateFlow()

    fun addTempPoint(point: LatLng) {
        _tempPathCoords.value = _tempPathCoords.value + point
    }

    fun clearTempPath() {
        _tempPathCoords.value = emptyList()
    }

    // =================================================================
    // 📌 2. 녹화 상태 관리
    // =================================================================

    private val _isRecording = MutableStateFlow(false)
    val isRecording = _isRecording.asStateFlow()

    private val _isPaused = MutableStateFlow(false)
    val isPaused = _isPaused.asStateFlow()

    private val _recordingTime = MutableStateFlow(0L)
    val recordingTime = _recordingTime.asStateFlow()

    fun startRecording() {
        _isRecording.value = true
        _isPaused.value = false
        _recordingTime.value = 0L
        clearTempPath()
    }

    fun pauseRecording() {
        _isPaused.value = true
    }

    fun resumeRecording() {
        _isPaused.value = false
    }

    fun stopRecording() {
        _isRecording.value = false
        _isPaused.value = false
    }

    fun addRecordingTime(delta: Long) {
        _recordingTime.value += delta
    }

    // ✅ 추가: MainScreen에서 사용하는 편의 함수
    fun updateRecordingTime(changeRate: Long?) {
        _recordingTime.value += changeRate ?: -_recordingTime.value
    }

    fun updateIsPaused(newState: Boolean?) {
        viewModelScope.launch {
            _isPaused.value = newState ?: !_isPaused.value
        }
    }

    fun updateIsRecording(newState: Boolean?) {
        viewModelScope.launch {
            _isRecording.value = newState ?: !_isRecording.value
        }
    }

    // =================================================================
    // 📌 3. 지도 오버레이 관리 (폴리라인, 마커)
    // =================================================================

    private val _polylineOverlay = MutableStateFlow<PolylineOverlay?>(null)
    val polylineOverlay = _polylineOverlay.asStateFlow()

    // 마커 리스트를 ViewModel 내부의 MutableList로 관리
    val currentMarkers: MutableList<Marker> = mutableListOf()

    fun setPolylineInstance(polyline: PolylineOverlay) {
        _polylineOverlay.value = polyline
    }

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

        println("🧹 TrailViewModel: 지도 객체 초기화 완료")
    }

    // =================================================================
    // 📌 4. 경로 목록 관리 (추천 경로, 내 경로)
    // =================================================================

    private val _recommendedPaths = MutableStateFlow<ResponseUiState<List<Path>>>(ResponseUiState.Idle)
    val recommendedPaths = _recommendedPaths.asStateFlow()
    private val _myPaths = MutableStateFlow<ResponseUiState<List<Path>>>(ResponseUiState.Idle)
    val myPaths = _myPaths.asStateFlow()

    private val _bookmarkedPaths = MutableStateFlow<ResponseUiState<List<BookmarkedPath>>>(ResponseUiState.Idle)
    val bookmarkedPaths = _bookmarkedPaths.asStateFlow()


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

    // =================================================================
    // 📌 5. 선택된 경로 관리
    // =================================================================

    fun updateSelectedPath(path: Path?) {
        viewModelScope.launch {
            _selectedPath.value = path
        }
    }

    fun clearSelectedPath() {
        _selectedPath.value = null
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


    fun updatePausedState() {
        viewModelScope.launch { _isPaused.value = !_isPaused.value }
    }


    fun updateSelectedPathLikes(isLiked: Boolean): Boolean {
        viewModelScope.launch {
            _selectedPath.value?.let {
                val preLikes = it.likes
                _selectedPath.value = it.copy(
                    likes = if (isLiked) preLikes - 1 else preLikes + 1
                )
            }
        }
        return !isLiked
    }

    // =================================================================
    // 📌 6. 경로 CRUD (생성, 수정, 삭제)
    // =================================================================

    // ✅ 수정: CreateScreen에서 사용 (녹화 완료 후 저장)
    fun savePath(token: String?, currentCoord: Coord?, radius: Float = 5000f) {
        viewModelScope.launch {
            if (token.isNullOrEmpty()) {
                _invalidToken.send(UiEvent.ToastEvent("유저 정보가 없습니다."))
                return@launch
            }

            _selectedPath.value?.let { path ->
                pathUseCase.createPathUseCase(token, path).collectLatest { result ->
                    if (result is AuthResult.Success) {
                        val coord = currentCoord ?: result.resultData.coord?.first() ?: Coord.DEFAULT
                        _selectedPath.value = result.resultData
                        getRecommendedPaths(coord, radius)
                    }
                }
            }
        }
    }
    fun saveCurrentDraft(name: String, description: String?) {
        // 1. Draft 생성
        createDraftPath(name, description)

        // 2. RoomDB에 저장
        _draftPath.value?.let { draft ->
            savePathToRoom(draft)
        }
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

    // =================================================================
    // 📌 7. UI 상태 관리 (시트, 팔로우, 편집 모드 등)
    // =================================================================

    private val _isSheetOpen = MutableStateFlow(false)
    val isSheetOpen get() = _isSheetOpen.asStateFlow()

    private val _isFollowingPath = MutableStateFlow(false)
    val isFollowingPath get() = _isFollowingPath.asStateFlow()

    private val _activeTab = MutableStateFlow(WalkPathTab.RECOMMENDED)
    val activeTab get() = _activeTab.asStateFlow()

    private val _isEditMode = MutableStateFlow(false)
    val isEditMode get() = _isEditMode.asStateFlow()

    fun updateIsSheetOpen(newState: Boolean?) {
        viewModelScope.launch {
            _isSheetOpen.value = newState ?: !_isSheetOpen.value
        }
    }

    fun updateIsFollowingPath(newState: Boolean?) {
        viewModelScope.launch {
            _isFollowingPath.value = newState ?: !_isFollowingPath.value
        }
    }

    fun updateActiveTab(walkPathTab: WalkPathTab) {
        viewModelScope.launch {
            _activeTab.value = walkPathTab
        }
    }

    fun updateIsEditMode(isEditing: Boolean? = null) {
        _isEditMode.value = isEditing ?: !_isEditMode.value
    }

    // =================================================================
    // 📌 8. Draft 기능 (임시 저장)
    // =================================================================

    // ✅ 추가: CreateScreen에서 사용하는 임시 경로 데이터
    private val _draftPath = MutableStateFlow<Path?>(null)
    val draftPath = _draftPath.asStateFlow()

    // ✅ 추가: 지도에 표시할 메모 마커 목록
    private val _memoMarkers = MutableStateFlow<List<com.sesac.domain.model.MemoMarker>>(emptyList())
    val memoMarkers = _memoMarkers.asStateFlow()

    fun addMemoMarker(latitude: Double, longitude: Double, memo: String) {
        val newMarker = com.sesac.domain.model.MemoMarker(latitude, longitude, memo)
        _memoMarkers.value = _memoMarkers.value + newMarker
    }

    fun clearMemoMarkers() {
        _memoMarkers.value = emptyList()
    }


    fun createDraftPath(name: String, description: String?) {
        val coords = tempPathCoords.value.map { latLng ->
            Coord(latLng.latitude, latLng.longitude)
        }

        _draftPath.value = Path(
            id = -1,
            pathName = name,
            pathComment = description ?: "",
            coord = coords,
            markers = _memoMarkers.value,
            likes = 0,
            uploader = "",
            // Provide default values for newly added fields in Path data class
            bookmarksCount = 0,
            isBookmarked = false,
            distanceFromMe = 0f,
            tags = emptyList()
        )
    }

    fun clearDraftPath() {
        _draftPath.value = null
        clearMemoMarkers() // ✅ 임시 경로 삭제 시 마커도 함께 삭제
    }

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
            Log.d("TrailViewModel", "🔄 Calling trailUseCase.saveDraftUseCase...")
            Log.d("TrailViewModel", "Draft details: id=${draft.id}, name=${draft.pathName}, coords=${draft.coord?.size}")

            val success = pathUseCase.saveDraftUseCase(draft).first()

            Log.d("TrailViewModel", "UseCase returned: $success")

            if (success) {
                loadDrafts()
                Log.d("TrailViewModel", "✅ Draft saved and list reloaded")
            } else {
                Log.e("TrailViewModel", "❌ UseCase returned false")
            }

            success
        } catch (e: Exception) {
            Log.e("TrailViewModel", "❌ Exception in saveDraft: ${e.message}", e)
            false
        }
    }

    fun saveDraftAsync(draft: Path) {
        viewModelScope.launch {
            saveDraft(draft)
        }
    }

    suspend fun deleteDraft(draft: Path): Boolean {
        return try {
            val success = pathUseCase.deleteDraftUseCase(draft).first()
            if (success) loadDrafts()
            success
        } catch (e: Exception) {
            false
        }
    }

    // Draft 전체 삭제 (suspend)
    suspend fun clearAllDrafts(): Boolean {
        return try {
            val success = pathUseCase.clearAllDraftsUseCase().first()
            if (success) _drafts.value = emptyList()
            success
        } catch (e: Exception) {
            false
        }
    }
    // =================================================================
// 📌 8-1. RoomDB 저장 전용 함수
// =================================================================

    // ✅ 추가: RoomDB에만 저장 (서버 전송 X)
    fun savePathToRoom(path: Path) {
        viewModelScope.launch {
            Log.d("TrailViewModel", "📦 === Starting savePathToRoom ===")
            Log.d("TrailViewModel", "Path ID: ${path.id}")
            Log.d("TrailViewModel", "Path Name: ${path.pathName}")
            Log.d("TrailViewModel", "Path Distance: ${path.distance}")
            Log.d("TrailViewModel", "Path Time: ${path.duration}")
            Log.d("TrailViewModel", "Path Coords: ${path.coord?.size ?: 0} coordinates")

            try {
                val success = saveDraft(path)

                Log.d("TrailViewModel", "saveDraft() returned: $success")

                if (success) {
                    Log.d("TrailViewModel", "✅ Successfully saved to RoomDB")

                    // 저장 확인을 위해 다시 불러와보기
                    val drafts = loadDrafts()
                    Log.d("TrailViewModel", "📋 Current drafts count: ${drafts.size}")
                    drafts.forEach { draft ->
                        Log.d("TrailViewModel", "  - Draft: ${draft.pathName}, coords: ${draft.coord?.size}")
                    }

                    clearTempPath() // 폴리라인 초기화
                    clearMemoMarkers() // 메모 마커 초기화

                    _invalidToken.send(UiEvent.ToastEvent("경로가 저장되었습니다"))
                } else {
                    Log.e("TrailViewModel", "❌ saveDraft returned false")
                    _invalidToken.send(UiEvent.ToastEvent("저장에 실패했습니다"))
                }
            } catch (e: Exception) {
                Log.e("TrailViewModel", "❌ Exception in savePathToRoom: ${e.message}", e)
                _invalidToken.send(UiEvent.ToastEvent("오류 발생: ${e.message}"))
            }

            Log.d("TrailViewModel", "📦 === Finished savePathToRoom ===")
        }
    }

    // =================================================================
    // 📌 9. 댓글 관리
    // =================================================================

    private val _commentsState = MutableStateFlow<ResponseUiState<List<Comment>>>(ResponseUiState.Idle)
    val commentsState: StateFlow<ResponseUiState<List<Comment>>> = _commentsState

    fun getComments(pathId: Int) {
        viewModelScope.launch {
            _commentsState.value = ResponseUiState.Loading
            commentUseCases.getCommentsUseCase("paths", pathId)
                .catch { e ->
                    _commentsState.value = ResponseUiState.Error(e.message ?: "알 수 없는 오류가 발생했습니다.")
                }
                .collectLatest { result ->
                    when (result) {
                        is AuthResult.Success -> {
                            _commentsState.value = ResponseUiState.Success("댓글을 불러왔습니다.", result.resultData)
                        }
                        is AuthResult.NetworkError -> {
                            _commentsState.value = ResponseUiState.Error(result.exception.message ?: "네트워크 오류")
                        }
                        else -> {}
                    }
                }
        }
    }

    fun createComment(token: String, pathId: Int, content: String) {
        viewModelScope.launch {
            commentUseCases.createCommentUseCase(token, "paths", pathId, content)
                .collectLatest { result ->
                    when (result) {
                        is AuthResult.Success -> getComments(pathId) // Refresh comments list
                        is AuthResult.NetworkError -> _commentsState.value =
                            ResponseUiState.Error(result.exception.message ?: "네트워크 오류")

                        else -> {}
                    }
                }
        }
    }

    fun updateComment(token: String, pathId: Int, commentId: Int, content: String) {
        viewModelScope.launch {
            commentUseCases.updateCommentUseCase(token, "paths", pathId, commentId, content)
                .collectLatest { result ->
                    when (result) {
                        is AuthResult.Success -> getComments(pathId) // Refresh comments list
                        is AuthResult.NetworkError -> _commentsState.value =
                            ResponseUiState.Error(result.exception.message ?: "네트워크 오류")
                        else -> {}
                    }
                }
        }
    }

    fun deleteComment(token: String, pathId: Int, commentId: Int) {
        viewModelScope.launch {
            commentUseCases.deleteCommentUseCase(token, "paths", pathId, commentId)
                .collectLatest { result ->
                    when (result) {
                        is AuthResult.Success -> getComments(pathId) // Refresh comments list
                        is AuthResult.NetworkError -> _commentsState.value =
                            ResponseUiState.Error(result.exception.message ?: "네트워크 오류")

                        else -> {}
                    }
                }
        }
    }

    // ToDo 삭제, InfoDetailScreen build용
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
        selectedPostForComments = path.toPost()
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
            content = newCommentContent,
            authorId = -1,
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