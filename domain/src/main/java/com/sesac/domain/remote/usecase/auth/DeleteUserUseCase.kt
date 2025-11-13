package com.sesac.domain.remote.usecase.auth

import com.sesac.domain.remote.repository.AuthRepository
import javax.inject.Inject

class DeleteUserUseCase @Inject constructor(
    private val authRepository: AuthRepository
) {
    suspend operator fun invoke(id: Int) = authRepository.deleteUser(id = id)
}