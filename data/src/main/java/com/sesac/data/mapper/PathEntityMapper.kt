package com.sesac.data.mapper

import com.sesac.data.entity.PathEntity
import com.sesac.data.type.DraftStatus
import com.sesac.domain.model.Coord
import com.sesac.domain.model.MemoMarker
import com.sesac.domain.model.UserPath
import com.squareup.moshi.Moshi
import com.squareup.moshi.Types
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory

fun UserPath.toPathEntity(): PathEntity {
    val moshi = Moshi.Builder()
        .add(KotlinJsonAdapterFactory())
        .build()

    val coordListAdapter = moshi.adapter<List<Coord>>(
        Types.newParameterizedType(List::class.java, Coord::class.java)
    )
    val geomJson = coordListAdapter.toJson(this.coord)

    val markerListAdapter = moshi.adapter<List<MemoMarker>>(
        Types.newParameterizedType(List::class.java, MemoMarker::class.java)
    )
    val markersJson = markerListAdapter.toJson(this.markers)


    return PathEntity(
        id = if (this.id == -1) 0 else this.id,
        pathName = this.name,
        pathComment = this.description,
        markers = markersJson,
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
        geom = geomJson, // 좌표 JSON으로 저장
        authUserId = 0, // 필요하면 유저 ID 넣기
        status = DraftStatus.DRAFT
    )
}


fun PathEntity.toUserPath(): UserPath {
    val moshi = Moshi.Builder()
        .add(KotlinJsonAdapterFactory())
        .build()

    val coordListAdapter = moshi.adapter<List<Coord>>(
        Types.newParameterizedType(List::class.java, Coord::class.java)
    )
    val coords = if (this.geom.isNotEmpty()) coordListAdapter.fromJson(this.geom) else emptyList()

    val markerListAdapter = moshi.adapter<List<MemoMarker>>(
        Types.newParameterizedType(List::class.java, MemoMarker::class.java)
    )
    val markers = if (!this.markers.isNullOrEmpty()) markerListAdapter.fromJson(this.markers) else emptyList()


    return UserPath(
        id = this.id,
        name = this.pathName,
        coord = coords,
        markers = markers,
        description = this.pathComment,
        difiiculty = when (this.level) {
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
}