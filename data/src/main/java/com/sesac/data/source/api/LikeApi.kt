package com.sesac.data.source.api

import com.sesac.data.dto.LikeToggleDTO
import com.sesac.domain.model.Like
import retrofit2.http.Header
import retrofit2.http.POST
import retrofit2.http.Path

interface LikeApi {
    @POST("posts/{id}/like-toggle/")
    suspend fun togglePostLike(
        @Path("id") postId: Int
    ): LikeToggleDTO
}