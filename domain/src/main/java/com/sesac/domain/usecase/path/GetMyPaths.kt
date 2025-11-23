package com.sesac.domain.usecase.path

import com.sesac.domain.repository.PathRepository
import javax.inject.Inject

class GetMyPaths @Inject constructor(
    private val pathRepository: PathRepository
) {
    suspend operator fun invoke(token: String) = pathRepository.getMyPaths(token)
}