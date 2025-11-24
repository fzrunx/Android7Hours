package com.sesac.domain.repository

import com.sesac.domain.model.Bookmark
import com.sesac.domain.result.AuthResult
import kotlinx.coroutines.flow.Flow

interface BookmarkRepository {
    suspend fun getMyBookmarks(token: String): Flow<AuthResult<List<Bookmark>>>
}
