package com.sesac.domain.usecase.trail

import com.sesac.domain.model.Coord
import com.sesac.domain.repository.TrailRepository
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class GetAllRecommendedPathsUseCase @Inject constructor(
    private val trailRepository: TrailRepository
) {
    suspend operator fun invoke(coord: Coord?, radius: Float?) = trailRepository.getAllRecommendedPaths(coord, radius)
}