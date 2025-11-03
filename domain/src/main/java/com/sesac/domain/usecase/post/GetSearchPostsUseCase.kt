package com.sesac.domain.usecase.post

import com.sesac.domain.repository.PostRepository
import javax.inject.Inject

class GetSearchPostsUseCase @Inject constructor(
    private val postRepository: PostRepository
) {
    operator fun invoke(query: String) = postRepository.getSearchPosts(query)
}
