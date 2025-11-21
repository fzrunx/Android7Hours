package com.sesac.domain.usecase.trail

import com.sesac.domain.repository.TrailRepository

class ClearAllDraftsUseCase(private val repository: TrailRepository) {
    suspend operator fun invoke() = repository.clearAllDrafts()
}