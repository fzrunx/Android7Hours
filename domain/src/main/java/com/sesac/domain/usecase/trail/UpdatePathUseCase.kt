package com.sesac.domain.usecase.trail

import com.sesac.domain.model.Path
import com.sesac.domain.repository.TrailRepository
import javax.inject.Inject

class UpdatePathUseCase @Inject constructor(
    private val trailRepository: TrailRepository
) {
    suspend operator fun invoke(token: String, id: Int, updatedPath: Path) = trailRepository.updatePath(token, id, updatedPath)
}