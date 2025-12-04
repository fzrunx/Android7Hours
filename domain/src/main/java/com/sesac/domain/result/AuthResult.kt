package com.sesac.domain.result

sealed class AuthResult<out T> {
    data object NoConstructor : AuthResult<Nothing>()
    data object Loading : AuthResult<Nothing>()

    data class Success<T>(val resultData: T) : AuthResult<T>()

    data class NetworkError(val exception: Throwable) : AuthResult<Nothing>()
//    data class RoomDBError(val exception: Throwable) : AuthResult<Nothing>()
}