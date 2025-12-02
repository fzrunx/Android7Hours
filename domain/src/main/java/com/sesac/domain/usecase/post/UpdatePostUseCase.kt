package com.sesac.domain.usecase.post

import com.sesac.domain.model.PostDetail
import com.sesac.domain.repository.PostRepository
import javax.inject.Inject

data class UpdatePostUseCase @Inject constructor(
    private val postRepository: PostRepository
) {
    suspend operator fun invoke(token: String, id: Int, post: PostDetail) = postRepository.updatePost(token, id, post)
}


