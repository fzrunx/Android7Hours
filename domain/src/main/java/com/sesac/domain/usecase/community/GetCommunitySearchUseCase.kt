package com.sesac.domain.usecase.community

import com.sesac.domain.repository.CommunityRepository
import javax.inject.Inject

class GetCommunitySearchUseCase @Inject constructor(
    private val postRepository: CommunityRepository
) {
    suspend fun invoke(query: String) = postRepository.getSearchPosts(query)
}
