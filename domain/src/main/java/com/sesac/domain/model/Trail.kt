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

data class UserPath(
    val id: Int,
    val name: String,
    val uploader: String,
    val distance: Float = 0f,
    val time: Int = 0,
    val likes: Int = 0,
    val distanceFromMe: Float = 0f, // React의 distance_from_me
    val latLngPoint: LatLngPoint? = null,
    val tags: List<String> = emptyList(),
    val difiiculty: String? = null,
    val description: String? = null,
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
            latLngPoint = null,
            tags = emptyList(),
            difiiculty = null
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
) {
    fun toUserPath(): UserPath = UserPath(id = -1, name = name, uploader = "", distance = distance)
}
