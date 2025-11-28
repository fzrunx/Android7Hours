package com.sesac.data.mapper

import com.sesac.data.dto.PlaceCategoryDTO
import com.sesac.data.dto.PlaceDTO
import com.sesac.domain.model.Place
import com.sesac.domain.model.PlaceCategory

object PlaceMapper {

    fun PlaceDTO.toDomain(): Place {
        // rawData.coordinates가 유효하면 우선적으로 사용, 그렇지 않으면 기존의 문자열 파싱 방식을 사용
        val (lat, lng) = if (rawData?.coordinates != null) {
            rawData.coordinates.lat to rawData.coordinates.lng
        } else {
            parseCoordinates(coordinates)
        }

        // distance 파싱 (문자열을 Float로)
        val distanceKm = distance?.toFloatOrNull()

        // 우선순위에 따라 카테고리 선택 (category3 > category2 > category1)
        val primaryCategory = category3 ?: category2 ?: category1

        return Place(
            id = id,
            title = title ?: "",
            tel = tel,
            address = address,
            latitude = lat,
            longitude = lng,
            source = source,
            isActive = isActive,
            category = primaryCategory?.toDomain() ?: PlaceCategory(0, "미분류", null, null),
            distance = distanceKm,
            createdAt = createdAt,
            updatedAt = updatedAt,
            isBookmarked = isBookmarked ?: false
        )
    }

    fun PlaceCategoryDTO.toDomain(): PlaceCategory {
        return PlaceCategory(
            id = id,
            name = name,
            parent = parent?.id,
            parentCategory = parent?.toDomain()
        )
    }

    fun List<PlaceDTO>.toDomain(): List<Place> {
        return this.map { it.toDomain() }
    }

    private fun parseCoordinates(coordinates: Any?): Pair<Double, Double> {
        if (coordinates !is String || coordinates.isNullOrBlank()) {
            return 0.0 to 0.0
        }

        // WKT 형식 "SRID=4326;POINT (126.938733 37.48335374)" 파싱
        val regex = "POINT \\(([^ ]+) ([^ ]+)\\)".toRegex()
        val matchResult = regex.find(coordinates)

        return matchResult?.let {
            try {
                val lng = it.groupValues[1].toDouble()
                val lat = it.groupValues[2].toDouble()
                lat to lng // Pair<Latitude, Longitude> 반환
            } catch (e: NumberFormatException) {
                0.0 to 0.0
            }
        } ?: (0.0 to 0.0) // 매치되지 않으면 0.0, 0.0 반환
    }
}