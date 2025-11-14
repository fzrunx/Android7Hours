package com.sesac.domain.model

data class CommonAuthUiState(
    val isLoggedIn: Boolean = false,
    val token: String? = null,
    val id: Int = -1,
    val nickname: String? = null, // Add other user info if needed
    val email: String? = null,
    val fullName: String? = null,
)