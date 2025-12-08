package com.sesac.data.repository

import android.util.Log
import com.sesac.data.source.api.LikeApi
import com.sesac.domain.repository.LikeRepository
import com.sesac.domain.result.AuthResult
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class LikeRepositoryImpl @Inject constructor(
    private val likeApi: LikeApi
) : LikeRepository {

    override suspend fun toggleLike(objectId: Int, type: String): Flow<AuthResult<Int>> = flow {
        emit(AuthResult.Loading)
        try {
            val response = when (type.uppercase()) {
                "POST" -> likeApi.togglePostLike(objectId)
                // "PATH" -> likeApi.togglePathLike(objectId) // 필요하면 추가
                else -> throw IllegalArgumentException("유효하지 않은 타입: $type")
            }

            emit(AuthResult.Success(response.likeCount)) // Int 반환

        } catch (e: Exception) {
            Log.d("TAG-LikeRepositoryImpl", "toggleLike error : $e")
            emit(AuthResult.NetworkError(e))
        }
    }
}