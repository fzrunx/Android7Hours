package com.sesac.domain.usecase.community

import com.sesac.domain.repository.CommunityRepository
import javax.inject.Inject

class GetAllCommunityUseCase @Inject constructor(
    private val communityRepository: CommunityRepository
) {
    suspend operator fun invoke() = communityRepository.getAllPosts()
}
