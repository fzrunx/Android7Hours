package com.sesac.domain.usecase.user

data class UserUseCase(
    val getAllUsers: GetUsersUseCase,
    val postUser: PostUserUseCase,
    val deleteUserUseCase: DeleteUserUseCase,
)