package com.sesac.domain.local.usecase.trail

import com.sesac.domain.local.model.MyRecord
import com.sesac.domain.local.repository.TrailRepository
import javax.inject.Inject

data class TrailUseCase(
    val addMyRecordUseCase: AddMyRecordUseCase,
    val getAllMyRecordUseCase: GetAllMyRecordUseCase,
    val getAllRecommendedPathsUseCase: GetAllRecommendedPathsUseCase,
)