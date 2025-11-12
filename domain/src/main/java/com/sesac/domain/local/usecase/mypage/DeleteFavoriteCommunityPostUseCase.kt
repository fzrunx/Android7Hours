package com.sesac.domain.local.usecase.mypage

import com.sesac.domain.local.repository.MypageRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class DeleteFavoriteCommunityPostUseCase @Inject constructor(
    private val repository: MypageRepository
) {
    suspend operator fun invoke(favoriteCommunityPostId: Int): Flow<Boolean> = repository.deleteFavoriteCommunityPosts(favoriteCommunityPostId)
}
