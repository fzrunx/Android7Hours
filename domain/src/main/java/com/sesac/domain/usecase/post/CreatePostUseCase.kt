package com.sesac.domain.usecase.post

import com.sesac.domain.model.PostDetail
import com.sesac.domain.repository.PostRepository
import javax.inject.Inject

class CreatePostUseCase @Inject constructor(
    private val repository: PostRepository
) {
    suspend operator fun invoke(token: String, post: PostDetail) = repository.createPost(token, post)
}