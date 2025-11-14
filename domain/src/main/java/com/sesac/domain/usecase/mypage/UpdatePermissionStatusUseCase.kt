package com.sesac.domain.usecase.mypage

import com.sesac.domain.repository.MypageRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class UpdatePermissionStatusUseCase @Inject constructor(
    private val repository: MypageRepository
) {
    suspend operator fun invoke(key: String, isEnabled: Boolean): Flow<Boolean> = repository.updatePermissionStatus(key, isEnabled)
}
