package com.sesac.domain.usecase.path

import com.sesac.domain.model.Path
import com.sesac.domain.repository.PathRepository
import javax.inject.Inject

class CreatePathUseCase @Inject constructor(
    private val pathRepository: PathRepository
) {
    suspend operator fun invoke(token: String, path: Path) = pathRepository.createPath(token, path)
}
