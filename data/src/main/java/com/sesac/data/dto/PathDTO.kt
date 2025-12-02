package com.sesac.data.dto

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class PathDTO(
    val id: Int,
    val source: String,
    @Json(name = "auth_user_nickname")
    val authUserNickname: String,
    @Json(name = "path_name")
    val pathName: String,
    @Json(name = "path_comment")
    val pathComment: String?,
    val level: Int,
    val distance: Double,
    val duration: Int?,
    @Json(name = "is_private")
    val isPrivate: Boolean,
    val thumbnail: String?,
    val coords: List<CoordDTO>,
    @Json(name = "bookmark_count")
    val bookmarkCount: Int,
    @Json(name = "is_bookmarked")
    val isBookmarked: Boolean,
)

@JsonClass(generateAdapter = true)
data class PathCreateRequestDTO(
    @Json(name = "path_name")
    val pathName: String?,
    @Json(name = "path_comment")
    val pathComment: String?,
    val level: Int,
    val distance: Float,
    val duration: Int?,
    val isPrivate: Boolean = false,
    val thumbnail: String?,
    val coords: List<CoordRequestDTO>,
    @Json(name = "start_time")
    val startTime: String?,
    @Json(name = "end_time")
    val endTime: String?,
)

@JsonClass(generateAdapter = true)
data class PathUpdateRequestDTO(
    @Json(name = "path_name")
    val pathName: String?,
    @Json(name = "path_comment")
    val pathComment: String?,
    val level: Int,
    val distance: Float,
    val duration: Int?,
    val thumbnail: String?,
    @Json(name = "is_private")
    val isPrivate: Boolean = false,
)


@JsonClass(generateAdapter = true)
data class CoordDTO(
    val lat: Double,
    val lng: Double,
    val z: Double?
) {
    companion object {
        val EMPTY = CoordDTO(
            lat = 37.5295,
            lng = 126.96454,
            z = null,
        )
    }
}

@JsonClass(generateAdapter = true)
data class CoordRequestDTO(
    val lat: Double,
    val lng: Double
) {
    companion object {
        val EMPTY = CoordRequestDTO(
            lat = 37.5295,
            lng = 126.96454,
        )
    }
}
