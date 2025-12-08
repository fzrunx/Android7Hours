package com.sesac.data.mapper

import com.sesac.data.entity.PathEntity
import com.sesac.data.type.DraftStatus
import com.sesac.domain.model.Coord
import com.sesac.domain.model.MemoMarker
import com.sesac.domain.model.Path
import com.squareup.moshi.Moshi
import com.squareup.moshi.Types
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory

fun Path.toPathEntity(): PathEntity {
    val moshi = Moshi.Builder()
        .add(KotlinJsonAdapterFactory())
        .build()

    val coordListAdapter = moshi.adapter<List<Coord>>(
        Types.newParameterizedType(List::class.java, Coord::class.java)
    )
    val geomJson = coordListAdapter.toJson(this.coord ?: emptyList()) // ✅ nullable 처리

    val markerListAdapter = moshi.adapter<List<MemoMarker>>(
        Types.newParameterizedType(List::class.java, MemoMarker::class.java)
    )
    val markersJson = markerListAdapter.toJson(this.markers ?: emptyList()) // ✅ nullable 처리

    return PathEntity(
        id = if (this.id == -1) 0 else this.id,
        pathName = this.pathName,
        pathComment = this.pathComment,
        markers = markersJson,
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
        geom = geomJson,
        authUserId = 0,
        status = DraftStatus.DRAFT
    )
}

fun PathEntity.toUserPath(): Path {
    val moshi = Moshi.Builder()
        .add(KotlinJsonAdapterFactory())
        .build()

    val coordListAdapter = moshi.adapter<List<Coord>>(
        Types.newParameterizedType(List::class.java, Coord::class.java)
    )
    val coords = if (this.geom.isNotEmpty()) {
        coordListAdapter.fromJson(this.geom) ?: emptyList()
    } else {
        emptyList()
    }

    val markerListAdapter = moshi.adapter<List<MemoMarker>>(
        Types.newParameterizedType(List::class.java, MemoMarker::class.java)
    )
    val markers = if (!this.markers.isNullOrEmpty()) {
        markerListAdapter.fromJson(this.markers) ?: emptyList()
    } else {
        emptyList()
    }

    return Path(
        id = this.id,
        uploader = "", // ✅ PathEntity에 없으면 빈 문자열
        pathName = this.pathName,
        pathComment = this.pathComment,
        level = when (this.level) {
            1 -> "초급"
            2 -> "중급"
            3 -> "고급"
            else -> null // ✅ nullable 처리
        },
        distance = this.distance ?: 0f,
        duration = this.duration ?: 0,
        isPrivate = this.isPrivate,
        thumbnail = this.thumbnail,
        coord = coords, // ✅ List<Coord>
        markers = markers, // ✅ List<MemoMarker>
        tags = emptyList(), // ✅ PathEntity에 tags 없으면 빈 리스트
        bookmarkCount = 0, // ✅ 필수 필드 추가
        isBookmarked = false, // ✅ 필수 필드 추가
        likes = 0,
        distanceFromMe = 0f
    )
}