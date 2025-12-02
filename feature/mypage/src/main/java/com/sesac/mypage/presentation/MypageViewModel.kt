package com.sesac.mypage.presentation

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sesac.domain.type.BookmarkType
import com.sesac.domain.model.BookmarkedPath
import com.sesac.domain.model.Breed
import com.sesac.domain.model.FavoriteCommunityPost
import com.sesac.domain.model.FavoriteWalkPath
import com.sesac.domain.model.InvitationCode
import com.sesac.domain.model.MypageSchedule
import com.sesac.domain.model.Path
import com.sesac.domain.model.Pet
import com.sesac.domain.model.User
import com.sesac.domain.result.AuthResult
import com.sesac.domain.result.AuthUiState
import com.sesac.domain.result.ResponseUiState
import com.sesac.domain.usecase.auth.AuthUseCase
import com.sesac.domain.usecase.bookmark.BookmarkUseCase
import com.sesac.domain.usecase.mypage.MypageUseCase
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
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import okhttp3.MultipartBody
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
    private val _selectedPet = MutableStateFlow<Pet?>(null)
    val selectedPet = _selectedPet.asStateFlow()

    private val _addPetState = MutableStateFlow<ResponseUiState<Unit>>(ResponseUiState.Idle)
    val addPetState = _addPetState.asStateFlow()

    private val _updatePetState = MutableStateFlow<ResponseUiState<Unit>>(ResponseUiState.Idle)
    val updatePetState = _updatePetState.asStateFlow()

    private val _deletePetState = MutableStateFlow<ResponseUiState<Unit>>(ResponseUiState.Idle)
    val deletePetState = _deletePetState.asStateFlow()

    // AddPetScreen
    private val _breeds = MutableStateFlow<List<Breed>>(emptyList())
    val breeds = _breeds.asStateFlow()
    private val _bookmarkedPaths =
        MutableStateFlow<ResponseUiState<List<BookmarkedPath>>>(ResponseUiState.Idle)
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

    // Invite Code
    private val _invitationCode =
        MutableStateFlow<ResponseUiState<InvitationCode>>(ResponseUiState.Idle)
    val invitationCode get() = _invitationCode.asStateFlow()

    fun onFilterChange(filter: String) {
        _activeFilter.value = filter
    }

    fun generateInvitationCode() {
        viewModelScope.launch {
            _invitationCode.value = ResponseUiState.Loading
            val token = sessionUseCase.getAccessToken().first()
            if (token == null) {
                _invitationCode.value = ResponseUiState.Error("로그인이 필요합니다.")
                return@launch
            }
            userUseCase.postInvitationCodeUseCase(token)
                .collectLatest { result ->
                    when (result) {
                        is AuthResult.Success -> {
                            _invitationCode.value = ResponseUiState.Success("초대 코드가 생성되었습니다.", result.resultData)
                        }
                        is AuthResult.NetworkError -> {
                            _invitationCode.value = ResponseUiState.Error(result.exception.message ?: "네트워크 오류")
                        }
                        else -> {
                            _invitationCode.value = ResponseUiState.Error("초대 코드 생성에 실패했습니다.")
                        }
                    }
                }
        }
    }

    fun resetInvitationCodeState() {
        _invitationCode.value = ResponseUiState.Idle
    }

    fun getPathInfo(pathId: Int) {
        viewModelScope.launch {
            _selectedPath.value = ResponseUiState.Loading
            pathUseCase.getPathById(pathId)
                .catch { e ->
                    _selectedPath.value = ResponseUiState.Error(e.message ?: "알 수 없는 오류가 발생했습니다.")
                }
                .collectLatest { path ->
                    when (path) {
                        is AuthResult.Success -> {
                            val selectedPath = path.resultData
                            _selectedPath.value = ResponseUiState.Success("산책로 불러오기 성공", selectedPath)
                        }
                        is AuthResult.NetworkError -> _selectedPath.value = ResponseUiState.Error(path.exception.message ?: "unknown")
                        else -> {}
                    }
                }
        }
    }

    fun resetSelectedPathState() {
        _selectedPath.value = ResponseUiState.Idle
    }

    fun loadPetForEditing(petId: Int) {
        val petToEdit = _userPets.value.find { it.id == petId }
        _selectedPet.value = petToEdit
    }

    fun clearSelectedPet() {
        _selectedPet.value = null
    }

    fun getAllUserPets() {
        viewModelScope.launch {
            val token = sessionUseCase.getAccessToken().first() ?: return@launch
            petUseCase.getUserPetsUseCase(token).collectLatest { result ->
                if (result is AuthResult.Success) {
                    _userPets.value = result.resultData
                } else {
                    // TODO: Pet list loading failure error handling
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

    fun addPet(pet: Pet) {
        viewModelScope.launch {
            _addPetState.value = ResponseUiState.Loading
            val token = sessionUseCase.getAccessToken().first()
            if (token == null) {
                _addPetState.value = ResponseUiState.Error("로그인이 필요합니다.")
                return@launch
            }
            petUseCase.postUserPetUseCase(token, pet).collectLatest { result ->
                when (result) {
                    is AuthResult.Success -> {
                        getAllUserPets()
                        _addPetState.value = ResponseUiState.Success("반려견이 추가되었습니다.", Unit)
                    }

                    is AuthResult.NetworkError -> {
                        _addPetState.value =
                            ResponseUiState.Error(result.exception.message ?: "오류가 발생했습니다.")
                    }

                    is AuthResult.Loading -> {}
                    else -> {
                        _addPetState.value = ResponseUiState.Error("알 수 없는 오류가 발생했습니다.")
                    }
                }
            }
        }
    }

    fun updatePet(pet: Pet) {
        viewModelScope.launch {
            _updatePetState.value = ResponseUiState.Loading
            val token = sessionUseCase.getAccessToken().first()
            if (token == null) {
                _updatePetState.value = ResponseUiState.Error("로그인이 필요합니다.")
                return@launch
            }

            petUseCase.updatePetUseCase(token, pet.id, pet).collectLatest { result ->
                when (result) {
                    is AuthResult.Success -> {
                        getAllUserPets()
                        _updatePetState.value = ResponseUiState.Success("반려견 정보가 수정되었습니다.", Unit)
                    }

                    is AuthResult.Loading -> {}
                    else -> _updatePetState.value = ResponseUiState.Error("수정에 실패했습니다.")
                }
            }
        }
    }

    fun deletePet(petId: Int) {
        viewModelScope.launch {
            _deletePetState.value = ResponseUiState.Loading
            val token = sessionUseCase.getAccessToken().first()
            if (token == null) {
                _deletePetState.value = ResponseUiState.Error("로그인이 필요합니다.")
                return@launch
            }

            petUseCase.deletePetUseCase(token, petId).collectLatest { result ->
                when (result) {
                    is AuthResult.Success -> {
                        getAllUserPets()
                        _deletePetState.value = ResponseUiState.Success("반려견이 삭제되었습니다.", Unit)
                    }

                    is AuthResult.Loading -> {}
                    else -> _deletePetState.value = ResponseUiState.Error("삭제에 실패했습니다.")
                }
            }
        }
    }

    fun resetAddPetState() {
        _addPetState.value = ResponseUiState.Idle
    }

    fun resetUpdatePetState() {
        _updatePetState.value = ResponseUiState.Idle
    }

    fun resetDeletePetState() {
        _deletePetState.value = ResponseUiState.Idle
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
                    _bookmarkedPaths.value =
                        ResponseUiState.Error(e.message ?: "알 수 없는 오류가 발생했습니다.")
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
                            _bookmarkedPaths.value = ResponseUiState.Error(
                                bookmarksResult.exception.message ?: "unknown"
                            )
                        }

                        else -> {
                            // Other AuthResult states are not handled here.
                        }
                    }
                }
        }
    }

    fun getStats(uiState: AuthUiState) {
        viewModelScope.launch {
            _stats.value = ResponseUiState.Loading
            val token = sessionUseCase.getAccessToken().first()
            if (token == null) {
                _stats.value = ResponseUiState.Error("로그인이 필요합니다.")
                return@launch
            }

            pathUseCase.getMyPaths(token)
                .catch { e ->
                    _stats.value = ResponseUiState.Error(e.message ?: "알 수 없는 오류가 발생했습니다.")
                }
                .collectLatest { myPathResult ->
                    val userNickname = uiState.user?.nickname
                    when (myPathResult) {
                        is AuthResult.Success -> {
                            val pathList =
                                myPathResult.resultData.filter { it.uploader == userNickname }
                            val myPathStats = getMyPathStatsUtils(pathList)
                            _stats.value = ResponseUiState.Success("마이페이지 스탯을 불러왔습니다", myPathStats)
                        }

                        is AuthResult.NetworkError -> {
                            _stats.value =
                                ResponseUiState.Error(myPathResult.exception.message ?: "unknown")
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
                        Log.e(
                            "MypageViewModel",
                            "Toggle bookmark failed: ${bookmarkResponse.exception}"
                        )
                    }
                }
        }
    }

    fun getFavoriteCommunityPost() {
        viewModelScope.launch {
            mypageUseCase.getFavoriteCommunityPostsUseCase()
                .collectLatest { _favoritePosts.value = it }
        }
    }

    fun deleteFavoriteCommunityPost(favoriteCommunityPost: FavoriteCommunityPost) {
        viewModelScope.launch {
            mypageUseCase.deleteFavoriteCommunityPostUseCase(favoriteCommunityPost.id)
                .collectLatest { success ->
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

    fun updateProfileImage(imagePart: MultipartBody.Part) {
        viewModelScope.launch {
            val token = sessionUseCase.getAccessToken().first()
            // [수정] authUseCase 안에 있는 updateProfile 호출
            token?.let {
                userUseCase.updateProfile(token, imagePart, null)
                    .collectLatest { result ->
                        when (result) {
                            is AuthResult.Loading -> {
                                Log.d("MypageViewModel", "업로드 진행 중: Loading")
                            }

                            is AuthResult.Success -> {
                                val updatedUser = result.resultData
                                Log.d(
                                    "MypageViewModel",
                                    "업로드 성공, 유저 정보: ${updatedUser.profileImageUrl}"
                                )
                                // 성공 후 유저 정보 갱신
                                sessionUseCase.saveUser(updatedUser)
                                getAllUserPets()
                            }

                            is AuthResult.NetworkError -> {
                                Log.e(
                                    "MypageViewModel",
                                    "업로드 실패: 네트워크 오류 - ${result.exception.message}"
                                )
                            }

                            else -> {
                                Log.e("MypageViewModel", "업로드 실패: 알 수 없는 오류 - $result")
                            }
                        }
                    }
            }
        }
    }
}