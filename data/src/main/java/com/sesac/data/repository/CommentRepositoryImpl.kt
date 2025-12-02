package com.sesac.data.repository

import android.util.Log
import com.sesac.data.mapper.createCommentRequest
import com.sesac.data.mapper.toDomain
import com.sesac.data.source.api.PostApi
import com.sesac.domain.model.CommentItem
import com.sesac.domain.repository.CommentRepository
import com.sesac.domain.result.AuthResult
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class CommentRepositoryImpl @Inject constructor(
    private val postApi: PostApi
) : CommentRepository {

    override suspend fun getComments(postId: Int): Flow<AuthResult<List<CommentItem>>> = flow {
        emit(AuthResult.Loading)
        try {
            val comments = postApi.getComments(postId).toDomain()
            emit(AuthResult.Success(comments))
        } catch (e: Exception) {
            Log.e("TAG-CommentRepositoryImpl", "Get Comments error: $e")
            emit(AuthResult.NetworkError(e))
        }
    }

    override suspend fun createComment(token: String, postId: Int, content: String): Flow<AuthResult<CommentItem>> = flow {
        emit(AuthResult.Loading)
        try {
            val dto = createCommentRequest(content)
            val createdCommentDTO = postApi.createComment("Bearer $token", postId, dto)
            emit(AuthResult.Success(createdCommentDTO.toDomain()))
        } catch (e: Exception) {
            Log.e("TAG-CommentRepositoryImpl", "Create Comment error: $e")
            emit(AuthResult.NetworkError(e))
        }
    }

    override suspend fun deleteComment(token: String, postId: Int, commentId: Int): Flow<AuthResult<Unit>> = flow {
        emit(AuthResult.Loading)
        try {
            postApi.deleteComment("Bearer $token", postId, commentId)
            emit(AuthResult.Success(Unit))
        } catch (e: Exception) {
            Log.e("TAG-CommentRepositoryImpl", "delete Comment error: $e")
            emit(AuthResult.NetworkError(e))
        }
    }
}