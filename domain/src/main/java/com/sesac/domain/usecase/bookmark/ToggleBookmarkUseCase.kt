package com.sesac.domain.usecase.bookmark

import com.sesac.domain.model.BookmarkResponse
import com.sesac.domain.type.BookmarkType
import com.sesac.domain.repository.PathRepository
import com.sesac.domain.repository.PostRepository
import com.sesac.domain.result.AuthResult
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class ToggleBookmarkUseCase @Inject constructor(
    private val pathRepository: PathRepository,
    private val postRepository: PostRepository
) {
    suspend operator fun invoke(
        token: String,
        id: Int,
        type: BookmarkType,
    ): Flow<AuthResult<BookmarkResponse>> {
        return when (type) {
            BookmarkType.PATH -> pathRepository.toggleBookmark(token, id)
            BookmarkType.POST -> postRepository.toggleBookmark(token, id)
        }
    }
}
