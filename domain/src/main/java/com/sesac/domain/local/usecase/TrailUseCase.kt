package com.sesac.domain.local.usecase

import com.sesac.domain.local.model.MyRecord
import com.sesac.domain.local.repository.TrailRepository
import javax.inject.Inject

class GetAllRecommendedPathsUseCase @Inject constructor(
    private val trailRepository: TrailRepository
) {
    suspend operator fun invoke() = trailRepository.getAllRecommendedPaths()
}

class GetAllMyRecordUseCase @Inject constructor(
    private val trailRepository: TrailRepository
) {
    suspend operator fun invoke() = trailRepository.getAllMyRecord()
}

class AddMyRecordUseCase @Inject constructor(
    private val trailRepository: TrailRepository
) {
    suspend operator fun invoke(newRecord: MyRecord) = trailRepository.addMyRecord(newRecord)
}