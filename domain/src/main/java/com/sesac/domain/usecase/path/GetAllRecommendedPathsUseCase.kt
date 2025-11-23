package com.sesac.domain.usecase.path

import com.sesac.domain.model.Coord
import com.sesac.domain.repository.PathRepository
import javax.inject.Inject

class GetAllRecommendedPathsUseCase @Inject constructor(
    private val pathRepository: PathRepository
) {
    suspend operator fun invoke(coord: Coord?, radius: Float?) = pathRepository.getAllRecommendedPaths(coord, radius)
}