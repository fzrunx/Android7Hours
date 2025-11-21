package com.sesac.domain.model

import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class Coord(
    val latitude: Double = 0.0,
    val longitude: Double = 0.0,
    val altitude: Double? = null,
) {
    companion object {
        val DEFAULT = Coord(
//            latitude = 37.52952,
//            longitude = 126.9645,
            latitude = 35.0996,
            longitude = 129.1237,
        )
    }
}