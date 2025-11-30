package com.sesac.data.dto

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class Coordinates(
    val lat: Double,
    val lng: Double
)
@JsonClass(generateAdapter = true)
data class PlaceDTO(
    val id: Int,
    val title: String?,
    val tel: String?,
    val address: String?,
    val coordinates: Any?, // "latitude,longitude" 형식
    val source: String?,
    @Json(name = "is_active")
    val isActive: Boolean,
    val category1: PlaceCategoryDTO?,
    val category2: PlaceCategoryDTO?,
    val category3: PlaceCategoryDTO?,
    @Json(name = "raw_data")
    val rawData: RawData?,
    @Json(name = "created_at")
    val createdAt: String,
    @Json(name = "is_bookmarked") // ✨ 추가
    val isBookmarked: Boolean?, // ✨ 추가
    @Json(name = "updated_at")
    val updatedAt: String,
    val distance: String? // readOnly, 서버에서 계산된 거리
)

@JsonClass(generateAdapter = true)
data class PlaceCategoryDTO(
    val id: Int,
    val name: String,
    val parent: PlaceCategoryDTO? = null // 중첩된 상위 카테고리 정보
)

@JsonClass(generateAdapter = true)
data class RawData(
    val coordinates: Coordinates? // 서버 응답이 명확하다면 JsonElement 대신 실제 데이터 타입 사용 권장 (Coordinates? 또는 String?)
)