package com.sesac.domain.usecase.comment

import com.sesac.domain.type.CommentType
import com.sesac.domain.repository.CommentRepository
import javax.inject.Inject

class GetCommentsUseCase @Inject constructor(
    private val commentRepository: CommentRepository
) {
    suspend operator fun invoke(
        objectId: Int,
        type: CommentType,
    ) = commentRepository.getComments(objectId = objectId, type = type)
}
