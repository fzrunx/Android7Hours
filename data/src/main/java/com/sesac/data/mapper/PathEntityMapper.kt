package com.sesac.data.mapper

import com.sesac.data.entity.PathEntity
import com.sesac.data.type.DraftStatus
import com.sesac.domain.model.Coord
import com.sesac.domain.model.Path

fun Path.toPathEntity() = PathEntity(
    id = if (this.id == -1) 0 else this.id,
    pathName = this.pathName,
    pathComment = this.pathComment,
    level = when (this.level) {
        "초급" -> 1
        "중급" -> 2
        "고급" -> 3
        else -> 0
    },
    distance = this.distance,
    duration = this.duration,
    isPrivate = this.isPrivate,
    thumbnail = this.thumbnail ?: "",
    geom = "", // 필요하면 좌표 JSON으로 저장
    authUserId = 0, // 필요하면 유저 ID 넣기
    status = DraftStatus.DRAFT
)

fun PathEntity.toUserPath() = Path(
    id = this.id,
    pathName = this.pathName,
    pathComment = this.pathComment,
    level = when(this.level) {
        1 -> "초급"
        2 -> "중급"
        3 -> "고급"
        else -> "알 수 없음"
    },
    distance = this.distance ?: 0f,
    duration = this.duration,
    isPrivate = this.isPrivate,
    thumbnail = this.thumbnail,
    uploader = "",
    coord = listOf(Coord()), // 필요하면 좌표 JSON으로 저장
//    authUserId = this.authUserId,
    bookmarkCount = this.bookmarksCount,
    isBookmarked = this.isBookmarked,
)