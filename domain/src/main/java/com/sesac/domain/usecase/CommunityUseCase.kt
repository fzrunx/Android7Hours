package com.sesac.domain.usecase

import com.sesac.domain.repository.CommunityRepository
import javax.inject.Inject


class GetAllCommunityUseCase @Inject constructor(
    private val communityRepository: CommunityRepository
) {
    suspend operator fun invoke() = communityRepository.getAllPosts()
}

class GetCommunityDetailUseCase @Inject constructor(
    private val postRepository: CommunityRepository
) {
    suspend fun invoke(postId: Int) = postRepository.getPostDetail(postId)
}

class GetCommunitySearchUseCase @Inject constructor(
    private val postRepository: CommunityRepository
) {
    suspend fun invoke(query: String) = postRepository.getSearchPosts(query)
}
