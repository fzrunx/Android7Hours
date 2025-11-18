package com.sesac.data.mapper

import com.sesac.data.dto.CoordDTO
import com.sesac.data.dto.CoordRequestDTO
import com.sesac.domain.model.Coord

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