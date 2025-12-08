package com.sesac.domain.usecase.path

import com.sesac.domain.model.Path
import com.sesac.domain.repository.PathRepository

class DeleteDraftUseCase(private val repository: PathRepository) {
    suspend operator fun invoke(draft: Path) = repository.deleteDraft(draft)
}