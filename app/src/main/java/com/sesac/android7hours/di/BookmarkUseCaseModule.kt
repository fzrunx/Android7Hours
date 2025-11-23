package com.sesac.android7hours.di

import com.sesac.domain.repository.BookmarkRepository
import com.sesac.domain.repository.PathRepository
import com.sesac.domain.usecase.bookmark.BookmarkUseCase
import com.sesac.domain.usecase.bookmark.GetMyBookmarksUseCase
import com.sesac.domain.usecase.bookmark.ToggleBookmarkUseCase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ActivityRetainedComponent
import dagger.hilt.android.scopes.ActivityRetainedScoped

@Module
@InstallIn(ActivityRetainedComponent::class)
object BookmarkUseCaseModule {

    @Provides
    @ActivityRetainedScoped
    fun provideGetMyBookmarksUseCase(
        repository: BookmarkRepository,
        pathRepository: PathRepository,
//        postRepository: PostRepository,
    ): BookmarkUseCase {
        return BookmarkUseCase(
            getMyBookmarksUseCase = GetMyBookmarksUseCase(repository),
            toggleBookmarkUseCase = ToggleBookmarkUseCase(pathRepository, /*postRepository*/),
        )
    }
}
