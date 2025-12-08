package com.sesac.data.repositoryprofile_image

import android.content.Context
import com.sesac.data.dto.CommentRequestDTO
import com.sesac.data.mapper.CommentMapper.toDomain
import com.sesac.data.source.api.PathApi
import com.sesac.data.source.api.PostApi
import com.sesac.domain.model.Comment
import com.sesac.domain.repository.CommentRepository
import com.sesac.domain.result.AuthResult
import com.sesac.domain.type.CommentType
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
    override suspend fun getComments(objectId: Int, type: CommentType,): Flow<AuthResult<List<Comment>>> = flow {
        emit(AuthResult.Loading)
        val response = when (type) {
            CommentType.PATH -> pathApi.getComments(pathId = objectId)
            CommentType.POST -> postApi.getComments(postId = objectId)
            else -> throw IllegalArgumentException("Unknown comment type: $type")
        }
        val domainModel = response.map { it.toDomain(context, objectId) }
        emit(AuthResult.Success(domainModel))
    }.catch {
        emit(AuthResult.NetworkError(it))
    }

    override suspend fun createComment(
        token: String,
        objectId: Int,
        content: String,
        type: CommentType,
    ): Flow<AuthResult<Comment>> = flow {
        emit(AuthResult.Loading)
        val request = CommentRequestDTO(content = content)
        val response = when (type) {
            CommentType.PATH -> pathApi.createComment("Bearer $token", pathId = objectId, request = request)
            CommentType.POST -> postApi.createComment("Bearer $token", postId = objectId, request = request)
            else -> throw IllegalArgumentException("Unknown comment type: $type")
        }
        val domainModel = response.toDomain(context, objectId)
        emit(AuthResult.Success(domainModel))
    }.catch {
        emit(AuthResult.NetworkError(it))
    }

    override suspend fun updateComment(
        token: String,
        objectId: Int,
        commentId: Int,
        content: String,
        type: CommentType,
    ): Flow<AuthResult<Comment>> = flow {
        emit(AuthResult.Loading)
        val request = CommentRequestDTO(content = content)
        val response = when (type) {
            CommentType.PATH -> {
                pathApi.updateComment("Bearer $token", pathId = objectId, commentId = commentId, request = request)
            }
//            CommentType.POST -> postApi.updateComment("Bearer $token", postId = objectId, commentId = commentId, request = request)
            else -> throw IllegalArgumentException("Unknown comment type: $type")
        }
        val domainModel = response.toDomain(context, objectId)
        emit(AuthResult.Success(domainModel))
    }.catch {
        emit(AuthResult.NetworkError(it))
    }

    override suspend fun deleteComment(
        token: String,
        objectId: Int,
        commentId: Int,
        type: CommentType,
    ): Flow<AuthResult<Unit>> = flow {
        emit(AuthResult.Loading)
        when (type) {
            CommentType.PATH -> pathApi.deleteComment("Bearer $token", pathId = objectId, commentId = commentId)
            CommentType.POST -> postApi.deleteComment("Bearer $token", postId = objectId, commentId = commentId)
            else -> throw IllegalArgumentException("Unknown comment type: $type")
        }
        emit(AuthResult.Success(Unit))
    }.catch {
        emit(AuthResult.NetworkError(it))
    }
}
