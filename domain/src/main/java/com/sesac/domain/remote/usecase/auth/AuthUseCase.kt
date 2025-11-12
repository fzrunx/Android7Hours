package com.sesac.domain.remote.usecase.auth

data class AuthUseCase(
    val getAllUsers: GetAllUsersUseCase,
    val postUser: PostUserUseCase,
    val login: LoginUseCase,
)