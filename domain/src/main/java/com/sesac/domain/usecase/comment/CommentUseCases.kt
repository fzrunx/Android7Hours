package com.sesac.domain.usecase.comment

import javax.inject.Inject

data class CommentUseCases @Inject constructor(
    val getCommentsUseCase: GetCommentsUseCase,
    val createCommentUseCase: CreateCommentUseCase,
    val updateCommentUseCase: UpdateCommentUseCase,
    val deleteCommentUseCase: DeleteCommentUseCase
)
