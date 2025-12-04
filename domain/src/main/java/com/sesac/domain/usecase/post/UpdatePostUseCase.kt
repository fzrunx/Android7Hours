package com.sesac.domain.usecase.post

import com.sesac.domain.model.Post
import com.sesac.domain.repository.PostRepository
import okhttp3.MultipartBody
import javax.inject.Inject

class UpdatePostUseCase @Inject constructor(
    private val postRepository: PostRepository
) {
    suspend operator fun invoke(token: String, id: Int, post: Post, image: MultipartBody.Part?) = postRepository.updatePost(token, id, post, image)
}