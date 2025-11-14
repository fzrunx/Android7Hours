package com.sesac.domain.usecase.community

import com.sesac.domain.repository.CommunityRepository
import javax.inject.Inject

class GetCommunityDetailUseCase @Inject constructor(
    private val postRepository: CommunityRepository
) {
    suspend fun invoke(postId: Int) = postRepository.getPostDetail(postId)
}