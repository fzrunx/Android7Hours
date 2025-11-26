package com.sesac.data.repository

import android.util.Log
import com.sesac.data.mapper.toDomain
import com.sesac.data.source.api.BookmarkApi
import com.sesac.domain.model.Bookmark
import com.sesac.domain.repository.BookmarkRepository
import com.sesac.domain.result.AuthResult
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class BookmarkRepositoryImpl @Inject constructor(
    private val bookmarkApi: BookmarkApi
) : BookmarkRepository {
    override suspend fun getMyBookmarks(token: String): Flow<AuthResult<List<Bookmark>>> = flow {
        emit(AuthResult.Loading)
        try {
            val bookmarks = bookmarkApi.getMyBookmarks("Bearer $token").map { it.toDomain() }
            emit(AuthResult.Success(bookmarks))
        } catch (e: Exception) {
            Log.d("TAG-BookmarkRepositoryImpl", "getMyBookmarks error : $e")
            emit(AuthResult.NetworkError(e))
        }
    }
}