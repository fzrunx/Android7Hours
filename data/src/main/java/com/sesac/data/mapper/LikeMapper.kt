package com.sesac.data.mapper

import com.sesac.data.dto.LikeResponseDTO
import com.sesac.domain.model.Like

fun LikeResponseDTO.toDomain(): Like {
    return Like(
        isLiked = this.isLiked,
        likeCount = this.likeCount,
        status = this.status
    )
}


