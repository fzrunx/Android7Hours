package com.sesac.data.mapper

import com.sesac.data.dto.PathDTO
import com.sesac.data.dto.CoordRequestDTO
import com.sesac.data.dto.PathCreateRequestDTO
import com.sesac.data.dto.PathUpdateRequestDTO
import com.sesac.domain.model.UserPath

fun PathDTO.toUserPath() = UserPath(
    id = this.id,
    name = this.pathName,
    uploader = this.authUserNickname,
    distance = this.distance.toFloat(),
    time = this.duration ?: 0,
    likes = 0,
    distanceFromMe = 0f,
    coord = this.coords.toCoordList(),
    tags = emptyList(),
    difiiculty = when(this.level) {
        1 -> "초급"
        2 -> "중급"
        3 -> "고급"
        else -> "알 수 없음"
    },
    description = this.pathComment,
    isPrivate = this.isPrivate,
)

fun UserPath.toPathCreateRequestDTO() = PathCreateRequestDTO(
    pathName = this.name,
    pathComment = this.description,
    level = when(difiiculty) {
        "초급" -> 1
        "중급" -> 2
        "고급" -> 3
        else -> 0
    },
    distance = this.distance,
    duration = this.time,
    coords = this.coord?.toCoordRequestDTOList() ?: listOf(CoordRequestDTO.EMPTY),
    startTime = null, // Not available in UserPath
    endTime = null, // Not available in UserPath
    thumbnail = this.thumbnail,
    isPrivate = this.isPrivate,
)

fun UserPath.toPathUpdateRequestDTO(): PathUpdateRequestDTO {
    return PathUpdateRequestDTO(
        pathName = this.name,
        pathComment = this.description,
        level = when(difiiculty) {
            "초급" -> 1
            "중급" -> 2
            "고급" -> 3
            else -> 0
        },
        distance = this.distance,
        duration = this.time,
        thumbnail = this.thumbnail,
        isPrivate = this.isPrivate,
    )
}

fun List<PathDTO>.toUserPathList() =
    this.map { it.toUserPath() }.toList()

fun List<UserPath>.toPathCreateRequestDTOList() =
    this.map { it.toPathCreateRequestDTO() }.toList()

fun List<UserPath>.toPathUpdateRequestDTOList() =
    this.map { it.toPathUpdateRequestDTO() }.toList()

