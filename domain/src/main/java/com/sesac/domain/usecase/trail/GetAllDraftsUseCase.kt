package com.sesac.domain.usecase.trail

import com.sesac.domain.repository.TrailRepository

class GetAllDraftsUseCase(private val repository: TrailRepository) {
    suspend operator fun invoke() = repository.getAllDrafts()
}