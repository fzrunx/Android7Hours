package com.sesac.domain.usecase.comment

import com.sesac.domain.repository.CommentRepository
import javax.inject.Inject

class CreateCommentUseCase @Inject constructor(
    private val commentRepository: CommentRepository
) {
    suspend operator fun invoke(token: String, type: String, objectId: Int, content: String) =
        commentRepository.createComment(token, type, objectId, content)
}
