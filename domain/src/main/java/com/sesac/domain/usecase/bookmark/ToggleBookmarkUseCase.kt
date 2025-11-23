package com.sesac.domain.usecase.bookmark

import com.sesac.domain.model.BookmarkResponse
import com.sesac.domain.model.BookmarkType
import com.sesac.domain.repository.TrailRepository
import com.sesac.domain.result.AuthResult
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class ToggleBookmarkUseCase @Inject constructor(
    private val trailRepository: TrailRepository,
//    private val postRepository: PostRepository
) {
    suspend operator fun invoke(
        token: String,
        id: Int,
        type: BookmarkType,
    ): Flow<AuthResult<BookmarkResponse>> {
        return when (type) {
            BookmarkType.PATH -> trailRepository.toggleBookmark(token, id)
            //            BookmarkType.POST -> postRepository.toggleBookmark(token, id)
            BookmarkType.POST -> AuthResult.Success(BookmarkResponse.EMPTY)
        } as Flow<AuthResult<BookmarkResponse>>
    }
}