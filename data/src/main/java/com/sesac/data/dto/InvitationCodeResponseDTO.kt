package com.sesac.data.dto

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class InvitationCodeResponseDTO(
    @Json(name = "code")
    val code: String
)