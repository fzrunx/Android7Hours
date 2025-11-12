package com.sesac.domain.local.usecase.community

import com.sesac.domain.local.repository.CommunityRepository
import javax.inject.Inject

class GetAllCommunityUseCase @Inject constructor(
    private val communityRepository: CommunityRepository
) {
    suspend operator fun invoke() = communityRepository.getAllPosts()
}
