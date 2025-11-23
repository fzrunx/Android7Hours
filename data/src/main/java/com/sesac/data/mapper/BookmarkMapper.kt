package com.sesac.data.mapper

import com.sesac.data.dto.BookmarkedObject
import com.sesac.data.dto.BookmarkedPathDTO
import com.sesac.data.dto.BookmarkDTO
import com.sesac.data.dto.BookmarkResponseDTO
import com.sesac.data.dto.PostDTO
import com.sesac.domain.model.Bookmark
import com.sesac.domain.model.BookmarkResponse
import com.sesac.domain.model.BookmarkedItem
import com.sesac.domain.model.BookmarkedPath
import com.sesac.domain.model.Post
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import java.util.TimeZone

/**
 * Converts a [BookmarkDTO] to a [Bookmark] domain model.
 */
fun BookmarkDTO.toDomain(): Bookmark {
    fun parseDate(dateString: String): Date {
        val formats = arrayOf(
            "yyyy-MM-dd'T'HH:mm:ss.SSSSSS'Z'",
            "yyyy-MM-dd'T'HH:mm:ss'Z'",
            "yyyy-MM-dd'T'HH:mm:ss.SSSSSSXXX", // ISO 8601 with timezone offset
            "yyyy-MM-dd'T'HH:mm:ssXXX"
        )
        for (format in formats) {
            try {
                val parser = SimpleDateFormat(format, Locale.US).apply {
                    timeZone = TimeZone.getTimeZone("UTC")
                }
                return parser.parse(dateString) ?: continue
            } catch (e: ParseException) {
                // Try next format
            }
        }
        // Return current date as a fallback if parsing fails
        return Date()
    }

    return Bookmark(
        id = this.id,
        createdAt = parseDate(this.createdAt),
        bookmarkedItem = this.bookmarkedObject.toDomain()
    )
}

/**
 * Converts a [BookmarkedObject] (polymorphic DTO) to a [BookmarkedItem] (polymorphic domain model).
 */
fun BookmarkedObject.toDomain(): BookmarkedItem {
    return when (this) {
        is BookmarkedPathDTO -> this.toDomain()
        is PostDTO -> this.toPost() // Uses the existing mapper from PostMapper
    }
}

/**
 * Converts a [BookmarkedPathDTO] to a [BookmarkedPath] domain model.
 */
fun BookmarkedPathDTO.toDomain(): BookmarkedPath {
    return BookmarkedPath(
        id = this.id,
        source = this.source,
        uploader = this.authUserNickname,
        pathName = this.pathName,
        pathComment = this.pathComment,
        level = this.level,
        distance = this.distance,
        duration = this.duration,
        isPrivate = this.isPrivate,
        thumbnail = this.thumbnail,
        bookmarksCount = this.bookmarksCount,
        isBookmarked = this.isBookmarked
    )
}

fun BookmarkResponseDTO.toBookmarkResponse(): BookmarkResponse{
    return BookmarkResponse(
        bookmarked = this.bookmarked,
        bookmarksCount = this.bookmarksCount,
        status = this.status
    )
}

/**
 * Converts a list of [BookmarkDTO] to a list of [Bookmark] domain models.
 */
fun List<BookmarkDTO>.toDomain(): List<Bookmark> = this.map { it.toDomain() }
