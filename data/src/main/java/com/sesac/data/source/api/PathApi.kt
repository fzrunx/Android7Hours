package com.sesac.data.source.api

import com.sesac.data.dto.BookmarkResponseDTO
import com.sesac.data.dto.CommentDTO
import com.sesac.data.dto.CommentRequestDTO
import com.sesac.data.dto.PathDTO
import com.sesac.data.dto.PathCreateRequestDTO
import com.sesac.data.dto.PathUpdateRequestDTO
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.PATCH
import retrofit2.http.POST
import retrofit2.http.Path
import retrofit2.http.Query

interface PathApi {
    @GET("paths/")
    suspend fun getPaths(
//        @Header("Authorization") token: String,
        @Query("lat") lat: Double?,
        @Query("lng") lng: Double?,
        @Query("radius") radius: Float?
    ): List<PathDTO>

    @GET("paths/mine/")
    suspend fun getMyPaths(
        @Header("Authorization") token: String,
    ): List<PathDTO>

    @GET("paths/{id}/")
    suspend fun getPathById(
        @Header("Authorization") token: String,
        @Path("id") id: Int
    ): PathDTO

    @POST("paths/")
    suspend fun createPath(
        @Header("Authorization") token: String,
        @Body request: PathCreateRequestDTO
    ): PathDTO

    @POST("paths/{id}/bookmark_toggle/")
    suspend fun bookmarkToggle(
        @Header("Authorization") token: String,
        @Path("id") id: Int,
    ): BookmarkResponseDTO

    @PATCH("paths/{id}/")
    suspend fun updatePath(
        @Header("Authorization") token: String,
        @Path("id") id: Int,
        @Body request: PathUpdateRequestDTO
    ): PathDTO

    @DELETE("paths/{id}/")
    suspend fun deletePath(
        @Header("Authorization") token: String,
        @Path("id") id: Int
    )

    // ========== Comments ==========

    @GET("paths/{id}/comments/")
    suspend fun getComments(
        @Path("id") pathId: Int
    ): List<CommentDTO>

    @POST("paths/{id}/comments/")
    suspend fun createComment(
        @Header("Authorization") token: String,
        @Path("id") pathId: Int,
        @Body request: CommentRequestDTO
    ): CommentDTO

    @PATCH("paths/{id}/comments/{commentId}/")
    suspend fun updateComment(
        @Header("Authorization") token: String,
        @Path("id") pathId: Int,
        @Path("commentId") commentId: Int,
        @Body request: CommentRequestDTO
    ): CommentDTO

    @DELETE("paths/{id}/comments/{commentId}/")
    suspend fun deleteComment(
        @Header("Authorization") token: String,
        @Path("id") pathId: Int,
        @Path("commentId") commentId: Int,
    )
}