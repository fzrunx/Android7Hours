package com.sesac.domain.usecase.comment

data class CommentUseCase(
    val getCommentsUseCase: GetCommentsUseCase,
    val createCommentUseCase: CreateCommentUseCase,
    val updateCommentUseCase: UpdateCommentUseCase,
    val deleteCommentUseCase: DeleteCommentUseCase
)