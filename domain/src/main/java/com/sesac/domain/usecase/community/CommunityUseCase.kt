package com.sesac.domain.usecase.community


data class CommunityUseCase(
    val getAllCommunityUseCase: GetAllCommunityUseCase,
    val getCommunityDetailUseCase: GetCommunityDetailUseCase,
    val getCommunitySearchUseCase: GetCommunitySearchUseCase,
)