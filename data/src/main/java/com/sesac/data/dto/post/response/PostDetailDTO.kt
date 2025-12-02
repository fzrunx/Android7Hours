package com.sesac.data.dto.post.response

import com.sesac.data.dto.comment.response.CommentItemDTO
import com.sesac.data.dto.post.type.PostTypeDTO
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class PostDetailDTO(
    // 1. 고유 ID
    val id: Int,

    // 2. 작성자 정보: PostListDTO와 동일하게 평면화된 구조 반영
    @Json(name = "auth_id")
    val authId: Int?,
    @Json(name = "auth_name")
    val authUserNickname: String,
    @Json(name = "auth_profile_image")
    val authUserProfileImageUrl: String?,

    // 3. 게시글 타입 및 제목
    @Json(name = "post_type")
    val postType: PostTypeDTO,
    val title: String,

    // 4. 게시글 본문 (Detail 고유 필드)
    val content: String,

    // 5. 썸네일 이미지 URL
    val image: String?,

    // 6. 댓글 목록 (Detail 고유, nested DTO 사용)
    // List<CommentItemDTO>는 중첩된 author 객체(CommentAuthorDTO)를 포함합니다.
    val comments: List<CommentItemDTO>,

    // 7. 카운트 정보
    @Json(name = "view_count")
    val viewCount: Int,
    @Json(name = "comment_count")
    val commentCount: Int,
    @Json(name = "like_count")
    val likeCount: Int,
    @Json(name = "bookmark_count")
    val bookmarkCount: Int,

    // 8. 상태 정보 (사용자별)
    @Json(name = "is_liked")
    val isLiked: Boolean,
    @Json(name = "is_bookmarked")
    val isBookmarked: Boolean,

    // 9. 시간 정보 (String 타입)
    @Json(name = "created_at")
    val createdAt: String,
    @Json(name = "updated_at")
    val updatedAt: String
)