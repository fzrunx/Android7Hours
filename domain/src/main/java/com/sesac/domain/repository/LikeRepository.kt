package com.sesac.domain.repository

import com.sesac.domain.model.Like
import com.sesac.domain.result.AuthResult
import kotlinx.coroutines.flow.Flow

interface LikeRepository {
    /**
     * 특정 Post 또는 Path에 대해 북마크 상태를 토글합니다.
     * @param objectId 북마크할 객체의 ID (Post ID 또는 Path ID)
     * @param type 북마크할 객체의 타입 ("POST" 또는 "PATH")
     * @return 토글 후의 북마크 카운트(Int)를 담은 AuthResult
     */
    suspend fun toggleLike(objectId: Int, type: String): Flow<AuthResult<Int>>

}