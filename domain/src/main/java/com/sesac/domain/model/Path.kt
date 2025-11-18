package com.sesac.domain.model

import java.util.Date

data class RecommendedPath(
    val id: Int,
    val name: String,
    val distance: String,
    val time: String,
    val rating: Double,
    val reviews: Int,
    val color: Any
)

data class Path(
    val id: Int,
    val createdAt: Date = Date(System.currentTimeMillis()),
    val updatedAt: Date = Date(System.currentTimeMillis()),
    val pathName: String,
    val pathComment: String?,
    val level: Int = 2,
    val distance: Float?,
    val duration: Int,
    val isPrivate: Boolean? = true,
    val thumbnail: String = "",
    val geom: String = "",
    val authUserId: Int,
    ) {
    companion object {
        val EMPTY = Path(
            id = -1,
            createdAt = Date(System.currentTimeMillis()),
            updatedAt = Date(System.currentTimeMillis()),
            pathName = "",
            pathComment = null,
            level = 2,
            distance = 0f,
            duration = 0,
            isPrivate = true,
            thumbnail = "",
            geom = "",
            authUserId = -1,
        )
    }
}

data class UserPath(
    val id: Int,
    val name: String,
    val uploader: String,
    val distance: Float = 0f,
    val time: Int = 0,
    val likes: Int = 0,
    val distanceFromMe: Float = 0f, // React의 distance_from_me
    val coord: List<Coord>? = null,
    val tags: List<String> = emptyList(),
    val difiiculty: String? = null,
    val description: String? = null,
    val isPrivate: Boolean = false,
    val thumbnail: String? = null,
) {
    companion object {
        val EMPTY = UserPath(
            id = -1,
            name = "",
            uploader = "",
            distance = 0f,
            time = 0,
            likes = 0,
            distanceFromMe = 0f,
            coord = null,
            tags = emptyList(),
            difiiculty = null,
            isPrivate = false,
            thumbnail = "",
        )
    }

    fun toMyRecord(): MyRecord = MyRecord(id = -1, name = name, distance = distance)
}

data class MyRecord(
    val id: Int,
    val name: String,
    val date: Date = Date(System.currentTimeMillis()),
    val distance: Float = 0f,
    val time: Int = 0,
    val steps: Int = 0,
    val calories: Int = 0,
    val color: Any? = null,
)
