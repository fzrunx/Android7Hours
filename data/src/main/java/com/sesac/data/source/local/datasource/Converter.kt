package com.sesac.data.source.local.datasource

import androidx.room.TypeConverter
import com.sesac.data.type.DraftStatus
import com.sesac.domain.model.Coord
import com.squareup.moshi.Types
import com.squareup.moshi.Moshi
import java.util.Date

class Converters {
    private val moshi = Moshi.Builder().build()
    private val type = Types.newParameterizedType(List::class.java, Coord::class.java)
    private val adapter = moshi.adapter<List<Coord>>(type)
    @TypeConverter
    fun fromDraftStatus(status: DraftStatus) = status.name

    @TypeConverter
    fun toDraftStatus(value: String) = DraftStatus.valueOf(value)

    @TypeConverter
    fun fromDate(date: Date) = date.time

    @TypeConverter
    fun toDate(time: Long) = Date(time)

    // Coord List <-> JSON String 변환
    @TypeConverter
    fun fromCoordList(coords: List<Coord>?) = coords?.let { adapter.toJson(it) }

    @TypeConverter
    fun toCoordList(json: String?) = json?.let { adapter.fromJson(it) }
}
