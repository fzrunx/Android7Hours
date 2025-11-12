package com.sesac.android7hours.di

import com.sesac.domain.local.repository.MypageRepository
import com.sesac.domain.local.usecase.mypage.AddScheduleUseCase
import com.sesac.domain.local.usecase.mypage.DeleteFavoriteCommunityPostUseCase
import com.sesac.domain.local.usecase.mypage.DeleteFavoriteWalkPathsUseCase
import com.sesac.domain.local.usecase.mypage.DeleteScheduleUseCase
import com.sesac.domain.local.usecase.mypage.GetFavoriteCommunityPostsUseCase
import com.sesac.domain.local.usecase.mypage.GetFavoriteWalkPathsUseCase
import com.sesac.domain.local.usecase.mypage.GetMypageStatsUseCase
import com.sesac.domain.local.usecase.mypage.GetSchedulesUseCase
import com.sesac.domain.local.usecase.mypage.MypageUseCase
import com.sesac.domain.local.usecase.mypage.UpdatePermissionStatusUseCase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object MypageUseCaseModule {
    @Provides
    @Singleton
    fun provideMypageUseCase(repository: MypageRepository): MypageUseCase {
        return MypageUseCase(
            addScheduleUseCase = AddScheduleUseCase(repository),
            deleteFavoriteCommunityPostUseCase = DeleteFavoriteCommunityPostUseCase(repository),
            deleteFavoriteWalkPathsUseCase = DeleteFavoriteWalkPathsUseCase(repository),
            deleteScheduleUseCase = DeleteScheduleUseCase(repository),
            getFavoriteCommunityPostsUseCase = GetFavoriteCommunityPostsUseCase(repository),
            getFavoriteWalkPathsUseCase = GetFavoriteWalkPathsUseCase(repository),
            getMypageStatsUseCase = GetMypageStatsUseCase(repository),
            getSchedulesUseCase = GetSchedulesUseCase(repository),
            updatePermissionStatusUseCase = UpdatePermissionStatusUseCase(repository),
        )
    }
}