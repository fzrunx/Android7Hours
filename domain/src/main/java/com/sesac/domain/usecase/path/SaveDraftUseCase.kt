package com.sesac.domain.usecase.path

import com.sesac.domain.model.Path
import com.sesac.domain.repository.PathRepository
import com.sesac.domain.result.AuthResult
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow

class SaveDraftUseCase(private val repository: PathRepository) {
    suspend operator fun invoke(draft: Path) = repository.saveDraft(draft)
}
