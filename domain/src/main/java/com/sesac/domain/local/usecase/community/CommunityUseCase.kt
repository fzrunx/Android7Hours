package com.sesac.domain.local.usecase.community

import com.sesac.domain.local.repository.CommunityRepository
import javax.inject.Inject


data class CommunityUseCase(
    val getAllCommunityUseCase: GetAllCommunityUseCase,
    val getCommunityDetailUseCase: GetCommunityDetailUseCase,
    val getCommunitySearchUseCase: GetCommunitySearchUseCase,
)