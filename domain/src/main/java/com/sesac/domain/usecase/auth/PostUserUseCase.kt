package com.sesac.domain.usecase.auth

import com.sesac.domain.model.Auth
import com.sesac.domain.repository.AuthRepository
import javax.inject.Inject

class PostUserUseCase @Inject constructor(
    private val authRepository: AuthRepository
) {
    suspend operator fun invoke(user: Auth) = authRepository.postUser(user)
}
