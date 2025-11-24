package com.sesac.domain.usecase.bookmark

import com.sesac.domain.repository.BookmarkRepository
import javax.inject.Inject

class GetMyBookmarksUseCase @Inject constructor(
    private val repository: BookmarkRepository
) {
    suspend operator fun invoke(token: String) = repository.getMyBookmarks(token)
}
