package com.sesac.data.mapper

import com.sesac.data.dto.LikeResponseDTO
import com.sesac.domain.model.LikeResponse

fun LikeResponseDTO.toLikeResponse(): LikeResponse {
    return LikeResponse(
        isLiked = this.isLiked,
        likeCount = this.likeCount,
        status = this.status
    )
}


