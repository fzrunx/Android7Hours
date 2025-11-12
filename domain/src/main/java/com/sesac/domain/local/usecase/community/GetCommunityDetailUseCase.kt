package com.sesac.domain.local.usecase.community

import com.sesac.domain.local.repository.CommunityRepository
import javax.inject.Inject

class GetCommunityDetailUseCase @Inject constructor(
    private val postRepository: CommunityRepository
) {
    suspend fun invoke(postId: Int) = postRepository.getPostDetail(postId)
}