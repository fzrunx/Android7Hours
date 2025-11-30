package com.sesac.common.model

import android.net.Uri
import android.os.Bundle
import androidx.navigation.NavType
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import kotlinx.serialization.decodeFromString

// ✅ serializeAsValue를 오버라이드하여 URL 인코딩 처리
val PlaceParcelerNavType = object : NavType<PlaceParceler>(isNullableAllowed = false) {

    override fun get(bundle: Bundle, key: String): PlaceParceler? {
        val jsonString = bundle.getString(key)
        return jsonString?.let {
            try {
                Json.decodeFromString<PlaceParceler>(it)
            } catch (e: Exception) {
                null
            }
        }
    }

    override fun parseValue(value: String): PlaceParceler {
        // URL 디코딩 후 JSON 파싱
        val decoded = Uri.decode(value)
        return Json.decodeFromString(decoded)
    }

    override fun serializeAsValue(value: PlaceParceler): String {
        // JSON으로 인코딩 후 URL 인코딩
        val jsonString = Json.encodeToString(value)
        return Uri.encode(jsonString)
    }

    override fun put(bundle: Bundle, key: String, value: PlaceParceler) {
        bundle.putString(key, Json.encodeToString(value))
    }
}
