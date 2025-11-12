package com.sesac.domain.local.usecase.trail

import com.sesac.domain.local.repository.TrailRepository
import javax.inject.Inject

class GetAllMyRecordUseCase @Inject constructor(
    private val trailRepository: TrailRepository
) {
    suspend operator fun invoke() = trailRepository.getAllMyRecord()
}
