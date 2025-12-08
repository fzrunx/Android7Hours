package com.sesac.data.dto

sealed interface LikedObject {
    val id: Int // Common identifier for liked items
    val type: String // Moshi 식별자

}
