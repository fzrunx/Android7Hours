package com.sesac.data.source.api

import com.sesac.data.dto.BookmarkResponseDTO
import com.sesac.data.dto.LikeResponseDTO
import com.sesac.data.dto.comment.request.CommentCreateRequestDTO
import com.sesac.data.dto.comment.response.CommentItemDTO
import com.sesac.data.dto.post.request.PostCreateRequestDTO
import com.sesac.data.dto.post.request.PostUpdateRequestDTO
import com.sesac.data.dto.post.response.PostDetailDTO
import com.sesac.data.dto.post.response.PostListItemDTO
import retrofit2.http.Header
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PATCH
import retrofit2.http.DELETE
import retrofit2.http.Path
import retrofit2.http.Query

interface PostApi {

    @GET("posts/")
    suspend fun getPostsList(
        @Header("Authorization") token: String,
        @Query("query") query: String? = null
    ): List<PostListItemDTO>

    @GET("posts/mine/")
    suspend fun getMyPosts(
        @Header("Authorization") token: String
    ): List<PostListItemDTO>

    @GET("posts/{id}/")
    suspend fun getPostDetail(
        @Header("Authorization") token: String,
        @Path("id") id: Int
    ): PostDetailDTO

    @POST("posts/")
    suspend fun createPost(
        @Header("Authorization") token: String,
        @Body request: PostCreateRequestDTO
    ): PostDetailDTO

    @PATCH("posts/{id}/")
    suspend fun updatePost(
        @Header("Authorization") token: String,
        @Path("id") id: Int,
        @Body request: PostUpdateRequestDTO
    ): PostDetailDTO

    @DELETE("posts/{id}/")
    suspend fun deletePost(
        @Header("Authorization") token: String,
        @Path("id") id: Int
    ): Unit

    @POST("posts/{id}/bookmark-toggle/")
    suspend fun bookmarkToggle(
        @Header("Authorization") token: String,
        @Path("id") id: Int,
    ): BookmarkResponseDTO

    @POST("posts/{id}/like-toggle/")
    suspend fun likeToggle(
        @Header("Authorization") token: String,
        @Path("id") id: Int,
    ): LikeResponseDTO


    @GET("posts/{postId}/comments/")
    suspend fun getComments(
        @Path("postId") postId: Int
    ): List<CommentItemDTO>

    @POST("posts/{postId}/comments/")
    suspend fun createComment(
        @Header("Authorization") token: String,
        @Path("postId") postId: Int,
        @Body request: CommentCreateRequestDTO
    ): CommentItemDTO

    @DELETE("posts/{postId}/comments/{commentId}/")
    suspend fun deleteComment(
        @Header("Authorization") token: String,
        @Path("postId") postId: Int,
        @Path("commentId") commentId: Int
    ): Unit
}



//
//    @POST("posts/{post_id}/like_toggle/")
//    suspend fun toggleLike(
//        @Header("Authorization") token: String,
//        @Path("post_id") postId: Int
//    ): LikeResponseDTO

