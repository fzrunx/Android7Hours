package com.sesac.domain.usecase.path

import com.sesac.domain.repository.PathRepository

class GetAllDraftsUseCase(private val repository: PathRepository) {
    suspend operator fun invoke() = repository.getAllDrafts()
}