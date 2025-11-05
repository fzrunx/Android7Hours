package com.sesac.domain.usecase

import com.sesac.domain.model.FavoriteCommunityPost
import com.sesac.domain.model.FavoriteWalkPath
import com.sesac.domain.model.MypageMenuItem
import com.sesac.domain.model.MypagePermission
import com.sesac.domain.model.MypageSchedule
import com.sesac.domain.model.MypageStat
import com.sesac.domain.repository.MypageRepository
import kotlinx.coroutines.flow.Flow
import org.threeten.bp.LocalDate
import javax.inject.Inject

// Stats
class GetMypageStatsUseCase @Inject constructor(
    private val repository: MypageRepository
) {
    suspend operator fun invoke(): Flow<List<MypageStat>> = repository.getMypageStats()
}

// Menu Items
//class GetMypageMenuItemsUseCase @Inject constructor(
//    private val repository: MypageRepository
//) {
//    suspend operator fun invoke(): Flow<List<MypageMenuItem>> = repository.getMypageMenuItems()
//}

// Favorite Walk Paths
class GetFavoriteWalkPathsUseCase @Inject constructor(
    private  val repository: MypageRepository
) {
    suspend operator fun invoke(): Flow<List<FavoriteWalkPath>> = repository.getFavoriteWalkPaths()
}

class DeleteFavoriteWalkPathsUseCase @Inject constructor(
    private val repository: MypageRepository
) {
    suspend operator fun invoke(favoriteWalkPathId: Int): Flow<Boolean> = repository.deleteFavoriteWalkPaths(favoriteWalkPathId)
}

// Favorite Community Posts
class GetFavoriteCommunityPostsUseCase @Inject constructor(
    private val repository: MypageRepository
) {
    suspend operator fun invoke(): Flow<List<FavoriteCommunityPost>> = repository.getFavoriteCommunityPosts()
}

class DeleteFavoriteCommunityPostUseCase @Inject constructor(
    private val repository: MypageRepository
) {
    suspend operator fun invoke(favoriteCommunityPostId: Int): Flow<Boolean> = repository.deleteFavoriteCommunityPosts(favoriteCommunityPostId)
}

// Schedules
class GetSchedulesUseCase @Inject constructor(
    private val repository: MypageRepository
) {
    suspend operator fun invoke(date: LocalDate): Flow<List<MypageSchedule>> = repository.getSchedules(date)
}

class AddScheduleUseCase @Inject constructor(
    private val repository: MypageRepository
) {
    suspend operator fun invoke(schedule: MypageSchedule): Flow<Boolean> = repository.addSchedule(schedule)
}

class DeleteScheduleUseCase @Inject constructor(
    private val repository: MypageRepository
) {
    suspend operator fun invoke(scheduleId: Long): Flow<Boolean> = repository.deleteSchedule(scheduleId)
}

// Permissions
//class GetMypagePermissionsUseCase @Inject constructor(
//    private val repository: MypageRepository
//) {
//    suspend operator fun invoke(): Flow<List<MypagePermission>> = repository.getMypagePermissions()
//}

class UpdatePermissionStatusUseCase @Inject constructor(
    private val repository: MypageRepository
) {
    suspend operator fun invoke(key: String, isEnabled: Boolean): Flow<Boolean> = repository.updatePermissionStatus(key, isEnabled)
}
