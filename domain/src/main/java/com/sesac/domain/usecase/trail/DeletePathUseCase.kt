package com.sesac.domain.usecase.trail

import com.sesac.domain.repository.TrailRepository
import javax.inject.Inject

class DeletePathUseCase @Inject constructor(
    private val trailRepository: TrailRepository
) {
    suspend operator fun invoke(token: String, id: Int) = trailRepository.deletePath(token, id)
}