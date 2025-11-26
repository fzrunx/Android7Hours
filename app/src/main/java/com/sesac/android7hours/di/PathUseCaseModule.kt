package com.sesac.android7hours.di

import com.sesac.domain.repository.PathRepository
import com.sesac.domain.usecase.path.CreatePathUseCase
import com.sesac.domain.usecase.path.DeleteDraftUseCase
import com.sesac.domain.usecase.path.DeletePathUseCase
import com.sesac.domain.usecase.path.GetAllDraftsUseCase
import com.sesac.domain.usecase.path.GetAllMyRecordUseCase
import com.sesac.domain.usecase.path.GetAllRecommendedPathsUseCase
import com.sesac.domain.usecase.path.GetMyPaths
import com.sesac.domain.usecase.path.PathUseCase
import com.sesac.domain.usecase.path.SaveDraftUseCase
import com.sesac.domain.usecase.path.UpdatePathUseCase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ActivityRetainedComponent
import dagger.hilt.android.scopes.ActivityRetainedScoped

@Module
@InstallIn(ActivityRetainedComponent::class)
object PathUseCaseModule {
    @Provides
    @ActivityRetainedScoped
    fun providePathUseCase(repository: PathRepository): PathUseCase {
        return PathUseCase(
            getAllMyRecordUseCase = GetAllMyRecordUseCase(repository),
            createPathUseCase = CreatePathUseCase(repository),
            updatePathUseCase = UpdatePathUseCase(repository),
            deletePathUseCase = DeletePathUseCase(repository),
            getAllRecommendedPathsUseCase = GetAllRecommendedPathsUseCase(repository),
            getMyPaths = GetMyPaths(repository),
            saveDraftUseCase = SaveDraftUseCase(repository),       // ⭐ 추가
            getAllDraftsUseCase = GetAllDraftsUseCase(repository), // ⭐ Draft 목록 불러오기
            deleteDraftUseCase = DeleteDraftUseCase(repository),   // ⭐ Draft 삭제

        )
    }
}