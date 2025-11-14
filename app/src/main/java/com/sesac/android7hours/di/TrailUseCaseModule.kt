package com.sesac.android7hours.di

import com.sesac.domain.repository.TrailRepository
import com.sesac.domain.usecase.trail.AddMyRecordUseCase
import com.sesac.domain.usecase.trail.GetAllMyRecordUseCase
import com.sesac.domain.usecase.trail.GetAllRecommendedPathsUseCase
import com.sesac.domain.usecase.trail.TrailUseCase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object TrailUseCaseModule {
    @Provides
    @Singleton
    fun provideTrailUseCase(repository: TrailRepository): TrailUseCase {
        return TrailUseCase(
            addMyRecordUseCase = AddMyRecordUseCase(repository),
            getAllMyRecordUseCase = GetAllMyRecordUseCase(repository),
            getAllRecommendedPathsUseCase = GetAllRecommendedPathsUseCase(repository),
        )
    }
}