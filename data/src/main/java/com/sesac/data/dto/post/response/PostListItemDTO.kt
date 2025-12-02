package com.sesac.data.dto.post.response

import com.sesac.data.dto.post.type.PostTypeDTO
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

/**
 * 게시글 목록 조회 API (/posts/) 응답을 위한 Data Transfer Object.
 * 실제 JSON 응답 구조(평면화된 작성자 정보)를 반영합니다.
 */
@JsonClass(generateAdapter = true)
data class PostListItemDTO(
    // 고유 ID
    val id: Int,

    // 🟢 작성자 정보: 평면화된 구조 반영
    @Json(name = "auth_id")
    val authId: Int?,
    @Json(name = "auth_name")
    val authUserNickname: String,
    @Json(name = "auth_profile_image")
    val authUserProfileImageUrl: String?,

    // 게시글 타입 및 제목
    @Json(name = "post_type")
    val postType: PostTypeDTO,
    val title: String,

    // 썸네일 이미지 URL
    val image: String?,

    // 카운트 정보
    @Json(name = "view_count")
    val viewCount: Int,
    @Json(name = "comment_count")
    val commentCount: Int,
    @Json(name = "like_count")
    val likeCount: Int,
    @Json(name = "bookmark_count")
    val bookmarkCount: Int,

    // 상태 정보 (사용자별: 좋아요/즐겨찾기 여부)
    @Json(name = "is_liked")
    val isLiked: Boolean,
    @Json(name = "is_bookmarked")
    val isBookmarked: Boolean,

    // 시간 정보 (String 타입)
    @Json(name = "created_at")
    val createdAt: String,
    @Json(name = "updated_at")
    val updatedAt: String
)