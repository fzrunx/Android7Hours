package com.sesac.android7hours.di

import com.sesac.domain.repository.LikeRepository
import com.sesac.domain.repository.PathRepository
import com.sesac.domain.repository.PostRepository
import com.sesac.domain.usecase.like.LikeUseCase
import com.sesac.domain.usecase.like.ToggleLikeUseCase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ActivityRetainedComponent
import dagger.hilt.android.scopes.ActivityRetainedScoped

@Module
@InstallIn(ActivityRetainedComponent::class)
object LikeUseCaseModule {
    @Provides
    @ActivityRetainedScoped
    fun provideLikeUseCase(
        repository: LikeRepository,
        pathRepository: PathRepository,
        postRepository: PostRepository
    ): LikeUseCase {
        return LikeUseCase(
            toggleLikeUseCase = ToggleLikeUseCase(pathRepository, postRepository)
        )
    }
}