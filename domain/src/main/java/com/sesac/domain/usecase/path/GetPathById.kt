package com.sesac.domain.usecase.path

import com.sesac.domain.repository.PathRepository
import javax.inject.Inject

class GetPathById @Inject constructor(
    private val pathRepository: PathRepository
) {
    suspend operator fun invoke(id: Int) = pathRepository.getPathById(id)
}
