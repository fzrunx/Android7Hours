package com.sesac.domain.model

import kotlinx.serialization.Serializable

@Serializable
data class Place(
    val id: Int,
    val title: String,
    val tel: String?,
    val address: String?,
    val latitude: Double,
    val longitude: Double,
    val source: String?,
    val isActive: Boolean,
    val category: PlaceCategory,
    val distance: Float?, // km 단위
    val createdAt: String,
    val updatedAt: String,
    val isBookmarked: Boolean,

    // UI용 추가 필드
    val imageUrl: String? = null,
    val description: String? = null,
    val operatingHours: String? = null,
    val rating: Float = 0f,
    val reviewCount: Int = 0,
    val tags: List<String> = emptyList(),

)

@Serializable
data class PlaceCategory(
    val id: Int,
    val name: String,
    val parent: Int?,
    val parentCategory: PlaceCategory?
)