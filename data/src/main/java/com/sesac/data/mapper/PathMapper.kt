package com.sesac.data.mapper

import com.sesac.data.dto.PathDTO
import com.sesac.data.dto.CoordRequestDTO
import com.sesac.data.dto.PathCreateRequestDTO
import com.sesac.data.dto.PathUpdateRequestDTO
import com.sesac.domain.model.Path

fun PathDTO.toPath() = Path(
    id = this.id,
    uploader = this.authUserNickname,
    pathName = this.pathName,
    pathComment = this.pathComment,
    level = when(this.level) {
        1 -> "초급"
        2 -> "중급"
        3 -> "고급"
        else -> "알 수 없음"
    },
    distance = this.distance.toFloat(),
    duration = this.duration ?: 0,
    isPrivate = this.isPrivate,
    thumbnail = this.thumbnail,
    coord = this.coords.toCoordList(),
    bookmarkCount = this.bookmarkCount,
    isBookmarked = this.isBookmarked,
    likes = 0,
    distanceFromMe = 0f,
    tags = emptyList(),
)

fun Path.toPathCreateRequestDTO() = PathCreateRequestDTO(
    pathName = this.pathName,
    pathComment = this.pathComment,
    level = when(level) {
        "초급" -> 1
        "중급" -> 2
        "고급" -> 3
        else -> 0
    },
    distance = this.distance,
    duration = this.duration,
    coords = this.coord?.toCoordRequestDTOList() ?: listOf(CoordRequestDTO.EMPTY),
    startTime = null, // Not available in UserPath
    endTime = null, // Not available in UserPath
    thumbnail = this.thumbnail,
    isPrivate = this.isPrivate,
)

fun Path.toPathUpdateRequestDTO(): PathUpdateRequestDTO {
    return PathUpdateRequestDTO(
        pathName = this.pathName,
        pathComment = this.pathComment,
        level = when(level) {
            "초급" -> 1
            "중급" -> 2
            "고급" -> 3
            else -> 0
        },
        distance = this.distance,
        duration = this.duration,
        thumbnail = this.thumbnail,
        isPrivate = this.isPrivate,
    )
}

fun List<PathDTO>.toPathList() =
    this.map { it.toPath() }.toList()

fun List<Path>.toPathCreateRequestDTOList() =
    this.map { it.toPathCreateRequestDTO() }.toList()

fun List<Path>.toPathUpdateRequestDTOList() =
    this.map { it.toPathUpdateRequestDTO() }.toList()