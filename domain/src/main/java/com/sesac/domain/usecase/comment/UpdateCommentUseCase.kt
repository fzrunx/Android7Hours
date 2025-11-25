package com.sesac.domain.usecase.comment

import com.sesac.domain.model.CommentType
import com.sesac.domain.repository.CommentRepository
import javax.inject.Inject

class UpdateCommentUseCase @Inject constructor(
    private val commentRepository: CommentRepository
) {
    suspend operator fun invoke(
        token: String,
        objectId: Int,
        commentId: Int,
        content: String,
        type: CommentType,
    ) = commentRepository.updateComment(
        token = token,
        objectId = objectId,
        commentId = commentId,
        content = content,
        type = type,
        )
}
