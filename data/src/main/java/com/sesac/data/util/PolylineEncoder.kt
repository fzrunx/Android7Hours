package com.sesac.data.util

import com.google.maps.internal.PolylineEncoding
import com.naver.maps.geometry.LatLng

object PolylineEncoder {
    // ✅ List<LatLng> → 압축된 문자열
    fun encode(coords: List<LatLng>): String {
        val points = coords.map {
            com.google.maps.model.LatLng(it.latitude, it.longitude)
        }
        return PolylineEncoding.encode(points)
    }

    // ✅ 압축된 문자열 → List<LatLng>
    fun decode(encoded: String): List<LatLng> {
        return PolylineEncoding.decode(encoded).map {
            LatLng(it.lat, it.lng)
        }
    }
}