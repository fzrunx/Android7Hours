package com.sesac.android7hours.di

import com.sesac.domain.repository.TrailRepository
import com.sesac.domain.usecase.trail.CreatePathUseCase
import com.sesac.domain.usecase.trail.DeletePathUseCase
import com.sesac.domain.usecase.trail.GetAllMyRecordUseCase
import com.sesac.domain.usecase.trail.GetAllRecommendedPathsUseCase
import com.sesac.domain.usecase.trail.GetMyPaths
import com.sesac.domain.usecase.trail.TrailUseCase
import com.sesac.domain.usecase.trail.UpdatePathUseCase
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
            getAllMyRecordUseCase = GetAllMyRecordUseCase(repository),
            createPathUseCase = CreatePathUseCase(repository),
            updatePathUseCase = UpdatePathUseCase(repository),
            deletePathUseCase = DeletePathUseCase(repository),
            getAllRecommendedPathsUseCase = GetAllRecommendedPathsUseCase(repository),
            getMyPaths = GetMyPaths(repository),
        )
    }
}