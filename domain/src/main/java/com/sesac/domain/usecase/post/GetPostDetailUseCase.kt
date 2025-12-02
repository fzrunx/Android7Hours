package com.sesac.domain.usecase.post

import com.sesac.domain.repository.PostRepository
import javax.inject.Inject

class GetPostDetailUseCase @Inject constructor(
    private val repository: PostRepository
) {
    suspend operator fun invoke(token: String, postId: Int) = repository.getPostDetail(token, postId)
}