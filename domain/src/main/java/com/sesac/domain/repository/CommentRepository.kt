package com.sesac.domain.repository

import com.sesac.domain.model.CommentItem
import com.sesac.domain.result.AuthResult
import kotlinx.coroutines.flow.Flow

interface CommentRepository {

    suspend fun getComments(postId: Int): Flow<AuthResult<List<CommentItem>>>

    suspend fun createComment(token: String, postId: Int, content: String): Flow<AuthResult<CommentItem>>

    suspend fun deleteComment(token: String, postId: Int, commentId: Int): Flow<AuthResult<Unit>>
}