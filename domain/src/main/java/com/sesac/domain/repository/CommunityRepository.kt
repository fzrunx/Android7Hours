package com.sesac.domain.repository

import com.sesac.domain.model.Community
import kotlinx.coroutines.flow.Flow

interface CommunityRepository {
    suspend fun getAllPosts(): Flow<List<Community>>
    suspend fun getPostDetail(postId: Int): Flow<Community?>
    suspend fun getSearchPosts(query: String): Flow<List<Community>>
//    suspend fun createPost(post: PostModel)
//    suspend fun updatePost(postId: Int, post: PostModel)
//    suspend fun deletePost(postId: Int)
}
