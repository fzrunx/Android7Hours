package com.sesac.domain.result

sealed interface JoinUiState {
    object Idle : JoinUiState
    object Loading : JoinUiState
    data class Success(val message: String) : JoinUiState
    data class Error(val message: String) : JoinUiState
}

sealed class ResponseUiState<out T> {
    object Idle : ResponseUiState<Nothing>()
    object Loading : ResponseUiState<Nothing>()
    data class Success<T>(val message: String, val result: T) : ResponseUiState<T>()
    data class Error(val message: String) : ResponseUiState<Nothing>()
}

data class AuthUiState(
    val isLoggedIn: Boolean = false,
    val token: String? = null,
    val id: Int = -1,
    val nickname: String? = null, // Add other user info if needed
    val email: String? = null,
    val fullName: String? = null,
    val profileImageUrl: String? = null,
) {
    fun reset() = AuthUiState()
}