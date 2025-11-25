package com.sesac.data.source.remote.api

import com.sesac.data.dto.BookmarkDTO
import retrofit2.http.GET
import retrofit2.http.Header

interface BookmarkApi {
    @GET("users/me/bookmarks/")
    suspend fun getMyBookmarks(
        @Header("Authorization") token: String
    ): List<BookmarkDTO>
}
