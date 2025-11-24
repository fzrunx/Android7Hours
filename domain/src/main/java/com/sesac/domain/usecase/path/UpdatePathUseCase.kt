package com.sesac.domain.usecase.path

import com.sesac.domain.model.Path
import com.sesac.domain.repository.PathRepository
import javax.inject.Inject

class UpdatePathUseCase @Inject constructor(
    private val pathRepository: PathRepository
) {
    suspend operator fun invoke(token: String, id: Int, updatedPath: Path) = pathRepository.updatePath(token, id, updatedPath)
}