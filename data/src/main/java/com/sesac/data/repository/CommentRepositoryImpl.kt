package com.sesac.data.repository

import android.content.Context
import com.sesac.data.dto.CommentRequestDTO
import com.sesac.data.mapper.CommentMapper.toDomain
import com.sesac.data.mapper.toDomain
import com.sesac.data.source.api.PathApi
import com.sesac.data.source.api.PostApi
import com.sesac.domain.model.Comment
import com.sesac.domain.repository.CommentRepository
import com.sesac.domain.result.AuthResult
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class CommentRepositoryImpl @Inject constructor(
    private val pathApi: PathApi,
    private val postApi: PostApi,
    @ApplicationContext private val context: Context
) : CommentRepository {
    override suspend fun getComments(type: String, objectId: Int): Flow<AuthResult<List<Comment>>> = flow {
        emit(AuthResult.Loading)
        val response = when (type) {
            "paths" -> pathApi.getComments(pathId = objectId)
            "posts" -> postApi.getComments(postId = objectId)
            else -> throw IllegalArgumentException("Unknown comment type: $type")
        }
        val domainModel = response.map { it.toDomain(context, objectId) }
        emit(AuthResult.Success(domainModel))
    }.catch {
        emit(AuthResult.NetworkError(it))
    }

    override suspend fun createComment(
        token: String,
        type: String,
        objectId: Int,
        content: String
    ): Flow<AuthResult<Comment>> = flow {
        emit(AuthResult.Loading)
        val request = CommentRequestDTO(content = content)
        val response = when (type) {
            "paths" -> pathApi.createComment("Bearer $token", pathId = objectId, request = request)
            "posts" -> postApi.createComment("Bearer $token", postId = objectId, request = request)
            else -> throw IllegalArgumentException("Unknown comment type: $type")
        }
        val domainModel = response.toDomain(context, objectId)
        emit(AuthResult.Success(domainModel))
    }.catch {
        emit(AuthResult.NetworkError(it))
    }

    override suspend fun updateComment(
        token: String,
        type: String,
        objectId: Int,
        commentId: Int,
        content: String
    ): Flow<AuthResult<Comment>> = flow {
        emit(AuthResult.Loading)
        val request = CommentRequestDTO(content = content)
        val response = when (type) {
            "paths" -> pathApi.updateComment("Bearer $token", pathId = objectId, commentId = commentId, request = request)
            "posts" -> postApi.updateComment("Bearer $token", postId = objectId, commentId = commentId, request = request)
            else -> throw IllegalArgumentException("Unknown comment type: $type")
        }
        val domainModel = response.toDomain(context, objectId)
        emit(AuthResult.Success(domainModel))
    }.catch {
        emit(AuthResult.NetworkError(it))
    }

    override suspend fun deleteComment(
        token: String,
        type: String,
        objectId: Int,
        commentId: Int
    ): Flow<AuthResult<Unit>> = flow {
        emit(AuthResult.Loading)
        when (type) {
            "paths" -> pathApi.deleteComment("Bearer $token", pathId = objectId, commentId = commentId)
            "posts" -> postApi.deleteComment("Bearer $token", postId = objectId, commentId = commentId)
            else -> throw IllegalArgumentException("Unknown comment type: $type")
        }
        emit(AuthResult.Success(Unit))
    }.catch {
        emit(AuthResult.NetworkError(it))
    }
}
