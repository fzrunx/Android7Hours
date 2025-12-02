package com.sesac.data.mapper

import com.sesac.data.dto.CommunityDTO
import com.sesac.domain.model.Community

fun CommunityDTO.toCommunity(
    userName: String,
    content: String?,
) = Community(
    id = this.id,
    title = this.title,
    userName = userName,
    content = content ?: "",
)

//fun Community.toCommunityDTO