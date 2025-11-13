package com.sesac.domain.local.model

data class CommonUiState(
    val isLoggedIn: Boolean = false,
    val nickname: String? = null, // Add other user info if needed
    val email: String? = null,
    val fullName: String? = null,
)