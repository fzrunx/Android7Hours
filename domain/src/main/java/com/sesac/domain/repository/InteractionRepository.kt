package com.sesac.domain.repository

import com.sesac.domain.model.Like
import com.sesac.domain.result.AuthResult
import kotlinx.coroutines.flow.Flow

/**
 * 게시글 외의 다른 도메인 객체(예: 댓글)와도 공유할 수 있는
 * 일반적인 사용자 상호작용(Interaction)을 담당하는 Repository입니다.
 * 현재는 좋아요 토글 기능만 포함합니다.
 */
interface InteractionRepository {

    /**
     * 특정 객체에 대한 좋아요 상태를 토글합니다.
     * @param token 사용자 인증 토큰
     * @param id 좋아요 대상 객체의 ID (예: 게시글 ID)
     * @param postType 좋아요 대상 객체의 타입 (예: "post", "review",...)
     * @return 토글 후의 좋아요 상태 및 카운트
     */
    suspend fun toggleLike(
        objectId: Int, type: String
    ): Flow<AuthResult<Like>>
}