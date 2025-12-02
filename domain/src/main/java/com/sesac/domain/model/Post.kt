package com.sesac.domain.model

import java.time.Instant
import java.util.Date

// 공통 속성 인터페이스
sealed interface PostBaseItem {
    // 공통 필드 정의 (val 키워드를 사용하여 구현 클래스에서 override 해야 함)
    val id: Int
    val userId: Int
    val authUserNickname: String
    val authUserProfileImageUrl: String?
    val postType: PostType
    val title: String
    val image: String?
    val viewCount: Int
    val commentCount: Int
    val likeCount: Int
    val bookmarkCount: Int
    val isLiked: Boolean
    val isBookmarked: Boolean
    val createdAt: Date
    val updatedAt: Date
}



// 게시글 목록 출력용 (content, comments 삭제) + 북마크 목록에서 재사용
data class PostListItem(
    // PostBaseItem의 모든 속성 구현
    override val id: Int,
    override val userId: Int,
    override val authUserNickname: String,
    override val authUserProfileImageUrl: String?,
    override val postType: PostType,
    override val title: String,
    override val image: String?,
    override val viewCount: Int,
    override val commentCount: Int,
    override val likeCount: Int,
    override val bookmarkCount: Int,
    override val isLiked: Boolean,
    override val isBookmarked: Boolean,
    override val createdAt: Date,
    override val updatedAt: Date,
) : PostBaseItem, BookmarkedItem, LikedItem {
    companion object {
        val EMPTY = PostListItem(
            id = -1,
            userId = -1,
            authUserNickname = "",
            authUserProfileImageUrl = null,
            postType = PostType.UNKNOWN,
            title = "",
            image = null,
            createdAt = Date(0L),
            updatedAt = Date(0L),
            viewCount = 0,
            commentCount = 0,
            likeCount = 0,
            bookmarkCount = 0,
            isLiked = false,
            isBookmarked = false
        )
    }
}

// 상세 페이지 출력용(게시글 목록 출력용 + content, comments)
data class PostDetail(
    // PostBaseItem의 모든 속성 구현
    override val id: Int,
    override val userId: Int,
    override val authUserNickname: String,
    override val authUserProfileImageUrl: String?,
    override val postType: PostType,
    override val title: String,
    override val image: String?,
    override val viewCount: Int,
    override val commentCount: Int,
    override val likeCount: Int,
    override val bookmarkCount: Int,
    override val isLiked: Boolean,
    override val isBookmarked: Boolean,
    override val createdAt: Date,
    override val updatedAt: Date,
    val content: String,
    var comments: List<CommentItem>?,
) : PostBaseItem {
    companion object {
        val EMPTY = PostDetail(
            id = -1,
            userId = -1,
            authUserNickname = "",
            authUserProfileImageUrl = null,
            postType = PostType.UNKNOWN,
            title = "",
            image = null,
            createdAt = Date(0L),
            updatedAt = Date(0L),
            viewCount = 0,
            commentCount = 0,
            likeCount = 0,
            bookmarkCount = 0,
            isLiked = false,
            isBookmarked = false,
            content = "",
            comments = emptyList()
        )
    }
}

data class BookmarkedPost(
    override val id: Int,
    val userId: Int,
    val authUserNickname: String,
    val authUserProfileImageUrl: String?,
    val postType: PostType,
    val title: String,
    val image: String?,
    val viewCount: Int,
    val commentCount: Int,
    val likeCount: Int,
    val bookmarkCount: Int,
    val isLiked: Boolean,
    val isBookmarked: Boolean
) : BookmarkedItem