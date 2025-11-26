package com.sesac.domain.usecase.session

import com.sesac.domain.model.User
import com.sesac.domain.repository.SessionRepository
import javax.inject.Inject

class SaveUserUseCase @Inject constructor(
private val repository: SessionRepository
) {
    // ▼▼▼ [핵심] 여기에 'user: User'를 파라미터로 받아야 합니다!
    suspend operator fun invoke(user: User) {
        repository.saveUser(user)
    }
}