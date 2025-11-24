package com.sesac.domain.usecase.user

import com.sesac.domain.model.Auth
import com.sesac.domain.repository.UserRepository
import javax.inject.Inject

class PostUserUseCase @Inject constructor(
    private val userRepository: UserRepository
) {
    suspend operator fun invoke(user: Auth) = userRepository.postUser(user)
}
