package com.sesac.data.mapper

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
import com.sesac.domain.model.Post
import com.sesac.domain.type.PostType

fun String?.toDomainPostType(): PostType {
    return when (this?.lowercase()) {
        "review" -> PostType.REVIEW
        "info" -> PostType.INFO
        else -> PostType.UNKNOWN
    }
}

/**
 * Converts a [BookmarkDTO] to a [Bookmark] domain model.
 */
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
        is BookmarkedPostDTO -> this.toDomain() // Uses the existing mapper from PostMapper
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
        bookmarkCount = this.bookmarksCount,
        isBookmarked = this.isBookmarked
    )
}

fun BookmarkedPostDTO.toPost(): Post {

    return Post(
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
        updatedAt = parseDate(this.updatedAt),
        content = "",
    )
}

fun BookmarkResponseDTO.toBookmarkResponse(): BookmarkResponse{
    return BookmarkResponse(
        isBookmarked = this.bookmarked,
        bookmarkCount = this.bookmarksCount,
        status = this.status,
    )
}

/**
 * Converts a list of [BookmarkDTO] to a list of [Bookmark] domain models.
 */
fun List<BookmarkDTO>.toDomain(): List<Bookmark> = this.map { it.toDomain() }
