package com.sesac.domain.usecase.post

import com.sesac.domain.model.Post
import com.sesac.domain.repository.PostRepository
import javax.inject.Inject

data class UpdatePostUseCase(
    private val postRepository: PostRepository
) {
    suspend operator fun invoke(token: String, id: Int, post: Post) = postRepository.updatePost(token, id, post)
}


