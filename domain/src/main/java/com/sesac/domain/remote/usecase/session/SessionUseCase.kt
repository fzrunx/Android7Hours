package com.sesac.domain.remote.usecase.session

data class SessionUseCase(
    val getAccessToken: GetAccessTokenUseCase,
    val getRefreshToken: GetRefreshTokenUseCase,
    val getUserInfo: GetUserInfoUseCase,
    val saveSession: SaveSessionUseCase,
    val clearSession: ClearSessionUseCase,
)
