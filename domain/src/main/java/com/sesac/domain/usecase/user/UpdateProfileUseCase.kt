package com.sesac.domain.usecase.user

import com.sesac.domain.repository.UserRepository
import okhttp3.MultipartBody
import okhttp3.RequestBody
import javax.inject.Inject

class UpdateProfileUseCase @Inject constructor(
    private val repository: UserRepository
) {
    // repository의 함수를 실행시켜주는 역할
    suspend operator fun invoke(
        token: String,
        image: MultipartBody.Part?,
        nickname: RequestBody? = null
    ) = repository.updateProfile(token, image, nickname)
}