package com.sesac.domain.usecase.comment

import com.sesac.domain.type.CommentType
import com.sesac.domain.repository.CommentRepository
import javax.inject.Inject

class CreateCommentUseCase @Inject constructor(
    private val commentRepository: CommentRepository
) {
    suspend operator fun invoke(
        token: String,
        objectId: Int,
        content: String,
        type: CommentType,
    ) = commentRepository.createComment(token = token, objectId = objectId, content = content, type = type)
}
