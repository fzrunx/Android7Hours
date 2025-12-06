package com.sesac.trail.presentation

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.naver.maps.geometry.LatLng
import com.naver.maps.map.NaverMap
import com.naver.maps.map.overlay.Marker
import com.naver.maps.map.overlay.PolylineOverlay
import com.sesac.common.model.UiEvent
import com.sesac.domain.model.BookmarkedPath
import com.sesac.domain.model.Comment
import com.sesac.domain.model.Coord
import com.sesac.domain.model.MypageSchedule
import com.sesac.domain.model.Path
import com.sesac.domain.model.Place
import com.sesac.domain.model.User
import com.sesac.domain.result.AuthResult
import com.sesac.domain.result.LocationFlowResult
import com.sesac.domain.result.ResponseUiState
import com.sesac.domain.type.BookmarkType
import com.sesac.domain.type.CommentType
import com.sesac.domain.usecase.bookmark.BookmarkUseCase
import com.sesac.domain.usecase.comment.CommentUseCase
import com.sesac.domain.usecase.location.LocationUseCase
import com.sesac.domain.usecase.mypage.AddScheduleUseCase
import com.sesac.domain.usecase.mypage.DiaryUseCase
import com.sesac.domain.usecase.mypage.MypageUseCase
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
import kotlinx.coroutines.launch
import org.threeten.bp.LocalDate
import javax.inject.Inject

@HiltViewModel
class TrailViewModel @Inject constructor(
    private val sessionUseCase: SessionUseCase,
    private val pathUseCase: PathUseCase,
    private val locationUseCase: LocationUseCase,
    private val bookmarkUseCase: BookmarkUseCase,
    private val commentUseCase: CommentUseCase,
    private val placeUseCases: PlaceUseCase,
    private val addScheduleUseCase: AddScheduleUseCase,
    private val mypageUseCase: MypageUseCase,
    private val diaryUseCase: DiaryUseCase,
) : ViewModel() {
    private val _invalidToken = Channel<UiEvent>()
    val invalidToken = _invalidToken.receiveAsFlow()
    private var lastRecommendedPathFetchLocation: LatLng? = null

    // =================================================================
    // 📌 1. 지도 녹화 관련 데이터 (MainScreen에서 사용)
    // =================================================================

    private val _currentLocation = MutableStateFlow<ResponseUiState<Coord?>>(ResponseUiState.Idle)
    val currentLocation: StateFlow<ResponseUiState<Coord?>> = _currentLocation.asStateFlow()

    private val _tempPathCoords = MutableStateFlow<List<LatLng>>(emptyList())
    val tempPathCoords = _tempPathCoords.asStateFlow()

    fun startLocationUpdates() {
        Log.d("TrailViewModel", "startLocationUpdates() called")
        viewModelScope.launch {
            locationUseCase.getRealtimeLocationUseCase().collect { result ->
                when (result) {
                    is LocationFlowResult.Success -> {
                        val newLocation = result.coord
                        val newPoint = newLocation.toLatLng()

                        // 경로 따라가기 모드일 때 위치 업데이트
                        if (_isFollowingPath.value) {
                            updateUserLocation(newPoint)
                            updateUserLocationMarker(newPoint)
                        }

                        // 녹화 중일 때 좌표 추가
                        if (_isRecording.value) {
                            val lastPoint = _tempPathCoords.value.lastOrNull()
                            if (lastPoint != null) {
                                val diff = lastPoint.distanceTo(newPoint)
                                if (diff < 5) { // 5m 미만 이동은 무시
                                    return@collect
                                }
                            }
                            addTempPoint(newPoint)
                        }

                        // 스마트 데이터 로딩
                        val distance = lastRecommendedPathFetchLocation?.distanceTo(newLocation.toLatLng()) ?: Double.MAX_VALUE
                        if (distance > 1000) { // 1km 이상 이동 시 갱신
                            Log.d("TAG-TrailViewModel", "Fetching new recommended paths. Moved ${distance}m")
                            getRecommendedPaths(newLocation, 5000f)
                            loadPlaces(lat = newLocation.latitude, lng = newLocation.longitude, radius = 5)
                            lastRecommendedPathFetchLocation = newLocation.toLatLng()
                        }
                    }
                    is LocationFlowResult.Error -> {
                        Log.e("TrailViewModel", "Location error: ${result.exception.message}")
                    }
                }
            }
        }
    }
    
    // TODO: 이 함수는 일회성 위치를 가져오는 데 사용될 수 있으므로 다른 화면에서 사용되는지 확인 후 삭제가 필요합니다.
    fun getCurrentLocation() {
        viewModelScope.launch {
            _currentLocation.value = ResponseUiState.Idle
            locationUseCase.getCurrentLocationUseCase().collectLatest { location ->
                when (location) {
                    is LocationFlowResult.Success -> {
                        _currentLocation.value = ResponseUiState.Success("현재 위치 갱신 성공", location.coord)
                        Log.d("TAG-TrailViewModel", "현재 위치 : ${location.coord}")
                    }

                    is LocationFlowResult.Error -> _currentLocation.value =
                        ResponseUiState.Error(location.exception.message ?: "unknown error")
                }
            }
        }
    }

    private fun addTempPoint(point: LatLng) {
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


    private val _recordingTime = MutableStateFlow(0L)
    val recordingTime = _recordingTime.asStateFlow()

    fun startRecording() {
        _isRecording.value = true
        _recordingTime.value = 0L
        clearTempPath()
        clearMemoMarkers()
    }


    fun stopRecording() {
        _isRecording.value = false
//        _recordingTime.value = 0L
    }

    fun addRecordingTime(delta: Long) {
        _recordingTime.value += delta
    }

    // ✅ 추가: MainScreen에서 사용하는 편의 함수
    fun updateRecordingTime(changeRate: Long?) {
        _recordingTime.value += changeRate ?: -_recordingTime.value
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

    private val _bookmarkedPaths =
        MutableStateFlow<ResponseUiState<List<BookmarkedPath>>>(ResponseUiState.Idle)
    val bookmarkedPaths = _bookmarkedPaths.asStateFlow()
    private val _userInfo = MutableStateFlow<User?>(null)
    val userInfo = _userInfo.asStateFlow()


    private val _selectedPath = MutableStateFlow<Path?>(null)
    val selectedPath get() = _selectedPath.asStateFlow()

//    init {
//        getRecommendedPaths()
//        getMyRecords()
//    }

    fun getCurrentUserInfo() {
        viewModelScope.launch {
            _userInfo.value = sessionUseCase.getUserInfo().first()
        }
    }

    fun getRecommendedPaths(coord: Coord, radius: Float = 5000f) {
        viewModelScope.launch {
            _recommendedPaths.value = ResponseUiState.Loading
            pathUseCase.getAllRecommendedPathsUseCase(coord, radius)
                .catch { e ->
                    _recommendedPaths.value =
                        ResponseUiState.Error(e.message ?: "알 수 없는 오류가 발생했습니다.")
                }
                .collectLatest { pathsResult ->
                    when (pathsResult) {
                        is AuthResult.Success -> {
                            Log.d("TAG-TarilVieModel", "현재 위치 : $coord")
                            _recommendedPaths.value =
                                ResponseUiState.Success("추천 경로를 불러왔습니다.", pathsResult.resultData)
                        }

                        is AuthResult.NetworkError -> {
                            _recommendedPaths.value =
                                ResponseUiState.Error(pathsResult.exception.message ?: "unknown")
                        }

                        else -> Unit
                    }
                }
        }
    }

    fun getMyPaths() {
        viewModelScope.launch {
            _myPaths.value = ResponseUiState.Loading
            val token = sessionUseCase.getAccessToken().first()
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
                            _myPaths.value =
                                ResponseUiState.Success("내 경로를 불러왔습니다.", pathsResult.resultData)
                        }

                        is AuthResult.NetworkError -> {
                            _myPaths.value =
                                ResponseUiState.Error(pathsResult.exception.message ?: "unknown")
                        }

                        else -> Unit
                    }
                }
        }
    }

    // =================================================================
    // 📌 5. 선택된 경로 관리
    // =================================================================


    // 🔥 다이어리 상태 - Map으로 여러 일정의 다이어리 관리
    private val _diaryMap = MutableStateFlow<Map<Long, String>>(emptyMap())
    val diaryMap get() = _diaryMap.asStateFlow()


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
                            val pathList =
                                bookmarksResult.resultData.mapNotNull { it.bookmarkedItem as? BookmarkedPath }
                            _bookmarkedPaths.value =
                                ResponseUiState.Success("북마크를 불러왔습니다.", pathList)
                        }

                        is AuthResult.NetworkError -> {
                            _bookmarkedPaths.value =
                                ResponseUiState.Error(bookmarksResult.exception.message ?: "unknown")
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
                        _selectedPath.value = _selectedPath.value?.copy(bookmarkCount = bookmarkResponse.resultData.bookmarkCount)
                    } else if (bookmarkResponse is AuthResult.NetworkError) {
                        Log.e(
                            "MypageViewModel",
                            "Toggle bookmark failed: ${bookmarkResponse.exception}"
                        )
                    }
                }
        }
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
                                _updateState.value =
                                    ResponseUiState.Success("산책로가 수정되었습니다.", result.resultData)
                            }

                            is AuthResult.NetworkError -> {
                                _updateState.value =
                                    ResponseUiState.Error(result.exception.message ?: "네트워크 오류")
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

    fun deletePath(pathId: Int) {
        viewModelScope.launch {
            val token = sessionUseCase.getAccessToken().first()
            if (token.isNullOrEmpty()) {
                _invalidToken.send(UiEvent.ToastEvent("유저 정보가 없습니다."))
                return@launch
            }
            pathUseCase.deletePathUseCase(token, pathId).collectLatest { result ->
                if (result is AuthResult.Success) {
                    getMyPaths()
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
            Log.d(
                "TrailViewModel",
                "Draft details: id=${draft.id}, name=${draft.pathName}, coords=${draft.coord?.size}"
            )

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
                    Log.e("TrailViewModel", "Failed to save draft to RoomDB")
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
                        is AuthResult.Loading -> {}
                        is AuthResult.Success -> {
                            Log.d("TrailViewModel", "Path uploaded successfully to server. ${result.resultData}")
                            _createState.value = ResponseUiState.Success("경로가 서버로 업로드되었습니다.", result.resultData)
                            // RoomDB 삭제
                            val deleted = deleteDraft(savedPathWithId)
                            if (deleted) {
                                getMyPaths()
                                loadDrafts()
//                                _createState.value = ResponseUiState.Success(
//                                    "경로가 서버로 업로드되었습니다.",
////                                    savedPathWithId,
//                                    result.resultData
//                                )

                                // ✅ 3️⃣ MypageSchedule 생성 및 저장 (isCompleted = false)
                                val scheduleId = savedPathWithId.id.toLong()
                                val newSchedule = MypageSchedule(
                                    id = scheduleId,
                                    date = LocalDate.now(),
                                    title = savedPathWithId.pathName,
                                    memo = "",
                                    isPath = true,
                                    pathId = savedPathWithId.id,
                                    isCompleted = false  // ✅ 처음엔 false
                                )

                                addScheduleUseCase(newSchedule).collectLatest { success ->
                                    if (success) {
                                        Log.d("TrailViewModel", "✅ Schedule 추가 성공: scheduleId=$scheduleId")

                                        // ✅ 4️⃣ 다이어리 생성
                                        generateAndSaveDiary(scheduleId, result.resultData)

                                        // ✅ 5️⃣ Schedule을 isCompleted = true로 업데이트
                                        completeSchedule(scheduleId)
                                    } else {
                                        Log.e("TrailViewModel", "❌ Schedule 추가 실패")
                                    }
                                }
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
                                    getMyPaths()
                                    loadDrafts()
                                    _createState.value = ResponseUiState.Success(
                                        "경로가 서버로 업로드되었습니다.",
                                        savedPathWithId
                                    )

                                    // ✅ Schedule 생성 및 완료 처리
                                    val scheduleId = savedPathWithId.id.toLong()
                                    val newSchedule = MypageSchedule(
                                        id = scheduleId,
                                        date = LocalDate.now(),
                                        title = savedPathWithId.pathName,
                                        memo = "",
                                        isPath = true,
                                        pathId = savedPathWithId.id,
                                        isCompleted = false
                                    )

                                    addScheduleUseCase(newSchedule).collectLatest { success ->
                                        if (success) {
                                            Log.d("TrailViewModel", "✅ Schedule 추가 성공")
                                            generateAndSaveDiary(scheduleId, savedPathWithId)
                                            completeSchedule(scheduleId)
                                        }
                                    }
                                }
                            } else {
                                // 진짜 네트워크 에러
                                Log.e("TrailViewModel", "❌ 실제 업로드 실패: $errorMsg")
                                _createState.value = ResponseUiState.Error("서버 업로드 실패: $errorMsg")
                            }
                        }

                        else -> {}
                    }
                }
            } catch (e: Exception) {
                Log.e(
                    "TrailViewModel",
                    "An exception occurred in savePathAndUpload: ${e.message}",
                    e
                )
                _invalidToken.send(UiEvent.ToastEvent("오류 발생: ${e.message}"))
                _createState.value = ResponseUiState.Error("오류 발생: ${e.message}")
            }
        }
    }

    // ✅ 다이어리 생성/저장
    private fun generateAndSaveDiary(scheduleId: Long, path: Path) {
        viewModelScope.launch {
            try {
                Log.d("TrailViewModel", "✅ [다이어리 생성 시작] scheduleId=$scheduleId, pathId=${path.id}")

                val diary = diaryUseCase(path)

                Log.d("TrailViewModel", "✅ [다이어리 생성 성공] ${diary.diary.take(50)}...")

                mypageUseCase.saveDiaryToLocalUseCase(scheduleId, path.id, diary.diary)

                _diaryMap.value = _diaryMap.value + (scheduleId to diary.diary)

                Log.d("TrailViewModel", "✅ [다이어리 저장 완료] scheduleId=$scheduleId")
            } catch (e: Exception) {
                Log.e("TrailViewModel", "❌ [다이어리 생성 실패]", e)
                _diaryMap.value = _diaryMap.value + (scheduleId to "다이어리 생성 실패: ${e.message}")
            }
        }
    }

    // ✅ Schedule 완료 처리 (MypageUseCase 사용)
    private fun completeSchedule(scheduleId: Long) {
        viewModelScope.launch {
            try {
                Log.d("TrailViewModel", "✅ [Schedule 완료 처리 시작] scheduleId=$scheduleId")

                // ✅ 방법 1: MypageUseCase를 통해 일정 조회 후 업데이트
                mypageUseCase.getSchedulesUseCase(LocalDate.now()).collectLatest { schedules ->
                    val schedule = schedules.find { it.id == scheduleId }

                    if (schedule != null) {
                        Log.d("TrailViewModel", "✅ Schedule 찾음: ${schedule.title}")

                        val completedSchedule = schedule.copy(isCompleted = true)

                        mypageUseCase.updateScheduleUseCase(completedSchedule).collectLatest { success ->
                            if (success) {
                                Log.d("TrailViewModel", "✅ [Schedule 완료 업데이트 성공] scheduleId=$scheduleId")
                            } else {
                                Log.e("TrailViewModel", "❌ [Schedule 완료 업데이트 실패]")
                            }
                        }
                    } else {
                        Log.e("TrailViewModel", "❌ [Schedule을 찾을 수 없음] scheduleId=$scheduleId")

                        // ✅ 방법 2: 찾을 수 없으면 새로 생성 (fallback)
                        Log.d("TrailViewModel", "⚠️ Schedule 재생성 시도")
                        val newSchedule = MypageSchedule(
                            id = scheduleId,
                            date = LocalDate.now(),
                            title = _selectedPath.value?.pathName ?: "산책로",
                            memo = "",
                            isPath = true,
                            pathId = scheduleId.toInt(),
                            isCompleted = true  // 바로 완료 상태로
                        )

                        mypageUseCase.addScheduleUseCase(newSchedule).collectLatest { success ->
                            if (success) {
                                Log.d("TrailViewModel", "✅ Schedule 재생성 성공")
                            }
                        }
                    }
                }
            } catch (e: Exception) {
                Log.e("TrailViewModel", "❌ [Schedule 완료 처리 실패]", e)
            }
        }
    }

    fun saveDiaryForPath(scheduleId: Long, path: Path) {
        viewModelScope.launch {
            try {
                // 1️⃣ 다이어리 생성
                val diary = mypageUseCase.diaryUseCase(path)

                // 2️⃣ RoomDB 저장
                mypageUseCase.saveDiaryToLocalUseCase(scheduleId, path.id, diary.diary)

                Log.d("TrailViewModel", "✅ 다이어리 저장 완료: scheduleId=$scheduleId, pathId=${path.id}")
            } catch (e: Exception) {
                Log.e("TrailViewModel", "❌ 다이어리 저장 실패: ${e.message}", e)
            }
        }
    }

    // =================================================================
    // 📌 9. 댓글 관리
    // =================================================================

    private val _commentsState =
        MutableStateFlow<ResponseUiState<List<Comment>>>(ResponseUiState.Idle)
    val commentsState: StateFlow<ResponseUiState<List<Comment>>> = _commentsState

    fun getComments(pathId: Int) {
        viewModelScope.launch {
            _commentsState.value = ResponseUiState.Loading
            commentUseCase.getCommentsUseCase(pathId, CommentType.PATH)
                .catch { e ->
                    _commentsState.value = ResponseUiState.Error(e.message ?: "알 수 없는 오류가 발생했습니다.")
                }
                .collectLatest { result ->
                    when (result) {
                        is AuthResult.Success -> {
                            _commentsState.value =
                                ResponseUiState.Success("댓글을 불러왔습니다.", result.resultData)
                        }

                        is AuthResult.NetworkError -> {
                            _commentsState.value =
                                ResponseUiState.Error(result.exception.message ?: "네트워크 오류")
                        }

                        else -> {}
                    }
                }
        }
    }

    fun createComment(token: String, pathId: Int, content: String) {
        viewModelScope.launch {
            commentUseCase.createCommentUseCase(token, pathId, content, CommentType.PATH)
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
            commentUseCase.updateCommentUseCase(
                token,
                pathId,
                commentId,
                content,
                CommentType.PATH
            )
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
            commentUseCase.deleteCommentUseCase(token, pathId, commentId, CommentType.PATH)
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

        Log.d(
            "TrailViewModel",
            "Starting to follow path: ${path.pathName}. Markers in path: ${path.markers?.size ?: 0}"
        )

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

    private fun updateUserLocationMarker(location: LatLng) {
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
                        _placesState.value =
                            ResponseUiState.Success("장소를 불러왔습니다.", result.resultData)
                    }

                    is AuthResult.NetworkError -> {
                        _placesState.value =
                            ResponseUiState.Error(result.exception.message ?: "네트워크 오류")
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
            commentUseCase.getCommentsUseCase(
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

            commentUseCase.createCommentUseCase(
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

            commentUseCase.updateCommentUseCase(
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

            commentUseCase.deleteCommentUseCase(
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

}

