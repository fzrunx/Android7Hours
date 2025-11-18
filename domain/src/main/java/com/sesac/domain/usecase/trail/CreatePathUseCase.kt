package com.sesac.domain.usecase.trail

import com.sesac.domain.model.UserPath
import com.sesac.domain.repository.TrailRepository
import javax.inject.Inject

class CreatePathUseCase @Inject constructor(
    private val trailRepository: TrailRepository
) {
    suspend operator fun invoke(token: String, path: UserPath) = trailRepository.createPath(token, path)
}
