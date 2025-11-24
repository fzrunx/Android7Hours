package com.sesac.domain.usecase.path

import com.sesac.domain.repository.PathRepository
import javax.inject.Inject

class GetAllMyRecordUseCase @Inject constructor(
    private val pathRepository: PathRepository
) {
    suspend operator fun invoke() = pathRepository.getAllMyRecord()
}
