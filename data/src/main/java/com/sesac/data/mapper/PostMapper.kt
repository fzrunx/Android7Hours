package com.sesac.data.mapper

import com.sesac.data.dto.PostDTO
import com.sesac.domain.model.Post
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import java.util.TimeZone

fun PostDTO.toPost(): Post {
    // Helper function to parse date strings, trying multiple common ISO 8601 formats.
    fun parseDate(dateString: String): Date {
        val formats = arrayOf(
            "yyyy-MM-dd'T'HH:mm:ss.SSSSSS'Z'", // With microseconds
            "yyyy-MM-dd'T'HH:mm:ss'Z'"        // Without microseconds
        )
        for (format in formats) {
            try {
                val parser = SimpleDateFormat(format, Locale.getDefault()).apply {
                    timeZone = TimeZone.getTimeZone("UTC")
                }
                return parser.parse(dateString) ?: continue
            } catch (e: ParseException) {
                // Try next format
            }
        }
        throw ParseException("Failed to parse date: $dateString", 0)
    }

    return Post(
        id = this.id,
        userId = this.authUser,
        postType = this.postType,
        title = this.title,
        content = this.content,
        imageUrl = this.image,
        createdAt = parseDate(this.createdAt),
        updatedAt = parseDate(this.updatedAt),
        commentsCount = this.commentsCount,
        likesCount = this.likesCount,
        bookmarksCount = this.bookmarksCount,
        isLiked = this.isLiked,
        isBookmarked = this.isBookmarked
    )
}

fun List<PostDTO>.toPostList(): List<Post> =
    this.map { it.toPost() }