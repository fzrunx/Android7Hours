package com.sesac.domain.usecase.like

import com.sesac.domain.model.BookmarkResponse
import com.sesac.domain.model.LikeResponse
import com.sesac.domain.model.LikeType
import com.sesac.domain.repository.PathRepository
import com.sesac.domain.repository.PostRepository
import com.sesac.domain.result.AuthResult
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class ToggleLikeUseCase @Inject constructor(
    private val pathRepository: PathRepository,
    private val postRepository: PostRepository,
) {
    suspend operator fun invoke(
        token: String,
        id: Int,
        type: LikeType,
    ): Flow<AuthResult<LikeResponse>> {
        return when (type) {
            LikeType.PATH -> pathRepository.toggleLike(token, id)
            LikeType.POST -> postRepository.toggleLike(token, id)
        }
    }
}