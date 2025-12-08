package com.sesac.data.source.api

import com.sesac.data.dto.BookmarkDTO
import com.sesac.data.dto.BookmarkToggleDTO
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST
import retrofit2.http.Path

interface BookmarkApi {
    @GET("users/me/bookmarks/")
    suspend fun getMyBookmarks(
        @Header("Authorization") token: String
    ): List<BookmarkDTO>

    @POST("posts/{id}/bookmark-toggle/")
    suspend fun togglePostBookmark(
        @Path("id") id: Int
    ): BookmarkToggleDTO

    @POST("paths/{id}/bookmark-toggle/")
    suspend fun togglePathBookmark(
        @Path("id") id: Int
    ): BookmarkToggleDTO

}
