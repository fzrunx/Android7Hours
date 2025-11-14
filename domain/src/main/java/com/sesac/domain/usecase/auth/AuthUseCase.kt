package com.sesac.domain.usecase.auth

data class AuthUseCase(
    val getAllUsers: GetAllUsersUseCase,
    val postUser: PostUserUseCase,
    val deleteUserUseCase: DeleteUserUseCase,
    val login: LoginUseCase
)