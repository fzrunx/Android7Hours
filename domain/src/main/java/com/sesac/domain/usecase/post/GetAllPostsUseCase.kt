package com.sesac.domain.usecase.post

import com.sesac.domain.repository.PostRepository
import javax.inject.Inject


class GetAllPostsUseCase @Inject constructor(
    private val postRepository: PostRepository
) {
    operator fun invoke() = postRepository.getAllPosts()
}
