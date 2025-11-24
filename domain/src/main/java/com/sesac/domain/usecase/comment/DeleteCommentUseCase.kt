package com.sesac.domain.usecase.comment

import com.sesac.domain.repository.CommentRepository
import javax.inject.Inject

class DeleteCommentUseCase @Inject constructor(
    private val commentRepository: CommentRepository
) {
    suspend operator fun invoke(token: String, type: String, objectId: Int, commentId: Int) =
        commentRepository.deleteComment(token, type, objectId, commentId)
}
