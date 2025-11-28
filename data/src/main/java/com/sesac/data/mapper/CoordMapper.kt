package com.sesac.data.mapper

import android.location.Location
import com.sesac.data.dto.CoordDTO
import com.sesac.data.dto.CoordRequestDTO
import com.sesac.domain.model.Coord

object CoordMapper {
    fun CoordDTO.toCoord() = Coord(
        latitude = this.lat,
        longitude = this.lng,
        altitude = this.z,
    )

    fun Coord.toCoordDTO() = CoordDTO(
        lat = this.latitude,
        lng = this.longitude,
        z = this.altitude,
    )

//fun Coord.toNaverLocation() = LocationSource

    fun List<CoordDTO>.toCoordList(): List<Coord> =
        this.map { it.toCoord() }.toList()

    fun List<Coord>.toCoordDTOList(): List<CoordDTO> =
        this.map { it.toCoordDTO() }.toList()

    fun CoordRequestDTO.toCoord() = Coord(
        latitude = this.lat,
        longitude = this.lng,
    )

    fun Coord.toCoordRequestDTO() = CoordRequestDTO(
        lat = this.latitude,
        lng = this.longitude,
    )

    fun List<CoordRequestDTO>.toCoordFromRequestList() =
        this.map { it.toCoord() }.toList()

    fun List<Coord>.toCoordRequestDTOList(): List<CoordRequestDTO> =
        this.map { it.toCoordRequestDTO() }.toList()

    /**
     * Android Location을 Coord 데이터 클래스로 변환
     */
    fun Location.toCoord(): Coord {
        return Coord(
            latitude = this.latitude,
            longitude = this.longitude,
            altitude = if (this.hasAltitude()) this.altitude else null
        )
    }

    /**
     * Nullable Location을 Coord로 변환 (null인 경우 기본값 반환)
     */
    fun Location?.toCoordOrDefault(): Coord {
        return this?.toCoord() ?: Coord.DEFAULT
    }

    fun Coord.toLocation(coord: Coord, provider: String = "custom"): Location {
        return Location(provider).apply {
            latitude = coord.latitude
            longitude = coord.longitude
            coord.altitude?.let { altitude = it }
        }
    }

    fun Coord.toLocationOrNull(coord: Coord?, provider: String = "custom"): Location? {
        return coord?.let { toLocation(it, provider) }
    }
}
