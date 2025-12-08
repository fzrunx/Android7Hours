package com.sesac.domain.model

data class Comment(
    val id: Int,
    val objectId: Int, // The ID of the post or other object it belongs to
    val authorId: Int,
    val authorNickName: String,
    val content: String,
    val createdAt: String, // 'yyyy-MM-dd'T'HH:mm:ss'Z''
    val timeAgo: String?  = "방금 전",
    val authorImage: String?= "https://img.icons8.com/?size=100&id=Fx70T4fgtNmt&format=png&color=000000",
) {
    companion object {
        val EMPTY = Comment(
            id = -1,
            objectId = -1,
            authorId = -1,
            authorNickName = "",
            content = "",
            createdAt = "2000-01-01 01:00:00",
            timeAgo  = "방금 전",
            authorImage = "https://img.icons8.com/?size=100&id=Fx70T4fgtNmt&format=png&color=000000",
        )
    }
}
