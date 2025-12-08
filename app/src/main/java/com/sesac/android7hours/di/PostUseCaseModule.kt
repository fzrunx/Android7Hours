package com.sesac.android7hours.di

import com.sesac.domain.repository.PostRepository
import com.sesac.domain.usecase.post.CreatePostUseCase
import com.sesac.domain.usecase.post.DeletePostUseCase
import com.sesac.domain.usecase.post.GetMyPostsUseCase
import com.sesac.domain.usecase.post.GetPostDetailUseCase
import com.sesac.domain.usecase.post.GetPostListUseCase
import com.sesac.domain.usecase.post.PostUseCase
import com.sesac.domain.usecase.post.UpdatePostUseCase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ActivityRetainedComponent
import dagger.hilt.android.scopes.ActivityRetainedScoped

@Module
@InstallIn(ActivityRetainedComponent::class)
object PostUseCaseModule {
    @Provides
    @ActivityRetainedScoped
    fun providePostUseCase(repository: PostRepository): PostUseCase {
        return PostUseCase(
            getPostListUseCase = GetPostListUseCase(repository),
            getMyPostsUseCase = GetMyPostsUseCase(repository),
            getPostDetailUseCase = GetPostDetailUseCase(repository),
            createPostUseCase = CreatePostUseCase(repository),
            updatePostUseCase = UpdatePostUseCase(repository),
            deletePostUseCase = DeletePostUseCase(repository),
        )
    }
}