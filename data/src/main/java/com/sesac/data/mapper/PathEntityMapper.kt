package com.sesac.data.mapper

import com.sesac.data.entity.PathEntity
import com.sesac.data.type.DraftStatus
import com.sesac.domain.model.UserPath

fun UserPath.toPathEntity() = PathEntity(
    id = this.id,
    pathName = this.name,
    pathComment = this.description,
    level = when (this.difiiculty) {
        "초급" -> 1
        "중급" -> 2
        "고급" -> 3
        else -> 0
    },
    distance = this.distance,
    duration = this.time,
    isPrivate = this.isPrivate,
    thumbnail = this.thumbnail ?: "",
    geom = "", // 필요하면 좌표 JSON으로 저장
    authUserId = 0, // 필요하면 유저 ID 넣기
    status = DraftStatus.DRAFT
)

fun PathEntity.toUserPath() = UserPath(
    id = this.id,
    name = this.pathName,
    description = this.pathComment,
    difiiculty = when(this.level) {
        1 -> "초급"
        2 -> "중급"
        3 -> "고급"
        else -> "알 수 없음"
    },
    distance = this.distance ?: 0f,
    time = this.duration,
    isPrivate = this.isPrivate,
    thumbnail = this.thumbnail,
    uploader = ""
)