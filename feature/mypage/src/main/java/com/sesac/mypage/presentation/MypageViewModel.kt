package com.sesac.mypage.presentation

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sesac.domain.model.BookmarkType
import com.sesac.domain.model.BookmarkedPath
import com.sesac.domain.model.Breed
import com.sesac.domain.model.FavoriteCommunityPost
import com.sesac.domain.model.FavoriteWalkPath
import com.sesac.domain.model.MypageSchedule
import com.sesac.domain.model.MypageStat
import com.sesac.domain.model.Path
import com.sesac.domain.usecase.mypage.MypageUseCase
import com.sesac.domain.model.User
import com.sesac.domain.model.Pet
import com.sesac.domain.result.AuthResult
import com.sesac.domain.result.JoinUiState
import com.sesac.domain.result.ResponseUiState
import com.sesac.domain.usecase.auth.AuthUseCase
import com.sesac.domain.usecase.bookmark.BookmarkUseCase
import com.sesac.domain.usecase.path.PathUseCase
import com.sesac.domain.usecase.pet.PetUseCase
import com.sesac.domain.usecase.session.SessionUseCase
import com.sesac.domain.usecase.user.UserUseCase
import com.sesac.mypage.model.MyPathStats
import com.sesac.mypage.utils.getMyPathStatsUtils
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import kotlinx.coroutines.flow.first
import org.threeten.bp.LocalDate
import javax.inject.Inject

@HiltViewModel
class MypageViewModel @Inject constructor(
    private val authUseCase: AuthUseCase,
    private val userUseCase: UserUseCase,
    private val sessionUseCase: SessionUseCase,
    private val bookmarkUseCase: BookmarkUseCase,
    private val petUseCase: PetUseCase,
    private val pathUseCase: PathUseCase,

    private val mypageUseCase: MypageUseCase,
) : ViewModel() {
    val tabLabels = listOf("산책로", "커뮤니티")
    private val _activeFilter = MutableStateFlow<String>(tabLabels[0])
    val activeFilter get() = _activeFilter.asStateFlow()
    // MypageMainScreen
    private val _stats = MutableStateFlow<ResponseUiState<List<MyPathStats>>>(ResponseUiState.Idle)
    val stats = _stats.asStateFlow()
    // MypageDetailScreen
    private val _userPets = MutableStateFlow<List<Pet>>(emptyList())
    val userPets = _userPets.asStateFlow()
    // AddPetScreen
    private val _breeds = MutableStateFlow<List<Breed>>(emptyList())
    val breeds = _breeds.asStateFlow()
//    private val _bookmarkedPaths = MutableStateFlow<AuthResult<List<BookmarkedPath?>>>(AuthResult.NoConstructor)
    private val _bookmarkedPaths = MutableStateFlow<ResponseUiState<List<BookmarkedPath>>>(ResponseUiState.Idle)
    val bookmarkedPaths = _bookmarkedPaths.asStateFlow()
    private val _selectedPath = MutableStateFlow<ResponseUiState<Path>>(ResponseUiState.Idle)
    val selectedPath = _selectedPath.asStateFlow()



    // MypageFavoriteScreen
    private val _favoriteWalkPaths = MutableStateFlow<List<FavoriteWalkPath>>(emptyList())
    val favoriteWalkPaths get() = _favoriteWalkPaths.asStateFlow()
    private val _favoritePosts = MutableStateFlow<List<FavoriteCommunityPost>>(emptyList())
    val favoritePosts get() = _favoritePosts.asStateFlow()
    // MypageManageScreen
    private val _schedules = MutableStateFlow<List<MypageSchedule>>(emptyList())
    val schedules get() = _schedules.asStateFlow()

//    init {
//        viewModelScope.launch {
//            getStats()
//        }
//    }

    fun onFilterChange(filter: String) {
        _activeFilter.value = filter
    }

//    fun getStats() {
//        viewModelScope.launch {
//            mypageUseCase.getMypageStatsUseCase().collectLatest { _stats.value = it }
//        }
//    }

    fun getUserPets(userId: Int) {
        viewModelScope.launch {
            petUseCase.getPetInfoUseCase(userId).collectLatest { result ->
                if (result is AuthResult.Success) {
                    _userPets.value = result.resultData
                }
            }
        }
    }

    fun getPathInfo(pathId: Int) {
        viewModelScope.launch {
            _selectedPath.value = ResponseUiState.Loading // 시작 시 Loading 상태로 변경
            pathUseCase.getPathById(pathId)
                .catch { e ->
                    _selectedPath.value = ResponseUiState.Error(e.message ?: "알 수 없는 오류가 발생했습니다.")
                }
                .collectLatest { path ->
                    when(path) {
                        is AuthResult.Success -> {
                            val selectedPath = path.resultData
                            _selectedPath.value = ResponseUiState.Success("포스트 불러오기 성공", selectedPath)
                        }
                        is AuthResult.NetworkError -> _selectedPath.value = ResponseUiState.Error(path.exception.message ?: "unknown")
                        else -> {} // AuthResult.Loading은 이미 처리했으므로 생략
                    }
                }
        }
    }

    fun resetSelectedPathState() {
        _selectedPath.value = ResponseUiState.Idle
    }

    fun getAllUserPets(token: String) {
        viewModelScope.launch {
            petUseCase.getUserPetsUseCase(token).collectLatest { result ->
                if (result is AuthResult.Success) {
                    _userPets.value = result.resultData
                }
            }
        }
    }

    fun getBreeds() {
        viewModelScope.launch {
            petUseCase.getBreedsUseCase().collectLatest { result ->
                if (result is AuthResult.Success) {
                    _breeds.value = result.resultData
                }
            }
        }
    }

    fun addPet(token: String, pet: Pet, onResult: (Boolean) -> Unit) {
        viewModelScope.launch {
            petUseCase.postUserPetUseCase(token, pet).collectLatest { result ->
                when(result) {
                    is AuthResult.Success -> {
                        // Refresh user's pet list
                        pet.owner.toIntOrNull()?.let { getAllUserPets(token) }
                        onResult(true)
                    }
                    else -> {
                        onResult(false)
                    }
                }
            }
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

    /**
     * 북마크된 산책로 목록을 가져와 통계를 계산하고 UI 상태를 업데이트합니다.
     */
    fun getStats() {
        viewModelScope.launch {
            _stats.value = ResponseUiState.Loading
            val token = sessionUseCase.getAccessToken().first()
            if (token == null) {
                _stats.value = ResponseUiState.Error("로그인이 필요합니다.")
                return@launch
            }

            bookmarkUseCase.getMyBookmarksUseCase(token)
                .catch { e ->
                    _stats.value = ResponseUiState.Error(e.message ?: "알 수 없는 오류가 발생했습니다.")
                }
                .collectLatest { bookmarksResult ->
                    when (bookmarksResult) {
                        is AuthResult.Success -> {
                            val pathList = bookmarksResult.resultData.mapNotNull { it.bookmarkedItem as? BookmarkedPath }
                            val myPathStats = getMyPathStatsUtils(pathList)
                            _stats.value = ResponseUiState.Success("마이페이지 스탯을 불러왔습니다", myPathStats)
                        }

                        is AuthResult.NetworkError -> {
                            _stats.value = ResponseUiState.Error(bookmarksResult.exception.message ?: "unknown")
                        }
                        else -> {
                            _stats.value = ResponseUiState.Error("데이터를 불러오는데 실패했습니다.")
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
                    } else if (bookmarkResponse is AuthResult.NetworkError) {
                        Log.e("MypageViewModel", "Toggle bookmark failed: ${bookmarkResponse.exception}")
                    }
                }
        }
    }

    fun getFavoriteCommunityPost() {
        viewModelScope.launch {
            mypageUseCase.getFavoriteCommunityPostsUseCase().collectLatest { _favoritePosts.value = it }
        }
    }

    fun deleteFavoriteCommunityPost(favoriteCommunityPost: FavoriteCommunityPost) {
        viewModelScope.launch {
            mypageUseCase.deleteFavoriteCommunityPostUseCase(favoriteCommunityPost.id).collectLatest { success ->
                if (success) {
                    getFavoriteCommunityPost()
                }
            }
        }
    }

    fun getSchedules(date: LocalDate) {
        viewModelScope.launch {
            mypageUseCase.getSchedulesUseCase(date)
                .catch { e -> /* Handle error */ }
                .collectLatest { _schedules.value = it }
        }
    }

    fun addSchedule(schedule: MypageSchedule) {
        viewModelScope.launch {
            mypageUseCase.addScheduleUseCase(schedule).collectLatest { success ->
                if (success) {
                    getSchedules(schedule.date) // Reload schedules for the date
                }
            }
        }
    }

    fun deleteSchedule(schedule: MypageSchedule) {
        viewModelScope.launch {
            mypageUseCase.deleteScheduleUseCase(schedule.id).collectLatest { success ->
                if (success) {
                    getSchedules(schedule.date) // Reload schedules for the date
                }
            }
        }
    }

    // MypageSettingScreen
//    val permissions = getMypagePermissionsUseCase()

    fun updatePermission(key: String, isEnabled: Boolean) {
        viewModelScope.launch {
            mypageUseCase.updatePermissionStatusUseCase(key, isEnabled).collectLatest {
                // Can optionally reload permissions if the state is mutable
            }
        }
    }

    fun logout() {
        viewModelScope.launch {
            sessionUseCase.clearSession()
        }
    }

    fun signOut(user: User) {
        viewModelScope.launch {
            userUseCase.deleteUserUseCase(user.id).collectLatest { }
        }
    }

    fun signOut(id: Int) {
        viewModelScope.launch {
            userUseCase.deleteUserUseCase(id).collectLatest { }
        }
    }
}