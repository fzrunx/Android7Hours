package com.sesac.domain.usecase.user

import com.sesac.domain.repository.UserRepository
import javax.inject.Inject

class DeleteUserUseCase @Inject constructor(
    private val authRepository: UserRepository
) {
    suspend operator fun invoke(id: Int) = authRepository.deleteUser(id = id)
}