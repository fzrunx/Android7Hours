package com.sesac.domain.usecase.post

import com.sesac.domain.model.Post
import com.sesac.domain.repository.PostRepository
import okhttp3.MultipartBody
import javax.inject.Inject

class CreatePostUseCase @Inject constructor(
    private val repository: PostRepository
) {
    suspend operator fun invoke(token: String, post: Post, image: MultipartBody.Part?) = repository.createPost(token, post, image)
}