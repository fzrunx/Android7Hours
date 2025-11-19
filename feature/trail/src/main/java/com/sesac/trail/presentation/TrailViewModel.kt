package com.sesac.trail.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sesac.domain.model.Coord
import com.sesac.domain.model.MyRecord
import com.sesac.domain.model.UiEvent
import com.sesac.domain.model.UserPath
import com.sesac.domain.result.AuthResult
import com.sesac.domain.usecase.trail.TrailUseCase
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
import com.sesac.domain.model.Comment
import com.sesac.domain.model.Post
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import java.util.Date


@HiltViewModel
class TrailViewModel @Inject constructor(
    private val trailUseCase: TrailUseCase,
): ViewModel() {
    private val _invalidToken = Channel<UiEvent>()
    val invalidToken = _invalidToken.receiveAsFlow()
    private val _recommendedPaths = MutableStateFlow<AuthResult<List<UserPath>>>(AuthResult.NoConstructor)
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

    private val _myPaths = MutableStateFlow<AuthResult<List<UserPath>>>(AuthResult.NoConstructor)
    val myPaths = _myPaths.asStateFlow()

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

    private val _selectedPath = MutableStateFlow<UserPath?>(null)
    val selectedPath get() = _selectedPath.asStateFlow()

//    init {
//        getRecommendedPaths()
//        getMyRecords()
//    }

    fun getRecommendedPaths(coord: Coord, radius: Float = 5000f) {
        viewModelScope.launch {
            trailUseCase.getAllRecommendedPathsUseCase(coord, radius).collectLatest { paths ->
                if (paths is AuthResult.Success) {
                    _recommendedPaths.value = paths
                }
            }
        }
    }

    fun getMyPaths(token: String) {
        viewModelScope.launch {
            trailUseCase.getMyPaths(token).collectLatest { paths ->
                if (paths is AuthResult.Success) { _myPaths.value = paths }
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
                    trailUseCase.createPathUseCase(token, path).collectLatest { createdPath ->
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

    fun updatePath(token: String?) {
        viewModelScope.launch {
            if (token.isNullOrEmpty()) {
                _invalidToken.send(UiEvent.ToastEvent("유저 정보가 없습니다."))
                return@launch
            }
            _selectedPath.value?.let { path ->
                trailUseCase.updatePathUseCase(token, path.id, path).collectLatest { result ->
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
            trailUseCase.deletePathUseCase(token, pathId).collectLatest { result ->
                if (result is AuthResult.Success) {
                    getMyPaths(token)
                }
            }
        }
    }

    fun updateIsEditMode(isEditing: Boolean? = null) {
        _isEditMode.value = isEditing ?: !_isEditMode.value
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