package com.sesac.android7hours.di

import com.sesac.domain.repository.CommentRepository
import com.sesac.domain.usecase.comment.CommentUseCases
import com.sesac.domain.usecase.comment.CreateCommentUseCase
import com.sesac.domain.usecase.comment.DeleteCommentUseCase
import com.sesac.domain.usecase.comment.GetCommentsUseCase
import com.sesac.domain.usecase.comment.UpdateCommentUseCase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ActivityRetainedComponent
import dagger.hilt.android.scopes.ActivityRetainedScoped

@Module
@InstallIn(ActivityRetainedComponent::class)
object CommentUseCaseModule {

    @Provides
    @ActivityRetainedScoped
    fun provideCommentUseCases(repository: CommentRepository): CommentUseCases {
        return CommentUseCases(
            getCommentsUseCase = GetCommentsUseCase(repository),
            createCommentUseCase = CreateCommentUseCase(repository),
            updateCommentUseCase = UpdateCommentUseCase(repository),
            deleteCommentUseCase = DeleteCommentUseCase(repository)
        )
    }
}
