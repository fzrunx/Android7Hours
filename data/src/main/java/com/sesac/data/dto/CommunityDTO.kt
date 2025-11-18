package com.sesac.data.dto

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class CommunityDTO(
    val address: String,
    val category: Category,
    val coordinates: String,
    @Json(name = "created_at")
    val createdAt: String,
    val distance: Any,
    val id: Int,
    @Json(name = "is_active")
    val isActive: Boolean,
    val source: String,
    val tel: String,
    val title: String,
    @Json(name = "updated_at")
    val updatedAt: String
)

@JsonClass(generateAdapter = true)
data class Category(
    val id: Int,
    val name: String,
    val parent: Int,
    @Json(name = "parent_category")
    val parentCategory: ParentCategory
)

@JsonClass(generateAdapter = true)
data class ParentCategory(
    val id: Int,
    val name: String
)