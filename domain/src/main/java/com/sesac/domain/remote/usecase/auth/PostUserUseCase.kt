package com.sesac.domain.remote.usecase.auth

import com.sesac.domain.remote.model.UserAPI
import com.sesac.domain.remote.repository.AuthRepository
import javax.inject.Inject

class PostUserUseCase @Inject constructor(
    private val authRepository: AuthRepository
) {
    suspend operator fun invoke(user: UserAPI) = authRepository.postUser(user)
}
