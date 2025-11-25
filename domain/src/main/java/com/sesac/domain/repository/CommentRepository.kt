package com.sesac.domain.repository

import com.sesac.domain.model.Comment
import com.sesac.domain.model.CommentType
import com.sesac.domain.result.AuthResult
import kotlinx.coroutines.flow.Flow

interface CommentRepository {
    /**
     * 특정 객체(산책로, 게시물 등)에 달린 댓글 목록을 가져옵니다.
     *
     * @param type 댓글이 달린 객체의 종류 (예: "paths", "posts")
     * @param objectId 객체의 고유 ID
     * @return 댓글 목록을 담은 Flow
     */
    suspend fun getComments(objectId: Int, type: CommentType,): Flow<AuthResult<List<Comment>>>

    /**
     * 새로운 댓글을 작성합니다.
     *
     * @param token 사용자의 인증 토큰
     * @param type 댓글을 작성할 객체의 종류
     * @param objectId 객체의 고유 ID
     * @param content 댓글 내용
     * @return 생성된 댓글 객체를 담은 Flow
     */
    suspend fun createComment(token: String, objectId: Int, content: String, type: CommentType,): Flow<AuthResult<Comment>>

    /**
     * 기존 댓글을 수정합니다.
     *
     * @param token 사용자의 인증 토큰
     * @param type 댓글이 달린 객체의 종류
     * @param objectId 객체의 고유 ID
     * @param commentId 수정할 댓글의 ID
     * @param content 새로운 댓글 내용
     * @return 수정된 댓글 객체를 담은 Flow
     */
    suspend fun updateComment(token: String, objectId: Int, commentId: Int, content: String, type: CommentType,): Flow<AuthResult<Comment>>

    /**
     * 댓글을 삭제합니다.
     *
     * @param token 사용자의 인증 토큰
     * @param type 댓글이 달린 객체의 종류
     * @param objectId 객체의 고유 ID
     * @param commentId 삭제할 댓글의 ID
     * @return 작업 완료 결과를 담은 Flow
     */
    suspend fun deleteComment(token: String, objectId: Int, commentId: Int, type: CommentType,): Flow<AuthResult<Unit>>
}
