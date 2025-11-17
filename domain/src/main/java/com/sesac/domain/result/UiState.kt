package com.sesac.domain.result

sealed interface JoinUiState {
    object Idle : JoinUiState
    object Loading : JoinUiState
    data class Success(val message: String) : JoinUiState
    data class Error(val message: String) : JoinUiState
}

data class AuthUiState(
    val isLoggedIn: Boolean = false,
    val token: String? = null,
    val id: Int = -1,
    val nickname: String? = null, // Add other user info if needed
    val email: String? = null,
    val fullName: String? = null,
) {
    fun reset() = AuthUiState()
}