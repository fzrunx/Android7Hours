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
    @Json(name = "bookmarks_count")
    val bookmarksCount: Int,
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
data class PathUploadDto(
    @Json(name = "path_name") val pathName: String,
    @Json(name = "distance") val distance: Float,
    @Json(name = "duration") val duration: Int,

    // ✅ 좌표는 인코딩된 문자열로 전송 (95% 압축!)
    @Json(name = "polyline") val polyline: String,

    // 마커는 개수가 적으므로 일반 배열
    @Json(name = "markers") val markers: List<List<Any>>?
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
