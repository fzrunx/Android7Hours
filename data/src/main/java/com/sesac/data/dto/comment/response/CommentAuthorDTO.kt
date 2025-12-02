package com.sesac.data.dto.comment.response

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

/**
 * 댓글 API 응답에 포함된 작성자 정보를 담는 DTO.
 * 실제 JSON에 포함된 최소한의 필드(id, nickname)만 매핑합니다.
 */
@JsonClass(generateAdapter = true)
data class CommentAuthorDTO (
    @Json(name = "id")
    val authId: Int?, // Domain Layer의 userId로 매핑됩니다.
    @Json(name = "nickname")
    val authUserNickname: String?, // Domain Layer의 authorUsername으로 매핑됩니다.
    @Json(name = "profile_image")
    val authUserProfileImageUrl: String? // Domain Layer의 authorProfile
)