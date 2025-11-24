package com.sesac.trail.presentation

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sesac.domain.model.Coord
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
import com.naver.maps.geometry.LatLng // ⭐ 추가
import com.naver.maps.map.NaverMap
import com.naver.maps.map.overlay.Marker
import com.naver.maps.map.overlay.PolylineOverlay
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import com.sesac.domain.model.Comment
import com.sesac.domain.model.Post
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.update
import java.util.Date

@HiltViewModel
class TrailViewModel @Inject constructor(
    private val trailUseCase: TrailUseCase,
): ViewModel() {

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

    val currentMarkers: MutableList<Marker> = mutableListOf()

    fun setPolylineInstance(polyline: PolylineOverlay) {
        _polylineOverlay.value = polyline
    }

    fun clearAllMapObjects(naverMap: NaverMap?) {
        if (naverMap == null) return

        _polylineOverlay.value?.map = null
        _polylineOverlay.value = null

        currentMarkers.forEach { marker ->
            marker.map = null
        }
        currentMarkers.clear()

        println("🧹 TrailViewModel: 지도 객체 초기화 완료")
    }

    // =================================================================
    // 📌 4. 경로 목록 관리 (추천 경로, 내 경로)
    // =================================================================

    private val _recommendedPaths = MutableStateFlow<AuthResult<List<UserPath>>>(AuthResult.NoConstructor)
    val recommendedPaths = _recommendedPaths.asStateFlow()

    private val _myPaths = MutableStateFlow<AuthResult<List<UserPath>>>(AuthResult.NoConstructor)
    val myPaths = _myPaths.asStateFlow()

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
                if (paths is AuthResult.Success) {
                    _myPaths.value = paths
                }
            }
        }
    }

    // =================================================================
    // 📌 5. 선택된 경로 관리
    // =================================================================

    private val _selectedPath = MutableStateFlow<UserPath?>(null)
    val selectedPath get() = _selectedPath.asStateFlow()

    fun updateSelectedPath(path: UserPath?) {
        viewModelScope.launch {
            _selectedPath.value = path
        }
    }

    fun clearSelectedPath() {
        _selectedPath.value = null
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

    // ✅ 수정: 중복 제거, 하나로 통합
    private val _invalidToken = Channel<UiEvent>()
    val invalidToken = _invalidToken.receiveAsFlow()

    // ✅ 수정: CreateScreen에서 사용 (녹화 완료 후 저장)
    fun savePath(token: String?, currentCoord: Coord?, radius: Float = 5000f) {
        viewModelScope.launch {
            if (token.isNullOrEmpty()) {
                _invalidToken.send(UiEvent.ToastEvent("유저 정보가 없습니다."))
                return@launch
            }

            _selectedPath.value?.let { path ->
                trailUseCase.createPathUseCase(token, path).collectLatest { result ->
                    if (result is AuthResult.Success) {
                        val coord = currentCoord ?: result.resultData.coord?.first() ?: Coord.DEFAULT
                        _selectedPath.value = result.resultData
                        getRecommendedPaths(coord, radius)
                    }
                }
            }
        }
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
    private val _draftPath = MutableStateFlow<UserPath?>(null)
    val draftPath = _draftPath.asStateFlow()

    fun createDraftPath(name: String, description: String?) {
        val coords = tempPathCoords.value.map { latLng ->
            Coord(latLng.latitude, latLng.longitude)
        }

        _draftPath.value = UserPath(
            id = -1,
            name = name,
            description = description ?: "",
            coord = coords,
            likes = 0,
            uploader = ""
        )
    }

    fun clearDraftPath() {
        _draftPath.value = null
    }

    private val _drafts = MutableStateFlow<List<UserPath>>(emptyList())
    val drafts: StateFlow<List<UserPath>> get() = _drafts.asStateFlow()

    suspend fun loadDrafts(): List<UserPath> {
        return try {
            val list = trailUseCase.getAllDraftsUseCase().first()
            _drafts.value = list
            list
        } catch (e: Exception) {
            emptyList()
        }
    }

    // Draft 저장 (suspend) - 로그 추가
    suspend fun saveDraft(draft: UserPath): Boolean {
        return try {
            Log.d("TrailViewModel", "🔄 Calling trailUseCase.saveDraftUseCase...")
            Log.d("TrailViewModel", "Draft details: id=${draft.id}, name=${draft.name}, coords=${draft.coord?.size}")

            val success = trailUseCase.saveDraftUseCase(draft).first()

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

    fun saveDraftAsync(draft: UserPath) {
        viewModelScope.launch {
            saveDraft(draft)
        }
    }

    suspend fun deleteDraft(draft: UserPath): Boolean {
        return try {
            val success = trailUseCase.deleteDraftUseCase(draft).first()
            if (success) loadDrafts()
            success
        } catch (e: Exception) {
            false
        }
    }

    suspend fun clearAllDrafts(): Boolean {
        return try {
            val success = trailUseCase.clearAllDraftsUseCase().first()
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
    fun savePathToRoom(path: UserPath) {
        viewModelScope.launch {
            Log.d("TrailViewModel", "📦 === Starting savePathToRoom ===")
            Log.d("TrailViewModel", "Path ID: ${path.id}")
            Log.d("TrailViewModel", "Path Name: ${path.name}")
            Log.d("TrailViewModel", "Path Distance: ${path.distance}")
            Log.d("TrailViewModel", "Path Time: ${path.time}")
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
                        Log.d("TrailViewModel", "  - Draft: ${draft.name}, coords: ${draft.coord?.size}")
                    }

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

    private val _comments = MutableStateFlow<List<Comment>>(emptyList())
    val comments: StateFlow<List<Comment>> get() = _comments.asStateFlow()

    var selectedPostForComments by mutableStateOf<Post?>(null)
        private set

    var isCommentsOpen by mutableStateOf(false)
        private set

    var newCommentContent by mutableStateOf("")

    fun handleOpenComments(path: UserPath) {
        selectedPostForComments = Post(
            id = path.id.toLong(),
            author = path.uploader,
            authorImage = "",
            timeAgo = "",
            content = path.name,
            image = null,
            likes = path.likes,
            comments = 0,
            isLiked = false,
            category = "",
            createdAt = Date()
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
            author = "나",
            authorImage = "https://picsum.photos/seed/me/200",
            timeAgo = "방금 전",
            content = newCommentContent
        )

        _comments.update { it + newComment }

        // ✅ 수정: Post 객체의 comments 카운트 업데이트
        selectedPostForComments?.let { currentPost ->
            selectedPostForComments = currentPost.copy(
                comments = currentPost.comments + 1
            )
        }

        newCommentContent = ""
        return true
    }
}