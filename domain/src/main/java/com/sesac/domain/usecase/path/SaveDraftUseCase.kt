package com.sesac.domain.usecase.path

import com.sesac.domain.model.Path
import com.sesac.domain.repository.PathRepository

class SaveDraftUseCase(private val repository: PathRepository) {
    suspend operator fun invoke(draft: Path) = repository.saveDraft(draft)
}