package com.sesac.domain.usecase.path

import com.sesac.domain.repository.PathRepository

class ClearAllDraftsUseCase(private val repository: PathRepository) {
    suspend operator fun invoke() = repository.clearAllDrafts()
}