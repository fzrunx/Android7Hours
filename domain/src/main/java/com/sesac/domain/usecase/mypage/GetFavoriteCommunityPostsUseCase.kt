package com.sesac.domain.usecase.mypage

import com.sesac.domain.model.FavoriteCommunityPost
import com.sesac.domain.repository.MypageRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetFavoriteCommunityPostsUseCase @Inject constructor(
    private val repository: MypageRepository
) {
    suspend operator fun invoke(): Flow<List<FavoriteCommunityPost>> = repository.getFavoriteCommunityPosts()
}