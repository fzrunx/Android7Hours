package com.sesac.domain.usecase.trail

data class TrailUseCase(
    val addMyRecordUseCase: AddMyRecordUseCase,
    val getAllMyRecordUseCase: GetAllMyRecordUseCase,
    val getAllRecommendedPathsUseCase: GetAllRecommendedPathsUseCase,
)