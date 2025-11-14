package com.sesac.domain.usecase.mypage

import com.sesac.domain.model.FavoriteWalkPath
import com.sesac.domain.repository.MypageRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

// Favorite Walk Paths
class GetFavoriteWalkPathsUseCase @Inject constructor(
    private  val repository: MypageRepository
) {
    suspend operator fun invoke(): Flow<List<FavoriteWalkPath>> = repository.getFavoriteWalkPaths()
}
