package com.sesac.data.repository

import com.sesac.data.source.local.datasource.MockCommunity
import com.sesac.domain.model.Community
import com.sesac.domain.repository.CommunityRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject


class CommunityRepositoryImpl @Inject constructor() : CommunityRepository {
    override suspend fun getAllPosts(): Flow<List<Community>> = flow {
        emit(MockCommunity.postList)
    }

    override suspend fun getPostDetail(postId: Int): Flow<Community?> = flow {
        // MockPostList doesn't have IDs. I'll use the index as an ID for now.
        emit(MockCommunity.postList.getOrNull(postId))
    }

    override suspend fun getSearchPosts(query: String): Flow<List<Community>> = flow {
        val filteredList = MockCommunity.postList.filter {
            it.title.contains(query, ignoreCase = true) || it.content.contains(query, ignoreCase = true)
        }
        emit(filteredList)
    }
}
