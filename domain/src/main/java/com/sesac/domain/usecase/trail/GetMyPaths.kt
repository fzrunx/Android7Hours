package com.sesac.domain.usecase.trail

import com.sesac.domain.repository.TrailRepository
import javax.inject.Inject

class GetMyPaths @Inject constructor(
    private val trailRepository: TrailRepository
) {
    suspend operator fun invoke(token: String) = trailRepository.getMyPaths(token)
}