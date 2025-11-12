package com.sesac.domain.local.usecase.mypage

import com.sesac.domain.local.model.FavoriteCommunityPost
import com.sesac.domain.local.model.FavoriteWalkPath
import com.sesac.domain.local.model.MypageSchedule
import com.sesac.domain.local.model.MypageStat
import com.sesac.domain.local.repository.MypageRepository
import kotlinx.coroutines.flow.Flow
import org.threeten.bp.LocalDate
import javax.inject.Inject

data class MypageUseCase(
    val addScheduleUseCase: AddScheduleUseCase,
    val deleteFavoriteCommunityPostUseCase: DeleteFavoriteCommunityPostUseCase,
    val deleteFavoriteWalkPathsUseCase: DeleteFavoriteWalkPathsUseCase,
    val deleteScheduleUseCase: DeleteScheduleUseCase,
    val getFavoriteCommunityPostsUseCase: GetFavoriteCommunityPostsUseCase,
    val getFavoriteWalkPathsUseCase: GetFavoriteWalkPathsUseCase,
    val getMypageStatsUseCase: GetMypageStatsUseCase,
    val getSchedulesUseCase: GetSchedulesUseCase,
    val updatePermissionStatusUseCase: UpdatePermissionStatusUseCase,
)