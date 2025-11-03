package com.sesac.domain.repository

import com.sesac.domain.model.post.PostModel
import kotlinx.coroutines.flow.Flow

interface PostRepository {
    fun getAllPosts(): Flow<List<PostModel>>
    fun getPostDetail(postId: Int): Flow<PostModel?>
    fun getSearchPosts(query: String): Flow<List<PostModel>>
}
