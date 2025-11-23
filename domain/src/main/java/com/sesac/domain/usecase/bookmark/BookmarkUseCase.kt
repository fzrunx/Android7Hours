package com.sesac.domain.usecase.bookmark

import com.sesac.domain.repository.BookmarkRepository
import javax.inject.Inject

data class BookmarkUseCase(
    val getMyBookmarksUseCase: GetMyBookmarksUseCase,
    val toggleBookmarkUseCase: ToggleBookmarkUseCase,
)