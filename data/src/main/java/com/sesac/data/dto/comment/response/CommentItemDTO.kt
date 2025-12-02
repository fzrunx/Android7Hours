package com.sesac.data.dto.comment.response

import com.sesac.data.dto.comment.response.CommentAuthorDTO
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

/**
 * 신규 Domain Model (CommentItem)에 매핑하기 위한 DTO.
 * 실제 JSON 구조에 맞춰 CommentAuthorDTO 객체로 작성자 정보를 수신합니다.
 */
@JsonClass(generateAdapter = true)
data class CommentItemDTO(
    val id: Int,
    val author: CommentAuthorDTO,  // 🟢 CommentAuthorDTO 사용
    val content: String,
    @Json(name = "created_at")
    val createdAt: String
)