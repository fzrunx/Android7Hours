package com.sesac.domain.model

data class AuthorSummary(
    val id: Int?,
    val nickname: String,
    val profileImageUrl: String? = null
) {
    constructor(user: User) : this(
        id = user.id,
        nickname = user.nickname ?: "",
        profileImageUrl = null
    )
    companion object {
        val EMPTY = AuthorSummary(
            id = -1,
            nickname = "",
            profileImageUrl = null
        )
    }
}