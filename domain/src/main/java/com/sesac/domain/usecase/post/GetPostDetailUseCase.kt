package com.sesac.domain.usecase.post

import com.sesac.domain.repository.PostRepository
import javax.inject.Inject

class GetPostDetailUseCase @Inject constructor(
    private val postRepository: PostRepository
) {
    operator fun invoke(postId: Int) = postRepository.getPostDetail(postId)
}
