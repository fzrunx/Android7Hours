package com.sesac.domain.usecase.post

data class PostUseCase(
    val getPostListUseCase: GetPostListUseCase,
    val getMyPostsUseCase: GetMyPostsUseCase,
    val getPostDetailUseCase: GetPostDetailUseCase,
    val createPostUseCase: CreatePostUseCase,
    val updatePostUseCase: UpdatePostUseCase,
    val deletePostUseCase: DeletePostUseCase
)
