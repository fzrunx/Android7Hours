package com.sesac.data.source.api

import com.sesac.data.dto.BookmarkToggleResponseDTO
import com.sesac.data.dto.CommentDTO
import com.sesac.data.dto.CommentRequestDTO
import com.sesac.data.dto.LikeToggleResponseDTO
import com.sesac.data.dto.PostDTO
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Multipart
import retrofit2.http.PATCH
import retrofit2.http.POST
import retrofit2.http.Part
import retrofit2.http.Path

interface PostApi {

    @GET("posts/")
    suspend fun getPosts(
        @Header("Authorization") token: String
    ): List<PostDTO>

    @GET("posts/{id}/")
    suspend fun getPostById(
        @Header("Authorization") token: String,
        @Path("id") id: Int
    ): PostDTO

    @Multipart
    @POST("posts/")
    suspend fun createPost(
        @Header("Authorization") token: String,
        @Part("title") title: RequestBody,
        @Part("content") content: RequestBody,
        @Part("post_type") postType: RequestBody,
        @Part image: MultipartBody.Part?
    ): PostDTO

    @Multipart
    @PATCH("posts/{id}/")
    suspend fun updatePost(
        @Header("Authorization") token: String,
        @Path("id") id: Int,
        @Part("title") title: RequestBody,
        @Part("content") content: RequestBody,
        @Part("post_type") postType: RequestBody,
        @Part image: MultipartBody.Part? // To remove image, a different mechanism might be needed
    ): PostDTO

    @DELETE("posts/{id}/")
    suspend fun deletePost(
        @Header("Authorization") token: String,
        @Path("id") id: Int
    )

    @POST("posts/{post_id}/like-toggle/")
    suspend fun toggleLike(
        @Header("Authorization") token: String,
        @Path("post_id") postId: Int
    ): LikeToggleResponseDTO

    @POST("posts/{post_id}/bookmark-toggle/")
    suspend fun toggleBookmark(
        @Header("Authorization") token: String,
        @Path("post_id") postId: Int
    ): BookmarkToggleResponseDTO

    // ========== Comments ==========

    @GET("posts/{id}/comments/")
    suspend fun getComments(
        @Path("id") postId: Int
    ): List<CommentDTO>

    @POST("posts/{id}/comments/")
    suspend fun createComment(
        @Header("Authorization") token: String,
        @Path("id") postId: Int,
        @Body request: CommentRequestDTO
    ): CommentDTO

    @PATCH("posts/{id}/comments/{commentId}/")
    suspend fun updateComment(
        @Header("Authorization") token: String,
        @Path("id") postId: Int,
        @Path("commentId") commentId: Int,
        @Body request: CommentRequestDTO
    ): CommentDTO

    @DELETE("posts/{id}/comments/{commentId}/")
    suspend fun deleteComment(
        @Header("Authorization") token: String,
        @Path("id") postId: Int,
        @Path("commentId") commentId: Int,
    )
}