package com.sesac.domain.usecase.mypage

import com.sesac.domain.repository.MypageRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class DeleteFavoriteWalkPathsUseCase @Inject constructor(
    private val repository: MypageRepository
) {
    suspend operator fun invoke(favoriteWalkPathId: Int): Flow<Boolean> = repository.deleteFavoriteWalkPaths(favoriteWalkPathId)
}
