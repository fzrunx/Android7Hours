package com.sesac.domain.usecase.post

import com.sesac.domain.model.Token
import com.sesac.domain.repository.PostRepository
import javax.inject.Inject

class GetMyPostsUseCase @Inject constructor(
    private val repository: PostRepository
) {
    suspend operator fun invoke(token: String) = repository.getMyPosts(token)
}