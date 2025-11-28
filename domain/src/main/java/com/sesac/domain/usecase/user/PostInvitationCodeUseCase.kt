package com.sesac.domain.usecase.user

import com.sesac.domain.repository.UserRepository
import javax.inject.Inject

class PostInvitationCodeUseCase @Inject constructor(
    private val userRepository: UserRepository
) {
    suspend operator fun invoke(token: String) = userRepository.postInvitationCode(token)
}