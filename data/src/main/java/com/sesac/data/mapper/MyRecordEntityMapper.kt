package com.sesac.data.mapper

import com.sesac.data.entity.PathEntity
import com.sesac.data.type.DraftStatus
import com.sesac.domain.model.MyRecord
import java.util.Date

fun MyRecord.toPathEntity(): PathEntity {
    return PathEntity(
        id = if (this.id == -1) 0 else this.id,
        pathName = this.name,
        pathComment = null,
        markers = null,
        level = 0,
        distance = this.distance,
        duration = this.time,
        isPrivate = true,
        thumbnail = "",
        geom = "",
        authUserId = 0, // 필요하면 유저 ID 넣기
        createdAt = this.date.time,
        updatedAt = this.date.time,
        status = DraftStatus.RECORD,
    )
}

fun PathEntity.toMyRecord(): MyRecord {
    return MyRecord(
        id = this.id,
        name = this.pathName,
        date = Date(this.createdAt),
        distance = this.distance ?: 0f,
        time = this.duration,
    )
}
