package com.sesac.domain.usecase.trail

import com.sesac.domain.model.UserPath
import com.sesac.domain.repository.TrailRepository

class DeleteDraftUseCase(private val repository: TrailRepository) {
    suspend operator fun invoke(draft: UserPath) = repository.deleteDraft(draft)
}