package com.sesac.domain.usecase.path


import com.sesac.domain.model.Path
import com.sesac.domain.repository.PathRepository
import javax.inject.Inject

class UploadPathUseCase @Inject constructor(
    private val repository: PathRepository
) {
    suspend operator fun invoke(path: Path) {
        repository.uploadPath(path)
    }
}