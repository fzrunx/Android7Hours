package com.sesac.common.model

import android.os.Parcelable
import com.sesac.domain.model.Place
import com.sesac.domain.model.PlaceCategory
import kotlinx.parcelize.Parcelize
import kotlinx.serialization.Serializable
import kotlinx.parcelize.TypeParceler

@Parcelize
@Serializable
@TypeParceler<PlaceCategory, PlaceCategoryParceler>()
data class PlaceParceler(
    val id: Int,
    val title: String,
    val tel: String?,
    val address: String?,
    val latitude: Double,
    val longitude: Double,
    val source: String?,
    val isActive: Boolean,
    val category: PlaceCategory,
    val distance: Float?,
    val createdAt: String,
    val updatedAt: String,
    val isBookmarked: Boolean,
    val imageUrl: String?,
    val description: String?,
    val operatingHours: String?,
    val rating: Float,
    val reviewCount: Int,
    val tags: List<String>
) : Parcelable

// ✅ Place → PlaceParceler 변환
fun Place.toParceler(): PlaceParceler {
    return PlaceParceler(
        id = id,
        title = title,
        tel = tel,
        address = address,
        latitude = latitude,
        longitude = longitude,
        source = source,
        isActive = isActive,
        category = category,
        distance = distance,
        createdAt = createdAt,
        updatedAt = updatedAt,
        isBookmarked = isBookmarked,
        imageUrl = imageUrl,
        description = description,
        operatingHours = operatingHours,
        rating = rating,
        reviewCount = reviewCount,
        tags = tags
    )
}

// ✅ PlaceParceler → Place 변환
fun PlaceParceler.toPlace(): Place {
    return Place(
        id = id,
        title = title,
        tel = tel,
        address = address,
        latitude = latitude,
        longitude = longitude,
        source = source,
        isActive = isActive,
        category = category,
        distance = distance,
        createdAt = createdAt,
        updatedAt = updatedAt,
        isBookmarked = isBookmarked,
        imageUrl = imageUrl,
        description = description,
        operatingHours = operatingHours,
        rating = rating,
        reviewCount = reviewCount,
        tags = tags
    )
}