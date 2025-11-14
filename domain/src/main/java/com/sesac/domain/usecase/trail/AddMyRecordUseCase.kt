package com.sesac.domain.usecase.trail

import com.sesac.domain.model.MyRecord
import com.sesac.domain.repository.TrailRepository
import javax.inject.Inject

class AddMyRecordUseCase @Inject constructor(
    private val trailRepository: TrailRepository
) {
    suspend operator fun invoke(newRecord: MyRecord) = trailRepository.addMyRecord(newRecord)
}