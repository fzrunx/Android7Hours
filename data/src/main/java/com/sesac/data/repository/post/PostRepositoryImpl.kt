package com.sesac.data.repository.post

import com.sesac.common.data.model.post.PostModelSerialize
import com.sesac.data.source.local.datasource.MockPostList
import com.sesac.domain.model.post.PostModel
import com.sesac.domain.repository.PostRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject


class PostRepositoryImpl @Inject constructor() : PostRepository {
    override fun getAllPosts(): Flow<List<PostModel>> = flow {
        emit(MockPostList.postList)
    }

    override fun getPostDetail(postId: Int): Flow<PostModel?> = flow {
        // MockPostList doesn't have IDs. I'll use the index as an ID for now.
        emit(MockPostList.postList.getOrNull(postId))
    }

    override fun getSearchPosts(query: String): Flow<List<PostModel>> = flow {
        val filteredList = MockPostList.postList.filter {
            it.title.contains(query, ignoreCase = true) || it.content.contains(query, ignoreCase = true)
        }
        emit(filteredList)
    }
}
