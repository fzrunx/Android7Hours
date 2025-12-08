package com.sesac.common.model

import android.os.Parcel
import kotlinx.parcelize.Parceler
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import kotlinx.serialization.decodeFromString
import com.sesac.domain.model.PlaceCategory // Import PlaceCategory

object PlaceCategoryParceler : Parceler<PlaceCategory> {
    override fun create(parcel: Parcel): PlaceCategory {
        val jsonString = parcel.readString() ?: throw IllegalArgumentException("PlaceCategoryParceler: JSON string cannot be null")
        return Json.decodeFromString<PlaceCategory>(jsonString)
    }

    override fun newArray(size: Int): Array<PlaceCategory> {
        return Array(size) {
            PlaceCategory(
                id = -1, // Dummy ID
                name = "", // Dummy Name
                parent = null,
                parentCategory = null
            )
        }
    }

    override fun PlaceCategory.write(parcel: Parcel, flags: Int) {
        parcel.writeString(Json.encodeToString(this))
    }
}
