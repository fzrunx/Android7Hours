package com.sesac.domain.usecase.session

data class SessionUseCase(
    val getAccessToken: GetAccessTokenUseCase,
    val getRefreshToken: GetRefreshTokenUseCase,
    val getUserInfo: GetUserInfoUseCase,
    val saveSession: SaveSessionUseCase,
    val saveUser: SaveUserUseCase,
    val clearSession: ClearSessionUseCase,
)
