package com.sesac.data.source.api

import com.sesac.data.dto.DiaryDTO
import com.sesac.data.dto.DiaryRequestDTO
import retrofit2.http.Body
import retrofit2.http.POST

interface DiaryApi {
    @POST("generate-diary")
    suspend fun generateDiary(
        @Body data: DiaryRequestDTO
    ): DiaryDTO
}