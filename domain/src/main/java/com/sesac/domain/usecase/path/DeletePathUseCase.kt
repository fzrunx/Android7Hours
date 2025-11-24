package com.sesac.domain.usecase.path

import com.sesac.domain.repository.PathRepository
import javax.inject.Inject

class DeletePathUseCase @Inject constructor(
    private val pathRepository: PathRepository
) {
    suspend operator fun invoke(token: String, id: Int) = pathRepository.deletePath(token, id)
}