package com.sesac.trail.presentation

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.naver.maps.geometry.LatLng
import com.naver.maps.map.NaverMap
import com.naver.maps.map.overlay.Marker
import com.naver.maps.map.overlay.PolylineOverlay
import com.sesac.common.model.UiEvent
import com.sesac.domain.model.BookmarkType
import com.sesac.domain.model.BookmarkedPath
import com.sesac.domain.model.Comment
import com.sesac.domain.model.CommentType
import com.sesac.domain.model.Coord
import com.sesac.domain.model.Path
import com.sesac.domain.model.Place
import com.sesac.domain.model.Post
import com.sesac.domain.result.AuthResult
import com.sesac.domain.result.LocationFlowResult
import com.sesac.domain.result.ResponseUiState
import com.sesac.domain.usecase.bookmark.BookmarkUseCase
import com.sesac.domain.usecase.comment.CommentUseCases
import com.sesac.domain.usecase.location.LocationUseCase
import com.sesac.domain.usecase.path.PathUseCase
import com.sesac.domain.usecase.place.PlaceUseCase
import com.sesac.domain.usecase.session.SessionUseCase
import com.sesac.trail.presentation.ui.WalkPathTab
import com.sesac.trail.utils.toLatLng
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class TrailViewModel @Inject constructor(
    private val sessionUseCase: SessionUseCase,
    private val pathUseCase: PathUseCase,
    private val locationUseCase: LocationUseCase,
    private val bookmarkUseCase: BookmarkUseCase,
    private val commentUseCases: CommentUseCases,
    private val placeUseCases: PlaceUseCase
): ViewModel() {
    private val _invalidToken = Channel<UiEvent>()
    val invalidToken = _invalidToken.receiveAsFlow()

    // =================================================================
    // 📌 1. 지도 녹화 관련 데이터 (MainScreen에서 사용)
    // =================================================================

    private val _currentLocation = MutableStateFlow<ResponseUiState<Coord?>>(ResponseUiState.Idle)
    val currentLocation: StateFlow<ResponseUiState<Coord?>> = _currentLocation.asStateFlow()
    // ✅ 수정: LatLng 타입으로 변경 (UI 레이어에서 사용하는 타입)
    private val _tempPathCoords = MutableStateFlow<List<LatLng>>(emptyList())
    val tempPathCoords = _tempPathCoords.asStateFlow()


    fun getCurrentLocation() {
        viewModelScope.launch {
            _currentLocation.value = ResponseUiState.Idle
            locationUseCase.getCurrentLocationUseCase().collectLatest { location ->
                when (location) {
                    is LocationFlowResult.Success -> {
                        _currentLocation.value = ResponseUiState.Success("현재 위치 갱신 성공", location.coord)
                        Log.d("TAG-TrailViewModel", "현재 위치 : ${location.coord}")
                    }
                    is LocationFlowResult.Error -> _currentLocation.value = ResponseUiState.Error(location.exception.message ?: "unknown error")
                }
            }
        }
    }
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
        clearMemoMarkers()
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
        _recordingTime.value = 0L
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
                        Log.d("TAG-TarilVieModel", "현재 위치 : $coord")
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
        viewModelScope.launch {
            _selectedPath.value = null
        }
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
    
    private val _createState = MutableStateFlow<ResponseUiState<Path>>(ResponseUiState.Idle)
    val createState = _createState.asStateFlow()
    private val _updateState = MutableStateFlow<ResponseUiState<Path>>(ResponseUiState.Idle)
    val updateState = _updateState.asStateFlow()

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
    fun updatePath() {
        viewModelScope.launch {
            val token = sessionUseCase.getAccessToken().first()
            if (token.isNullOrEmpty()) {
                _invalidToken.send(UiEvent.ToastEvent("유저 정보가 없습니다."))
                return@launch
            }
            _updateState.value = ResponseUiState.Loading
            _selectedPath.value?.let { path ->
                pathUseCase.updatePathUseCase(token, path.id, path)
                    .catch { e ->
                        _updateState.value = ResponseUiState.Error(e.message ?: "알 수 없는 오류")
                    }
                    .collectLatest { result ->
                    when (result) {
                        is AuthResult.Success -> {
                            _updateState.value = ResponseUiState.Success("산책로가 수정되었습니다.", result.resultData)
                        }
                        is AuthResult.NetworkError -> {
                            _updateState.value = ResponseUiState.Error(result.exception.message ?: "네트워크 오류")
                        }
                        else -> {}
                    }
                }
            }
        }
    }

    fun resetCreateState() {
        viewModelScope.launch {
            _createState.value = ResponseUiState.Idle
        }
    }

    fun resetUpdateState() {
        viewModelScope.launch {
            _updateState.value = ResponseUiState.Idle
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


    fun createDraftPath(selectedPath: Path): Path {
        val coords = tempPathCoords.value.map { latLng ->
            Coord(latLng.latitude, latLng.longitude)
        }

        val newDraft = selectedPath.copy(
            coord = coords,
            markers = _memoMarkers.value
        )


        _draftPath.value = newDraft
        return newDraft
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
    suspend fun saveDraft(draft: Path): Path? {
        return try {
            Log.d("TrailViewModel", "🔄 Calling trailUseCase.saveDraftUseCase...")
            Log.d("TrailViewModel", "Draft details: id=${draft.id}, name=${draft.pathName}, coords=${draft.coord?.size}")

            val savedPath = pathUseCase.saveDraftUseCase(draft).first()

            Log.d("TrailViewModel", "UseCase returned: $savedPath")

            loadDrafts()
            Log.d("TrailViewModel", "✅ Draft saved and list reloaded")
            savedPath
        } catch (e: Exception) {
            Log.e("TrailViewModel", "❌ Exception in saveDraft: ${e.message}", e)
            null
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

    // =================================================================
// 📌 8-1. RoomDB 저장 전용 함수
// =================================================================

    // ✅ 추가: RoomDB에만 저장 (서버 전송 X)
    fun savePathAndUpload(path: Path) {
        viewModelScope.launch {
            _createState.value = ResponseUiState.Loading
            val token = sessionUseCase.getAccessToken().first()
            try {
                // 1️⃣ RoomDB에 저장
                val savedPathWithId = saveDraft(path)
                if (savedPathWithId == null) {
                    Log.e(
                        "TrailViewModel",
                        "Failed to save draft to RoomDB or retrieve generated ID."
                    )
//                    _invalidToken.send(UiEvent.ToastEvent("경로 저장 실패"))
                    _createState.value = ResponseUiState.Error("경로 저장 실패")
                    return@launch
                }
                Log.d("TAG-TrailViewModel", "✅ RoomDB 저장 완료 - path: $savedPathWithId")


                // 2️⃣ 서버 업로드
                Log.d("TrailViewModel", "Attempting to upload path to server...")
                token?.let {
                    val result = pathUseCase.createPathUseCase(token, savedPathWithId)
                        .first { it is AuthResult.Success || it is AuthResult.NetworkError }
                    when (result) {
                        is AuthResult.Loading -> { }
                        is AuthResult.Success -> {
                            Log.d("TrailViewModel", "Path uploaded successfully to server.")
                            _createState.value = ResponseUiState.Success("경로가 서버로 업로드되었습니다.", savedPathWithId)
                            // RoomDB 삭제
                            val deleted = deleteDraft(savedPathWithId)
                            if (deleted) {
//                                _invalidToken.send(UiEvent.ToastEvent("경로가 서버로 업로드되었습니다"))
                                Log.d("TAG-TrailViewModel", "savedPathWithid : $savedPathWithId")
                                Log.d("TAG-TrailViewModel", "result : ${result.resultData}")
                                getMyPaths(token)
                                loadDrafts()
                                _createState.value = ResponseUiState.Success("경로가 서버로 업로드되었습니다.", savedPathWithId)
                            }
                        }


                        is AuthResult.NetworkError -> {
                            val errorMsg = result.exception.message ?: ""
                            // 🔥 JsonDataException이면 실제로는 저장 성공한 것

                            if (errorMsg.contains("JsonDataException") ||
                                errorMsg.contains("Required value") ||
                                errorMsg.contains("missing at")
                            ) {

                                Log.d("TrailViewModel", "✅ JSON 파싱 에러지만 서버 저장은 성공으로 간주")
                                // RoomDB 삭제
                                val deleted = deleteDraft(savedPathWithId)
                                if (deleted) {
//                                    _invalidToken.send(UiEvent.ToastEvent("경로가 서버로 업로드되었습니다"))
                                    getMyPaths(token)
                                    loadDrafts()
                                    _createState.value = ResponseUiState.Success("경로가 서버로 업로드되었습니다.", savedPathWithId)
                                } else {
//                                    _invalidToken.send(UiEvent.ToastEvent("서버 업로드 완료, RoomDB 삭제 실패"))
                                    _createState.value = ResponseUiState.Success("서버 업로드 완료, RoomDB 삭제 실패", savedPathWithId)
                                }
                            } else {
                                // 진짜 네트워크 에러
                                Log.e("TrailViewModel", "❌ 실제 업로드 실패: $errorMsg")
//                                _invalidToken.send(UiEvent.ToastEvent("서버 업로드 실패: $errorMsg"))
                                _createState.value = ResponseUiState.Error("서버 업로드 실패 $errorMsg")
                            }
                        }
                        else -> {}
                    }
                }
            } catch (e: Exception) {
                Log.e("TrailViewModel", "An exception occurred in savePathAndUpload: ${e.message}", e)
                _invalidToken.send(UiEvent.ToastEvent("오류 발생: ${e.message}"))
                _createState.value = ResponseUiState.Error("오류 발생: ${e.message}")
            }
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
            commentUseCases.getCommentsUseCase(pathId, CommentType.PATH)
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
            commentUseCases.createCommentUseCase(token, pathId, content, CommentType.PATH)
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
            commentUseCases.updateCommentUseCase(token, pathId, commentId, content, CommentType.PATH)
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
            commentUseCases.deleteCommentUseCase(token, pathId, commentId, CommentType.PATH)
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
    // =================================================================
    // 📌 10. 따라가기
    // =================================================================
    private val _isFollowing = MutableStateFlow(false)
    val isFollowing = _isFollowing.asStateFlow()

    private val _offRoute = MutableStateFlow(false)
    val offRoute = _offRoute.asStateFlow()

    // 🔹 1. 따라가기 시작 시 초기화
    private val _isRouteCompleted = MutableStateFlow(false)
    val isRouteCompleted = _isRouteCompleted.asStateFlow()

    private val _remainingDistance = MutableStateFlow(0f)
    val remainingDistance = _remainingDistance.asStateFlow()


    fun startFollowing(path: Path) {
        // 경로 검증
        val coords = path.coord
        if (coords == null || coords.size < 2) {
            Log.e("TrailViewModel", "❌ 따라가기 실패: 좌표가 부족합니다 (${coords?.size ?: 0}개)")
            viewModelScope.launch {
                _invalidToken.send(UiEvent.ToastEvent("경로 데이터가 올바르지 않습니다"))
            }
            return
        }

        Log.d("TrailViewModel", "Starting to follow path: ${path.pathName}. Markers in path: ${path.markers?.size ?: 0}")

        Log.d("TrailViewModel", "✅ 따라가기 시작: ${path.pathName}, 좌표 ${coords.size}개")
        _selectedPath.value = path
        _memoMarkers.value = path.markers ?: emptyList()
        _isFollowing.value = true
        _isRouteCompleted.value = false  // ✅ 초기화
        _offRoute.value = false

        // 전체 거리 계산
        var totalDist = 0.0
        for (i in 0 until coords.size - 1) {
            totalDist += coords[i].toLatLng().distanceTo(coords[i + 1].toLatLng())
        }
        _remainingDistance.value = totalDist.toFloat()
    }

    fun stopFollowing() {
        _isFollowing.value = false
        _isRouteCompleted.value = false
    }

    // 사용자 현재 위치 업데이트
    fun updateUserLocation(current: LatLng) {
        if (!_isFollowing.value) return

        val path = _selectedPath.value ?: return
        val coords = path.coord ?: emptyList()

        // ✅ 1. 도착 지점 근처인지 확인 (완료 조건)
        val destination = coords.last().toLatLng()
        val distanceToDestination = current.distanceTo(destination)

        if (distanceToDestination < 20.0) {  // 20m 이내면 완료
            if (!_isRouteCompleted.value) {
                _isRouteCompleted.value = true
                _remainingDistance.value = 0f
                _offRoute.value = false
                viewModelScope.launch {
                    _invalidToken.send(UiEvent.ToastEvent("🎉 경로 완료! 수고하셨습니다!"))
                }
                Log.d("TrailViewModel", "🎉 경로 완료!")
            }
            return
        }

        // ✅ 2. 경로에서 가장 가까운 지점 찾기
        var minDistance = Double.MAX_VALUE
        var closestIndex = 0

        for (i in coords.indices) {
            val dist = current.distanceTo(coords[i].toLatLng())
            if (dist < minDistance) {
                minDistance = dist
                closestIndex = i
            }
        }

        // ✅ 3. 남은 거리 계산 (가장 가까운 지점부터 도착점까지)
        var remaining = 0.0
        for (i in closestIndex until coords.size - 1) {
            remaining += coords[i].toLatLng().distanceTo(coords[i + 1].toLatLng())
        }
        _remainingDistance.value = remaining.toFloat()

        // ✅ 4. 이탈 감지 (경로에서 30m 이상 떨어짐)
        _offRoute.value = minDistance > 30.0

        Log.d("TrailViewModel", "📍 현재: 도착까지 ${remaining.toInt()}m, 경로까지 ${minDistance.toInt()}m")
    }

    // 🔹 4. 사용자 위치 마커 표시용
    private val _userLocationMarker = MutableStateFlow<LatLng?>(null)
    val userLocationMarker = _userLocationMarker.asStateFlow()

    fun updateUserLocationMarker(location: LatLng) {
        _userLocationMarker.value = location
    }
    // 마커 제거 함수
    fun clearUserLocationMarker() {
        _userLocationMarker.value = null
    }
    // =================================================================
    // 📌 11. 정보
    // =================================================================
    private val _placesState = MutableStateFlow<ResponseUiState<List<Place>>>(ResponseUiState.Idle)
    val placesState: StateFlow<ResponseUiState<List<Place>>> = _placesState



    fun loadPlaces(
        categoryId: Int? = null,
        lat: Double? = null,
        lng: Double? = null,
        radius: Int? = 5000 // 기본 5km
    ) {
        viewModelScope.launch {
            _placesState.value = ResponseUiState.Loading
            placeUseCases.getPlaceUseCase(
                categoryId = categoryId,
                latitude = lat,
                longitude = lng,
                radius = radius
            ).catch { e ->
                _placesState.value = ResponseUiState.Error(e.message ?: "알 수 없는 오류가 발생했습니다.")
            }.collectLatest { result ->
                when (result) {
                    is AuthResult.Success -> {
                        _placesState.value = ResponseUiState.Success("장소를 불러왔습니다.", result.resultData)
                    }
                    is AuthResult.NetworkError -> {
                        _placesState.value = ResponseUiState.Error(result.exception.message ?: "네트워크 오류")
                    }
                    else -> {
                        // You might want to handle other states like Loading, NoToken, etc.
                    }
                }
            }
        }
    }



    fun loadPlaceComments(placeId: Int) {
        viewModelScope.launch {
            _commentsState.value = ResponseUiState.Loading
            commentUseCases.getCommentsUseCase(
                objectId = placeId,
                type = CommentType.PATH  // ✅ 장소 댓글도 PATH 타입 사용
            )
                .catch { e ->
                    _commentsState.value = ResponseUiState.Error(e.message ?: "알 수 없는 오류가 발생했습니다.")
                }
                .collectLatest { result ->
                    when (result) {
                        is AuthResult.Success -> {
                            _commentsState.value = ResponseUiState.Success(
                                "댓글을 불러왔습니다.",
                                result.resultData
                            )
                        }
                        is AuthResult.NetworkError -> {
                            _commentsState.value = ResponseUiState.Error(
                                result.exception.message ?: "네트워크 오류"
                            )
                        }
                        else -> {}
                    }
                }
        }
    }

    fun postPlaceComment(placeId: Int, content: String, type: CommentType) {
        viewModelScope.launch {
            val token = sessionUseCase.getAccessToken().first()
            if (token == null) {
                _invalidToken.send(UiEvent.ToastEvent("로그인이 필요합니다."))
                return@launch
            }

            commentUseCases.createCommentUseCase(
                token = token,
                objectId = placeId,
                content = content,
                type = type
            ).collectLatest { result ->
                when (result) {
                    is AuthResult.Success -> {
                        loadPlaceComments(placeId) // 댓글 목록 새로고침
                    }
                    is AuthResult.NetworkError -> {
                        _commentsState.value = ResponseUiState.Error(
                            result.exception.message ?: "네트워크 오류"
                        )
                    }
                    else -> {}
                }
            }
        }
    }

    fun updatePlaceComment(placeId: Int, commentId: Int, content: String, type: CommentType) {
        viewModelScope.launch {
            val token = sessionUseCase.getAccessToken().first()
            if (token == null) {
                _invalidToken.send(UiEvent.ToastEvent("로그인이 필요합니다."))
                return@launch
            }

            commentUseCases.updateCommentUseCase(
                token = token,
                objectId = placeId,
                commentId = commentId,
                content = content,
                type = type
            ).collectLatest { result ->
                when (result) {
                    is AuthResult.Success -> {
                        loadPlaceComments(placeId)
                    }
                    is AuthResult.NetworkError -> {
                        _commentsState.value = ResponseUiState.Error(
                            result.exception.message ?: "네트워크 오류"
                        )
                    }
                    else -> {}
                }
            }
        }
    }

    fun deletePlaceComment(placeId: Int, commentId: Int, type: CommentType) {
        viewModelScope.launch {
            val token = sessionUseCase.getAccessToken().first()
            if (token == null) {
                _invalidToken.send(UiEvent.ToastEvent("로그인이 필요합니다."))
                return@launch
            }

            commentUseCases.deleteCommentUseCase(
                token = token,
                objectId = placeId,
                commentId = commentId,
                type = type
            ).collectLatest { result ->
                when (result) {
                    is AuthResult.Success -> {
                        loadPlaceComments(placeId)
                    }
                    is AuthResult.NetworkError -> {
                        _commentsState.value = ResponseUiState.Error(
                            result.exception.message ?: "네트워크 오류"
                        )
                    }
                    else -> {}
                }
            }
        }
    }

    // 현재 로그인한 사용자 ID (댓글 작성자 확인용)
    val currentUserId: Int
        get() = -1 // TODO: 실제 사용자 ID로 변경 필요
}

