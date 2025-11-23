package com.sesac.domain.usecase.trail

import com.sesac.domain.model.Path
import com.sesac.domain.repository.TrailRepository
import javax.inject.Inject

class CreatePathUseCase @Inject constructor(
    private val trailRepository: TrailRepository
) {
    suspend operator fun invoke(token: String, path: Path) = trailRepository.createPath(token, path)
}
