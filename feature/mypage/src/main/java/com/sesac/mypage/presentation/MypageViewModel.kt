package com.sesac.mypage.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sesac.domain.local.model.FavoriteCommunityPost
import com.sesac.domain.local.model.FavoriteWalkPath
import com.sesac.domain.local.model.MypageSchedule
import com.sesac.domain.local.model.MypageStat
import com.sesac.domain.local.usecase.AddScheduleUseCase
import com.sesac.domain.local.usecase.DeleteFavoriteCommunityPostUseCase
import com.sesac.domain.local.usecase.DeleteFavoriteWalkPathsUseCase
import com.sesac.domain.local.usecase.DeleteScheduleUseCase
import com.sesac.domain.local.usecase.GetFavoriteCommunityPostsUseCase
import com.sesac.domain.local.usecase.GetFavoriteWalkPathsUseCase
import com.sesac.domain.local.usecase.GetMypageStatsUseCase
import com.sesac.domain.local.usecase.GetSchedulesUseCase
import com.sesac.domain.local.usecase.UpdatePermissionStatusUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import org.threeten.bp.LocalDate
import javax.inject.Inject

@HiltViewModel
class MypageViewModel @Inject constructor(
    private val getMypageStatsUseCase: GetMypageStatsUseCase,
    private val getFavoriteWalkPathsUseCase: GetFavoriteWalkPathsUseCase,
    private val deleteFavoriteWalkPathsUseCase: DeleteFavoriteWalkPathsUseCase,
    private val getFavoriteCommunityPostsUseCase: GetFavoriteCommunityPostsUseCase,
    private val deleteFavoriteCommunityPostUseCase: DeleteFavoriteCommunityPostUseCase,
    private val getSchedulesUseCase: GetSchedulesUseCase,
    private val addScheduleUseCase: AddScheduleUseCase,
    private val deleteScheduleUseCase: DeleteScheduleUseCase,
//    private val getMypagePermissionsUseCase: GetMypagePermissionsUseCase,
    private val updatePermissionStatusUseCase: UpdatePermissionStatusUseCase
) : ViewModel() {
    val tabLabels = listOf("산책로", "커뮤니티")
    private val _activeFilter = MutableStateFlow<String>(tabLabels[0])
    val activeFilter get() = _activeFilter.asStateFlow()
    // MypageMainScreen
    private val _stats = MutableStateFlow<List<MypageStat>>(emptyList())
    val stats = _stats.asStateFlow()
    // MypageFavoriteScreen
    private val _favoriteWalkPaths = MutableStateFlow<List<FavoriteWalkPath>>(emptyList())
    val favoriteWalkPaths get() = _favoriteWalkPaths.asStateFlow()
    private val _favoritePosts = MutableStateFlow<List<FavoriteCommunityPost>>(emptyList())
    val favoritePosts get() = _favoritePosts.asStateFlow()
    // MypageManageScreen
    private val _schedules = MutableStateFlow<List<MypageSchedule>>(emptyList())
    val schedules get() = _schedules.asStateFlow()

    init {
        viewModelScope.launch {
            getStats()
        }
    }

    fun onFilterChange(filter: String) {
        _activeFilter.value = filter
    }

    fun getStats() {
        viewModelScope.launch {
            getMypageStatsUseCase().collectLatest { _stats.value = it }
        }
    }

    fun getFavoriteWalkPaths() {
        viewModelScope.launch {
            getFavoriteWalkPathsUseCase().collectLatest { _favoriteWalkPaths.value = it }
        }
    }

    fun deleteFavoriteWalkPath(favoriteWalkPath: FavoriteWalkPath) {
        viewModelScope.launch {
            deleteFavoriteWalkPathsUseCase(favoriteWalkPath.id).collectLatest { success ->
                if (success) {
                    getFavoriteWalkPaths()
                }
            }
        }
    }

    fun getFavoriteCommunityPost() {
        viewModelScope.launch {
            getFavoriteCommunityPostsUseCase().collectLatest { _favoritePosts.value = it }
        }
    }

    fun deleteFavoriteCommunityPost(favoriteCommunityPost: FavoriteCommunityPost) {
        viewModelScope.launch {
            deleteFavoriteCommunityPostUseCase(favoriteCommunityPost.id).collectLatest { success ->
                if (success) {
                    getFavoriteCommunityPost()
                }
            }
        }
    }

    fun getSchedules(date: LocalDate) {
        viewModelScope.launch {
            getSchedulesUseCase(date)
                .catch { e -> /* Handle error */ }
                .collectLatest { _schedules.value = it }
        }
    }

    fun addSchedule(schedule: MypageSchedule) {
        viewModelScope.launch {
            addScheduleUseCase(schedule).collectLatest { success ->
                if (success) {
                    getSchedules(schedule.date) // Reload schedules for the date
                }
            }
        }
    }

    fun deleteSchedule(schedule: MypageSchedule) {
        viewModelScope.launch {
            deleteScheduleUseCase(schedule.id).collectLatest { success ->
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
            updatePermissionStatusUseCase(key, isEnabled).collectLatest {
                // Can optionally reload permissions if the state is mutable
            }
        }
    }
}