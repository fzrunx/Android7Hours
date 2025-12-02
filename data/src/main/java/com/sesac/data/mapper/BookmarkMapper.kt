package com.sesac.data.mapper

import android.annotation.SuppressLint
import com.sesac.common.utils.parseDate
import com.sesac.data.dto.BookmarkDTO
import com.sesac.data.dto.BookmarkResponseDTO
import com.sesac.data.dto.BookmarkedObject
import com.sesac.data.dto.BookmarkedPathDTO
import com.sesac.data.dto.BookmarkedPostDTO
import com.sesac.domain.model.Bookmark
import com.sesac.domain.model.BookmarkResponse
import com.sesac.domain.model.BookmarkedItem
import com.sesac.domain.model.BookmarkedPath
import com.sesac.domain.model.PostListItem
import com.sesac.domain.model.PostType
import java.text.SimpleDateFormat
import java.time.Instant
import java.util.Locale
import java.util.TimeZone

@SuppressLint("NewApi")
private fun convertStringToInstant(dateString: String): Instant {
    val formats = arrayOf(
        "yyyy-MM-dd'T'HH:mm:ss.SSSSSSXXX",
        "yyyy-MM-dd'T'HH:mm:ssXXX",
        "yyyy-MM-dd'T'HH:mm:ss.SSSSSS'Z'",
        "yyyy-MM-dd'T'HH:mm:ss'Z'"
    )
    for (format in formats) {
        try {
            val parser = SimpleDateFormat(format, Locale.US).apply {
                timeZone = TimeZone.getTimeZone("UTC")
            }
            val date = parser.parse(dateString)

            // 🟢 안정적인 변환: Date.getTime() (밀리초)를 Instant.ofEpochMilli에 전달
            return date?.let {
                Instant.ofEpochMilli(it.time)
            } ?: Instant.EPOCH

        } catch (e: Exception) {
            // 다음 포맷 시도
        }
    }
    return Instant.EPOCH
}


fun String?.toDomainPostType(): PostType {
    return when (this?.lowercase()) {
        "review" -> PostType.REVIEW
        "info" -> PostType.INFO
        else -> PostType.UNKNOWN
    }
}


fun BookmarkDTO.toDomain(): Bookmark {
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
        is BookmarkedPostDTO -> this.toDomainListItem() // Uses the existing mapper from PostMapper
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
        bookmarkCount = this.bookmarkCount,
        isBookmarked = this.isBookmarked
    )
}

fun BookmarkedPostDTO.toDomainListItem(): PostListItem {

    return PostListItem(
        id = this.id,
        userId = this.authUser,
        authUserNickname = this.authUserNickname,
        authUserProfileImageUrl = this.authUserProfileImageUrl,
        postType = this.postType.toDomainPostType(),
        title = this.title,
        image = this.image,
        viewCount = this.viewCount,
        commentCount = this.commentCount,
        likeCount = this.likeCount,
        bookmarkCount = this.bookmarkCount,
        isLiked = this.isLiked,
        isBookmarked = this.isBookmarked,
        createdAt = parseDate(this.createdAt),
        updatedAt = parseDate(this.updatedAt)
    )
}

fun BookmarkResponseDTO.toBookmarkResponse(): BookmarkResponse{
    return BookmarkResponse(
        isBookmarked = this.isBookmarked,
        bookmarkCount = this.bookmarkCount,
        status = this.status
    )
}

/**
 * Converts a list of [BookmarkDTO] to a list of [Bookmark] domain models.
 */
fun List<BookmarkDTO>.toDomain(): List<Bookmark> = this.map { it.toDomain() }
