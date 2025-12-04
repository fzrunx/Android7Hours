package com.sesac.data.mapper

import com.sesac.data.entity.PathEntity
import com.sesac.data.type.DraftStatus
import com.sesac.domain.model.Path
import java.util.Date

//fun Path.toPathEntity(): PathEntity {
//    return PathEntity(
//        id = if (this.id == -1) 0 else this.id,
//        pathName = this.pathName,
//        pathComment = null,
//        markers = null,
//        level = 0,
//        distance = this.distance,
//        duration = this.duration,
//        isPrivate = true,
//        thumbnail = "",
//        geom = "",
//        authUserId = 0, // 필요하면 유저 ID 넣기
//        createdAt = System.currentTimeMillis(),
//        updatedAt = System.currentTimeMillis(),
//        status = DraftStatus.RECORD,
//    )
//}
//
//fun PathEntity.toMyRecord(): Path {
//    return MyRecord(
//        id = this.id,
//        name = this.pathName,
//        date = Date(this.createdAt),
//        distance = this.distance ?: 0f,
//        time = this.duration,
//    )
//}
