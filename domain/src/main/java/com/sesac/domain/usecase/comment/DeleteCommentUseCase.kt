package com.sesac.domain.usecase.comment

import com.sesac.domain.model.CommentType
import com.sesac.domain.repository.CommentRepository
import javax.inject.Inject

class DeleteCommentUseCase @Inject constructor(
    private val commentRepository: CommentRepository
) {
    suspend operator fun invoke(
        token: String,
        objectId: Int,
        commentId: Int,
        type: CommentType,
    ) = commentRepository.deleteComment(token = token, objectId = objectId, commentId = commentId, type = type)
}
